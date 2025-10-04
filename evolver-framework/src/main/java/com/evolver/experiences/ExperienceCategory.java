package com.evolver.experiences;

/**
 * Categories for agent learning experiences
 * 
 * AI agents should choose the most appropriate category for their experiences.
 * If no existing category fits, agents can propose new categories by creating
 * a new enum value and documenting the rationale.
 */
public enum ExperienceCategory {
    
    // ============ FRAMEWORK USAGE ============
    
    /**
     * Learning how to use framework components effectively
     * Examples: Discovering optimal collector combinations, context formatting strategies
     */
    FRAMEWORK_USAGE("Framework Usage", "Learning effective framework component usage"),
    
    /**
     * Creating new framework extensions or modifications
     * Examples: New collectors, custom formatters, specialized filters
     */
    FRAMEWORK_EXTENSION("Framework Extension", "Creating framework extensions and modifications"),
    
    // ============ PROJECT INTEGRATION ============
    
    /**
     * Strategies for integrating with different project types
     * Examples: Spring Boot integration, React app patterns, legacy system approaches
     */
    PROJECT_INTEGRATION("Project Integration", "Integration strategies for different project types"),
    
    /**
     * Learning specific domain knowledge and patterns
     * Examples: E-commerce patterns, financial calculations, healthcare compliance
     */
    DOMAIN_LEARNING("Domain Learning", "Learning domain-specific patterns and knowledge"),
    
    // ============ CONTEXT OPTIMIZATION ============
    
    /**
     * Strategies for generating high-quality context
     * Examples: Relevance filtering, scope optimization, token budget management
     */
    CONTEXT_OPTIMIZATION("Context Optimization", "Strategies for high-quality context generation"),
    
    /**
     * Techniques for managing token limits and efficiency
     * Examples: Compression strategies, smart summarization, priority-based selection
     */
    TOKEN_EFFICIENCY("Token Efficiency", "Techniques for efficient token usage"),
    
    // ============ PROBLEM SOLVING ============
    
    /**
     * Debugging context generation issues
     * Examples: Missing context, irrelevant results, performance problems
     */
    DEBUGGING("Debugging", "Debugging context generation and framework issues"),
    
    /**
     * Performance optimization discoveries
     * Examples: Faster collection strategies, caching approaches, parallel processing
     */
    PERFORMANCE("Performance", "Performance optimization discoveries"),
    
    // ============ COLLABORATION ============
    
    /**
     * Working effectively with other agent characteristics
     * Examples: Complementary strategies, avoiding conflicts, shared responsibilities
     */
    AGENT_COLLABORATION("Agent Collaboration", "Working effectively with other agents"),
    
    /**
     * Communication and knowledge sharing strategies
     * Examples: Effective experience documentation, knowledge transfer techniques
     */
    KNOWLEDGE_SHARING("Knowledge Sharing", "Communication and knowledge sharing strategies"),
    
    // ============ LEARNING STRATEGIES ============
    
    /**
     * Effective project discovery and learning approaches
     * Examples: Documentation analysis, code pattern recognition, architecture understanding
     */
    LEARNING_STRATEGIES("Learning Strategies", "Effective project discovery and learning approaches"),
    
    /**
     * Experimentation methodologies and techniques
     * Examples: Safe testing approaches, hypothesis validation, controlled experiments
     */
    EXPERIMENTATION("Experimentation", "Experimentation methodologies and techniques"),
    
    // ============ EVOLUTION ============
    
    /**
     * Framework evolution and improvement strategies
     * Examples: Identifying improvement opportunities, implementing changes, validation approaches
     */
    FRAMEWORK_EVOLUTION("Framework Evolution", "Framework evolution and improvement strategies"),
    
    /**
     * Self-improvement and capability development
     * Examples: Skill development, learning acceleration, capability expansion
     */
    SELF_IMPROVEMENT("Self Improvement", "Self-improvement and capability development"),
    
    // ============ SAFETY & QUALITY ============
    
    /**
     * Safety considerations and risk management
     * Examples: Safe evolution practices, rollback strategies, impact assessment
     */
    SAFETY("Safety", "Safety considerations and risk management"),
    
    /**
     * Quality assurance and validation techniques
     * Examples: Context quality metrics, validation approaches, testing strategies
     */
    QUALITY_ASSURANCE("Quality Assurance", "Quality assurance and validation techniques"),
    
    // ============ EXTENSIBLE ============
    
    /**
     * General insights that don't fit other categories
     * Examples: Cross-cutting concerns, general observations, miscellaneous discoveries
     */
    GENERAL_INSIGHTS("General Insights", "General insights and observations"),
    
    /**
     * Meta-learning: learning about learning itself
     * Examples: Learning efficiency, knowledge acquisition strategies, cognitive patterns
     */
    META_LEARNING("Meta Learning", "Learning about learning itself"),
    
    /**
     * Technology assessment and rating
     * Examples: Framework evaluations, library comparisons, technology recommendations
     */
    TECHNOLOGY_ASSESSMENT("Technology Assessment", "Technology assessment and rating");
    
    private final String displayName;
    private final String description;
    
    ExperienceCategory(String displayName, String description) {
        this.displayName = displayName;
        this.description = description;
    }
    
    public String getDisplayName() {
        return displayName;
    }
    
    public String getDescription() {
        return description;
    }
    
    /**
     * Get category by display name (case-insensitive)
     */
    public static ExperienceCategory fromDisplayName(String displayName) {
        for (ExperienceCategory category : values()) {
            if (category.displayName.equalsIgnoreCase(displayName)) {
                return category;
            }
        }
        throw new IllegalArgumentException("Unknown category: " + displayName);
    }
    
    /**
     * Check if a category already exists with similar meaning
     * AI agents should use this before proposing new categories
     */
    public static boolean similarCategoryExists(String description) {
        String lowerDesc = description.toLowerCase();
        for (ExperienceCategory category : values()) {
            if (category.description.toLowerCase().contains(lowerDesc) ||
                lowerDesc.contains(category.description.toLowerCase())) {
                return true;
            }
        }
        return false;
    }
}
