package com.evolver.context;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

/**
 * Core engine for AI context engineering.
 * Orchestrates context collection, filtering, prioritization, and delivery.
 */
public class ContextEngine {
    private final List<ContextCollector> collectors;
    private final ContextFilter filter;
    private final ContextPrioritizer prioritizer;
    private final ContextFormatter formatter;
    private final ContextConfig config;

    public ContextEngine(ContextConfig config) {
        this.config = config;
        this.collectors = new ArrayList<>();
        this.filter = new ContextFilter(config);
        this.prioritizer = new ContextPrioritizer(config);
        this.formatter = new ContextFormatter(config);
    }

    /**
     * Register a context collector
     */
    public void registerCollector(ContextCollector collector) {
        collectors.add(collector);
    }

    /**
     * Gather context for a specific task
     */
    public CompletableFuture<ContextPackage> gatherContext(ContextRequest request) {
        // Collect context from all registered collectors in parallel
        List<CompletableFuture<ContextFragment>> futures = collectors.stream()
            .filter(collector -> collector.isApplicable(request))
            .map(collector -> CompletableFuture.supplyAsync(() ->
                collector.collect(request)))
            .collect(Collectors.toList());

        return CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]))
            .thenApply(v -> {
                // Gather all fragments
                List<ContextFragment> fragments = futures.stream()
                    .map(CompletableFuture::join)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());

                // Filter irrelevant context
                fragments = filter.filter(fragments, request);

                // Prioritize based on relevance and token budget
                fragments = prioritizer.prioritize(fragments, request);

                // Format and package
                return formatter.format(fragments, request);
            });
    }

    /**
     * Analyze context quality and provide metrics
     */
    public ContextMetrics analyzeContext(ContextPackage contextPackage) {
        return ContextMetrics.builder()
            .totalTokens(contextPackage.getEstimatedTokens())
            .fragmentCount(contextPackage.getFragments().size())
            .relevanceScore(calculateRelevanceScore(contextPackage))
            .coverage(calculateCoverage(contextPackage))
            .build();
    }

    private double calculateRelevanceScore(ContextPackage pkg) {
        return pkg.getFragments().stream()
            .mapToDouble(ContextFragment::getRelevanceScore)
            .average()
            .orElse(0.0);
    }

    private double calculateCoverage(ContextPackage pkg) {
        Set<String> coveredAspects = pkg.getFragments().stream()
            .flatMap(f -> f.getAspects().stream())
            .collect(Collectors.toSet());

        return (double) coveredAspects.size() / config.getRequiredAspects().size();
    }
}
