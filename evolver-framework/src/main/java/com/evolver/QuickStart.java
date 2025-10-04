package com.evolver;

import com.evolver.injection.FrameworkInjector;
import com.evolver.experiences.ExperienceRepository;
import com.evolver.experiences.ExperienceDemo;

/**
 * Quick Start for Evolver Framework
 * 
 * Drop this entire 'evolver-framework' folder into any project and run this class.
 * The framework will auto-detect your project and begin learning.
 */
public class QuickStart {
    
    public static void main(String[] args) {
        System.out.println("[ROCKET] Evolver Framework - Quick Start");
        System.out.println("==================================");
        System.out.println();
        
        if (args.length > 0) {
            switch (args[0]) {
                case "experiences":
                    showExperienceDemo();
                    break;
                case "database":
                    showDatabaseDemo();
                    break;
                case "workflow":
                    showAgentWorkflow();
                    break;
                default:
                    showFrameworkInjection();
            }
        } else {
            showFrameworkInjection();
        }
    }
    
    private static void showFrameworkInjection() {
        System.out.println("[ASCII][ASCII] Portable Framework Injection");
        System.out.println("------------------------------");
        System.out.println();
        
        System.out.println("The Evolver framework is now ready for injection!");
        System.out.println();
        
        System.out.println("[ASCII][ASCII] To inject into your project:");
        System.out.println("```java");
        System.out.println("// ONE line injection");
        System.out.println("FrameworkInjector.inject()");
        System.out.println("    .withLearningDock(\"docs/\")              // Where to start learning");
        System.out.println("    .withAgentCharacteristic(\"DocumentationObsessed\")  // Agent personality");
        System.out.println("    .start();                                // Begin autonomous learning");
        System.out.println("```");
        System.out.println();
        
        System.out.println("[ASCII][ASCII] Available Agent Characteristics:");
        String[] characteristics = {
            "DocumentationObsessed - Creates comprehensive docs, focuses on clarity",
            "PerformanceFreak - Optimizes for speed, measures everything",
            "SecurityParanoid - Validates inputs, checks vulnerabilities", 
            "ArchitectureEnforcer - Enforces patterns, maintains structure",
            "TestingEvangelist - Writes tests first, ensures coverage",
            "LegacyWhisperer - Understands old code, preserves stability"
        };
        
        for (String characteristic : characteristics) {
            System.out.println("  [ASCII][ASCII] " + characteristic);
        }
        System.out.println();
        
        System.out.println("[TARGET] Learning Docks (where agents start):");
        System.out.println("  [ASCII][ASCII] \"docs/\" - Learn from documentation");
        System.out.println("  [ASCII][ASCII] \"src/main/api/\" - Learn from API patterns");
        System.out.println("  [ASCII][ASCII] \"src/main/models/\" - Learn from domain models");
        System.out.println("  [ASCII][ASCII] \"tests/\" - Learn from test patterns");
        System.out.println("  [ASCII][ASCII] \"config/\" - Learn from configuration");
        System.out.println();
        
        System.out.println("[IDEA] What happens next:");
        System.out.println("  1. [DISCOVER] Agent discovers your project structure");
        System.out.println("  2. [LEARN] Agent learns your patterns and conventions");
        System.out.println("  3. [TARGET] Agent optimizes context for your specific domain");
        System.out.println("  4. [ASCII][ASCII] Framework evolves to match your project perfectly");
        System.out.println();
        
        System.out.println("[LEARN] Documentation:");
        System.out.println("  [ASCII] INJECTION_GUIDE.md - Complete injection guide");
        System.out.println("  [ASCII] AGENT_EXPERIENCE_GUIDELINES.md - AI agent collaboration");
        System.out.println("  [ASCII] README.md - Framework overview");
        System.out.println();
        
        System.out.println("[ROCKET] Demo Commands:");
        System.out.println("  ./gradlew agentDemo        - See autonomous agents in action");
        System.out.println("  ./gradlew shareExperience  - See simple experience database");
        System.out.println("  ./gradlew agentWorkflow    - See complete agent workflow");
        System.out.println("  ./gradlew inject           - Demo framework injection");
        System.out.println();
        
        System.out.println("[ASCII] Ready to enhance your project with autonomous context engineering!");
    }
    
    private static void showDatabaseDemo() {
        System.out.println("[ASCII][ASCII][ASCII] Simple Experience Database");
        System.out.println("-----------------------------");
        System.out.println();
        
        System.out.println("AI agents use simple commands like a map or database...");
        System.out.println();
        
        // Show the simple database functionality
        System.out.println("[ASCII][ASCII] Experience Database Demo:");
        try {
            ExperienceRepository.getInstance().showStats();
        } catch (Exception e) {
            System.out.println("Database demo not available: " + e.getMessage());
        }
        
        System.out.println();
        System.out.println("[IDEA] Key Benefits:");
        System.out.println("  [ASCII] Simple as map operations: AgentCommands.find(\"CATEGORY\")");
        System.out.println("  [ASCII] Smart discovery: AgentCommands.whatINeed(char,cat,project)");
        System.out.println("  [ASCII] Duplicate prevention: AgentCommands.exists(title,category)");
        System.out.println("  [ASCII] Auto-loading: Experiences load automatically from files");
        System.out.println("  [ASCII] Fast search: In-memory database with smart indexes");
    }
    
    private static void showAgentWorkflow() {
        System.out.println("[ASCII][ASCII] Complete Agent Workflow");
        System.out.println("--------------------------");
        System.out.println();
        
        System.out.println("Watch an agent: Learn [ASCII] Problem-solve [ASCII] Share discovery...");
        System.out.println();
        
        // Run the intelligent agent demo
        System.out.println("[BRAIN] Intelligent Agent Demo:");
        try {
            Class<?> agentDemo = Class.forName("com.evolver.agent.IntelligentAgentInterface");
            agentDemo.getMethod("main", String[].class).invoke(null, (Object) new String[]{});
        } catch (Exception e) {
            System.out.println("Agent demo not available: " + e.getMessage());
        }
    }
    
    private static void showExperienceDemo() {
        System.out.println("[ASCII][ASCII] AI Agent Experience Sharing");
        System.out.println("------------------------------");
        System.out.println();
        
        System.out.println("Agents learn from each other by sharing experiences...");
        System.out.println();
        
        // Run the experience demo
        ExperienceDemo.main(new String[]{});
        
        System.out.println();
        System.out.println("[ASCII][ASCII] Experience Repository Structure:");
        System.out.println("  evolver-framework/");
        System.out.println("  [ASCII][ASCII][ASCII] experiences/");
        System.out.println("      [ASCII][ASCII][ASCII] categories/");
        System.out.println("      [ASCII]   [ASCII][ASCII][ASCII] framework_usage/");
        System.out.println("      [ASCII]   [ASCII][ASCII][ASCII] context_optimization/");
        System.out.println("      [ASCII]   [ASCII][ASCII][ASCII] project_integration/");
        System.out.println("      [ASCII]   [ASCII][ASCII][ASCII] debugging/");
        System.out.println("      [ASCII]   [ASCII][ASCII][ASCII] ... (all categories)");
        System.out.println("      [ASCII][ASCII][ASCII] experience_index.json");
        System.out.println();
        
        System.out.println("[ASCII][ASCII] For AI Agents:");
        System.out.println("  [ASCII] Read AGENT_EXPERIENCE_GUIDELINES.md");
        System.out.println("  [ASCII] Always check existing experiences before sharing new ones");
        System.out.println("  [ASCII] Share your discoveries to help other agents learn");
        System.out.println("  [ASCII] Be specific and actionable in your experience descriptions");
    }
}

