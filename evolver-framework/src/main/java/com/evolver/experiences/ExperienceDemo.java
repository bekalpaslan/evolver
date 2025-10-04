package com.evolver.experiences;

import java.util.List;

/**
 * Demonstration of AI agent experience sharing system
 * 
 * This shows how AI agents can collaborate by sharing their learning experiences,
 * reading from other agents, and building collective knowledge.
 */
public class ExperienceDemo {
    
    public static void main(String[] args) {
        System.out.println("🤝 AI Agent Experience Sharing Demo");
        System.out.println("====================================");
        System.out.println();
        
        ExperienceRepository repository = new ExperienceRepository();
        
        // Show available categories
        showAvailableCategories();
        System.out.println();
        
        // Demonstrate experience sharing
        demonstrateExperienceSharing(repository);
        System.out.println();
        
        // Demonstrate experience discovery
        demonstrateExperienceDiscovery(repository);
        System.out.println();
        
        // Show collaboration examples
        demonstrateCollaboration(repository);
        System.out.println();
        
        System.out.println("✅ Experience sharing demo complete!");
        System.out.println();
        System.out.println("[TARGET] For AI Agents:");
        System.out.println("  • Always check existing experiences before sharing");
        System.out.println("  • Read relevant experiences before making decisions");
        System.out.println("  • Share your best strategies and insights");
        System.out.println("  • Be specific and actionable in descriptions");
        System.out.println("  • Tag experiences for discoverability");
    }
    
    private static void showAvailableCategories() {
        System.out.println("📂 Available Experience Categories:");
        System.out.println("----------------------------------");
        
        for (ExperienceCategory category : ExperienceCategory.values()) {
            System.out.printf("  🏷️  %-25s - %s%n", 
                            category.getDisplayName(), 
                            category.getDescription());
        }
    }
    
    private static void demonstrateExperienceSharing(ExperienceRepository repository) {
        System.out.println("📝 Experience Sharing Examples:");
        System.out.println("------------------------------");
        
        // Example 1: Context optimization experience
        Experience contextOpt = ExperienceBuilder
            .contextOptimization("agent_001", "PerformanceFreak")
            .title("Optimizing Context for Large Codebases")
            .description("Strategy for efficiently gathering context from 100K+ line projects")
            .situation("Working with legacy Java monolith, 150K lines, poor documentation")
            .approach("Created hierarchical context collection: package-level → class-level → method-level")
            .outcome("Reduced context gathering time from 45s to 8s, improved relevance by 60%")
            .lessonLearned("Start broad, then drill down based on task specificity")
            .lessonLearned("Cache package-level context for similar tasks")
            .contextItem("projectSize", "150K lines")
            .contextItem("language", "Java")
            .contextItem("framework", "Spring Boot")
            .tag("large-codebase")
            .tag("performance")
            .tag("hierarchical")
            .projectType("Java/Spring")
            .build();
        
        boolean shared1 = repository.shareExperience(contextOpt);
        System.out.println("📄 Shared context optimization experience: " + shared1);
        
        // Example 2: Integration experience
        Experience integration = ExperienceBuilder
            .projectIntegration("agent_002", "ArchitectureEnforcer")
            .title("Seamless React Integration Strategy")
            .description("How to integrate with React projects without disrupting existing workflows")
            .situation("React app with TypeScript, complex component hierarchy, strict ESLint rules")
            .approach("Created TypeScript-aware collectors, respected existing import patterns")
            .outcome("Zero build errors, seamless integration, team didn't notice the change")
            .lessonLearned("Respect existing code style and patterns")
            .lessonLearned("TypeScript types provide excellent context hints")
            .contextItem("language", "TypeScript")
            .contextItem("framework", "React")
            .contextItem("bundler", "Vite")
            .tag("typescript")
            .tag("react")
            .tag("seamless")
            .projectType("React/TypeScript")
            .build();
        
        boolean shared2 = repository.shareExperience(integration);
        System.out.println("📄 Shared integration experience: " + shared2);
        
        // Example 3: Debugging experience
        Experience debugging = ExperienceBuilder
            .debugging("agent_003", "DocumentationObsessed")
            .title("Solving Missing Context Issue")
            .description("How to debug when relevant context is being filtered out")
            .situation("Important business logic missing from generated context")
            .approach("Added debug logging to collectors, traced filtering decisions")
            .outcome("Found overly restrictive relevance filter, adjusted threshold from 0.8 to 0.6")
            .lessonLearned("Debug mode is essential for understanding context decisions")
            .lessonLearned("Relevance thresholds need project-specific tuning")
            .contextItem("issue", "missing context")
            .contextItem("cause", "relevance threshold")
            .contextItem("solution", "threshold adjustment")
            .tag("debugging")
            .tag("relevance")
            .tag("filtering")
            .build();
        
        boolean shared3 = repository.shareExperience(debugging);
        System.out.println("📄 Shared debugging experience: " + shared3);
    }
    
    private static void demonstrateExperienceDiscovery(ExperienceRepository repository) {
        System.out.println("[DISCOVER] Experience Discovery Examples:");
        System.out.println("---------------------------------");
        
        // Find experiences by category
        List<Experience> contextOptExperiences = repository.findExperiencesByCategory(
            ExperienceCategory.CONTEXT_OPTIMIZATION);
        System.out.println("📊 Context Optimization experiences found: " + contextOptExperiences.size());
        
        // Find experiences by characteristic
        List<Experience> perfFreakExperiences = repository.findExperiencesByCharacteristic("PerformanceFreak");
        System.out.println("⚡ PerformanceFreak experiences found: " + perfFreakExperiences.size());
        
        // Search by text
        List<Experience> reactExperiences = repository.searchExperiences("React");
        System.out.println("⚛️ React-related experiences found: " + reactExperiences.size());
        
        // Find by tags
        List<Experience> debuggingExperiences = repository.findExperiencesByTags(List.of("debugging"));
        System.out.println("🐛 Debugging experiences found: " + debuggingExperiences.size());
        
        // Show statistics
        System.out.println();
        System.out.println("📈 Repository Statistics:");
        repository.getStatistics().forEach((key, value) -> 
            System.out.println("  • " + key + ": " + value));
    }
    
    private static void demonstrateCollaboration(ExperienceRepository repository) {
        System.out.println("🤝 Agent Collaboration Scenarios:");
        System.out.println("---------------------------------");
        
        System.out.println("Scenario 1: New agent joining the team");
        System.out.println("  🤖 NewAgent: 'I need to learn React integration'");
        System.out.println("  [DISCOVER] Searches: category=PROJECT_INTEGRATION, tags=['react']");
        
        List<Experience> reactIntegration = repository.searchExperiences("React integration");
        if (!reactIntegration.isEmpty()) {
            Experience exp = reactIntegration.get(0);
            System.out.println("  [LEARN] Found: '" + exp.getTitle() + "' by " + exp.getAgentCharacteristic());
            System.out.println("  [IDEA] Key lesson: " + (exp.getLessonsLearned() != null && !exp.getLessonsLearned().isEmpty() 
                ? exp.getLessonsLearned().get(0) : "Respect existing patterns"));
        }
        System.out.println();
        
        System.out.println("Scenario 2: Facing performance issues");
        System.out.println("  🤖 SlowAgent: 'Context generation is taking too long'");
        System.out.println("  [DISCOVER] Searches: category=PERFORMANCE, characteristic=PerformanceFreak");
        
        List<Experience> performanceExps = repository.findExperiencesByCategory(ExperienceCategory.PERFORMANCE);
        System.out.println("  [LEARN] Found " + performanceExps.size() + " performance-related experiences");
        System.out.println("  [IDEA] Learning from experts who solved similar problems");
        System.out.println();
        
        System.out.println("Scenario 3: Sharing breakthrough discovery");
        System.out.println("  🤖 InnovativeAgent: 'Discovered amazing new caching strategy!'");
        System.out.println("  📝 Creates: Detailed experience with situation, approach, outcome");
        System.out.println("  [TARGET] Result: All agents benefit from the discovery");
        System.out.println("  🔄 Collective intelligence grows");
    }
}
