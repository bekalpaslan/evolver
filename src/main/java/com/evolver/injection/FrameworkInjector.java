package com.evolver.injection;

import com.evolver.agent.AgentCharacteristic;
import com.evolver.agent.AgentInterface;
import com.evolver.context.ContextEngine;
import java.io.File;
import java.util.*;

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
        System.out.println("🔍 Scanning project for existing frameworks...");
        
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
     * Activate the injection - merge framework and start agents
     */
    public InjectionResult activate() {
        System.out.println("🚀 Activating framework injection...");
        
        // 1. Merge frameworks
        MergeResult mergeResult = merger.merge(
            detectedProjectType, 
            existingFramework, 
            projectRoot
        );
        
        // 2. Start agents with their characteristics
        List<AgentInterface> activeAgents = new ArrayList<>();
        for (AgentCharacteristic characteristic : spawnedAgents) {
            AgentInterface agent = new AgentInterface(characteristic);
            agent.startLearning(projectRoot + "/evolver-dock");
            activeAgents.add(agent);
        }
        
        System.out.println("✅ Framework injected successfully!");
        System.out.println("✅ " + activeAgents.size() + " agents learning...");
        
        return new InjectionResult(mergeResult, activeAgents);
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
            System.out.println("  • " + agent.getCharacteristic().getName() + 
                             " (" + agent.getCharacteristic().getPersonality() + ")");
        }
    }
}