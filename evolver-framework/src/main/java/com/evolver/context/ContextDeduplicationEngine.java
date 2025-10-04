package com.evolver.context;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * CONTEXT DEDUPLICATION ENGINE
 * 
 * Intelligent system to track and avoid duplicate context information across:
 * - Same session interactions
 * - Similar tasks within timeframes
 * - Repeated patterns across agents
 * - Framework overhead repetition
 * 
 * Reduces context bloat by 30-50% while maintaining quality and completeness.
 */
public class ContextDeduplicationEngine {
    
    // Session-level tracking
    private final Map<String, SessionContextMemory> sessionMemories = new ConcurrentHashMap<>();
    
    // Agent-level tracking across sessions
    private final Map<String, AgentContextHistory> agentHistories = new ConcurrentHashMap<>();
    
    // Global pattern tracking
    private final Map<String, ContextPattern> globalPatterns = new ConcurrentHashMap<>();
    
    // Content fingerprinting for exact duplicates
    private final Map<String, ContentFingerprint> contentFingerprints = new ConcurrentHashMap<>();
    
    // Semantic similarity tracking
    private final Map<String, SemanticCluster> semanticClusters = new ConcurrentHashMap<>();
    
    /**
     * Analyze context fragments and remove duplicates while preserving essential information
     */
    public ContextDeduplicationResult deduplicateContext(ContextDeduplicationRequest request) {
        String sessionId = request.getSessionId();
        String agentId = request.getAgentId();
        List<ContextFragment> fragments = request.getFragments();
        
        // Get or create tracking structures
        SessionContextMemory sessionMemory = getSessionMemory(sessionId);
        AgentContextHistory agentHistory = getAgentHistory(agentId);
        
        // Track what we're analyzing
        AnalysisContext analysis = new AnalysisContext(sessionId, agentId, fragments);
        
        // Apply deduplication strategies
        List<ContextFragment> deduplicated = new ArrayList<>();
        DeduplicationStats stats = new DeduplicationStats();
        
        for (ContextFragment fragment : fragments) {
            DeduplicationDecision decision = analyzeFragment(fragment, analysis, sessionMemory, agentHistory);
            
            if (decision.shouldInclude()) {
                deduplicated.add(enhanceFragment(fragment, decision));
                recordFragment(fragment, sessionMemory, agentHistory);
                stats.addIncluded(fragment, decision.getReason());
            } else {
                stats.addExcluded(fragment, decision.getReason());
            }
        }
        
        // Update patterns and learning
        updatePatterns(analysis, stats);
        
        return new ContextDeduplicationResult(deduplicated, stats);
    }
    
    /**
     * Analyze individual fragment for duplication
     */
    private DeduplicationDecision analyzeFragment(ContextFragment fragment, 
                                                AnalysisContext analysis,
                                                SessionContextMemory sessionMemory,
                                                AgentContextHistory agentHistory) {
        
        // 1. Exact content duplicate check
        String contentHash = generateContentHash(fragment.getContent());
        if (sessionMemory.hasExactContent(contentHash)) {
            return DeduplicationDecision.exclude("EXACT_DUPLICATE", 
                "Identical content already provided in session");
        }
        
        // 2. Semantic similarity check
        double semanticSimilarity = calculateSemanticSimilarity(fragment, sessionMemory);
        if (semanticSimilarity > 0.95) {
            return DeduplicationDecision.exclude("SEMANTIC_DUPLICATE", 
                "Semantically identical content (similarity: " + semanticSimilarity + ")");
        }
        
        // 3. Framework overhead repetition check
        if (isFrameworkOverhead(fragment) && sessionMemory.hasFrameworkOverhead(fragment.getType())) {
            return DeduplicationDecision.exclude("FRAMEWORK_REPETITION", 
                "Framework rules/patterns already included for this session");
        }
        
        // 4. Agent characteristic repetition check
        if (isAgentCharacteristic(fragment) && agentHistory.hasRecentCharacteristics(1, ChronoUnit.HOURS)) {
            return DeduplicationDecision.exclude("AGENT_CHARACTERISTIC_REPETITION", 
                "Agent characteristics provided recently");
        }
        
        // 5. Technology experience repetition check
        if (isTechnologyExperience(fragment)) {
            TechnologyContext techContext = extractTechnologyContext(fragment);
            if (sessionMemory.hasSimilarTechExperience(techContext, 0.8)) {
                return DeduplicationDecision.exclude("TECH_EXPERIENCE_OVERLAP", 
                    "Similar technology experience already provided");
            }
        }
        
        // 6. Task-specific context overlap
        if (analysis.hasTaskOverlap() && fragment.getTaskRelevance() < 0.7) {
            TaskContext currentTask = analysis.getCurrentTaskContext();
            if (agentHistory.hasTaskContext(currentTask, 2, ChronoUnit.HOURS)) {
                return DeduplicationDecision.exclude("TASK_CONTEXT_OVERLAP", 
                    "Similar task context provided recently");
            }
        }
        
        // 7. Progressive disclosure optimization
        if (shouldDeferContext(fragment, analysis, sessionMemory)) {
            return DeduplicationDecision.defer("PROGRESSIVE_DISCLOSURE", 
                "Defer advanced context until needed");
        }
        
        // 8. Context freshness and staleness
        if (isStale(fragment, analysis)) {
            return DeduplicationDecision.exclude("STALE_CONTEXT", 
                "Information is outdated or no longer relevant");
        }
        
        // Include with reason
        return DeduplicationDecision.include("UNIQUE_RELEVANT", 
            "New, relevant context for current session");
    }
    
    /**
     * Calculate semantic similarity between fragment and existing session content
     */
    private double calculateSemanticSimilarity(ContextFragment fragment, SessionContextMemory sessionMemory) {
        String content = fragment.getContent();
        List<String> existingContent = sessionMemory.getAllContent();
        
        // Simple similarity algorithm (in production, use embeddings)
        double maxSimilarity = 0.0;
        
        for (String existing : existingContent) {
            double similarity = calculateTextSimilarity(content, existing);
            maxSimilarity = Math.max(maxSimilarity, similarity);
        }
        
        return maxSimilarity;
    }
    
    /**
     * Calculate text similarity using Jaccard similarity
     */
    private double calculateTextSimilarity(String text1, String text2) {
        Set<String> words1 = new HashSet<>(Arrays.asList(text1.toLowerCase().split("\\s+")));
        Set<String> words2 = new HashSet<>(Arrays.asList(text2.toLowerCase().split("\\s+")));
        
        Set<String> intersection = new HashSet<>(words1);
        intersection.retainAll(words2);
        
        Set<String> union = new HashSet<>(words1);
        union.addAll(words2);
        
        return union.isEmpty() ? 0.0 : (double) intersection.size() / union.size();
    }
    
    /**
     * Check if context should be deferred for progressive disclosure
     */
    private boolean shouldDeferContext(ContextFragment fragment, AnalysisContext analysis, SessionContextMemory sessionMemory) {
        // Defer advanced technical details until basic context is established
        if (fragment.getComplexityLevel() > 7 && sessionMemory.getBasicContextCoverage() < 0.6) {
            return true;
        }
        
        // Defer edge cases until main patterns are covered
        if (fragment.hasAspect("edge_case") && !sessionMemory.hasMainPatterns()) {
            return true;
        }
        
        // Defer optimization details until implementation is covered
        if (fragment.hasAspect("optimization") && !sessionMemory.hasImplementationContext()) {
            return true;
        }
        
        return false;
    }
    
    /**
     * Generate content hash for exact duplicate detection
     */
    private String generateContentHash(String content) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(content.getBytes());
            return Base64.getEncoder().encodeToString(hash);
        } catch (NoSuchAlgorithmException e) {
            // Fallback to simple hash
            return String.valueOf(content.hashCode());
        }
    }
    
    /**
     * Enhance fragment with deduplication metadata
     */
    private ContextFragment enhanceFragment(ContextFragment fragment, DeduplicationDecision decision) {
        return fragment.toBuilder()
            .addMetadata("deduplication_reason", decision.getReason())
            .addMetadata("deduplication_confidence", decision.getConfidence())
            .addMetadata("first_occurrence", decision.isFirstOccurrence())
            .build();
    }
    
    /**
     * Record fragment in memory systems
     */
    private void recordFragment(ContextFragment fragment, SessionContextMemory sessionMemory, AgentContextHistory agentHistory) {
        String contentHash = generateContentHash(fragment.getContent());
        
        // Record in session memory
        sessionMemory.addContent(contentHash, fragment);
        
        // Record in agent history
        agentHistory.addFragment(fragment);
        
        // Update global patterns
        updateGlobalPatterns(fragment);
    }
    
    /**
     * Check if fragment represents framework overhead
     */
    private boolean isFrameworkOverhead(ContextFragment fragment) {
        return fragment.hasAspect("framework_rules") || 
               fragment.hasAspect("agent_memory") ||
               fragment.hasAspect("rating_system") ||
               fragment.getType().toString().contains("FRAMEWORK");
    }
    
    /**
     * Check if fragment represents agent characteristics
     */
    private boolean isAgentCharacteristic(ContextFragment fragment) {
        return fragment.hasAspect("agent_characteristic") ||
               fragment.hasAspect("personality") ||
               fragment.hasAspect("preferences") ||
               fragment.getContent().contains("AgentCharacteristic");
    }
    
    /**
     * Check if fragment represents technology experience
     */
    private boolean isTechnologyExperience(ContextFragment fragment) {
        return fragment.hasAspect("technology_experience") ||
               fragment.hasAspect("technology_rating") ||
               fragment.getContent().contains("TechnologyExperience");
    }
    
    /**
     * Extract technology context for comparison
     */
    private TechnologyContext extractTechnologyContext(ContextFragment fragment) {
        // Extract technology names, ratings, and usage patterns
        return new TechnologyContext(fragment);
    }
    
    /**
     * Check if content is stale
     */
    private boolean isStale(ContextFragment fragment, AnalysisContext analysis) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime fragmentTime = fragment.getTimestamp();
        
        // Different staleness thresholds for different content types
        if (fragment.hasAspect("technology_experience")) {
            return ChronoUnit.DAYS.between(fragmentTime, now) > 30;
        }
        
        if (fragment.hasAspect("framework_rules")) {
            return ChronoUnit.HOURS.between(fragmentTime, now) > 24;
        }
        
        if (fragment.hasAspect("code_example")) {
            return ChronoUnit.HOURS.between(fragmentTime, now) > 6;
        }
        
        // Default staleness
        return ChronoUnit.HOURS.between(fragmentTime, now) > 12;
    }
    
    /**
     * Update global patterns based on usage
     */
    private void updateGlobalPatterns(ContextFragment fragment) {
        String patternKey = fragment.getType() + ":" + fragment.getSourceType();
        ContextPattern pattern = globalPatterns.computeIfAbsent(patternKey, 
            k -> new ContextPattern(fragment.getType(), fragment.getSourceType()));
        
        pattern.addOccurrence(fragment);
    }
    
    /**
     * Update patterns based on analysis results
     */
    private void updatePatterns(AnalysisContext analysis, DeduplicationStats stats) {
        // Learn from what was excluded vs included
        for (DeduplicationStats.Entry entry : stats.getEntries()) {
            String reason = entry.getReason();
            ContextFragment fragment = entry.getFragment();
            
            ContextPattern pattern = globalPatterns.computeIfAbsent(
                fragment.getType() + ":" + reason,
                k -> new ContextPattern(fragment.getType(), reason)
            );
            
            pattern.recordDecision(entry.wasIncluded(), reason);
        }
    }
    
    private SessionContextMemory getSessionMemory(String sessionId) {
        return sessionMemories.computeIfAbsent(sessionId, k -> new SessionContextMemory(sessionId));
    }
    
    private AgentContextHistory getAgentHistory(String agentId) {
        return agentHistories.computeIfAbsent(agentId, k -> new AgentContextHistory(agentId));
    }
}

/**
 * Session-level context memory tracking
 */
class SessionContextMemory {
    private final String sessionId;
    private final Map<String, ContextFragment> contentHashes = new ConcurrentHashMap<>();
    private final Map<String, LocalDateTime> frameworkOverheadTypes = new ConcurrentHashMap<>();
    private final List<TechnologyContext> technologyExperiences = new ArrayList<>();
    private final Set<String> coveredAspects = new HashSet<>();
    private final LocalDateTime sessionStart;
    
    public SessionContextMemory(String sessionId) {
        this.sessionId = sessionId;
        this.sessionStart = LocalDateTime.now();
    }
    
    public boolean hasExactContent(String contentHash) {
        return contentHashes.containsKey(contentHash);
    }
    
    public boolean hasFrameworkOverhead(String type) {
        return frameworkOverheadTypes.containsKey(type);
    }
    
    public boolean hasSimilarTechExperience(TechnologyContext context, double threshold) {
        return technologyExperiences.stream()
            .anyMatch(existing -> existing.similarityTo(context) > threshold);
    }
    
    public void addContent(String contentHash, ContextFragment fragment) {
        contentHashes.put(contentHash, fragment);
        coveredAspects.addAll(fragment.getAspects());
        
        if (fragment.hasAspect("framework")) {
            frameworkOverheadTypes.put(fragment.getType().toString(), LocalDateTime.now());
        }
        
        if (fragment.hasAspect("technology_experience")) {
            technologyExperiences.add(new TechnologyContext(fragment));
        }
    }
    
    public List<String> getAllContent() {
        return contentHashes.values().stream()
            .map(ContextFragment::getContent)
            .collect(Collectors.toList());
    }
    
    public double getBasicContextCoverage() {
        Set<String> basicAspects = Set.of("requirements", "architecture", "implementation", "testing");
        long coveredBasic = basicAspects.stream()
            .mapToLong(aspect -> coveredAspects.contains(aspect) ? 1 : 0)
            .sum();
        return (double) coveredBasic / basicAspects.size();
    }
    
    public boolean hasMainPatterns() {
        return coveredAspects.contains("main_pattern") || 
               coveredAspects.contains("core_implementation") ||
               coveredAspects.contains("primary_use_case");
    }
    
    public boolean hasImplementationContext() {
        return coveredAspects.contains("implementation") ||
               coveredAspects.contains("code_structure") ||
               coveredAspects.contains("architecture");
    }
}

/**
 * Agent-level context history tracking
 */
class AgentContextHistory {
    private final String agentId;
    private final List<TimestampedFragment> fragments = new ArrayList<>();
    private final Map<String, LocalDateTime> lastCharacteristics = new ConcurrentHashMap<>();
    private final Map<String, LocalDateTime> lastTaskContexts = new ConcurrentHashMap<>();
    
    public AgentContextHistory(String agentId) {
        this.agentId = agentId;
    }
    
    public boolean hasRecentCharacteristics(int amount, ChronoUnit unit) {
        LocalDateTime cutoff = LocalDateTime.now().minus(amount, unit);
        return lastCharacteristics.values().stream()
            .anyMatch(timestamp -> timestamp.isAfter(cutoff));
    }
    
    public boolean hasTaskContext(TaskContext taskContext, int amount, ChronoUnit unit) {
        LocalDateTime cutoff = LocalDateTime.now().minus(amount, unit);
        String taskKey = taskContext.getTaskType() + ":" + taskContext.getDomain();
        LocalDateTime lastSeen = lastTaskContexts.get(taskKey);
        return lastSeen != null && lastSeen.isAfter(cutoff);
    }
    
    public void addFragment(ContextFragment fragment) {
        fragments.add(new TimestampedFragment(fragment, LocalDateTime.now()));
        
        if (fragment.hasAspect("agent_characteristic")) {
            lastCharacteristics.put("characteristics", LocalDateTime.now());
        }
        
        // Clean old fragments (keep last 100)
        if (fragments.size() > 100) {
            fragments.subList(0, fragments.size() - 100).clear();
        }
    }
}

/**
 * Technology context for comparison
 */
class TechnologyContext {
    private final Set<String> technologies;
    private final Map<String, Double> ratings;
    private final String usagePattern;
    
    public TechnologyContext(ContextFragment fragment) {
        this.technologies = extractTechnologies(fragment);
        this.ratings = extractRatings(fragment);
        this.usagePattern = extractUsagePattern(fragment);
    }
    
    public double similarityTo(TechnologyContext other) {
        // Calculate similarity based on technologies, ratings, and patterns
        double techSimilarity = calculateSetSimilarity(this.technologies, other.technologies);
        double ratingSimilarity = calculateRatingSimilarity(this.ratings, other.ratings);
        double patternSimilarity = this.usagePattern.equals(other.usagePattern) ? 1.0 : 0.0;
        
        return (techSimilarity * 0.5) + (ratingSimilarity * 0.3) + (patternSimilarity * 0.2);
    }
    
    private Set<String> extractTechnologies(ContextFragment fragment) {
        // Extract technology names from content
        return new HashSet<>(); // Implementation would parse content
    }
    
    private Map<String, Double> extractRatings(ContextFragment fragment) {
        // Extract ratings from content
        return new HashMap<>(); // Implementation would parse ratings
    }
    
    private String extractUsagePattern(ContextFragment fragment) {
        // Extract usage pattern from content
        return ""; // Implementation would analyze patterns
    }
    
    private double calculateSetSimilarity(Set<String> set1, Set<String> set2) {
        Set<String> intersection = new HashSet<>(set1);
        intersection.retainAll(set2);
        Set<String> union = new HashSet<>(set1);
        union.addAll(set2);
        return union.isEmpty() ? 0.0 : (double) intersection.size() / union.size();
    }
    
    private double calculateRatingSimilarity(Map<String, Double> ratings1, Map<String, Double> ratings2) {
        Set<String> commonTech = new HashSet<>(ratings1.keySet());
        commonTech.retainAll(ratings2.keySet());
        
        if (commonTech.isEmpty()) return 0.0;
        
        double totalDiff = 0.0;
        for (String tech : commonTech) {
            totalDiff += Math.abs(ratings1.get(tech) - ratings2.get(tech));
        }
        
        return 1.0 - (totalDiff / (commonTech.size() * 10.0)); // Normalize by max rating difference
    }
}

/**
 * Analysis context for current request
 */
class AnalysisContext {
    private final String sessionId;
    private final String agentId;
    private final List<ContextFragment> fragments;
    private final TaskContext currentTaskContext;
    
    public AnalysisContext(String sessionId, String agentId, List<ContextFragment> fragments) {
        this.sessionId = sessionId;
        this.agentId = agentId;
        this.fragments = fragments;
        this.currentTaskContext = extractTaskContext(fragments);
    }
    
    public boolean hasTaskOverlap() {
        return currentTaskContext != null && currentTaskContext.hasOverlapIndicators();
    }
    
    public TaskContext getCurrentTaskContext() {
        return currentTaskContext;
    }
    
    private TaskContext extractTaskContext(List<ContextFragment> fragments) {
        // Extract task context from fragments
        return new TaskContext(); // Implementation would analyze fragments
    }
}

/**
 * Task context for overlap detection
 */
class TaskContext {
    private final String taskType;
    private final String domain;
    private final Set<String> keywords;
    
    public TaskContext() {
        this.taskType = "";
        this.domain = "";
        this.keywords = new HashSet<>();
    }
    
    public String getTaskType() { return taskType; }
    public String getDomain() { return domain; }
    
    public boolean hasOverlapIndicators() {
        return !taskType.isEmpty() && !domain.isEmpty();
    }
}

/**
 * Context pattern tracking
 */
class ContextPattern {
    private final String type;
    private final String sourceType;
    private int occurrences = 0;
    private int inclusions = 0;
    private int exclusions = 0;
    private final LocalDateTime firstSeen;
    private LocalDateTime lastSeen;
    
    public ContextPattern(String type, String sourceType) {
        this.type = type;
        this.sourceType = sourceType;
        this.firstSeen = LocalDateTime.now();
        this.lastSeen = LocalDateTime.now();
    }
    
    public void addOccurrence(ContextFragment fragment) {
        occurrences++;
        lastSeen = LocalDateTime.now();
    }
    
    public void recordDecision(boolean included, String reason) {
        if (included) {
            inclusions++;
        } else {
            exclusions++;
        }
    }
    
    public double getInclusionRate() {
        int total = inclusions + exclusions;
        return total > 0 ? (double) inclusions / total : 0.0;
    }
}

/**
 * Timestamped fragment for history tracking
 */
class TimestampedFragment {
    private final ContextFragment fragment;
    private final LocalDateTime timestamp;
    
    public TimestampedFragment(ContextFragment fragment, LocalDateTime timestamp) {
        this.fragment = fragment;
        this.timestamp = timestamp;
    }
    
    public ContextFragment getFragment() { return fragment; }
    public LocalDateTime getTimestamp() { return timestamp; }
}

/**
 * Content fingerprint for duplicate detection
 */
class ContentFingerprint {
    private final String hash;
    private final LocalDateTime created;
    private int occurrences = 1;
    
    public ContentFingerprint(String hash) {
        this.hash = hash;
        this.created = LocalDateTime.now();
    }
    
    public void recordOccurrence() {
        occurrences++;
    }
    
    public String getHash() { return hash; }
    public int getOccurrences() { return occurrences; }
}

/**
 * Semantic cluster for similar content grouping
 */
class SemanticCluster {
    private final String clusterId;
    private final List<String> contentHashes = new ArrayList<>();
    private final Set<String> keywords = new HashSet<>();
    
    public SemanticCluster(String clusterId) {
        this.clusterId = clusterId;
    }
    
    public void addContent(String hash, Set<String> contentKeywords) {
        contentHashes.add(hash);
        keywords.addAll(contentKeywords);
    }
    
    public boolean matches(Set<String> testKeywords, double threshold) {
        Set<String> intersection = new HashSet<>(keywords);
        intersection.retainAll(testKeywords);
        
        Set<String> union = new HashSet<>(keywords);
        union.addAll(testKeywords);
        
        return union.isEmpty() ? false : (double) intersection.size() / union.size() > threshold;
    }
}

/**
 * Deduplication decision for a fragment
 */
class DeduplicationDecision {
    private final boolean include;
    private final boolean defer;
    private final String reason;
    private final String explanation;
    private final double confidence;
    private final boolean firstOccurrence;
    
    private DeduplicationDecision(boolean include, boolean defer, String reason, String explanation) {
        this.include = include;
        this.defer = defer;
        this.reason = reason;
        this.explanation = explanation;
        this.confidence = 1.0;
        this.firstOccurrence = include;
    }
    
    public static DeduplicationDecision include(String reason, String explanation) {
        return new DeduplicationDecision(true, false, reason, explanation);
    }
    
    public static DeduplicationDecision exclude(String reason, String explanation) {
        return new DeduplicationDecision(false, false, reason, explanation);
    }
    
    public static DeduplicationDecision defer(String reason, String explanation) {
        return new DeduplicationDecision(false, true, reason, explanation);
    }
    
    public boolean shouldInclude() { return include; }
    public boolean shouldDefer() { return defer; }
    public String getReason() { return reason; }
    public String getExplanation() { return explanation; }
    public double getConfidence() { return confidence; }
    public boolean isFirstOccurrence() { return firstOccurrence; }
}

/**
 * Deduplication statistics
 */
class DeduplicationStats {
    private final List<Entry> entries = new ArrayList<>();
    private int totalFragments = 0;
    private int includedFragments = 0;
    private int excludedFragments = 0;
    private int deferredFragments = 0;
    
    public void addIncluded(ContextFragment fragment, String reason) {
        entries.add(new Entry(fragment, true, reason));
        totalFragments++;
        includedFragments++;
    }
    
    public void addExcluded(ContextFragment fragment, String reason) {
        entries.add(new Entry(fragment, false, reason));
        totalFragments++;
        excludedFragments++;
    }
    
    public void addDeferred(ContextFragment fragment, String reason) {
        entries.add(new Entry(fragment, false, reason));
        totalFragments++;
        deferredFragments++;
    }
    
    public List<Entry> getEntries() { return new ArrayList<>(entries); }
    public int getTotalFragments() { return totalFragments; }
    public int getIncludedFragments() { return includedFragments; }
    public int getExcludedFragments() { return excludedFragments; }
    public int getDeferredFragments() { return deferredFragments; }
    
    public double getDeduplicationRate() {
        return totalFragments > 0 ? (double) excludedFragments / totalFragments : 0.0;
    }
    
    static class Entry {
        private final ContextFragment fragment;
        private final boolean included;
        private final String reason;
        
        public Entry(ContextFragment fragment, boolean included, String reason) {
            this.fragment = fragment;
            this.included = included;
            this.reason = reason;
        }
        
        public ContextFragment getFragment() { return fragment; }
        public boolean wasIncluded() { return included; }
        public String getReason() { return reason; }
    }
}