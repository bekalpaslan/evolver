package com.evolver.agent;

import java.util.*;

/**
 * CONTEXT SUGGESTION ITEM
 * 
 * Individual suggestion for context enhancement with specific
 * action items and estimated benefits.
 */
public class ContextSuggestionItem {
    
    private final String category;
    private final SuggestionPriority priority;
    private final String title;
    private final String description;
    private final List<String> actionItems;
    private final int estimatedTimeSavings; // in minutes
    private final String reasoning;
    private final Map<String, Object> metadata;
    
    public ContextSuggestionItem(Builder builder) {
        this.category = builder.category;
        this.priority = builder.priority;
        this.title = builder.title;
        this.description = builder.description;
        this.actionItems = new ArrayList<>(builder.actionItems);
        this.estimatedTimeSavings = builder.estimatedTimeSavings;
        this.reasoning = builder.reasoning;
        this.metadata = new HashMap<>(builder.metadata);
    }
    
    public static Builder builder() {
        return new Builder();
    }
    
    // Getters
    public String getCategory() { return category; }
    public SuggestionPriority getPriority() { return priority; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public List<String> getActionItems() { return new ArrayList<>(actionItems); }
    public int getEstimatedTimeSavings() { return estimatedTimeSavings; }
    public String getReasoning() { return reasoning; }
    public Map<String, Object> getMetadata() { return new HashMap<>(metadata); }
    
    /**
     * Get formatted action plan
     */
    public String getFormattedActionPlan() {
        StringBuilder plan = new StringBuilder();
        plan.append(title).append("\n");
        plan.append("Priority: ").append(priority).append("\n");
        plan.append("Description: ").append(description).append("\n");
        plan.append("Expected Time Savings: ").append(estimatedTimeSavings).append(" minutes\n");
        plan.append("\nAction Items:\n");
        
        for (int i = 0; i < actionItems.size(); i++) {
            plan.append("  ").append(i + 1).append(". ").append(actionItems.get(i)).append("\n");
        }
        
        plan.append("\nReasoning: ").append(reasoning);
        
        return plan.toString();
    }
    
    public static class Builder {
        private String category;
        private SuggestionPriority priority = SuggestionPriority.MEDIUM;
        private String title;
        private String description;
        private List<String> actionItems = new ArrayList<>();
        private int estimatedTimeSavings = 0;
        private String reasoning;
        private Map<String, Object> metadata = new HashMap<>();
        
        public Builder category(String category) {
            this.category = category;
            return this;
        }
        
        public Builder priority(SuggestionPriority priority) {
            this.priority = priority;
            return this;
        }
        
        public Builder title(String title) {
            this.title = title;
            return this;
        }
        
        public Builder description(String description) {
            this.description = description;
            return this;
        }
        
        public Builder actionItems(List<String> actionItems) {
            this.actionItems = new ArrayList<>(actionItems);
            return this;
        }
        
        public Builder estimatedTimeSavings(int estimatedTimeSavings) {
            this.estimatedTimeSavings = estimatedTimeSavings;
            return this;
        }
        
        public Builder reasoning(String reasoning) {
            this.reasoning = reasoning;
            return this;
        }
        
        public Builder metadata(String key, Object value) {
            this.metadata.put(key, value);
            return this;
        }
        
        public ContextSuggestionItem build() {
            Objects.requireNonNull(title, "Title is required");
            Objects.requireNonNull(category, "Category is required");
            return new ContextSuggestionItem(this);
        }
    }
}