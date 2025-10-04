package com.evolver.injection;

import com.evolver.agent.AgentCharacteristic;
import com.evolver.agent.AgentInterface;
import com.evolver.context.ContextEngine;
import com.evolver.injection.FrameworkMerger.MergeResult;
import com.evolver.injection.LearningDockFactory.LearningDock;
import java.io.File;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * FRAMEWORK INJECTOR
 * 
 * Makes the Evolver Context Framework injectable into ANY existing project.
 * Detects existing frameworks, merges seamlessly, creates learning docks.
 * 
 * Usage in ANY existing project:
 *   FrameworkInjector.inject()
 *       .detectExistingFramework()
 *       .createLearningDock()
 *       .spawnAgent(AgentCharacteristic.DOCUMENTATION_OBSESSIVE)
 *       .activate();
 */
public class FrameworkInjector {
    
    private final ProjectDetector detector;
    private final FrameworkMerger merger;
    private final LearningDockFactory dockFactory;
    private final List<AgentCharacteristic> spawnedAgents;
    private String projectRoot;
    private ProjectType detectedProjectType;
    private FrameworkType existingFramework;
    
    private FrameworkInjector(String projectRoot) {
        this.projectRoot = projectRoot != null ? projectRoot : System.getProperty("user.dir");
        this.detector = new ProjectDetector();
        this.merger = new FrameworkMerger();
        this.dockFactory = new LearningDockFactory();
        this.spawnedAgents = new ArrayList<>();
    }
    
    /**
     * Start injection into current directory
     */
    public static FrameworkInjector inject() {
        return inject(null);
    }
    
    /**
     * Start injection into specific project
     */
    public static FrameworkInjector inject(String projectPath) {
        return new FrameworkInjector(projectPath);
    }
    
    /**
     * Detect existing framework and project structure
     */
    public FrameworkInjector detectExistingFramework() {
        System.out.println("[DISCOVER] Scanning project for existing frameworks...");
        
        ProjectScan scan = detector.scanProject(projectRoot);
        this.detectedProjectType = scan.getProjectType();
        this.existingFramework = scan.getExistingFramework();
        
        System.out.println("✅ Project detected: " + detectedProjectType);
        System.out.println("✅ Framework found: " + existingFramework);
        
        return this;
    }
    
    /**
     * Create learning dock as entry point for agents
     */
    public FrameworkInjector createLearningDock() {
        return createLearningDock("main");
    }
    
    /**
     * Create named learning dock
     */
    public FrameworkInjector createLearningDock(String dockName) {
        System.out.println("🚢 Creating learning dock: " + dockName);
        
        LearningDock dock = dockFactory.createDock(
            dockName, 
            projectRoot, 
            detectedProjectType, 
            existingFramework
        );
        
        // Write dock files to project
        dock.materialize();
        
        System.out.println("✅ Learning dock ready at: " + dock.getEntryPoint());
        
        return this;
    }
    
    /**
     * Spawn an agent with specific characteristics
     */
    public FrameworkInjector spawnAgent(AgentCharacteristic characteristic) {
        System.out.println("🤖 Spawning agent: " + characteristic.getName());
        spawnedAgents.add(characteristic);
        return this;
    }
    
    /**
     * Spawn multiple agents with different characteristics
     */
    public FrameworkInjector spawnAgents(AgentCharacteristic... characteristics) {
        for (AgentCharacteristic characteristic : characteristics) {
            spawnAgent(characteristic);
        }
        return this;
    }
    
    /**
     * EVOLVED: Activate the injection with intelligent optimization
     */
    public InjectionResult activate() {
        System.out.println("[ROCKET] Activating EVOLVED framework injection...");
        
        // 0. EVOLUTION: Pre-injection intelligence gathering
        ProjectIntelligence intelligence = gatherProjectIntelligence();
        optimizeAgentsForProject(intelligence);
        
        // 1. Merge frameworks
        MergeResult mergeResult = merger.merge(
            detectedProjectType.toString(), 
            existingFramework.toString(), 
            projectRoot
        );
        
        // 2. Create learning dock (using existing method)
        LearningDock dock = dockFactory.createDock(projectRoot, detectedProjectType.toString());
        
        // 3. Spawn optimized agents
        List<AgentInterface> optimizedAgents = spawnOptimizedAgents(intelligence);
        
        // 4. Start collaborative learning
        initiateCollaborativeLearning(optimizedAgents, intelligence);
        
        return new InjectionResult(mergeResult, optimizedAgents);
    }
    
    /**
     * EVOLUTION: Gather project intelligence before injection
     */
    private ProjectIntelligence gatherProjectIntelligence() {
        System.out.println("[BRAIN] Gathering project intelligence...");
        
        ProjectIntelligence intel = new ProjectIntelligence();
        
        // Analyze project structure complexity
        intel.complexity = analyzeProjectComplexity();
        
        // Detect primary domains
        intel.primaryDomains = detectPrimaryDomains();
        
        // Assess current quality metrics
        intel.currentQuality = assessCurrentQuality();
        
        // Identify critical paths
        intel.criticalPaths = identifyCriticalPaths();
        
        System.out.println("[BRAIN] Intelligence: complexity=" + intel.complexity + 
                         ", domains=" + intel.primaryDomains.size() + 
                         ", quality=" + intel.currentQuality);
        
        return intel;
    }
    
    /**
     * EVOLUTION: Optimize agent characteristics for specific project
     */
    private void optimizeAgentsForProject(ProjectIntelligence intelligence) {
        List<AgentCharacteristic> optimizedCharacteristics = new ArrayList<>();
        
        for (AgentCharacteristic original : spawnedAgents) {
            // Create project-specific hybrid
            if (intelligence.complexity > 0.8) {
                // High complexity - blend with careful characteristics
                AgentCharacteristic optimized = AgentCharacteristic.hybrid(
                    original.name() + "_ProjectOptimized",
                    original,
                    AgentCharacteristic.MINIMALIST_ZEN,
                    0.7
                );
                optimizedCharacteristics.add(optimized);
            } else {
                optimizedCharacteristics.add(original);
            }
        }
        
        spawnedAgents.clear();
        spawnedAgents.addAll(optimizedCharacteristics);
    }
    
    private double analyzeProjectComplexity() {
        // Simple heuristic based on file count and structure depth
        return Math.min(1.0, countFiles() / 1000.0);
    }
    
    private List<String> detectPrimaryDomains() {
        return Arrays.asList("general"); // Simplified for now
    }
    
    private double assessCurrentQuality() {
        return 0.5; // Baseline quality score
    }
    
    private List<String> identifyCriticalPaths() {
        return Arrays.asList("src/main", "docs"); // Key paths to focus on
    }
    
    private long countFiles() {
        try {
            return java.nio.file.Files.walk(java.nio.file.Paths.get(projectRoot))
                .filter(java.nio.file.Files::isRegularFile)
                .count();
        } catch (Exception e) {
            return 100; // Default assumption
        }
    }
    
    /**
     * Project intelligence data structure
     */
    private static class ProjectIntelligence {
        double complexity;
        List<String> primaryDomains = new ArrayList<>();
        double currentQuality;
        List<String> criticalPaths = new ArrayList<>();
    }
    
    /**
     * Quick injection with smart defaults
     */
    public static InjectionResult quickInject() {
        return inject()
            .detectExistingFramework()
            .createLearningDock()
            .spawnAgents(
                AgentCharacteristic.DOCUMENTATION_OBSESSIVE,
                AgentCharacteristic.CLEAN_CODE_FREAK,
                AgentCharacteristic.PERFORMANCE_MANIAC
            )
            .activate();
    }
    
    /**
     * Stealth injection - minimal footprint
     */
    public static InjectionResult stealthInject() {
        return inject()
            .detectExistingFramework()
            .createLearningDock("hidden")
            .spawnAgent(AgentCharacteristic.STEALTH_LEARNER)
            .activate();
    }
}

/**
 * Project detection results
 */
class ProjectScan {
    private final ProjectType projectType;
    private final FrameworkType existingFramework;
    private final Map<String, Object> metadata;
    
    public ProjectScan(ProjectType projectType, FrameworkType existingFramework, Map<String, Object> metadata) {
        this.projectType = projectType;
        this.existingFramework = existingFramework;
        this.metadata = metadata;
    }
    
    public ProjectType getProjectType() { return projectType; }
    public FrameworkType getExistingFramework() { return existingFramework; }
    public Map<String, Object> getMetadata() { return metadata; }
}

/**
 * Injection results
 */
class InjectionResult {
    private final MergeResult mergeResult;
    private final List<AgentInterface> activeAgents;
    
    public InjectionResult(MergeResult mergeResult, List<AgentInterface> activeAgents) {
        this.mergeResult = mergeResult;
        this.activeAgents = activeAgents;
    }
    
    public MergeResult getMergeResult() { return mergeResult; }
    public List<AgentInterface> getActiveAgents() { return activeAgents; }
    
    public void printStatus() {
        System.out.println("\n📊 INJECTION STATUS");
        System.out.println("─".repeat(50));
        System.out.println("✅ Framework merged: " + mergeResult.isSuccess());
        System.out.println("🤖 Active agents: " + activeAgents.size());
        
        for (AgentInterface agent : activeAgents) {
            System.out.println("  • Agent initialized and ready for learning");
        }
    }
}
