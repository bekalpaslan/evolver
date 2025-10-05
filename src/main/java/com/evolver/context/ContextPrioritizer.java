package com.evolver.context;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * Prioritizes context fragments based on relevance, importance, and token budget
 */
public class ContextPrioritizer {
    private static final Logger logger = Logger.getLogger(ContextPrioritizer.class.getName());

    private final ContextConfig config;

    public ContextPrioritizer(ContextConfig config) {
        this.config = Objects.requireNonNull(config, "ContextConfig cannot be null");
    }

    /**
     * Prioritize and select fragments within token budget
     */
    public List<ContextFragment> prioritize(List<ContextFragment> fragments, ContextRequest request) {
        if (fragments == null) {
            logger.warning("Received null fragments list, returning empty list");
            return Collections.emptyList();
        }

        if (request == null) {
            logger.warning("Received null request, returning empty list");
            return Collections.emptyList();
        }

        if (fragments.isEmpty()) {
            logger.fine("Received empty fragments list, returning empty list");
            return Collections.emptyList();
        }

        try {
            // Calculate priority score for each fragment
            Map<ContextFragment, Double> scores = new HashMap<>();
            for (ContextFragment fragment : fragments) {
                if (fragment != null) {
                    try {
                        double score = calculatePriorityScore(fragment, request);
                        scores.put(fragment, score);
                    } catch (Exception e) {
                        logger.log(Level.WARNING, "Error calculating priority score for fragment", e);
                        scores.put(fragment, 0.0); // Default low score
                    }
                }
            }

            // Sort by priority score (descending)
            List<ContextFragment> sorted = fragments.stream()
                .filter(Objects::nonNull)
                .sorted((f1, f2) -> {
                    double score1 = scores.getOrDefault(f1, 0.0);
                    double score2 = scores.getOrDefault(f2, 0.0);
                    return Double.compare(score2, score1);
                })
                .collect(Collectors.toList());

            // Select fragments within token budget using knapsack approach
            List<ContextFragment> selected = selectWithinBudget(sorted, request.getTokenBudget(), scores);

            logger.fine("Prioritized " + fragments.size() + " fragments to " + selected.size() +
                       " within budget of " + request.getTokenBudget() + " tokens");

            return selected;

        } catch (Exception e) {
            logger.log(Level.SEVERE, "Critical error during fragment prioritization", e);
            // Return safe subset on critical failure
            return fragments.stream()
                .filter(Objects::nonNull)
                .limit(Math.min(fragments.size(), 10)) // Return at most 10 fragments
                .collect(Collectors.toList());
        }
    }

    /**
     * Calculate priority score for a fragment
     */
    private double calculatePriorityScore(ContextFragment fragment, ContextRequest request) {
        double score = 0.0;

        // Base relevance score
        score += fragment.getRelevanceScore() * 0.4;

        // Type preference bonus
        if (request.getPreferredTypes() != null &&
            request.getPreferredTypes().contains(fragment.getType())) {
            score += 0.3;
        }

        // Focus area match bonus
        long focusMatches = fragment.getAspects().stream()
            .filter(request.getFocusAreas()::contains)
            .count();
        score += (focusMatches / (double) Math.max(1, request.getFocusAreas().size())) * 0.2;

        // Recency bonus (newer is better)
        long ageMinutes = java.time.Duration.between(
            fragment.getTimestamp(),
            java.time.Instant.now()
        ).toMinutes();
        double recencyScore = 1.0 / (1.0 + (ageMinutes / 60.0)); // Decay over hours
        score += recencyScore * 0.1;

        return score;
    }

    /**
     * Select fragments within token budget using a greedy knapsack approach
     */
    private List<ContextFragment> selectWithinBudget(List<ContextFragment> sortedFragments,
                                                   int tokenBudget,
                                                   Map<ContextFragment, Double> scores) {
        List<ContextFragment> selected = new ArrayList<>();
        int currentTokens = 0;
        Set<String> selectedIds = new HashSet<>();

        for (ContextFragment fragment : sortedFragments) {
            if (fragment == null) continue;

            try {
                // Check dependencies
                boolean dependenciesMet = fragment.getDependencies() == null ||
                    fragment.getDependencies().isEmpty() ||
                    fragment.getDependencies().stream().allMatch(selectedIds::contains);

                if (!dependenciesMet) {
                    continue; // Skip if dependencies not met
                }

                int fragmentTokens = Math.max(1, fragment.getEstimatedTokens()); // At least 1 token

                // Reserve some budget for essential fragments later
                int reservedBudget = (int) (tokenBudget * config.getReservedBudgetRatio());
                int availableBudget = tokenBudget - reservedBudget;

                if (currentTokens + fragmentTokens <= tokenBudget) {
                    selected.add(fragment);
                    selectedIds.add(fragment.getId() != null ? fragment.getId() : UUID.randomUUID().toString());
                    currentTokens += fragmentTokens;
                } else if (currentTokens < availableBudget) {
                    // Try to use reserved budget for high-priority fragments
                    double priorityScore = scores.getOrDefault(fragment, 0.0);
                    if (priorityScore > 0.8) {
                        selected.add(fragment);
                        selectedIds.add(fragment.getId() != null ? fragment.getId() : UUID.randomUUID().toString());
                        currentTokens += fragmentTokens;
                    }
                }

                if (currentTokens >= tokenBudget) {
                    break;
                }
            } catch (Exception e) {
                logger.log(Level.WARNING, "Error processing fragment in budget selection", e);
                // Continue with next fragment
            }
        }

        return selected;
    }
}
