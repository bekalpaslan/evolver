package com.evolver.agent;

import java.time.LocalDateTime;
import java.util.*;

/**
 * AGENT CONTEXT STRUCTURE SYSTEM
 * 
 * Agents gradually develop and refine their context explanation structures
 * through experimentation and learning. This system helps agents find the
 * most effective ways to provide context and communicate with users.
 */
public class AgentContextStructure {
    
    private static final Map<String, ContextPattern> contextPatterns = new HashMap<>();
    private static final Map<String, List<ContextExperiment>> contextExperiments = new HashMap<>();
    
    /**
     * Record a context structure experiment by an agent
     */
    public static void recordContextExperiment(String agentId, ContextExperiment experiment) {
        contextExperiments.computeIfAbsent(agentId, k -> new ArrayList<>()).add(experiment);
        
        // Update global context patterns based on successful experiments
        if (experiment.wasSuccessful() && experiment.getEffectivenessScore() >= 7.0) {
            String patternKey = experiment.getContextType() + ":" + experiment.getStructureType();
            
            ContextPattern pattern = contextPatterns.computeIfAbsent(patternKey, 
                k -> new ContextPattern(experiment.getContextType(), experiment.getStructureType()));
            pattern.addSuccessfulExperiment(experiment);
            
            System.out.println("[CONTEXT-INTEL] " + agentId + " found effective " + 
                             experiment.getContextType() + " pattern: " + experiment.getStructureName() +
                             " (Score: " + experiment.getEffectivenessScore() + "/10)");
        }
    }
    
    /**
     * Get recommended context structures for a specific context type
     */
    public static List<ContextStructureRecommendation> getRecommendedStructures(String contextType, String taskType) {
        return contextPatterns.values().stream()
            .filter(pattern -> pattern.getContextType().equalsIgnoreCase(contextType))
            .filter(pattern -> pattern.isApplicableFor(taskType))
            .sorted((a, b) -> Double.compare(b.getAverageEffectiveness(), a.getAverageEffectiveness()))
            .map(pattern -> new ContextStructureRecommendation(pattern, taskType))
            .limit(5)
            .collect(ArrayList::new, (list, item) -> list.add(item), (list1, list2) -> list1.addAll(list2));
    }
    
    /**
     * Create a new context experiment for an agent to try
     */
    public static ContextExperiment.Builder createExperiment(String agentId, String contextType) {
        return new ContextExperiment.Builder(agentId, contextType);
    }
    
    /**
     * Get agent's context learning progress
     */
    public static AgentContextProgress getAgentProgress(String agentId) {
        List<ContextExperiment> experiments = contextExperiments.getOrDefault(agentId, new ArrayList<>());
        return new AgentContextProgress(agentId, experiments);
    }
    
    /**
     * Find the most effective context structure for a specific scenario
     */
    public static Optional<ContextPattern> findBestPattern(String contextType, String taskType, double minEffectiveness) {
        return contextPatterns.values().stream()
            .filter(pattern -> pattern.getContextType().equalsIgnoreCase(contextType))
            .filter(pattern -> pattern.isApplicableFor(taskType))
            .filter(pattern -> pattern.getAverageEffectiveness() >= minEffectiveness)
            .max(Comparator.comparing(ContextPattern::getAverageEffectiveness));
    }
    
    /**
     * Get trending context structures that are gaining popularity
     */
    public static List<TrendingContextStructure> getTrendingStructures(int days) {
        LocalDateTime cutoff = LocalDateTime.now().minusDays(days);
        Map<String, TrendingContextStructure> trends = new HashMap<>();
        
        contextExperiments.values().forEach(experiments -> {
            experiments.stream()
                .filter(exp -> exp.getTimestamp().isAfter(cutoff))
                .filter(ContextExperiment::wasSuccessful)
                .forEach(exp -> {
                    String key = exp.getContextType() + ":" + exp.getStructureType();
                    trends.computeIfAbsent(key, k -> new TrendingContextStructure(
                        exp.getContextType(), exp.getStructureType()))
                        .addExperiment(exp);
                });
        });
        
        return trends.values().stream()
            .filter(trend -> trend.getRecentExperiments() >= 3)
            .sorted((a, b) -> Double.compare(b.getTrendScore(), a.getTrendScore()))
            .limit(10)
            .collect(ArrayList::new, (list, item) -> list.add(item), (list1, list2) -> list1.addAll(list2));
    }
}

/**
 * Context experiment - agent's attempt at a specific context structure
 */
class ContextExperiment {
    private final String agentId;
    private final String contextType;
    private final String structureType;
    private final String structureName;
    private final Map<String, Object> structureParameters;
    private final LocalDateTime timestamp;
    
    private final double effectivenessScore; // 0.0 - 10.0
    private final double userSatisfaction; // 0.0 - 10.0
    private final double clarityScore; // 0.0 - 10.0
    private final double completenessScore; // 0.0 - 10.0
    private final boolean successful;
    
    private final String feedback;
    private final List<String> workingAspects;
    private final List<String> improvementAreas;
    private final String taskType;
    
    private ContextExperiment(Builder builder) {
        this.agentId = builder.agentId;
        this.contextType = builder.contextType;
        this.structureType = builder.structureType;
        this.structureName = builder.structureName;
        this.structureParameters = new HashMap<>(builder.structureParameters);
        this.timestamp = LocalDateTime.now();
        
        this.effectivenessScore = roundToDecimal(builder.effectivenessScore, 1);
        this.userSatisfaction = roundToDecimal(builder.userSatisfaction, 1);
        this.clarityScore = roundToDecimal(builder.clarityScore, 1);
        this.completenessScore = roundToDecimal(builder.completenessScore, 1);
        this.successful = builder.successful;
        
        this.feedback = builder.feedback;
        this.workingAspects = new ArrayList<>(builder.workingAspects);
        this.improvementAreas = new ArrayList<>(builder.improvementAreas);
        this.taskType = builder.taskType;
    }
    
    public static class Builder {
        private final String agentId;
        private final String contextType;
        private String structureType = "";
        private String structureName = "";
        private Map<String, Object> structureParameters = new HashMap<>();
        
        private double effectivenessScore = 0.0;
        private double userSatisfaction = 0.0;
        private double clarityScore = 0.0;
        private double completenessScore = 0.0;
        private boolean successful = false;
        
        private String feedback = "";
        private List<String> workingAspects = new ArrayList<>();
        private List<String> improvementAreas = new ArrayList<>();
        private String taskType = "";
        
        public Builder(String agentId, String contextType) {
            this.agentId = agentId;
            this.contextType = contextType;
        }
        
        public Builder structureType(String type) {
            this.structureType = type;
            return this;
        }
        
        public Builder structureName(String name) {
            this.structureName = name;
            return this;
        }
        
        public Builder parameter(String key, Object value) {
            this.structureParameters.put(key, value);
            return this;
        }
        
        public Builder effectivenessScore(double score) {
            this.effectivenessScore = score;
            return this;
        }
        
        public Builder userSatisfaction(double score) {
            this.userSatisfaction = score;
            return this;
        }
        
        public Builder clarityScore(double score) {
            this.clarityScore = score;
            return this;
        }
        
        public Builder completenessScore(double score) {
            this.completenessScore = score;
            return this;
        }
        
        public Builder successful(boolean success) {
            this.successful = success;
            return this;
        }
        
        public Builder feedback(String feedback) {
            this.feedback = feedback;
            return this;
        }
        
        public Builder workingAspect(String aspect) {
            this.workingAspects.add(aspect);
            return this;
        }
        
        public Builder improvementArea(String area) {
            this.improvementAreas.add(area);
            return this;
        }
        
        public Builder taskType(String type) {
            this.taskType = type;
            return this;
        }
        
        public ContextExperiment build() {
            return new ContextExperiment(this);
        }
    }
    
    // Getters
    public String getAgentId() { return agentId; }
    public String getContextType() { return contextType; }
    public String getStructureType() { return structureType; }
    public String getStructureName() { return structureName; }
    public Map<String, Object> getStructureParameters() { return new HashMap<>(structureParameters); }
    public LocalDateTime getTimestamp() { return timestamp; }
    public double getEffectivenessScore() { return effectivenessScore; }
    public double getUserSatisfaction() { return userSatisfaction; }
    public double getClarityScore() { return clarityScore; }
    public double getCompletenessScore() { return completenessScore; }
    public boolean wasSuccessful() { return successful; }
    public String getFeedback() { return feedback; }
    public List<String> getWorkingAspects() { return new ArrayList<>(workingAspects); }
    public List<String> getImprovementAreas() { return new ArrayList<>(improvementAreas); }
    public String getTaskType() { return taskType; }
    
    private double roundToDecimal(double value, int decimalPlaces) {
        double multiplier = Math.pow(10, decimalPlaces);
        return Math.round(Math.max(0, Math.min(10, value)) * multiplier) / multiplier;
    }
}

/**
 * Context pattern - proven effective context structure
 */
class ContextPattern {
    private final String contextType;
    private final String structureType;
    private final List<ContextExperiment> successfulExperiments;
    private final Set<String> applicableTaskTypes;
    
    public ContextPattern(String contextType, String structureType) {
        this.contextType = contextType;
        this.structureType = structureType;
        this.successfulExperiments = new ArrayList<>();
        this.applicableTaskTypes = new HashSet<>();
    }
    
    public void addSuccessfulExperiment(ContextExperiment experiment) {
        successfulExperiments.add(experiment);
        applicableTaskTypes.add(experiment.getTaskType());
    }
    
    public double getAverageEffectiveness() {
        return successfulExperiments.stream()
            .mapToDouble(ContextExperiment::getEffectivenessScore)
            .average()
            .orElse(0.0);
    }
    
    public boolean isApplicableFor(String taskType) {
        return applicableTaskTypes.contains(taskType) || applicableTaskTypes.contains("general");
    }
    
    public String getContextType() { return contextType; }
    public String getStructureType() { return structureType; }
    public int getSuccessCount() { return successfulExperiments.size(); }
}

/**
 * Context structure recommendation based on proven patterns
 */
class ContextStructureRecommendation {
    public final String contextType;
    public final String structureType;
    public final double effectiveness;
    public final int provenUses;
    public final String description;
    public final List<String> keyFeatures;
    
    public ContextStructureRecommendation(ContextPattern pattern, String taskType) {
        this.contextType = pattern.getContextType();
        this.structureType = pattern.getStructureType();
        this.effectiveness = pattern.getAverageEffectiveness();
        this.provenUses = pattern.getSuccessCount();
        this.description = generateDescription(pattern, taskType);
        this.keyFeatures = extractKeyFeatures(pattern);
    }
    
    private String generateDescription(ContextPattern pattern, String taskType) {
        return String.format("Proven %s structure for %s tasks (%.1f/10 effectiveness, %d successful uses)",
            pattern.getStructureType(), taskType, pattern.getAverageEffectiveness(), pattern.getSuccessCount());
    }
    
    private List<String> extractKeyFeatures(ContextPattern pattern) {
        // Analyze successful experiments to extract common features
        List<String> features = new ArrayList<>();
        features.add("Proven effectiveness: " + String.format("%.1f/10", pattern.getAverageEffectiveness()));
        features.add("Success rate: " + pattern.getSuccessCount() + " proven uses");
        return features;
    }
}

/**
 * Agent's context learning progress tracking
 */
class AgentContextProgress {
    public final String agentId;
    public final int totalExperiments;
    public final int successfulExperiments;
    public final double averageEffectiveness;
    public final List<String> masteredContextTypes;
    public final List<String> learningContextTypes;
    
    public AgentContextProgress(String agentId, List<ContextExperiment> experiments) {
        this.agentId = agentId;
        this.totalExperiments = experiments.size();
        this.successfulExperiments = (int) experiments.stream().filter(ContextExperiment::wasSuccessful).count();
        this.averageEffectiveness = experiments.stream()
            .filter(ContextExperiment::wasSuccessful)
            .mapToDouble(ContextExperiment::getEffectivenessScore)
            .average()
            .orElse(0.0);
        
        // Determine mastered vs learning context types
        Map<String, Double> typeEffectiveness = new HashMap<>();
        experiments.stream()
            .filter(ContextExperiment::wasSuccessful)
            .forEach(exp -> {
                typeEffectiveness.merge(exp.getContextType(), exp.getEffectivenessScore(), Double::max);
            });
        
        this.masteredContextTypes = typeEffectiveness.entrySet().stream()
            .filter(entry -> entry.getValue() >= 8.0)
            .map(Map.Entry::getKey)
            .collect(ArrayList::new, (list, item) -> list.add(item), (list1, list2) -> list1.addAll(list2));
            
        this.learningContextTypes = typeEffectiveness.entrySet().stream()
            .filter(entry -> entry.getValue() < 8.0 && entry.getValue() >= 5.0)
            .map(Map.Entry::getKey)
            .collect(ArrayList::new, (list, item) -> list.add(item), (list1, list2) -> list1.addAll(list2));
    }
    
    public double getSuccessRate() {
        return totalExperiments > 0 ? (double) successfulExperiments / totalExperiments * 100 : 0.0;
    }
}

/**
 * Trending context structure gaining popularity
 */
class TrendingContextStructure {
    private final String contextType;
    private final String structureType;
    private final List<ContextExperiment> recentExperiments;
    
    public TrendingContextStructure(String contextType, String structureType) {
        this.contextType = contextType;
        this.structureType = structureType;
        this.recentExperiments = new ArrayList<>();
    }
    
    public void addExperiment(ContextExperiment experiment) {
        recentExperiments.add(experiment);
    }
    
    public int getRecentExperiments() {
        return recentExperiments.size();
    }
    
    public double getAverageEffectiveness() {
        return recentExperiments.stream()
            .mapToDouble(ContextExperiment::getEffectivenessScore)
            .average()
            .orElse(0.0);
    }
    
    public double getTrendScore() {
        return getRecentExperiments() * 2.0 + getAverageEffectiveness();
    }
    
    @Override
    public String toString() {
        return String.format("Trending: %s-%s (%.1f effectiveness, %d recent uses)", 
            contextType, structureType, getAverageEffectiveness(), getRecentExperiments());
    }
}