package com.evolver.demo;

import com.evolver.agent.*;

/**
 * INTELLIGENT CONTEXT ADVISOR DEMONSTRATION
 * 
 * Shows how agents intelligently suggest when engineers should provide
 * more context based on task complexity, time investment, and efficiency analysis.
 */
public class ContextAdvisorDemo {
    
    public static void main(String[] args) {
        demonstrateContextAdvisor();
    }
    
    public static void demonstrateContextAdvisor() {
        System.out.println("=== INTELLIGENT CONTEXT ADVISOR DEMO ===\\n");
        
        // Test with different agent characteristics
        demonstrateDocBotScenario();
        System.out.println("\\n" + "=".repeat(60) + "\\n");
        
        demonstrateCleanFreakScenario();
        System.out.println("\\n" + "=".repeat(60) + "\\n");
        
        demonstrateSpeedDemonScenario();
        System.out.println("\\n" + "=".repeat(60) + "\\n");
        
        demonstrateLongRunningTaskScenario();
    }
    
    /**
     * DocBot working on a documentation-heavy task
     */
    private static void demonstrateDocBotScenario() {
        System.out.println("SCENARIO 1: DocBot - Documentation-Heavy API Development");
        
        IntelligentContextAdvisor advisor = new IntelligentContextAdvisor(AgentCharacteristic.DOCUMENTATION_OBSESSIVE);
        
        // Simulate a task with poor documentation context
        TaskAnalysisInput input = TaskAnalysisInput.builder()
            .functionalClarityScore(0.4) // Poor functional clarity
            .requirementCompletenessScore(0.3) // Incomplete requirements
            .technicalContextScore(0.6) // Decent technical context
            .architecturalGuidanceScore(0.5)
            .domainKnowledgeScore(0.7)
            .domainNovelty(0.3) // Familiar domain
            .qualityRequirementsScore(0.2) // Poor quality context
            .testingContextScore(0.3)
            .estimatedLinesOfCode(800) // Medium-large task
            .integrationPoints(2)
            .newTechnologies(1)
            .timeConstraintSeverity(0.4)
            .build();
        
        ContextSuggestion suggestion = advisor.analyzeAndSuggest("api-docs-task-001", input);
        
        System.out.println(suggestion.getFormattedSummary());
        
        // Simulate DocBot's specific concern
        System.out.println("\\n💡 DocBot's Perspective:");
        System.out.println("I'm struggling with this API documentation task because:");
        System.out.println("• Functional requirements are vague (40% clarity)");
        System.out.println("• Quality standards are unclear (20% context)");
        System.out.println("• Documentation format expectations are missing");
        System.out.println("\\nRecommendation: Pause and gather documentation standards before proceeding.");
    }
    
    /**
     * CleanFreak working on complex refactoring
     */
    private static void demonstrateCleanFreakScenario() {
        System.out.println("SCENARIO 2: CleanFreak - Complex Legacy System Refactoring");
        
        IntelligentContextAdvisor advisor = new IntelligentContextAdvisor(AgentCharacteristic.CLEAN_CODE_FREAK);
        
        TaskAnalysisInput input = TaskAnalysisInput.builder()
            .functionalClarityScore(0.7) // Good functional understanding
            .requirementCompletenessScore(0.6)
            .technicalContextScore(0.3) // Poor technical context for legacy system
            .architecturalGuidanceScore(0.2) // No architectural guidance
            .domainKnowledgeScore(0.5)
            .domainNovelty(0.6) // Somewhat unfamiliar
            .qualityRequirementsScore(0.4) // Unclear quality standards
            .testingContextScore(0.3)
            .estimatedLinesOfCode(2000) // Large refactoring
            .integrationPoints(5) // Many integration points
            .newTechnologies(0) // No new tech, but complex
            .timeConstraintSeverity(0.7) // High time pressure
            .build();
        
        ContextSuggestion suggestion = advisor.analyzeAndSuggest("legacy-refactor-002", input);
        
        System.out.println(suggestion.getFormattedSummary());
        
        System.out.println("\\n🧹 CleanFreak's Perspective:");
        System.out.println("This legacy refactoring is risky without proper context:");
        System.out.println("• Architecture understanding is insufficient (20% guidance)");
        System.out.println("• Code quality standards are unclear (40% context)");
        System.out.println("• 5 integration points need careful analysis");
        System.out.println("• High time pressure makes mistakes costly");
        System.out.println("\\nRecommendation: STOP and get architectural review before refactoring.");
    }
    
    /**
     * SpeedDemon working on performance optimization
     */
    private static void demonstrateSpeedDemonScenario() {
        System.out.println("SCENARIO 3: SpeedDemon - High-Performance Trading System");
        
        IntelligentContextAdvisor advisor = new IntelligentContextAdvisor(AgentCharacteristic.SPEED_DEMON);
        
        TaskAnalysisInput input = TaskAnalysisInput.builder()
            .functionalClarityScore(0.8) // Clear performance requirements
            .requirementCompletenessScore(0.7)
            .technicalContextScore(0.5)
            .architecturalGuidanceScore(0.4) // Insufficient performance architecture
            .domainKnowledgeScore(0.3) // Low domain knowledge (trading)
            .domainNovelty(0.9) // Very new domain
            .qualityRequirementsScore(0.6)
            .testingContextScore(0.4) // Unclear performance testing
            .estimatedLinesOfCode(1200)
            .integrationPoints(3)
            .newTechnologies(2) // New performance tech
            .timeConstraintSeverity(0.8) // Very tight deadlines
            .build();
        
        ContextSuggestion suggestion = advisor.analyzeAndSuggest("trading-perf-003", input);
        
        System.out.println(suggestion.getFormattedSummary());
        
        System.out.println("\\n⚡ SpeedDemon's Perspective:");
        System.out.println("Performance optimization is critical but context is lacking:");
        System.out.println("• Trading domain is 90% novel to me");
        System.out.println("• Performance architecture guidance insufficient (40%)");
        System.out.println("• Testing strategy for performance unclear");
        System.out.println("• 2 new performance technologies add complexity");
        System.out.println("\\nRecommendation: Get domain expert and performance architect input NOW.");
    }
    
    /**
     * Long-running task with decreasing efficiency
     */
    private static void demonstrateLongRunningTaskScenario() {
        System.out.println("SCENARIO 4: Long-Running Task - Efficiency Decline Detection");
        
        IntelligentContextAdvisor advisor = new IntelligentContextAdvisor(AgentCharacteristic.CLEAN_CODE_FREAK);
        
        // Simulate a task that's been running for hours with slow progress
        TaskAnalysisInput input = TaskAnalysisInput.builder()
            .functionalClarityScore(0.5) // Medium clarity
            .requirementCompletenessScore(0.5)
            .technicalContextScore(0.5)
            .architecturalGuidanceScore(0.5)
            .domainKnowledgeScore(0.4) // Domain confusion
            .domainNovelty(0.7)
            .qualityRequirementsScore(0.5)
            .testingContextScore(0.5)
            .estimatedLinesOfCode(1500)
            .integrationPoints(4)
            .newTechnologies(1)
            .timeConstraintSeverity(0.6)
            .build();
        
        ContextSuggestion suggestion = advisor.analyzeAndSuggest("long-running-004", input);
        
        System.out.println(suggestion.getFormattedSummary());
        
        System.out.println("\\n⏰ Time Investment Analysis:");
        System.out.println("ALERT: Efficiency concerns detected!");
        System.out.println("• 3+ hours invested with only 25% progress");
        System.out.println("• Projected completion: 12+ hours (vs 8 hour estimate)");
        System.out.println("• High business value task at risk");
        System.out.println("• Domain confusion likely causing delays");
        System.out.println("\\n🚨 RECOMMENDATION: PAUSE and reassess approach immediately!");
        System.out.println("Potential time savings from context enhancement: 2+ hours");
    }
}