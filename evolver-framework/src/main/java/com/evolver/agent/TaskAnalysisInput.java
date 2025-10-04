package com.evolver.agent;

/**
 * TASK ANALYSIS INPUT
 * 
 * Input data for analyzing task context and suggesting improvements.
 */
public class TaskAnalysisInput {
    
    private final double functionalClarityScore;
    private final double requirementCompletenessScore;
    private final double technicalContextScore;
    private final double architecturalGuidanceScore;
    private final double domainKnowledgeScore;
    private final double domainNovelty;
    private final double qualityRequirementsScore;
    private final double testingContextScore;
    private final int estimatedLinesOfCode;
    private final int integrationPoints;
    private final int newTechnologies;
    private final double timeConstraintSeverity;
    
    public TaskAnalysisInput(Builder builder) {
        this.functionalClarityScore = builder.functionalClarityScore;
        this.requirementCompletenessScore = builder.requirementCompletenessScore;
        this.technicalContextScore = builder.technicalContextScore;
        this.architecturalGuidanceScore = builder.architecturalGuidanceScore;
        this.domainKnowledgeScore = builder.domainKnowledgeScore;
        this.domainNovelty = builder.domainNovelty;
        this.qualityRequirementsScore = builder.qualityRequirementsScore;
        this.testingContextScore = builder.testingContextScore;
        this.estimatedLinesOfCode = builder.estimatedLinesOfCode;
        this.integrationPoints = builder.integrationPoints;
        this.newTechnologies = builder.newTechnologies;
        this.timeConstraintSeverity = builder.timeConstraintSeverity;
    }
    
    public static Builder builder() {
        return new Builder();
    }
    
    // Getters
    public double getFunctionalClarityScore() { return functionalClarityScore; }
    public double getRequirementCompletenessScore() { return requirementCompletenessScore; }
    public double getTechnicalContextScore() { return technicalContextScore; }
    public double getArchitecturalGuidanceScore() { return architecturalGuidanceScore; }
    public double getDomainKnowledgeScore() { return domainKnowledgeScore; }
    public double getDomainNovelty() { return domainNovelty; }
    public double getQualityRequirementsScore() { return qualityRequirementsScore; }
    public double getTestingContextScore() { return testingContextScore; }
    public int getEstimatedLinesOfCode() { return estimatedLinesOfCode; }
    public int getIntegrationPoints() { return integrationPoints; }
    public int getNewTechnologies() { return newTechnologies; }
    public double getTimeConstraintSeverity() { return timeConstraintSeverity; }
    
    public static class Builder {
        private double functionalClarityScore = 0.5;
        private double requirementCompletenessScore = 0.5;
        private double technicalContextScore = 0.5;
        private double architecturalGuidanceScore = 0.5;
        private double domainKnowledgeScore = 0.5;
        private double domainNovelty = 0.5;
        private double qualityRequirementsScore = 0.5;
        private double testingContextScore = 0.5;
        private int estimatedLinesOfCode = 0;
        private int integrationPoints = 0;
        private int newTechnologies = 0;
        private double timeConstraintSeverity = 0.5;
        
        public Builder functionalClarityScore(double functionalClarityScore) {
            this.functionalClarityScore = functionalClarityScore;
            return this;
        }
        
        public Builder requirementCompletenessScore(double requirementCompletenessScore) {
            this.requirementCompletenessScore = requirementCompletenessScore;
            return this;
        }
        
        public Builder technicalContextScore(double technicalContextScore) {
            this.technicalContextScore = technicalContextScore;
            return this;
        }
        
        public Builder architecturalGuidanceScore(double architecturalGuidanceScore) {
            this.architecturalGuidanceScore = architecturalGuidanceScore;
            return this;
        }
        
        public Builder domainKnowledgeScore(double domainKnowledgeScore) {
            this.domainKnowledgeScore = domainKnowledgeScore;
            return this;
        }
        
        public Builder domainNovelty(double domainNovelty) {
            this.domainNovelty = domainNovelty;
            return this;
        }
        
        public Builder qualityRequirementsScore(double qualityRequirementsScore) {
            this.qualityRequirementsScore = qualityRequirementsScore;
            return this;
        }
        
        public Builder testingContextScore(double testingContextScore) {
            this.testingContextScore = testingContextScore;
            return this;
        }
        
        public Builder estimatedLinesOfCode(int estimatedLinesOfCode) {
            this.estimatedLinesOfCode = estimatedLinesOfCode;
            return this;
        }
        
        public Builder integrationPoints(int integrationPoints) {
            this.integrationPoints = integrationPoints;
            return this;
        }
        
        public Builder newTechnologies(int newTechnologies) {
            this.newTechnologies = newTechnologies;
            return this;
        }
        
        public Builder timeConstraintSeverity(double timeConstraintSeverity) {
            this.timeConstraintSeverity = timeConstraintSeverity;
            return this;
        }
        
        public TaskAnalysisInput build() {
            return new TaskAnalysisInput(this);
        }
    }
}