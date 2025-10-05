package com.evolver.context;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * Filters context fragments based on relevance and configuration
 */
public class ContextFilter {
    private static final Logger logger = Logger.getLogger(ContextFilter.class.getName());

    private final ContextConfig config;

    public ContextFilter(ContextConfig config) {
        this.config = Objects.requireNonNull(config, "ContextConfig cannot be null");
    }

    /**
     * Filter context fragments based on relevance and request
     */
    public List<ContextFragment> filter(List<ContextFragment> fragments, ContextRequest request) {
        if (fragments == null) {
            logger.warning("Received null fragments list, returning empty list");
            return Collections.emptyList();
        }

        if (request == null) {
            logger.warning("Received null request, returning empty list");
            return Collections.emptyList();
        }

        try {
            List<ContextFragment> filtered = fragments.stream()
                // Remove excluded types
                .filter(f -> {
                    try {
                        return f != null && !request.getExcludedTypes().contains(f.getType());
                    } catch (Exception e) {
                        logger.log(Level.WARNING, "Error checking excluded types for fragment", e);
                        return false;
                    }
                })
                // Apply minimum relevance threshold
                .filter(f -> {
                    try {
                        return f != null && f.getRelevanceScore() >= config.getMinRelevanceThreshold();
                    } catch (Exception e) {
                        logger.log(Level.WARNING, "Error checking relevance score for fragment", e);
                        return false;
                    }
                })
                // Remove duplicates
                .filter(distinctByContent())
                // Remove stale context
                .filter(f -> {
                    try {
                        return f != null && !isStale(f);
                    } catch (Exception e) {
                        logger.log(Level.WARNING, "Error checking staleness for fragment", e);
                        return false;
                    }
                })
                // Apply custom filters
                .filter(f -> {
                    try {
                        return f != null && applyCustomFilters(f, request);
                    } catch (Exception e) {
                        logger.log(Level.WARNING, "Error applying custom filters for fragment", e);
                        return false;
                    }
                })
                .filter(Objects::nonNull) // Final null check
                .collect(Collectors.toList());

            logger.fine("Filtered " + fragments.size() + " fragments to " + filtered.size());
            return filtered;

        } catch (Exception e) {
            logger.log(Level.SEVERE, "Critical error during fragment filtering", e);
            // Return safe empty list on critical failure
            return Collections.emptyList();
        }
    }

    private java.util.function.Predicate<ContextFragment> distinctByContent() {
        Set<String> seen = new HashSet<>();
        return fragment -> {
            if (fragment == null || fragment.getContent() == null) {
                return false;
            }
            return seen.add(fragment.getContent());
        };
    }

    private boolean isStale(ContextFragment fragment) {
        if (config.getContextMaxAge() == null) {
            return false;
        }

        try {
            return java.time.Duration.between(fragment.getTimestamp(), java.time.Instant.now())
                .compareTo(config.getContextMaxAge()) > 0;
        } catch (Exception e) {
            logger.log(Level.WARNING, "Error checking fragment staleness", e);
            return false; // Don't filter out on error
        }
    }

    private boolean applyCustomFilters(ContextFragment fragment, ContextRequest request) {
        if (config.getFilterRules() == null) {
            return true;
        }

        try {
            for (FilterRule rule : config.getFilterRules()) {
                if (rule != null && !rule.test(fragment, request)) {
                    return false;
                }
            }
        } catch (Exception e) {
            logger.log(Level.WARNING, "Error applying custom filter rules", e);
            return false; // Filter out on error to be safe
        }

        return true;
    }
}
