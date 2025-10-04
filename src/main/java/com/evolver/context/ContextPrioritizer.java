package com.evolver.context;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Prioritizes context fragments based on relevance, importance, and token budget
 */
public class ContextPrioritizer {
    private final ContextConfig config;

    public ContextPrioritizer(ContextConfig config) {
        this.config = config;
    }

    /**
     * Prioritize and select fragments within token budget
     */
    public List<ContextFragment> prioritize(List<ContextFragment> fragments, ContextRequest request) {
        // Calculate priority score for each fragment
        Map<ContextFragment, Double> scores = fragments.stream()
            .collect(Collectors.toMap(
                f -> f,
                f -> calculatePriorityScore(f, request)
            ));

        // Sort by priority score (descending)
        List<ContextFragment> sorted = fragments.stream()
            .sorted((f1, f2) -> Double.compare(scores.get(f2), scores.get(f1)))
            .collect(Collectors.toList());

        // Select fragments within token budget using knapsack approach
        return selectWithinBudget(sorted, request.getTokenBudget());
    }

    /**
     * Calculate priority score for a fragment
     */
    private double calculatePriorityScore(ContextFragment fragment, ContextRequest request) {
        double score = 0.0;

        // Base relevance score
        score += fragment.getRelevanceScore() * 0.4;

        // Type preference bonus
        if (request.getPreferredTypes().contains(fragment.getType())) {
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
    private List<ContextFragment> selectWithinBudget(List<ContextFragment> sortedFragments, int tokenBudget) {
        List<ContextFragment> selected = new ArrayList<>();
        int currentTokens = 0;
        Set<String> selectedIds = new HashSet<>();

        for (ContextFragment fragment : sortedFragments) {
            // Check dependencies
            boolean dependenciesMet = fragment.getDependencies().isEmpty() ||
                fragment.getDependencies().stream().allMatch(selectedIds::contains);

            if (!dependenciesMet) {
                continue; // Skip if dependencies not met
            }

            int fragmentTokens = fragment.getEstimatedTokens();

            // Reserve some budget for essential fragments later
            int reservedBudget = (int) (tokenBudget * config.getReservedBudgetRatio());
            int availableBudget = tokenBudget - reservedBudget;

            if (currentTokens + fragmentTokens <= tokenBudget) {
                selected.add(fragment);
                selectedIds.add(fragment.getId());
                currentTokens += fragmentTokens;
            } else if (currentTokens < availableBudget) {
                // Try to use reserved budget for high-priority fragments
                if (fragment.getRelevanceScore() > 0.8) {
                    selected.add(fragment);
                    selectedIds.add(fragment.getId());
                    currentTokens += fragmentTokens;
                }
            }

            if (currentTokens >= tokenBudget) {
                break;
            }
        }

        return selected;
    }
}
