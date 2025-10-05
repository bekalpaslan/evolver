package com.evolver.context;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.Executor;
import java.util.concurrent.ForkJoinPool;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * Core engine for AI context engineering.
 * Orchestrates context collection, filtering, prioritization, and delivery.
 */
public class ContextEngine {
    private static final Logger logger = Logger.getLogger(ContextEngine.class.getName());

    // Thread-safe collector registry
    private final List<ContextCollector> collectors = Collections.synchronizedList(new ArrayList<>());

    private final ContextFilter filter;
    private final ContextPrioritizer prioritizer;
    private final ContextFormatter formatter;
    private final ContextConfig config;
    private final Executor executor;

    // Memory management limits
    private final int maxFragmentsPerRequest;
    private final long maxMemoryUsagePerRequest;

    public ContextEngine(ContextConfig config) {
        this.config = config;
        this.filter = new ContextFilter(config);
        this.prioritizer = new ContextPrioritizer(config);
        this.formatter = new ContextFormatter(config);

        // Use custom executor for better control
        this.executor = ForkJoinPool.commonPool();

        // Configure memory limits from config or defaults
        this.maxFragmentsPerRequest = getConfigValue("maxFragmentsPerRequest", 1000);
        this.maxMemoryUsagePerRequest = getConfigValue("maxMemoryUsagePerRequest", 50L * 1024 * 1024); // 50MB
    }

    @SuppressWarnings("unchecked")
    private <T> T getConfigValue(String key, T defaultValue) {
        Object value = config.getCustomSetting(key);
        return value != null ? (T) value : defaultValue;
    }

    /**
     * Register a context collector (thread-safe)
     */
    public void registerCollector(ContextCollector collector) {
        if (collector == null) {
            throw new IllegalArgumentException("Collector cannot be null");
        }
        collectors.add(collector);
        logger.info("Registered collector: " + collector.getClass().getSimpleName());
    }

    /**
     * Unregister a context collector (thread-safe)
     */
    public void unregisterCollector(ContextCollector collector) {
        collectors.remove(collector);
        logger.info("Unregistered collector: " + collector.getClass().getSimpleName());
    }

    /**
     * Get registered collectors (defensive copy)
     */
    public List<ContextCollector> getCollectors() {
        synchronized (collectors) {
            return new ArrayList<>(collectors);
        }
    }

    /**
     * Gather context for a specific task with comprehensive error handling
     */
    public CompletableFuture<ContextPackage> gatherContext(ContextRequest request) {
        if (request == null) {
            return CompletableFuture.completedFuture(createErrorPackage("Request cannot be null"));
        }

        logger.info("Starting context gathering for task: " + request.getTaskDescription());

        // Get applicable collectors (thread-safe)
        List<ContextCollector> applicableCollectors;
        synchronized (collectors) {
            applicableCollectors = collectors.stream()
                .filter(collector -> {
                    try {
                        return collector.isApplicable(request);
                    } catch (Exception e) {
                        logger.log(Level.WARNING, "Error checking collector applicability: " + collector.getClass().getSimpleName(), e);
                        return false;
                    }
                })
                .collect(Collectors.toList());
        }

        if (applicableCollectors.isEmpty()) {
            logger.warning("No applicable collectors found for request: " + request.getTaskDescription());
            return CompletableFuture.completedFuture(createEmptyPackage(request));
        }

        // Collect context from all applicable collectors in parallel
        List<CompletableFuture<ContextFragment>> futures = applicableCollectors.stream()
            .map(collector -> CompletableFuture.supplyAsync(() -> {
                try {
                    long startTime = System.nanoTime();
                    ContextFragment fragment = collector.collect(request);
                    long durationMs = (System.nanoTime() - startTime) / 1_000_000;

                    if (fragment != null) {
                        logger.fine("Collector " + collector.getClass().getSimpleName() +
                                  " completed in " + durationMs + "ms");
                    }

                    return fragment;
                } catch (Exception e) {
                    logger.log(Level.SEVERE, "Collector failed: " + collector.getClass().getSimpleName(), e);
                    return null; // Return null for failed collectors
                }
            }, executor))
            .collect(Collectors.toList());

        // Process results with comprehensive error handling
        return CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]))
            .handle((result, throwable) -> {
                if (throwable != null) {
                    logger.log(Level.SEVERE, "Context collection failed", throwable);
                    return createErrorPackage("Context collection failed: " + throwable.getMessage());
                }

                try {
                    // Gather all fragments (filter out nulls and check memory limits)
                    List<ContextFragment> fragments = futures.stream()
                        .map(future -> {
                            try {
                                return future.join();
                            } catch (CompletionException e) {
                                logger.log(Level.WARNING, "Future completion failed", e.getCause());
                                return null;
                            }
                        })
                        .filter(Objects::nonNull)
                        .collect(Collectors.toList());

                    // Check memory limits before processing
                    if (shouldLimitCollection(request, fragments)) {
                        logger.warning("Collection exceeds memory limits, applying emergency filtering");
                        fragments = applyEmergencyFiltering(fragments);
                    }

                    // Filter irrelevant context
                    fragments = filter.filter(fragments, request);

                    // Prioritize based on relevance and token budget
                    fragments = prioritizer.prioritize(fragments, request);

                    // Format and package
                    ContextPackage contextPackage = formatter.format(fragments, request);

                    logger.info("Context gathering completed. Fragments: " + fragments.size() +
                              ", Tokens: " + contextPackage.getEstimatedTokens());

                    return contextPackage;

                } catch (Exception e) {
                    logger.log(Level.SEVERE, "Context processing failed", e);
                    return createErrorPackage("Context processing failed: " + e.getMessage());
                }
            });
    }

    /**
     * Check if collection should be limited due to memory constraints
     */
    private boolean shouldLimitCollection(ContextRequest request, List<ContextFragment> fragments) {
        return fragments.size() > maxFragmentsPerRequest ||
               estimateMemoryUsage(fragments) > maxMemoryUsagePerRequest;
    }

    /**
     * Estimate memory usage of fragments
     */
    private long estimateMemoryUsage(List<ContextFragment> fragments) {
        return fragments.stream()
            .mapToLong(fragment -> {
                long contentSize = fragment.getContent() != null ? fragment.getContent().length() * 2L : 0L;
                long metadataSize = estimateMapSize(fragment.getMetadata());
                long aspectsSize = fragment.getAspects().size() * 50L; // Rough estimate per aspect
                return contentSize + metadataSize + aspectsSize + 200L; // Object overhead
            })
            .sum();
    }

    /**
     * Rough estimate of map memory usage
     */
    private long estimateMapSize(Map<String, Object> map) {
        if (map == null) return 0;
        return map.size() * 100L; // Rough estimate per entry
    }

    /**
     * Apply emergency filtering when memory limits are exceeded
     */
    private List<ContextFragment> applyEmergencyFiltering(List<ContextFragment> fragments) {
        return fragments.stream()
            .sorted((a, b) -> Double.compare(b.getRelevanceScore(), a.getRelevanceScore()))
            .limit(maxFragmentsPerRequest / 2) // Keep only top 50%
            .collect(Collectors.toList());
    }

    /**
     * Create an empty context package for when no context is available
     */
    private ContextPackage createEmptyPackage(ContextRequest request) {
        return ContextPackage.builder()
            .request(request)
            .fragments(Collections.emptyList())
            .sections(Collections.emptyList())
            .metadata(Collections.singletonMap("status", "empty"))
            .build();
    }

    /**
     * Create an error context package
     */
    private ContextPackage createErrorPackage(String errorMessage) {
        Map<String, Object> errorMetadata = new HashMap<>();
        errorMetadata.put("status", "error");
        errorMetadata.put("error", errorMessage);
        errorMetadata.put("timestamp", System.currentTimeMillis());

        return ContextPackage.builder()
            .request(ContextRequest.builder().taskDescription("error").build())
            .fragments(Collections.emptyList())
            .sections(Collections.emptyList())
            .metadata(errorMetadata)
            .build();
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

        long coveredRequired = config.getRequiredAspects().stream()
            .filter(coveredAspects::contains)
            .count();

        return (double) coveredRequired / config.getRequiredAspects().size();
    }
}
