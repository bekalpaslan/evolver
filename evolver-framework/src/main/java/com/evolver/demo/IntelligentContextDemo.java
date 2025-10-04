package com.evolver.demo;

import com.evolver.agent.*;
import com.evolver.context.*;
import com.evolver.experiences.ExperienceRepository;
import java.util.*;

/**
 * INTELLIGENT CONTEXT FILLING DEMONSTRATION
 * 
 * This demo shows how different agent characteristics receive 
 * different context for the same task, and how technology ratings
 * are grouped by agent type.
 */
public class IntelligentContextDemo {
    
    public static void main(String[] args) {
        demonstrateIntelligentContextFilling();
        demonstrateAgentSpecificTechnologyIntelligence();
    }
    
    /**
     * Demonstrate how different agents get different context for the same task
     */
    public static void demonstrateIntelligentContextFilling() {
        System.out.println("=== INTELLIGENT CONTEXT FILLING DEMO ===\\n");
        
        ExperienceRepository repository = new ExperienceRepository();
        IntelligentContextEngine engine = new IntelligentContextEngine(repository);
        
        // Same task, different agent characteristics
        String taskType = "implement_rest_api";
        String domain = "web_development";
        
        // Test with Documentation Obsessive agent
        testAgentContext(engine, AgentCharacteristic.DOCUMENTATION_OBSESSIVE, taskType, domain);
        
        // Test with Clean Code Freak agent  
        testAgentContext(engine, AgentCharacteristic.CLEAN_CODE_FREAK, taskType, domain);
        
        // Test with Speed Demon agent
        testAgentContext(engine, AgentCharacteristic.SPEED_DEMON, taskType, domain);
        
        // Test with Chaos Monkey agent
        testAgentContext(engine, AgentCharacteristic.CHAOS_MONKEY, taskType, domain);
    }
    
    private static void testAgentContext(IntelligentContextEngine engine, 
                                       AgentCharacteristic characteristic,
                                       String taskType, String domain) {
        
        System.out.println("\\n--- Agent: " + characteristic.getName() + " ---");
        
        IntelligentContextRequest request = IntelligentContextRequest.builder()
            .taskType(taskType)
            .domain(domain)
            .agentCharacteristic(characteristic)
            .requiredInformation("REST API implementation", "best practices")
            .maxTokenBudget(4000)
            .priority(IntelligentContextRequest.ContextPriority.HIGH)
            .build();
        
        IntelligentContextResult result = engine.fillContext(request);
        
        System.out.println("Context Preferences:");
        System.out.println("- Token Budget Preference: " + 
            characteristic.getAttributes().get("tokenBudgetPreference"));
        System.out.println("- Relevance Threshold: " + 
            characteristic.getAttributes().get("relevanceThreshold"));
        System.out.println("- Priority Collectors: " + 
            characteristic.getAttributes().get("priorityCollectors"));
        System.out.println("- Focus Areas: " + 
            characteristic.getAttributes().get("focusAreas"));
        
        System.out.println("\\nContext Result:");
        System.out.println("- Effectiveness Score: " + 
            String.format("%.2f", result.getEffectivenessScore()));
        System.out.println("- Used Collectors: " + result.getUsedCollectors().size());
        System.out.println("- Applied Rules: " + result.getAppliedRules().size());
        
        // Show agent-specific technology experiences
        Map<String, List<TechnologyExperience>> agentTech = result.getAgentTechnologyExperiences();
        System.out.println("\\nAgent-Specific Technology Groups:");
        for (String group : agentTech.keySet()) {
            System.out.println("- " + group + ": " + agentTech.get(group).size() + " experiences");
        }
    }
    
    /**
     * Demonstrate agent-specific technology intelligence
     */
    public static void demonstrateAgentSpecificTechnologyIntelligence() {
        System.out.println("\\n\\n=== AGENT-SPECIFIC TECHNOLOGY INTELLIGENCE DEMO ===\\n");
        
        AgentTechnologyIntelligence intelligence = new AgentTechnologyIntelligence();
        
        // Simulate different agent experiences with same technology
        simulateAgentExperiences(intelligence);
        
        // Show how different agents rate the same technology
        demonstrateRatingDifferences(intelligence);
        
        // Show agent-specific recommendations
        demonstrateAgentRecommendations(intelligence);
        
        // Show harmony matrix differences
        demonstrateHarmonyDifferences(intelligence);
    }
    
    private static void simulateAgentExperiences(AgentTechnologyIntelligence intelligence) {
        System.out.println("Simulating agent experiences with Spring Boot 3.1.0...");
        
        // DocBot focuses on documentation quality
        TechnologyExperience docBotExperience = TechnologyExperience.builder("Spring Boot", "3.1.0", TechnologyType.FRAMEWORK)
            .easeOfUse(7.2)
            .power(8.9)
            .performance(8.1)
            .reliability(8.7)
            .documentationQuality(9.4) // DocBot loves good docs!
            .communitySupport(8.8)
            .overallSatisfaction(8.5)
            .projectContext("Enterprise REST API with comprehensive documentation")
            .agentNotes("Excellent documentation makes this framework shine for documentation-focused development")
            .build();
        
        intelligence.recordAgentExperience("DocBot", docBotExperience);
        
        // CleanFreak focuses on code quality
        TechnologyExperience cleanFreakExperience = TechnologyExperience.builder("Spring Boot", "3.1.0", TechnologyType.FRAMEWORK)
            .easeOfUse(8.8) // CleanFreak loves clean abstractions
            .power(9.2)
            .performance(8.0)
            .reliability(9.1)
            .documentationQuality(8.2)
            .communitySupport(8.5)
            .overallSatisfaction(9.0)
            .projectContext("Microservices with clean architecture patterns")
            .agentNotes("Perfect for implementing clean code principles and SOLID design patterns")
            .usedTogether("PostgreSQL", new TechnologyCombination("PostgreSQL", 9.3, 
                "Seamless integration with excellent transaction management", "enterprise_data"))
            .build();
        
        intelligence.recordAgentExperience("CleanFreak", cleanFreakExperience);
        
        // SpeedDemon focuses on performance
        TechnologyExperience speedDemonExperience = TechnologyExperience.builder("Spring Boot", "3.1.0", TechnologyType.FRAMEWORK)
            .easeOfUse(7.8)
            .power(8.7)
            .performance(9.5) // SpeedDemon prioritizes performance!
            .reliability(8.4)
            .documentationQuality(7.9)
            .communitySupport(8.3)
            .overallSatisfaction(8.7)
            .projectContext("High-throughput trading system")
            .agentNotes("Native compilation with GraalVM provides exceptional performance gains")
            .usedTogether("Redis", new TechnologyCombination("Redis", 9.7,
                "Perfect caching integration for sub-millisecond response times", "high_performance"))
            .build();
        
        intelligence.recordAgentExperience("SpeedDemon", speedDemonExperience);
        
        System.out.println("✓ Recorded experiences for DocBot, CleanFreak, and SpeedDemon\\n");
    }
    
    private static void demonstrateRatingDifferences(AgentTechnologyIntelligence intelligence) {
        System.out.println("--- RATING ANALYSIS: Spring Boot 3.1.0 ---");
        
        AgentRatingAnalysis analysis = intelligence.getAgentRatingAnalysis("Spring Boot", "3.1.0");
        
        System.out.println("Ratings by Agent Type:");
        for (Map.Entry<String, List<Double>> entry : analysis.getRatingsByAgent().entrySet()) {
            String agentType = entry.getKey();
            double avgRating = entry.getValue().stream().mapToDouble(Double::doubleValue).average().orElse(0.0);
            System.out.println("- " + agentType + ": " + String.format("%.1f", avgRating) + "/10");
        }
        
        System.out.println("\\nAgent Consensus: " + String.format("%.2f", analysis.getAgentConsensus()));
        System.out.println("Agent Divergence: " + String.format("%.2f", analysis.getAgentDivergence()));
        
        System.out.println("\\nAgent Notes Highlights:");
        for (Map.Entry<String, List<String>> entry : analysis.getNotesByAgent().entrySet()) {
            String agentType = entry.getKey();
            String notes = entry.getValue().get(0); // First note
            System.out.println("- " + agentType + ": \"" + notes.substring(0, Math.min(60, notes.length())) + "...\"");
        }
    }
    
    private static void demonstrateAgentRecommendations(AgentTechnologyIntelligence intelligence) {
        System.out.println("\\n--- AGENT-SPECIFIC RECOMMENDATIONS ---");
        
        // Get recommendations for each agent type
        String[] agentTypes = {"DocBot", "CleanFreak", "SpeedDemon"};
        String domain = "web_development";
        
        for (String agentType : agentTypes) {
            System.out.println("\\n" + agentType + " Recommendations for " + domain + ":");
            
            AgentTechnologyRecommendations recommendations = 
                intelligence.getRecommendationsForAgent(agentType, domain);
            
            System.out.println("- Top Rated: " + recommendations.getTopRated().size() + " technologies");
            System.out.println("- Harmonious Combinations: " + recommendations.getHarmoniousCombinations().size() + " pairs");
            System.out.println("- Domain Specific: " + recommendations.getDomainSpecific().size() + " technologies");
            System.out.println("- Emerging: " + recommendations.getEmergingTechnologies().size() + " technologies");
            
            // Show top recommendation with reasoning
            List<TechnologyRecommendation> allRecs = recommendations.getAllRecommendations();
            if (!allRecs.isEmpty()) {
                TechnologyRecommendation top = allRecs.get(0);
                System.out.println("  → Top Pick: " + top.getTechnology() + " " + top.getVersion() + 
                    " (Rating: " + String.format("%.1f", top.getRating()) + ")");
                System.out.println("    Reason: " + top.getReason());
            }
        }
    }
    
    private static void demonstrateHarmonyDifferences(AgentTechnologyIntelligence intelligence) {
        System.out.println("\\n--- HARMONY MATRIX DIFFERENCES ---");
        
        String[] agentTypes = {"CleanFreak", "SpeedDemon"};
        
        for (String agentType : agentTypes) {
            System.out.println("\\n" + agentType + " Technology Harmony Matrix:");
            
            Map<String, Map<String, Double>> harmonyMatrix = 
                intelligence.getHarmonyMatrixForAgent(agentType);
            
            for (Map.Entry<String, Map<String, Double>> techEntry : harmonyMatrix.entrySet()) {
                String tech = techEntry.getKey();
                System.out.println("- " + tech + " pairs well with:");
                
                techEntry.getValue().entrySet().stream()
                    .sorted((a, b) -> Double.compare(b.getValue(), a.getValue()))
                    .limit(3) // Top 3 harmonious technologies
                    .forEach(harmony -> {
                        System.out.println("    → " + harmony.getKey() + 
                            " (Harmony: " + String.format("%.1f", harmony.getValue()) + "/10)");
                    });
            }
        }
    }
}