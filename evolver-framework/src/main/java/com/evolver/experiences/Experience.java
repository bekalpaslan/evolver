package com.evolver.experiences;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * Represents a learning experience shared by an AI agent
 * 
 * AI agents should use this to document their discoveries, strategies,
 * and insights for other agents to learn from. This creates a collaborative
 * learning environment where agents build upon each other's experiences.
 */
public class Experience {
    private String id;
    private String agentId;
    private String agentCharacteristic;
    private ExperienceCategory category;
    private String title;
    private String description;
    private String situation;
    private String approach;
    private String outcome;
    private List<String> lessonsLearned;
    private Map<String, String> context;
    private LocalDateTime timestamp;
    private List<String> tags;
    private boolean recommended;
    private String projectType;
    
    // Default constructor for JSON deserialization
    public Experience() {}
    
    public Experience(String agentId, String agentCharacteristic, ExperienceCategory category,
                     String title, String description) {
        this.agentId = agentId;
        this.agentCharacteristic = agentCharacteristic;
        this.category = category;
        this.title = title;
        this.description = description;
        this.timestamp = LocalDateTime.now();
        this.id = generateId();
        this.recommended = false;
    }
    
    private String generateId() {
        return agentCharacteristic + "_" + category.name() + "_" + 
               System.currentTimeMillis() + "_" + 
               Math.abs(title.hashCode() % 1000);
    }
    
    // Getters and setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    
    public String getAgentId() { return agentId; }
    public void setAgentId(String agentId) { this.agentId = agentId; }
    
    public String getAgentCharacteristic() { return agentCharacteristic; }
    public void setAgentCharacteristic(String agentCharacteristic) { 
        this.agentCharacteristic = agentCharacteristic; 
    }
    
    public ExperienceCategory getCategory() { return category; }
    public void setCategory(ExperienceCategory category) { this.category = category; }
    
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public String getSituation() { return situation; }
    public void setSituation(String situation) { this.situation = situation; }
    
    public String getApproach() { return approach; }
    public void setApproach(String approach) { this.approach = approach; }
    
    public String getOutcome() { return outcome; }
    public void setOutcome(String outcome) { this.outcome = outcome; }
    
    public List<String> getLessonsLearned() { return lessonsLearned; }
    public void setLessonsLearned(List<String> lessonsLearned) { 
        this.lessonsLearned = lessonsLearned; 
    }
    
    public Map<String, String> getContext() { return context; }
    public void setContext(Map<String, String> context) { this.context = context; }
    
    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }
    
    public List<String> getTags() { return tags; }
    public void setTags(List<String> tags) { this.tags = tags; }
    
    public boolean isRecommended() { return recommended; }
    public void setRecommended(boolean recommended) { this.recommended = recommended; }
    
    public String getProjectType() { return projectType; }
    public void setProjectType(String projectType) { this.projectType = projectType; }
    
    /**
     * Convenience method for compatibility - returns the approach as solution
     */
    public String getSolution() { 
        return approach != null ? approach : outcome; 
    }
    
    /**
     * Static builder method for fluent API
     */
    public static ExperienceBuilder builder() {
        return new ExperienceBuilder();
    }
    
    @Override
    public String toString() {
        return String.format("Experience[%s]: %s by %s (%s)", 
                           category, title, agentCharacteristic, timestamp);
    }
}
