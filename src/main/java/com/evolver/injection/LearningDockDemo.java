package com.evolver.injection;

/**
 * Demo class to showcase LearningDock functionality
 */
public class LearningDockDemo {

    public static void main(String[] args) {
        System.out.println("🚢 Learning Dock Demo - Evolver Project Analysis");
        System.out.println("=".repeat(60));
        
        // Demonstrate LearningDock creation for current project
        demonstrateBasicDock();
        
        System.out.println();
        
        // Demonstrate custom entry points
        demonstrateCustomEntryPoints();
        
        System.out.println();
        
        // Demonstrate integration with FrameworkInjector
        demonstrateFrameworkIntegration();
    }
    
    private static void demonstrateBasicDock() {
        System.out.println("📍 1. Basic Learning Dock Creation");
        System.out.println("-".repeat(40));
        
        String projectRoot = System.getProperty("user.dir");
        
        LearningDock dock = new LearningDock(
            "evolver-main-dock",
            projectRoot,
            ProjectType.GRADLE_JAVA,
            FrameworkType.NONE
        );
        
        System.out.println("🏗️ Created dock: " + dock.getDockName());
        System.out.println("📁 Project root: " + dock.getProjectRoot());
        System.out.println("🔧 Project type: " + dock.getProjectType());
        System.out.println("⚙️ Framework: " + dock.getFrameworkType());
        
        // Show default entry points for Java project
        System.out.println("📍 Default entry points:");
        for (String entryPoint : dock.getEntryPoints()) {
            System.out.println("  • " + entryPoint);
        }
        
        System.out.println("🎯 Learning paths:");
        for (String learningPath : dock.getLearningPaths()) {
            System.out.println("  • " + learningPath);
        }
    }
    
    private static void demonstrateCustomEntryPoints() {
        System.out.println("🎯 2. Custom Entry Points");
        System.out.println("-".repeat(40));
        
        String projectRoot = System.getProperty("user.dir");
        
        LearningDock dock = new LearningDock(
            "evolver-custom-dock",
            projectRoot,
            ProjectType.GRADLE_JAVA,
            FrameworkType.NONE
        )
        .withEntryPoint("docs/")
        .withEntryPoint("src/main/java/com/evolver/agent/")
        .withEntryPoints("experiences.json", "docs/framework/");
        
        System.out.println("📍 Custom entry points added:");
        for (String entryPoint : dock.getEntryPoints()) {
            if (entryPoint.contains("docs/") || entryPoint.contains("agent/") || 
                entryPoint.contains("experiences.json") || entryPoint.contains("framework/")) {
                System.out.println("  ✨ " + entryPoint + " (custom)");
            } else {
                System.out.println("  • " + entryPoint);
            }
        }
    }
    
    private static void demonstrateFrameworkIntegration() {
        System.out.println("🔗 3. Framework Integration");
        System.out.println("-".repeat(40));
        
        try {
            // Simulate FrameworkInjector integration
            System.out.println("🚀 Simulating FrameworkInjector.inject()");
            System.out.println("   .detectExistingFramework()");
            System.out.println("   .createLearningDock('demo')");
            System.out.println("   .spawnAgent(DOCUMENTATION_OBSESSIVE)");
            System.out.println("   .activate()");
            
            String projectRoot = System.getProperty("user.dir");
            
            // Create dock as injector would
            LearningDock dock = new LearningDock(
                "framework-injector-dock",
                projectRoot,
                ProjectType.GRADLE_JAVA,
                FrameworkType.NONE
            );
            
            // Add agent-specific entry points
            dock.withEntryPoint("docs/agent/")
                .withEntryPoint("src/main/java/com/evolver/agent/")
                .withEntryPoint("experiences.json");
            
            System.out.println("✅ Learning dock configured for agent: DOCUMENTATION_OBSESSIVE");
            System.out.println("📍 Agent will focus on:");
            System.out.println("  • Agent documentation in docs/agent/");
            System.out.println("  • Agent implementation patterns");
            System.out.println("  • Experience database structure");
            
            // Demonstrate materialization (but don't actually create files in demo)
            System.out.println("🏗️ Would materialize dock at: " + dock.getEntryPoint());
            
        } catch (Exception e) {
            System.err.println("⚠️ Demo error: " + e.getMessage());
        }
    }
}