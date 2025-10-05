package com.evolver.injection;

/**
 * Live test to materialize a real LearningDock
 */
public class LearningDockLiveTest {

    public static void main(String[] args) {
        System.out.println("🚢 Live LearningDock Test - Creating Real Dock");
        System.out.println("=".repeat(60));
        
        String projectRoot = System.getProperty("user.dir");
        
        LearningDock dock = new LearningDock(
            "evolver-test-dock",
            projectRoot,
            ProjectType.GRADLE_JAVA,
            FrameworkType.NONE
        )
        .withEntryPoint("docs/agent/")
        .withEntryPoint("src/main/java/com/evolver/agent/")
        .withEntryPoint("experiences.json");
        
        System.out.println("🏗️ Materializing dock...");
        dock.materialize();
        
        System.out.println("🤖 Starting agent learning...");
        dock.startLearning();
        
        System.out.println("✅ Live test complete!");
        System.out.println("📍 Check: " + dock.getEntryPoint());
    }
}