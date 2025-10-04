package com.evolver.agent;

import java.util.*;

/**
 * AGENT RATING ANALYSIS
 * 
 * Comparative analysis of how different agent types rate the same technology.
 */
public class AgentRatingAnalysis {
    
    private final String technology;
    private final String version;
    private final Map<String, List<Double>> ratingsByAgent;
    private final Map<String, List<String>> notesByAgent;
    private final double agentConsensus;
    private final double agentDivergence;
    
    public AgentRatingAnalysis(Builder builder) {
        this.technology = builder.technology;
        this.version = builder.version;
        this.ratingsByAgent = new HashMap<>(builder.ratingsByAgent);
        this.notesByAgent = new HashMap<>(builder.notesByAgent);
        this.agentConsensus = builder.agentConsensus;
        this.agentDivergence = builder.agentDivergence;
    }
    
    public static Builder builder() {
        return new Builder();
    }
    
    // Getters
    public String getTechnology() { return technology; }
    public String getVersion() { return version; }
    public Map<String, List<Double>> getRatingsByAgent() { return new HashMap<>(ratingsByAgent); }
    public Map<String, List<String>> getNotesByAgent() { return new HashMap<>(notesByAgent); }
    public double getAgentConsensus() { return agentConsensus; }
    public double getAgentDivergence() { return agentDivergence; }
    
    public static class Builder {
        private String technology;
        private String version;
        private Map<String, List<Double>> ratingsByAgent = new HashMap<>();
        private Map<String, List<String>> notesByAgent = new HashMap<>();
        private double agentConsensus;
        private double agentDivergence;
        
        public Builder technology(String technology) {
            this.technology = technology;
            return this;
        }
        
        public Builder version(String version) {
            this.version = version;
            return this;
        }
        
        public Builder ratingsByAgent(Map<String, List<Double>> ratingsByAgent) {
            this.ratingsByAgent = new HashMap<>(ratingsByAgent);
            return this;
        }
        
        public Builder notesByAgent(Map<String, List<String>> notesByAgent) {
            this.notesByAgent = new HashMap<>(notesByAgent);
            return this;
        }
        
        public Builder agentConsensus(double agentConsensus) {
            this.agentConsensus = agentConsensus;
            return this;
        }
        
        public Builder agentDivergence(double agentDivergence) {
            this.agentDivergence = agentDivergence;
            return this;
        }
        
        public AgentRatingAnalysis build() {
            return new AgentRatingAnalysis(this);
        }
    }
}