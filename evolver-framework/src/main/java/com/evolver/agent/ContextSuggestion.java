package com.evolver.agent;

import java.time.LocalDateTime;
import java.util.*;

/**
 * CONTEXT SUGGESTION
 * 
 * Comprehensive analysis and recommendations for context enhancement
 * based on task complexity, time investment, and identified gaps.
 */
public class ContextSuggestion {
    
    private final String taskId;
    private final String agentCharacteristic;
    private final LocalDateTime analysisTimestamp;
    private final ContextGapAnalysis contextGaps;
    private final TaskComplexityAnalysis taskComplexity;
    private final TimeInvestmentAnalysis timeAnalysis;
    private final EfficiencyPrediction efficiencyPrediction;
    private final List<ContextSuggestionItem> suggestions;
    private final SuggestionPriority priority;
    private final String overallRecommendation;
    private final double estimatedTimeSavings;
    
    public ContextSuggestion(Builder builder) {
        this.taskId = builder.taskId;
        this.agentCharacteristic = builder.agentCharacteristic;
        this.analysisTimestamp = builder.analysisTimestamp;
        this.contextGaps = builder.contextGaps;
        this.taskComplexity = builder.taskComplexity;
        this.timeAnalysis = builder.timeAnalysis;
        this.efficiencyPrediction = builder.efficiencyPrediction;
        this.suggestions = new ArrayList<>(builder.suggestions);
        this.priority = builder.priority;
        this.overallRecommendation = builder.overallRecommendation;
        this.estimatedTimeSavings = builder.estimatedTimeSavings;
    }
    
    public static Builder builder() {
        return new Builder();
    }
    
    // Getters
    public String getTaskId() { return taskId; }
    public String getAgentCharacteristic() { return agentCharacteristic; }
    public LocalDateTime getAnalysisTimestamp() { return analysisTimestamp; }
    public ContextGapAnalysis getContextGaps() { return contextGaps; }
    public TaskComplexityAnalysis getTaskComplexity() { return taskComplexity; }
    public TimeInvestmentAnalysis getTimeAnalysis() { return timeAnalysis; }
    public EfficiencyPrediction getEfficiencyPrediction() { return efficiencyPrediction; }
    public List<ContextSuggestionItem> getSuggestions() { return new ArrayList<>(suggestions); }
    public SuggestionPriority getPriority() { return priority; }
    public String getOverallRecommendation() { return overallRecommendation; }
    public double getEstimatedTimeSavings() { return estimatedTimeSavings; }
    
    /**
     * Get formatted summary for display
     */
    public String getFormattedSummary() {
        StringBuilder summary = new StringBuilder();
        summary.append("\n=== CONTEXT ENHANCEMENT ANALYSIS ===");
        summary.append("\nTask: ").append(taskId);
        summary.append("\nAgent: ").append(agentCharacteristic);
        summary.append("\nPriority: ").append(priority);
        summary.append("\nEstimated Time Savings: ").append(String.format("%.0f minutes", estimatedTimeSavings));
        summary.append("\n\nRECOMMENDATION: ").append(overallRecommendation);
        
        if (!suggestions.isEmpty()) {
            summary.append("\n\nSUGGESTIONS:");
            for (int i = 0; i < suggestions.size(); i++) {
                ContextSuggestionItem item = suggestions.get(i);
                summary.append("\n").append(i + 1).append(". ").append(item.getTitle());
                summary.append("\n   Category: ").append(item.getCategory());
                summary.append("\n   Priority: ").append(item.getPriority());
                summary.append("\n   Time Savings: ").append(item.getEstimatedTimeSavings()).append(" minutes");
            }
        }
        
        return summary.toString();
    }
    
    public static class Builder {
        private String taskId;
        private String agentCharacteristic;
        private LocalDateTime analysisTimestamp;
        private ContextGapAnalysis contextGaps;
        private TaskComplexityAnalysis taskComplexity;
        private TimeInvestmentAnalysis timeAnalysis;
        private EfficiencyPrediction efficiencyPrediction;
        private List<ContextSuggestionItem> suggestions = new ArrayList<>();
        private SuggestionPriority priority;
        private String overallRecommendation;
        private double estimatedTimeSavings;
        
        public Builder taskId(String taskId) {
            this.taskId = taskId;
            return this;
        }
        
        public Builder agentCharacteristic(String agentCharacteristic) {
            this.agentCharacteristic = agentCharacteristic;
            return this;
        }
        
        public Builder analysisTimestamp(LocalDateTime analysisTimestamp) {
            this.analysisTimestamp = analysisTimestamp;
            return this;
        }
        
        public Builder contextGaps(ContextGapAnalysis contextGaps) {
            this.contextGaps = contextGaps;
            return this;
        }
        
        public Builder taskComplexity(TaskComplexityAnalysis taskComplexity) {
            this.taskComplexity = taskComplexity;
            return this;
        }
        
        public Builder timeAnalysis(TimeInvestmentAnalysis timeAnalysis) {
            this.timeAnalysis = timeAnalysis;
            return this;
        }
        
        public Builder efficiencyPrediction(EfficiencyPrediction efficiencyPrediction) {
            this.efficiencyPrediction = efficiencyPrediction;
            return this;
        }
        
        public Builder suggestions(List<ContextSuggestionItem> suggestions) {
            this.suggestions = new ArrayList<>(suggestions);
            return this;
        }
        
        public Builder priority(SuggestionPriority priority) {
            this.priority = priority;
            return this;
        }
        
        public Builder overallRecommendation(String overallRecommendation) {
            this.overallRecommendation = overallRecommendation;
            return this;
        }
        
        public Builder estimatedTimeSavings(double estimatedTimeSavings) {
            this.estimatedTimeSavings = estimatedTimeSavings;
            return this;
        }
        
        public ContextSuggestion build() {
            return new ContextSuggestion(this);
        }
    }
}

/**
 * SUGGESTION PRIORITY
 */
enum SuggestionPriority {
    LOW(1), MEDIUM(5), HIGH(10), CRITICAL(15);
    
    private final int value;
    
    SuggestionPriority(int value) {
        this.value = value;
    }
    
    public int getValue() {
        return value;
    }
}