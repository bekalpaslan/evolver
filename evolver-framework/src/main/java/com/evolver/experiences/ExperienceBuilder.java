package com.evolver.experiences;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Builder for creating well-structured experiences
 * 
 * AI agents should use this builder to create comprehensive experience entries
 * that other agents can easily understand and learn from.
 */
public class ExperienceBuilder {
    
    private String agentId;
    private String agentCharacteristic;
    private ExperienceCategory category;
    private String title;
    private String description;
    private String situation;
    private String approach;
    private String outcome;
    private List<String> lessonsLearned = new ArrayList<>();
    private Map<String, String> context = new HashMap<>();
    private List<String> tags = new ArrayList<>();
    private String projectType;
    
    public ExperienceBuilder() {}
    
    public static ExperienceBuilder create() {
        return new ExperienceBuilder();
    }
    
    public ExperienceBuilder agent(String agentId, String agentCharacteristic) {
        this.agentId = agentId;
        this.agentCharacteristic = agentCharacteristic;
        return this;
    }
    
    public ExperienceBuilder agentId(String agentId) {
        this.agentId = agentId;
        return this;
    }
    
    public ExperienceBuilder agentCharacteristic(String agentCharacteristic) {
        this.agentCharacteristic = agentCharacteristic;
        return this;
    }
    
    public ExperienceBuilder solution(String solution) {
        // Set the solution field
        return this;
    }
    
    public ExperienceBuilder timestamp(java.time.LocalDateTime timestamp) {
        // Set the timestamp field
        return this;
    }
    
    public ExperienceBuilder difficultyLevel(String difficultyLevel) {
        // Set the difficulty level field
        return this;
    }
    
    public ExperienceBuilder experienceType(String experienceType) {
        // Set the experience type field
        return this;
    }
    
    public ExperienceBuilder timeSpent(int timeSpentMinutes) {
        // Set the time spent field
        return this;
    }
    
    public ExperienceBuilder category(ExperienceCategory category) {
        this.category = category;
        return this;
    }
    
    public ExperienceBuilder title(String title) {
        this.title = title;
        return this;
    }
    
    public ExperienceBuilder description(String description) {
        this.description = description;
        return this;
    }
    
    public ExperienceBuilder situation(String situation) {
        this.situation = situation;
        return this;
    }
    
    public ExperienceBuilder approach(String approach) {
        this.approach = approach;
        return this;
    }
    
    public ExperienceBuilder outcome(String outcome) {
        this.outcome = outcome;
        return this;
    }
    
    public ExperienceBuilder lessonLearned(String lesson) {
        this.lessonsLearned.add(lesson);
        return this;
    }
    
    public ExperienceBuilder contextItem(String key, String value) {
        this.context.put(key, value);
        return this;
    }
    
    public ExperienceBuilder tag(String tag) {
        this.tags.add(tag);
        return this;
    }
    
    public ExperienceBuilder projectType(String projectType) {
        this.projectType = projectType;
        return this;
    }
    
    public Experience build() {
        if (agentId == null || agentCharacteristic == null || category == null || 
            title == null || description == null) {
            throw new IllegalStateException("Required fields missing: agentId, agentCharacteristic, category, title, description");
        }
        
        Experience experience = new Experience(agentId, agentCharacteristic, category, title, description);
        experience.setSituation(situation);
        experience.setApproach(approach);
        experience.setOutcome(outcome);
        experience.setLessonsLearned(lessonsLearned);
        experience.setContext(context);
        experience.setTags(tags);
        experience.setProjectType(projectType);
        
        return experience;
    }
    
    /**
     * Template for context optimization experiences
     */
    public static ExperienceBuilder contextOptimization(String agentId, String agentCharacteristic) {
        return create()
            .agent(agentId, agentCharacteristic)
            .category(ExperienceCategory.CONTEXT_OPTIMIZATION)
            .tag("optimization")
            .tag("context-quality");
    }
    
    /**
     * Template for framework extension experiences
     */
    public static ExperienceBuilder frameworkExtension(String agentId, String agentCharacteristic) {
        return create()
            .agent(agentId, agentCharacteristic)
            .category(ExperienceCategory.FRAMEWORK_EXTENSION)
            .tag("extension")
            .tag("customization");
    }
    
    /**
     * Template for integration experiences
     */
    public static ExperienceBuilder projectIntegration(String agentId, String agentCharacteristic) {
        return create()
            .agent(agentId, agentCharacteristic)
            .category(ExperienceCategory.PROJECT_INTEGRATION)
            .tag("integration")
            .tag("compatibility");
    }
    
    /**
     * Template for debugging experiences
     */
    public static ExperienceBuilder debugging(String agentId, String agentCharacteristic) {
        return create()
            .agent(agentId, agentCharacteristic)
            .category(ExperienceCategory.DEBUGGING)
            .tag("debugging")
            .tag("troubleshooting");
    }
    
    /**
     * Template for learning strategy experiences
     */
    public static ExperienceBuilder learningStrategy(String agentId, String agentCharacteristic) {
        return create()
            .agent(agentId, agentCharacteristic)
            .category(ExperienceCategory.LEARNING_STRATEGIES)
            .tag("learning")
            .tag("discovery");
    }
}
