package com.evolver.context;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Filters context fragments based on relevance and configuration
 */
public class ContextFilter {
    private final ContextConfig config;

    public ContextFilter(ContextConfig config) {
        this.config = config;
    }

    /**
     * Filter context fragments based on relevance and request
     */
    public List<ContextFragment> filter(List<ContextFragment> fragments, ContextRequest request) {
        return fragments.stream()
            // Remove excluded types
            .filter(f -> !request.getExcludedTypes().contains(f.getType()))
            // Apply minimum relevance threshold
            .filter(f -> f.getRelevanceScore() >= config.getMinRelevanceThreshold())
            // Remove duplicates
            .filter(distinctByContent())
            // Remove stale context
            .filter(f -> !isStale(f))
            // Apply custom filters
            .filter(f -> applyCustomFilters(f, request))
            .collect(Collectors.toList());
    }

    private java.util.function.Predicate<ContextFragment> distinctByContent() {
        Set<String> seen = new HashSet<>();
        return fragment -> seen.add(fragment.getContent());
    }

    private boolean isStale(ContextFragment fragment) {
        if (config.getContextMaxAge() == null) {
            return false;
        }
        return java.time.Duration.between(fragment.getTimestamp(), java.time.Instant.now())
            .compareTo(config.getContextMaxAge()) > 0;
    }

    private boolean applyCustomFilters(ContextFragment fragment, ContextRequest request) {
        // Apply custom filtering logic based on request parameters
        for (FilterRule rule : config.getFilterRules()) {
            if (!rule.test(fragment, request)) {
                return false;
            }
        }
        return true;
    }
}
