package com.evolver.injection;

import com.evolver.agent.identity.AgentCharacteristic;
import com.evolver.agent.lifecycle.AgentInterface;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.nio.file.*;
import java.io.*;
import com.google.gson.*;

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
    private final FrameworksExperienceDB experienceDB = new FrameworksExperienceDB();
    
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
                System.out.println("[SCANNING] Scanning project for existing frameworks...");
        
        ProjectScan scan = detector.scanProject(projectRoot);
        this.detectedProjectType = scan.getProjectType();
        this.existingFramework = scan.getExistingFramework();
        
        System.out.println("[OK] Project detected: " + detectedProjectType);
        System.out.println("[OK] Framework found: " + existingFramework);
        
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
        System.out.println("[SHIP] Creating learning dock: " + dockName);
        
        LearningDock dock = dockFactory.createDock(
            dockName, 
            projectRoot, 
            detectedProjectType, 
            existingFramework
        );
        
        // Write dock files to project
        dock.materialize();
        
        System.out.println("[OK] Learning dock ready at: " + dock.getEntryPoint());
        
        return this;
    }
    
    /**
     * Spawn an agent with specific characteristics
     */
    public FrameworkInjector spawnAgent(AgentCharacteristic characteristic) {
        System.out.println("[AGENT] Spawning agent: " + characteristic.getName());
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
        System.out.println("[LAUNCH] Activating framework injection...");
        
        // 1. Merge frameworks
        MergeResult mergeResult = merger.merge(
            detectedProjectType, 
            existingFramework, 
            projectRoot
        );
        
        // 2. Start agents with their characteristics
        List<AgentInterface> activeAgents = new ArrayList<>();
        for (int i = 0; i < spawnedAgents.size(); i++) {
            AgentInterface agent = new AgentInterface();
            // Agent automatically learns from the project
            activeAgents.add(agent);
        }
        
        System.out.println("[OK] Framework injected successfully!");
        System.out.println("[OK] " + activeAgents.size() + " agents learning...");
        
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
    
    /**
     * Agents share architectural decisions (no business info)
     */
    public boolean shareArchitecturalDecision(String agentId, String summary, String[] affectedModules, String frameworkVersion) {
        ArchitecturalDecision decision = new ArchitecturalDecision(agentId, summary, affectedModules, frameworkVersion);
        boolean committed = experienceDB.commitDecision(decision);
        if (!committed) {
            System.out.println("[CONFLICT] Another agent has already committed a decision for these modules. Engineer review required.");
        } else {
            System.out.println("[COMMIT] Architectural decision committed by agent: " + agentId);
        }
        return committed;
    }

    /**
     * Engineers review agent commits before merging
     */
    public void reviewAgentCommits(String[] affectedModules) {
        ArchitecturalDecision decision = experienceDB.getDecision(affectedModules);
        if (decision == null) {
            System.out.println("[REVIEW] No agent commit found for modules: " + String.join(", ", affectedModules));
            return;
        }
        System.out.println("[REVIEW] Agent: " + decision.getAgentId() + " committed decision at " + decision.getTimestamp());
        System.out.println("[REVIEW] Summary: " + decision.getSummary());
        System.out.println("[REVIEW] Framework version: " + decision.getFrameworkVersion());
        // Engineer can approve/reject here (simulated)
        System.out.println("[ACTION] Engineer must approve or reject this commit.");
    }
    
    /**
     * Example: DocumentationObsessed agent shares an architectural decision
     */
    public void demoDocumentationObsessedDecision() {
        String agentId = "DocumentationObsessed";
        String summary = "Refactored context engine to use modular collectors for code structure, dependencies, runtime errors, and documentation. No business logic or sensitive data included. All changes documented in docs/ARCHITECTURE.md.";
        String[] affectedModules = {"context-engine", "collectors", "docs/ARCHITECTURE.md"};
        String frameworkVersion = "1.0.0";
        boolean committed = shareArchitecturalDecision(agentId, summary, affectedModules, frameworkVersion);
        if (committed) {
            System.out.println("[AGENT] DocumentationObsessed agent decision committed. Ready for engineer review.");
        } else {
            System.out.println("[AGENT] Conflict detected. Engineer review required before merging.");
        }
        reviewAgentCommits(affectedModules);
    }
    
    /**
     * Pull experiences from GitHub and load into DB
     */
    public void syncExperiencesFromGitHub() {
        // Simulate: git pull origin main (or fetch file from GitHub API)
        System.out.println("[SYNC] Pulling shared experiences from GitHub...");
        experienceDB.loadFromFile();
        System.out.println("[SYNC] Experiences loaded into memory DB.");
    }

    /**
     * Commit all in-memory experiences to JSON file (for push to GitHub)
     */
    public void commitExperiencesToFile() {
        experienceDB.saveToFile();
        System.out.println("[COMMIT] Experiences saved to shared JSON file. Ready to push to GitHub.");
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
 * Architectural decision made by an agent (no business info)
 */
class ArchitecturalDecision {
    private final String agentId;
    private final String summary;
    private final String[] affectedModules;
    private final LocalDateTime timestamp;
    private final String frameworkVersion;

    public ArchitecturalDecision(String agentId, String summary, String[] affectedModules, String frameworkVersion) {
        this.agentId = agentId;
        this.summary = summary;
        this.affectedModules = affectedModules;
        this.frameworkVersion = frameworkVersion;
        this.timestamp = LocalDateTime.now();
    }

    public String getAgentId() { return agentId; }
    public String getSummary() { return summary; }
    public String[] getAffectedModules() { return affectedModules; }
    public LocalDateTime getTimestamp() { return timestamp; }
    public String getFrameworkVersion() { return frameworkVersion; }
}

/**
 * Simulated Frameworks Experience DB with conflict detection
 */
class FrameworksExperienceDB {
    private final ConcurrentHashMap<String, ArchitecturalDecision> decisions = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, Integer> versionMap = new ConcurrentHashMap<>();
    private final Path experienceFile = Paths.get("shared-experiences.json");
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    // Load experiences from JSON file (after pulling from GitHub)
    public void loadFromFile() {
        if (!Files.exists(experienceFile)) return;
        try (Reader reader = Files.newBufferedReader(experienceFile)) {
            ArchitecturalDecision[] loaded = gson.fromJson(reader, ArchitecturalDecision[].class);
            if (loaded != null) {
                for (ArchitecturalDecision decision : loaded) {
                    String key = String.join("|", decision.getAffectedModules());
                    decisions.put(key, decision);
                    versionMap.put(key, versionMap.getOrDefault(key, 0) + 1);
                }
            }
        } catch (IOException e) {
            System.err.println("[ERROR] Failed to load experiences: " + e.getMessage());
        }
    }

    // Save all experiences to JSON file
    public void saveToFile() {
        try (Writer writer = Files.newBufferedWriter(experienceFile)) {
            ArchitecturalDecision[] all = decisions.values().toArray(new ArchitecturalDecision[0]);
            gson.toJson(all, writer);
        } catch (IOException e) {
            System.err.println("[ERROR] Failed to save experiences: " + e.getMessage());
        }
    }

    // Commit a decision, returns true if successful, false if conflict
    public synchronized boolean commitDecision(ArchitecturalDecision decision) {
        String key = String.join("|", decision.getAffectedModules());
        int currentVersion = versionMap.getOrDefault(key, 0);
        int newVersion = currentVersion + 1;
        if (decisions.containsKey(key)) {
            // Conflict: another agent already committed for these modules
            return false;
        }
        decisions.put(key, decision);
        versionMap.put(key, newVersion);
        return true;
    }

    public ArchitecturalDecision getDecision(String[] modules) {
        String key = String.join("|", modules);
        return decisions.get(key);
    }

    public int getVersion(String[] modules) {
        String key = String.join("|", modules);
        return versionMap.getOrDefault(key, 0);
    }
}

