package com.evolver.agent;

import java.time.LocalDateTime;
import java.util.*;

/**
 * TECHNOLOGY EXPERIENCE - Record of agent interaction with technology
 * 
 * Agents use this to record detailed experiences with libraries, frameworks,
 * and tools. Each experience includes comprehensive ratings and contextual data.
 */
public class TechnologyExperience {
    
    private final String technologyName;
    private final String version;
    private final TechnologyType type;
    private final String projectContext;
    private final LocalDateTime timestamp;
    
    // Agent ratings (0.0 to 10.0 with 0.1 precision)
    private final double easeOfUse;
    private final double power;
    private final double performance;
    private final double reliability;
    private final double documentationQuality;
    private final double communitySupport;
    private final double overallSatisfaction;
    
    // Contextual information
    private final List<String> useCases;
    private final Map<String, CompatibilityData> compatibilityData;
    private final String recommendationReason;
    private final List<String> pros;
    private final List<String> cons;
    private final String agentNotes;
    
    // Technology combinations and harmony ratings
    private final Map<String, TechnologyCombination> usedTogether;
    
    public TechnologyExperience(Builder builder) {
        this.technologyName = builder.technologyName;
        this.version = builder.version;
        this.type = builder.type;
        this.projectContext = builder.projectContext;
        this.timestamp = LocalDateTime.now();
        
        // Validate and set ratings (ensure 0.1 precision)
        this.easeOfUse = roundToDecimal(builder.easeOfUse, 1);
        this.power = roundToDecimal(builder.power, 1);
        this.performance = roundToDecimal(builder.performance, 1);
        this.reliability = roundToDecimal(builder.reliability, 1);
        this.documentationQuality = roundToDecimal(builder.documentationQuality, 1);
        this.communitySupport = roundToDecimal(builder.communitySupport, 1);
        this.overallSatisfaction = roundToDecimal(builder.overallSatisfaction, 1);
        
        this.useCases = new ArrayList<>(builder.useCases);
        this.compatibilityData = new HashMap<>(builder.compatibilityData);
        this.recommendationReason = builder.recommendationReason;
        this.pros = new ArrayList<>(builder.pros);
        this.cons = new ArrayList<>(builder.cons);
        this.agentNotes = builder.agentNotes;
        this.usedTogether = new HashMap<>(builder.usedTogether);
    }
    
    /**
     * Create builder for technology experience
     */
    public static Builder builder(String technologyName, String version, TechnologyType type) {
        return new Builder(technologyName, version, type);
    }
    
    // Getters
    public String getName() { return technologyName; }
    public String getVersion() { return version; }
    public TechnologyType getType() { return type; }
    public String getProjectContext() { return projectContext; }
    public LocalDateTime getTimestamp() { return timestamp; }
    
    public double getEaseOfUse() { return easeOfUse; }
    public double getPower() { return power; }
    public double getPerformance() { return performance; }
    public double getReliability() { return reliability; }
    public double getDocumentationQuality() { return documentationQuality; }
    public double getCommunitySupport() { return communitySupport; }
    public double getOverallSatisfaction() { return overallSatisfaction; }
    
    public List<String> getUseCases() { return new ArrayList<>(useCases); }
    public Map<String, CompatibilityData> getCompatibilityData() { return new HashMap<>(compatibilityData); }
    public String getRecommendationReason() { return recommendationReason; }
    public List<String> getPros() { return new ArrayList<>(pros); }
    public List<String> getCons() { return new ArrayList<>(cons); }
    public String getAgentNotes() { return agentNotes; }
    public Map<String, TechnologyCombination> getUsedTogether() { return new HashMap<>(usedTogether); }
    
    public String getTechnologyKey() {
        return technologyName.toLowerCase() + ":" + version;
    }
    
    /**
     * Get overall assessment category
     */
    public AssessmentCategory getAssessmentCategory() {
        if (overallSatisfaction >= 8.0) return AssessmentCategory.EXCELLENT;
        if (overallSatisfaction >= 6.0) return AssessmentCategory.GOOD;
        if (overallSatisfaction >= 4.0) return AssessmentCategory.FAIR;
        return AssessmentCategory.POOR;
    }
    
    /**
     * Generate summary for other agents
     */
    public String generateSummary() {
        return String.format("%s v%s: %.1f/10 overall (%s) - %s", 
            technologyName, version, overallSatisfaction, 
            getAssessmentCategory().name().toLowerCase(),
            recommendationReason != null ? recommendationReason : "No specific recommendation");
    }
    
    private double roundToDecimal(double value, int decimalPlaces) {
        double multiplier = Math.pow(10, decimalPlaces);
        return Math.round(Math.max(0, Math.min(10, value)) * multiplier) / multiplier;
    }
    
    /**
     * Builder for TechnologyExperience
     */
    public static class Builder {
        private final String technologyName;
        private final String version;
        private final TechnologyType type;
        
        private String projectContext = "";
        private double easeOfUse = 5.0;
        private double power = 5.0;
        private double performance = 5.0;
        private double reliability = 5.0;
        private double documentationQuality = 5.0;
        private double communitySupport = 5.0;
        private double overallSatisfaction = 5.0;
        
        private List<String> useCases = new ArrayList<>();
        private Map<String, CompatibilityData> compatibilityData = new HashMap<>();
        private String recommendationReason = "";
        private List<String> pros = new ArrayList<>();
        private List<String> cons = new ArrayList<>();
        private String agentNotes = "";
        private Map<String, TechnologyCombination> usedTogether = new HashMap<>();
        
        public Builder(String technologyName, String version, TechnologyType type) {
            this.technologyName = technologyName;
            this.version = version;
            this.type = type;
        }
        
        public Builder projectContext(String context) {
            this.projectContext = context;
            return this;
        }
        
        public Builder easeOfUse(double rating) {
            this.easeOfUse = rating;
            return this;
        }
        
        public Builder power(double rating) {
            this.power = rating;
            return this;
        }
        
        public Builder performance(double rating) {
            this.performance = rating;
            return this;
        }
        
        public Builder reliability(double rating) {
            this.reliability = rating;
            return this;
        }
        
        public Builder documentationQuality(double rating) {
            this.documentationQuality = rating;
            return this;
        }
        
        public Builder communitySupport(double rating) {
            this.communitySupport = rating;
            return this;
        }
        
        public Builder overallSatisfaction(double rating) {
            this.overallSatisfaction = rating;
            return this;
        }
        
        public Builder useCase(String useCase) {
            this.useCases.add(useCase.toLowerCase());
            return this;
        }
        
        public Builder useCases(List<String> useCases) {
            useCases.forEach(uc -> this.useCases.add(uc.toLowerCase()));
            return this;
        }
        
        public Builder compatibility(String otherTech, CompatibilityData data) {
            this.compatibilityData.put(otherTech, data);
            return this;
        }
        
        public Builder recommendationReason(String reason) {
            this.recommendationReason = reason;
            return this;
        }
        
        public Builder pro(String pro) {
            this.pros.add(pro);
            return this;
        }
        
        public Builder con(String con) {
            this.cons.add(con);
            return this;
        }
        
        public Builder pros(List<String> pros) {
            this.pros.addAll(pros);
            return this;
        }
        
        public Builder cons(List<String> cons) {
            this.cons.addAll(cons);
            return this;
        }
        
        public Builder agentNotes(String notes) {
            this.agentNotes = notes;
            return this;
        }
        
        /**
         * Add a technology that was used together with this one
         */
        public Builder usedTogether(String technology, String version, double harmonyRating) {
            return usedTogether(technology, version, harmonyRating, null, null);
        }
        
        /**
         * Add a technology combination with detailed harmony assessment
         */
        public Builder usedTogether(String technology, String version, double harmonyRating, 
                                    String integrationNotes, String recommendedFor) {
            this.usedTogether.put(technology.toLowerCase(), 
                new TechnologyCombination(technology, version, harmonyRating, integrationNotes, recommendedFor));
            return this;
        }
        
        /**
         * Add multiple technology combinations
         */
        public Builder usedTogether(Map<String, TechnologyCombination> combinations) {
            combinations.forEach((key, value) -> this.usedTogether.put(key.toLowerCase(), value));
            return this;
        }
        
        public TechnologyExperience build() {
            return new TechnologyExperience(this);
        }
    }
    
    /**
     * Technology assessment categories
     */
    public enum AssessmentCategory {
        EXCELLENT("Highly recommended, exceptional experience"),
        GOOD("Recommended, good experience with minor issues"),
        FAIR("Acceptable, usable but with notable limitations"),
        POOR("Not recommended, significant issues encountered");
        
        private final String description;
        
        AssessmentCategory(String description) {
            this.description = description;
        }
        
        public String getDescription() { return description; }
    }
    
    /**
     * Technology types for categorization
     */
    public enum TechnologyType {
        LIBRARY, FRAMEWORK, DATABASE, BUILD_TOOL, TESTING_TOOL, 
        DEPLOYMENT_TOOL, MONITORING_TOOL, SECURITY_TOOL, 
        IDE_PLUGIN, LANGUAGE, PLATFORM, CLOUD_SERVICE, OTHER
    }
}