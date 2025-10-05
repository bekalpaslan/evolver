package com.evolver.context;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * GARBAGE COLLECTION CONTEXT
 */
class GarbageCollectionContext {
    private final String sessionId;
    private final String agentId;
    private final String taskType;
    private final LocalDateTime currentTime;
    private final long availableMemory;
    private final Map<String, Object> metadata;

    public GarbageCollectionContext(String sessionId, String agentId, String taskType) {
        this.sessionId = sessionId;
        this.agentId = agentId;
        this.taskType = taskType;
        this.currentTime = LocalDateTime.now();
        this.availableMemory = Runtime.getRuntime().freeMemory();
        this.metadata = new HashMap<>();
    }

    // Getters
    public String getSessionId() { return sessionId; }
    public String getAgentId() { return agentId; }
    public String getTaskType() { return taskType; }
    public LocalDateTime getCurrentTime() { return currentTime; }
    public long getAvailableMemory() { return availableMemory; }
    public Map<String, Object> getMetadata() { return metadata; }

    public void addMetadata(String key, Object value) {
        metadata.put(key, value);
    }
}

/**
 * STRATEGY RESULT
 */
class StrategyResult {
    private final List<ContextFragment> survivingFragments;
    private final int collectedFragments;
    private final String reason;

    public StrategyResult(List<ContextFragment> survivingFragments, int collectedFragments, String reason) {
        this.survivingFragments = survivingFragments;
        this.collectedFragments = collectedFragments;
        this.reason = reason;
    }

    public List<ContextFragment> getSurvivingFragments() { return survivingFragments; }
    public int getCollectedFragments() { return collectedFragments; }
    public String getReason() { return reason; }
}

/**
 * GARBAGE COLLECTION STRATEGY INTERFACE
 */
public interface GarbageCollectionStrategy {
    StrategyResult collect(List<ContextFragment> fragments, GarbageCollectionContext context);
}

/**
 * CONTEXT GARBAGE COLLECTOR
 *
 * Intelligent system for cleaning up stale, unused, or redundant context information.
 * Implements multiple garbage collection strategies to maintain optimal context efficiency.
 *
 * Strategies:
 * - Time-based expiration (TTL-based cleanup)
 * - Usage-based cleanup (LRU eviction)
 * - Relevance-based cleanup (task relevance decay)
 * - Size-based cleanup (memory pressure management)
 * - Priority-based cleanup (importance hierarchy)
 * - Session-based cleanup (session lifecycle management)
 * - Reference counting (unused context removal)
 * - Generational collection (age-based strategies)
 */
public class ContextGarbageCollector {

    // Collection strategies
    private final Map<String, GarbageCollectionStrategy> strategies = new ConcurrentHashMap<>();

    // Collection metrics and monitoring
    private final GarbageCollectionMetrics metrics = new GarbageCollectionMetrics();

    // Configuration
    private final GarbageCollectionConfig config;

    // Background cleanup scheduler
    private final ScheduledExecutorService cleanupScheduler = Executors.newScheduledThreadPool(2);

    public ContextGarbageCollector(GarbageCollectionConfig config) {
        this.config = config;
        initializeStrategies();
        startBackgroundCleanup();
    }

    /**
     * Initialize all garbage collection strategies
     */
    private void initializeStrategies() {
        strategies.put("TIME_BASED", new TimeBasedStrategy());
        strategies.put("USAGE_BASED", new UsageBasedStrategy());
        strategies.put("RELEVANCE_BASED", new RelevanceBasedStrategy());
        strategies.put("SIZE_BASED", new SizeBasedStrategy());
        strategies.put("PRIORITY_BASED", new PriorityBasedStrategy());
        strategies.put("SESSION_BASED", new SessionBasedStrategy());
        strategies.put("REFERENCE_COUNTING", new ReferenceCountingStrategy());
        strategies.put("GENERATIONAL", new GenerationalStrategy());
    }

    /**
     * Start background cleanup tasks
     */
    private void startBackgroundCleanup() {
        // Periodic cleanup every 30 minutes
        cleanupScheduler.scheduleAtFixedRate(
            this::performBackgroundCleanup,
            30, 30, TimeUnit.MINUTES
        );

        // Memory pressure monitoring every 5 minutes
        cleanupScheduler.scheduleAtFixedRate(
            this::checkMemoryPressure,
            5, 5, TimeUnit.MINUTES
        );
    }

    /**
     * Perform garbage collection on context fragments
     */
    public GarbageCollectionResult collectGarbage(List<ContextFragment> fragments,
                                                GarbageCollectionContext context) {

        GarbageCollectionResult.Builder resultBuilder = GarbageCollectionResult.builder();
        List<ContextFragment> survivingFragments = new ArrayList<>(fragments);

        // Apply each enabled strategy
        for (Map.Entry<String, GarbageCollectionStrategy> entry : strategies.entrySet()) {
            String strategyName = entry.getKey();
            GarbageCollectionStrategy strategy = entry.getValue();

            if (config.isStrategyEnabled(strategyName)) {
                StrategyResult strategyResult = strategy.collect(survivingFragments, context);
                survivingFragments = strategyResult.getSurvivingFragments();

                resultBuilder.addStrategyResult(strategyName, strategyResult);
                metrics.recordStrategyExecution(strategyName, strategyResult);
            }
        }

        GarbageCollectionResult result = resultBuilder
            .originalFragments(fragments.size())
            .survivingFragments(survivingFragments.size())
            .collectedFragments(fragments.size() - survivingFragments.size())
            .build();

        metrics.recordCollection(result);
        return result;
    }

    /**
     * Background cleanup task
     */
    private void performBackgroundCleanup() {
        try {
            // Clean up expired sessions
            cleanupExpiredSessions();

            // Clean up stale global patterns
            cleanupStalePatterns();

            // Optimize memory usage
            performMemoryOptimization();

            metrics.recordBackgroundCleanup();
        } catch (Exception e) {
            System.err.println("Background cleanup failed: " + e.getMessage());
        }
    }

    /**
     * Check for memory pressure and trigger aggressive cleanup if needed
     */
    private void checkMemoryPressure() {
        long usedMemory = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
        long maxMemory = Runtime.getRuntime().maxMemory();
        double memoryUsageRatio = (double) usedMemory / maxMemory;

        if (memoryUsageRatio > config.getMemoryPressureThreshold()) {
            performAggressiveCleanup();
            metrics.recordMemoryPressureCleanup();
        }
    }

    /**
     * Clean up expired sessions
     */
    private void cleanupExpiredSessions() {
        // Implementation would clean up session data older than configured TTL
        // This would integrate with SessionContextMemory cleanup
    }

    /**
     * Clean up stale global patterns
     */
    private void cleanupStalePatterns() {
        // Implementation would remove patterns that haven't been used recently
        // This would integrate with ContextPattern cleanup
    }

    /**
     * Perform memory optimization
     */
    private void performMemoryOptimization() {
        // Force garbage collection if memory usage is high
        if (config.isAggressiveGcEnabled()) {
            System.gc();
        }
    }

    /**
     * Perform aggressive cleanup under memory pressure
     */
    private void performAggressiveCleanup() {
        // Implementation would trigger more aggressive collection strategies
        // This would use SIZE_BASED and PRIORITY_BASED strategies primarily
    }

    /**
     * Get collection metrics
     */
    public GarbageCollectionMetrics getMetrics() {
        return metrics;
    }

    /**
     * Shutdown the garbage collector
     */
    public void shutdown() {
        cleanupScheduler.shutdown();
        try {
            if (!cleanupScheduler.awaitTermination(5, TimeUnit.SECONDS)) {
                cleanupScheduler.shutdownNow();
            }
        } catch (InterruptedException e) {
            cleanupScheduler.shutdownNow();
        }
    }
}

/**
 * GARBAGE COLLECTION CONFIGURATION
 */
class GarbageCollectionConfig {
    private final Map<String, Boolean> enabledStrategies = new HashMap<>();
    private final Map<String, Object> strategyConfigs = new HashMap<>();
    private double memoryPressureThreshold = 0.8;
    private boolean aggressiveGcEnabled = false;

    public GarbageCollectionConfig() {
        // Default strategy enablement
        enabledStrategies.put("TIME_BASED", true);
        enabledStrategies.put("USAGE_BASED", true);
        enabledStrategies.put("RELEVANCE_BASED", true);
        enabledStrategies.put("SIZE_BASED", false); // Only when needed
        enabledStrategies.put("PRIORITY_BASED", false); // Only when needed
        enabledStrategies.put("SESSION_BASED", true);
        enabledStrategies.put("REFERENCE_COUNTING", true);
        enabledStrategies.put("GENERATIONAL", true);
    }

    public boolean isStrategyEnabled(String strategy) {
        return enabledStrategies.getOrDefault(strategy, false);
    }

    public void setStrategyEnabled(String strategy, boolean enabled) {
        enabledStrategies.put(strategy, enabled);
    }

    public void setStrategyConfig(String strategy, String key, Object value) {
        strategyConfigs.put(strategy + "." + key, value);
    }

    public Object getStrategyConfig(String strategy, String key) {
        return strategyConfigs.get(strategy + "." + key);
    }

    public double getMemoryPressureThreshold() { return memoryPressureThreshold; }
    public void setMemoryPressureThreshold(double threshold) { memoryPressureThreshold = threshold; }

    public boolean isAggressiveGcEnabled() { return aggressiveGcEnabled; }
    public void setAggressiveGcEnabled(boolean enabled) { aggressiveGcEnabled = enabled; }
}

/**
 * GARBAGE COLLECTION RESULT
 */
class GarbageCollectionResult {
    private final int originalFragments;
    private final int survivingFragments;
    private final int collectedFragments;
    private final Map<String, StrategyResult> strategyResults;
    private final LocalDateTime collectionTime;

    private GarbageCollectionResult(Builder builder) {
        this.originalFragments = builder.originalFragments;
        this.survivingFragments = builder.survivingFragments;
        this.collectedFragments = builder.collectedFragments;
        this.strategyResults = builder.strategyResults;
        this.collectionTime = LocalDateTime.now();
    }

    public int getOriginalFragments() { return originalFragments; }
    public int getSurvivingFragments() { return survivingFragments; }
    public int getCollectedFragments() { return collectedFragments; }
    public Map<String, StrategyResult> getStrategyResults() { return strategyResults; }
    public LocalDateTime getCollectionTime() { return collectionTime; }

    public double getCollectionRate() {
        return originalFragments > 0 ? (double) collectedFragments / originalFragments : 0.0;
    }

    public String getFormattedReport() {
        StringBuilder report = new StringBuilder();
        report.append("🗑️ GARBAGE COLLECTION RESULT\\n");
        report.append("════════════════════════════════════\\n");
        report.append(String.format("Original fragments: %d\\n", originalFragments));
        report.append(String.format("Surviving fragments: %d\\n", survivingFragments));
        report.append(String.format("Collected fragments: %d\\n", collectedFragments));
        report.append(String.format("Collection rate: %.1f%%\\n", getCollectionRate() * 100));

        report.append("\\n📊 STRATEGY BREAKDOWN:\\n");
        strategyResults.forEach((strategy, result) ->
            report.append(String.format("  • %s: %d collected\\n",
                strategy, result.getCollectedFragments())));

        return report.toString();
    }

    static class Builder {
        private int originalFragments;
        private int survivingFragments;
        private int collectedFragments;
        private final Map<String, StrategyResult> strategyResults = new HashMap<>();

        public Builder originalFragments(int count) {
            this.originalFragments = count;
            return this;
        }

        public Builder survivingFragments(int count) {
            this.survivingFragments = count;
            return this;
        }

        public Builder collectedFragments(int count) {
            this.collectedFragments = count;
            return this;
        }

        public Builder addStrategyResult(String strategy, StrategyResult result) {
            this.strategyResults.put(strategy, result);
            return this;
        }

        public GarbageCollectionResult build() {
            return new GarbageCollectionResult(this);
        }
    }
}

/**
 * GARBAGE COLLECTION METRICS
 */
class GarbageCollectionMetrics {
    private final Map<String, Long> strategyExecutions = new ConcurrentHashMap<>();
    private final Map<String, Long> strategyCollections = new ConcurrentHashMap<>();
    private long totalCollections = 0;
    private long totalFragmentsCollected = 0;
    private long backgroundCleanups = 0;
    private long memoryPressureCleanups = 0;

    public void recordStrategyExecution(String strategy, StrategyResult result) {
        strategyExecutions.merge(strategy, 1L, Long::sum);
        strategyCollections.merge(strategy, (long) result.getCollectedFragments(), Long::sum);
    }

    public void recordCollection(GarbageCollectionResult result) {
        totalCollections++;
        totalFragmentsCollected += result.getCollectedFragments();
    }

    public void recordBackgroundCleanup() {
        backgroundCleanups++;
    }

    public void recordMemoryPressureCleanup() {
        memoryPressureCleanups++;
    }

    public String getFormattedMetrics() {
        StringBuilder metrics = new StringBuilder();
        metrics.append("📈 GARBAGE COLLECTION METRICS\\n");
        metrics.append("═══════════════════════════════════════\\n");
        metrics.append(String.format("Total collections: %d\\n", totalCollections));
        metrics.append(String.format("Total fragments collected: %d\\n", totalFragmentsCollected));
        metrics.append(String.format("Background cleanups: %d\\n", backgroundCleanups));
        metrics.append(String.format("Memory pressure cleanups: %d\\n", memoryPressureCleanups));

        metrics.append("\\n🎯 STRATEGY PERFORMANCE:\\n");
        strategyExecutions.forEach((strategy, executions) -> {
            long collections = strategyCollections.getOrDefault(strategy, 0L);
            double avgCollection = executions > 0 ? (double) collections / executions : 0.0;
            metrics.append(String.format("  • %s: %d executions, %.1f avg collected\\n",
                strategy, executions, avgCollection));
        });

        return metrics.toString();
    }
}

// ============================================================================
// GARBAGE COLLECTION STRATEGIES
// ============================================================================

/**
 * TIME-BASED STRATEGY - Remove context older than TTL
 */
class TimeBasedStrategy implements GarbageCollectionStrategy {
    @Override
    public StrategyResult collect(List<ContextFragment> fragments, GarbageCollectionContext context) {
        List<ContextFragment> surviving = new ArrayList<>();
        int collected = 0;

        for (ContextFragment fragment : fragments) {
            if (isExpired(fragment, context)) {
                collected++;
            } else {
                surviving.add(fragment);
            }
        }

        return new StrategyResult(surviving, collected, "Time-based expiration");
    }

    private boolean isExpired(ContextFragment fragment, GarbageCollectionContext context) {
        // Different TTL for different content types
        ChronoUnit unit = getTimeUnitForFragment(fragment);
        long ttl = getTTLForFragment(fragment);

        return ChronoUnit.MINUTES.between(fragment.getTimestamp(), context.getCurrentTime()) > ttl;
    }

    private ChronoUnit getTimeUnitForFragment(ContextFragment fragment) {
        return ChronoUnit.MINUTES; // Simplified
    }

    private long getTTLForFragment(ContextFragment fragment) {
        // Framework rules: 24 hours
        if (fragment.hasAspect("framework_rules")) return 1440;
        // Technology experiences: 7 days
        if (fragment.hasAspect("technology_experience")) return 10080;
        // Code examples: 6 hours
        if (fragment.hasAspect("code_example")) return 360;
        // Default: 12 hours
        return 720;
    }
}

/**
 * USAGE-BASED STRATEGY - Remove least recently used context
 */
class UsageBasedStrategy implements GarbageCollectionStrategy {
    @Override
    public StrategyResult collect(List<ContextFragment> fragments, GarbageCollectionContext context) {
        // Sort by last access time (most recent first)
        List<ContextFragment> sorted = fragments.stream()
            .sorted((a, b) -> compareLastAccess(a, b))
            .collect(Collectors.toList());

        // Keep only the most recently used 80%
        int keepCount = (int) (sorted.size() * 0.8);
        List<ContextFragment> surviving = sorted.subList(0, Math.min(keepCount, sorted.size()));
        int collected = sorted.size() - surviving.size();

        return new StrategyResult(surviving, collected, "LRU eviction");
    }

    private int compareLastAccess(ContextFragment a, ContextFragment b) {
        LocalDateTime aTime = getLastAccessTime(a);
        LocalDateTime bTime = getLastAccessTime(b);
        return bTime.compareTo(aTime); // Most recent first
    }

    private LocalDateTime getLastAccessTime(ContextFragment fragment) {
        // In real implementation, this would track actual access times
        return fragment.getTimestamp();
    }
}

/**
 * RELEVANCE-BASED STRATEGY - Remove context no longer relevant to current task
 */
class RelevanceBasedStrategy implements GarbageCollectionStrategy {
    @Override
    public StrategyResult collect(List<ContextFragment> fragments, GarbageCollectionContext context) {
        List<ContextFragment> surviving = new ArrayList<>();
        int collected = 0;

        for (ContextFragment fragment : fragments) {
            if (isRelevantToCurrentTask(fragment, context)) {
                surviving.add(fragment);
            } else {
                collected++;
            }
        }

        return new StrategyResult(surviving, collected, "Task relevance filtering");
    }

    private boolean isRelevantToCurrentTask(ContextFragment fragment, GarbageCollectionContext context) {
        String taskType = context.getTaskType();
        double relevance = calculateTaskRelevance(fragment, taskType);

        // Keep if relevance is above threshold
        return relevance > 0.3;
    }

    private double calculateTaskRelevance(ContextFragment fragment, String taskType) {
        // Simple relevance calculation based on aspects and content
        if (taskType.contains("api") && fragment.hasAspect("documentation")) return 0.9;
        if (taskType.contains("performance") && fragment.hasAspect("optimization")) return 0.8;
        if (taskType.contains("security") && fragment.hasAspect("security")) return 0.9;

        // Default relevance decay over time
        long ageMinutes = ChronoUnit.MINUTES.between(fragment.getTimestamp(), LocalDateTime.now());
        return Math.max(0.1, 1.0 - (ageMinutes / 1440.0)); // Decay over 24 hours
    }
}

/**
 * SIZE-BASED STRATEGY - Remove context when memory limits exceeded
 */
class SizeBasedStrategy implements GarbageCollectionStrategy {
    @Override
    public StrategyResult collect(List<ContextFragment> fragments, GarbageCollectionContext context) {
        long availableMemory = context.getAvailableMemory();
        long targetMemoryUsage = (long) (Runtime.getRuntime().maxMemory() * 0.7); // 70% target

        if (availableMemory > targetMemoryUsage) {
            return new StrategyResult(fragments, 0, "Memory usage within limits");
        }

        // Sort by size (largest first) and remove until memory pressure relieved
        List<ContextFragment> sortedBySize = fragments.stream()
            .sorted((a, b) -> Long.compare(b.getEstimatedTokens(), a.getEstimatedTokens()))
            .collect(Collectors.toList());

        List<ContextFragment> surviving = new ArrayList<>();
        long currentMemoryUsage = Runtime.getRuntime().totalMemory() - availableMemory;
        int collected = 0;

        for (ContextFragment fragment : sortedBySize) {
            long fragmentSize = estimateMemoryUsage(fragment);
            if (currentMemoryUsage - fragmentSize > targetMemoryUsage) {
                currentMemoryUsage -= fragmentSize;
                collected++;
            } else {
                surviving.add(fragment);
            }
        }

        return new StrategyResult(surviving, collected, "Memory pressure relief");
    }

    private long estimateMemoryUsage(ContextFragment fragment) {
        // Rough estimate: 100 bytes per token
        return fragment.getEstimatedTokens() * 100;
    }
}

/**
 * PRIORITY-BASED STRATEGY - Remove lower priority context first
 */
class PriorityBasedStrategy implements GarbageCollectionStrategy {
    @Override
    public StrategyResult collect(List<ContextFragment> fragments, GarbageCollectionContext context) {
        // Sort by priority (highest first)
        List<ContextFragment> sorted = fragments.stream()
            .sorted((a, b) -> Integer.compare(getPriority(b), getPriority(a)))
            .collect(Collectors.toList());

        // Keep top 70% by priority
        int keepCount = (int) (sorted.size() * 0.7);
        List<ContextFragment> surviving = sorted.subList(0, Math.min(keepCount, sorted.size()));
        int collected = sorted.size() - surviving.size();

        return new StrategyResult(surviving, collected, "Priority-based retention");
    }

    private int getPriority(ContextFragment fragment) {
        int priority = 1;

        // Framework rules have highest priority
        if (fragment.hasAspect("framework_rules")) priority += 10;
        if (fragment.hasAspect("agent_memory")) priority += 9;

        // Current task relevance
        if (fragment.getRelevanceScore() > 0.8) priority += 5;

        // Recent content
        long ageHours = ChronoUnit.HOURS.between(fragment.getTimestamp(), LocalDateTime.now());
        if (ageHours < 1) priority += 3;

        return priority;
    }
}

/**
 * SESSION-BASED STRATEGY - Clean up when sessions end
 */
class SessionBasedStrategy implements GarbageCollectionStrategy {
    @Override
    public StrategyResult collect(List<ContextFragment> fragments, GarbageCollectionContext context) {
        List<ContextFragment> surviving = new ArrayList<>();
        int collected = 0;

        for (ContextFragment fragment : fragments) {
            if (isSessionScoped(fragment) && !isCurrentSession(fragment, context)) {
                collected++;
            } else {
                surviving.add(fragment);
            }
        }

        return new StrategyResult(surviving, collected, "Session cleanup");
    }

    private boolean isSessionScoped(ContextFragment fragment) {
        return fragment.hasAspect("session_specific") ||
               fragment.hasAspect("temporary") ||
               fragment.getType().toString().contains("SESSION");
    }

    private boolean isCurrentSession(ContextFragment fragment, GarbageCollectionContext context) {
        // Check if fragment belongs to current session
        return fragment.getSourceType().equals(context.getSessionId());
    }
}

/**
 * REFERENCE COUNTING STRATEGY - Remove unreferenced context
 */
class ReferenceCountingStrategy implements GarbageCollectionStrategy {
    @Override
    public StrategyResult collect(List<ContextFragment> fragments, GarbageCollectionContext context) {
        List<ContextFragment> surviving = new ArrayList<>();
        int collected = 0;

        for (ContextFragment fragment : fragments) {
            if (getReferenceCount(fragment) > 0) {
                surviving.add(fragment);
            } else {
                collected++;
            }
        }

        return new StrategyResult(surviving, collected, "Reference counting");
    }

    private int getReferenceCount(ContextFragment fragment) {
        // In real implementation, this would track actual references
        // For now, assume recently created fragments are referenced
        long ageMinutes = ChronoUnit.MINUTES.between(fragment.getTimestamp(), LocalDateTime.now());
        return ageMinutes < 60 ? 1 : 0; // Referenced if less than 1 hour old
    }
}

/**
 * GENERATIONAL STRATEGY - Different strategies for different context ages
 */
class GenerationalStrategy implements GarbageCollectionStrategy {
    @Override
    public StrategyResult collect(List<ContextFragment> fragments, GarbageCollectionContext context) {
        List<ContextFragment> surviving = new ArrayList<>();
        int collected = 0;

        for (ContextFragment fragment : fragments) {
            long ageHours = ChronoUnit.HOURS.between(fragment.getTimestamp(), context.getCurrentTime());

            boolean shouldKeep = switch (getGeneration(ageHours)) {
                case 0 -> true; // Young generation - always keep
                case 1 -> ageHours < 24; // Middle generation - keep if < 24h
                case 2 -> fragment.getRelevanceScore() > 0.7; // Old generation - keep if highly relevant
                default -> false; // Ancient - remove
            };

            if (shouldKeep) {
                surviving.add(fragment);
            } else {
                collected++;
            }
        }

        return new StrategyResult(surviving, collected, "Generational collection");
    }

    private int getGeneration(long ageHours) {
        if (ageHours < 1) return 0; // Young (last hour)
        if (ageHours < 24) return 1; // Middle (last day)
        if (ageHours < 168) return 2; // Old (last week)
        return 3; // Ancient
    }
}