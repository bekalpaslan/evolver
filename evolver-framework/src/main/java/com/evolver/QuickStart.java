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
        System.out.println("🚀 Evolver Framework - Quick Start");
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
        System.out.println("📦 Portable Framework Injection");
        System.out.println("------------------------------");
        System.out.println();
        
        System.out.println("The Evolver framework is now ready for injection!");
        System.out.println();
        
        System.out.println("🔌 To inject into your project:");
        System.out.println("```java");
        System.out.println("// ONE line injection");
        System.out.println("FrameworkInjector.inject()");
        System.out.println("    .withLearningDock(\"docs/\")              // Where to start learning");
        System.out.println("    .withAgentCharacteristic(\"DocumentationObsessed\")  // Agent personality");
        System.out.println("    .start();                                // Begin autonomous learning");
        System.out.println("```");
        System.out.println();
        
        System.out.println("🎭 Available Agent Characteristics:");
        String[] characteristics = {
            "DocumentationObsessed - Creates comprehensive docs, focuses on clarity",
            "PerformanceFreak - Optimizes for speed, measures everything",
            "SecurityParanoid - Validates inputs, checks vulnerabilities", 
            "ArchitectureNazi - Enforces patterns, maintains structure",
            "TestingEvangelist - Writes tests first, ensures coverage",
            "LegacyWhisperer - Understands old code, preserves stability"
        };
        
        for (String characteristic : characteristics) {
            System.out.println("  🤖 " + characteristic);
        }
        System.out.println();
        
        System.out.println("🎯 Learning Docks (where agents start):");
        System.out.println("  📁 \"docs/\" - Learn from documentation");
        System.out.println("  📁 \"src/main/api/\" - Learn from API patterns");
        System.out.println("  📁 \"src/main/models/\" - Learn from domain models");
        System.out.println("  📁 \"tests/\" - Learn from test patterns");
        System.out.println("  📁 \"config/\" - Learn from configuration");
        System.out.println();
        
        System.out.println("💡 What happens next:");
        System.out.println("  1. 🔍 Agent discovers your project structure");
        System.out.println("  2. 📚 Agent learns your patterns and conventions");
        System.out.println("  3. 🎯 Agent optimizes context for your specific domain");
        System.out.println("  4. 🔄 Framework evolves to match your project perfectly");
        System.out.println();
        
        System.out.println("📚 Documentation:");
        System.out.println("  • INJECTION_GUIDE.md - Complete injection guide");
        System.out.println("  • AGENT_EXPERIENCE_GUIDELINES.md - AI agent collaboration");
        System.out.println("  • README.md - Framework overview");
        System.out.println();
        
        System.out.println("🚀 Demo Commands:");
        System.out.println("  ./gradlew agentDemo        - See autonomous agents in action");
        System.out.println("  ./gradlew shareExperience  - See simple experience database");
        System.out.println("  ./gradlew agentWorkflow    - See complete agent workflow");
        System.out.println("  ./gradlew inject           - Demo framework injection");
        System.out.println();
        
        System.out.println("✅ Ready to enhance your project with autonomous context engineering!");
    }
    
    private static void showDatabaseDemo() {
        System.out.println("🗄️ Simple Experience Database");
        System.out.println("-----------------------------");
        System.out.println();
        
        System.out.println("AI agents use simple commands like a map or database...");
        System.out.println();
        
        // Run the simple database demo
        SimpleDBDemo.main(new String[]{});
        
        System.out.println();
        System.out.println("💡 Key Benefits:");
        System.out.println("  • Simple as map operations: AgentCommands.find(\"CATEGORY\")");
        System.out.println("  • Smart discovery: AgentCommands.whatINeed(char,cat,project)");
        System.out.println("  • Duplicate prevention: AgentCommands.exists(title,category)");
        System.out.println("  • Auto-loading: Experiences load automatically from files");
        System.out.println("  • Fast search: In-memory database with smart indexes");
    }
    
    private static void showAgentWorkflow() {
        System.out.println("🤖 Complete Agent Workflow");
        System.out.println("--------------------------");
        System.out.println();
        
        System.out.println("Watch an agent: Learn → Problem-solve → Share discovery...");
        System.out.println();
        
        // Run the agent workflow demo
        SimpleAgentInterface.main(new String[]{});
    }
    
    private static void showExperienceDemo() {
        System.out.println("🤝 AI Agent Experience Sharing");
        System.out.println("------------------------------");
        System.out.println();
        
        System.out.println("Agents learn from each other by sharing experiences...");
        System.out.println();
        
        // Run the experience demo
        ExperienceDemo.main(new String[]{});
        
        System.out.println();
        System.out.println("📂 Experience Repository Structure:");
        System.out.println("  evolver-framework/");
        System.out.println("  └── experiences/");
        System.out.println("      ├── categories/");
        System.out.println("      │   ├── framework_usage/");
        System.out.println("      │   ├── context_optimization/");
        System.out.println("      │   ├── project_integration/");
        System.out.println("      │   ├── debugging/");
        System.out.println("      │   └── ... (all categories)");
        System.out.println("      └── experience_index.json");
        System.out.println();
        
        System.out.println("📖 For AI Agents:");
        System.out.println("  • Read AGENT_EXPERIENCE_GUIDELINES.md");
        System.out.println("  • Always check existing experiences before sharing new ones");
        System.out.println("  • Share your discoveries to help other agents learn");
        System.out.println("  • Be specific and actionable in your experience descriptions");
    }
}