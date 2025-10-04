package com.evolver.context;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * RESULT OF CONTEXT DEDUPLICATION
 * 
 * Contains deduplicated context fragments and detailed statistics
 * about what was removed and why.
 */
public class ContextDeduplicationResult {
    private final List<ContextFragment> deduplicatedFragments;
    private final DeduplicationStats stats;
    private final long originalTokenCount;
    private final long deduplicatedTokenCount;
    
    public ContextDeduplicationResult(List<ContextFragment> deduplicatedFragments, DeduplicationStats stats) {
        this.deduplicatedFragments = deduplicatedFragments;
        this.stats = stats;
        this.originalTokenCount = calculateOriginalTokens();
        this.deduplicatedTokenCount = calculateDeduplicatedTokens();
    }
    
    public List<ContextFragment> getDeduplicatedFragments() {
        return deduplicatedFragments;
    }
    
    public DeduplicationStats getStats() {
        return stats;
    }
    
    public long getOriginalTokenCount() {
        return originalTokenCount;
    }
    
    public long getDeduplicatedTokenCount() {
        return deduplicatedTokenCount;
    }
    
    public double getTokenReduction() {
        return originalTokenCount > 0 ? 
            1.0 - ((double) deduplicatedTokenCount / originalTokenCount) : 0.0;
    }
    
    public long getTokensSaved() {
        return originalTokenCount - deduplicatedTokenCount;
    }
    
    /**
     * Get summary of deduplication by reason
     */
    public Map<String, Integer> getDeduplicationSummary() {
        return stats.getEntries().stream()
            .filter(entry -> !entry.wasIncluded())
            .collect(Collectors.groupingBy(
                DeduplicationStats.Entry::getReason,
                Collectors.summingInt(entry -> 1)
            ));
    }
    
    /**
     * Get formatted report of deduplication results
     */
    public String getFormattedReport() {
        StringBuilder report = new StringBuilder();
        report.append("📊 CONTEXT DEDUPLICATION REPORT\n");
        report.append("═══════════════════════════════════\n");
        report.append(String.format("Original fragments: %d\n", stats.getTotalFragments()));
        report.append(String.format("Included fragments: %d\n", stats.getIncludedFragments()));
        report.append(String.format("Excluded fragments: %d\n", stats.getExcludedFragments()));
        report.append(String.format("Deferred fragments: %d\n", stats.getDeferredFragments()));
        report.append(String.format("Token reduction: %.1f%%\n", getTokenReduction() * 100));
        report.append(String.format("Tokens saved: %d\n", getTokensSaved()));
        report.append("\n📋 EXCLUSION REASONS:\n");
        
        getDeduplicationSummary().entrySet().stream()
            .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
            .forEach(entry -> 
                report.append(String.format("  • %s: %d fragments\n", 
                    formatReason(entry.getKey()), entry.getValue())));
        
        return report.toString();
    }
    
    private String formatReason(String reason) {
        return switch (reason) {
            case "EXACT_DUPLICATE" -> "Exact duplicates";
            case "SEMANTIC_DUPLICATE" -> "Semantic duplicates";
            case "FRAMEWORK_REPETITION" -> "Framework overhead repetition";
            case "AGENT_CHARACTERISTIC_REPETITION" -> "Agent characteristic repetition";
            case "TECH_EXPERIENCE_OVERLAP" -> "Technology experience overlap";
            case "TASK_CONTEXT_OVERLAP" -> "Task context overlap";
            case "PROGRESSIVE_DISCLOSURE" -> "Progressive disclosure deferral";
            case "STALE_CONTEXT" -> "Stale/outdated context";
            default -> reason;
        };
    }
    
    private long calculateOriginalTokens() {
        return stats.getEntries().stream()
            .mapToLong(entry -> entry.getFragment().getEstimatedTokens())
            .sum();
    }
    
    private long calculateDeduplicatedTokens() {
        return deduplicatedFragments.stream()
            .mapToLong(ContextFragment::getEstimatedTokens)
            .sum();
    }
}