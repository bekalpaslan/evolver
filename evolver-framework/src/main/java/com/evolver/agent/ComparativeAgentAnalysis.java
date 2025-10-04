package com.evolver.agent;

import java.util.*;

/**
 * COMPARATIVE AGENT ANALYSIS
 * 
 * Cross-agent comparison analysis for technology experiences.
 */
public class ComparativeAgentAnalysis {
    
    private final String technology;
    private final String version;
    private final Map<String, AgentTechnologyProfile> profilesByAgent;
    private final Map<String, String> agentPreferencePatterns;
    private final Map<String, Double> ratingDistribution;
    private final Map<String, Object> consensusMetrics;
    
    public ComparativeAgentAnalysis(Builder builder) {
        this.technology = builder.technology;
        this.version = builder.version;
        this.profilesByAgent = new HashMap<>(builder.profilesByAgent);
        this.agentPreferencePatterns = new HashMap<>(builder.agentPreferencePatterns);
        this.ratingDistribution = new HashMap<>(builder.ratingDistribution);
        this.consensusMetrics = new HashMap<>(builder.consensusMetrics);
    }
    
    public static Builder builder() {
        return new Builder();
    }
    
    // Getters
    public String getTechnology() { return technology; }
    public String getVersion() { return version; }
    public Map<String, AgentTechnologyProfile> getProfilesByAgent() { return new HashMap<>(profilesByAgent); }
    public Map<String, String> getAgentPreferencePatterns() { return new HashMap<>(agentPreferencePatterns); }
    public Map<String, Double> getRatingDistribution() { return new HashMap<>(ratingDistribution); }
    public Map<String, Object> getConsensusMetrics() { return new HashMap<>(consensusMetrics); }
    
    public static class Builder {
        private String technology;
        private String version;
        private Map<String, AgentTechnologyProfile> profilesByAgent = new HashMap<>();
        private Map<String, String> agentPreferencePatterns = new HashMap<>();
        private Map<String, Double> ratingDistribution = new HashMap<>();
        private Map<String, Object> consensusMetrics = new HashMap<>();
        
        public Builder technology(String technology) {
            this.technology = technology;
            return this;
        }
        
        public Builder version(String version) {
            this.version = version;
            return this;
        }
        
        public Builder profilesByAgent(Map<String, AgentTechnologyProfile> profilesByAgent) {
            this.profilesByAgent = new HashMap<>(profilesByAgent);
            return this;
        }
        
        public Builder agentPreferencePatterns(Map<String, String> agentPreferencePatterns) {
            this.agentPreferencePatterns = new HashMap<>(agentPreferencePatterns);
            return this;
        }
        
        public Builder ratingDistribution(Map<String, Double> ratingDistribution) {
            this.ratingDistribution = new HashMap<>(ratingDistribution);
            return this;
        }
        
        public Builder consensusMetrics(Map<String, Object> consensusMetrics) {
            this.consensusMetrics = new HashMap<>(consensusMetrics);
            return this;
        }
        
        public ComparativeAgentAnalysis build() {
            return new ComparativeAgentAnalysis(this);
        }
    }
}