package com.evolver.experiences;

import java.util.List;

/**
 * Simple Agent Interface - How AI Agents Use the Experience Database
 * 
 * This shows the typical patterns that AI agents should follow
 * when interacting with the experience database.
 */
public class SimpleAgentInterface {
    
    private final String agentId;
    private final String characteristic;
    private final String projectType;
    
    public SimpleAgentInterface(String agentId, String characteristic, String projectType) {
        this.agentId = agentId;
        this.characteristic = characteristic;
        this.projectType = projectType;
    }
    
    // ============ PATTERN 1: LEARN BEFORE ACTING ============
    
    /**
     * Before making any decision, check what others learned
     */
    public List<Experience> learnFromOthers(ExperienceCategory category) {
        System.out.println("🧠 Learning from other agents about " + category.getDisplayName() + "...");
        
        // Get experiences relevant to my situation
        List<Experience> relevant = AgentCommands.whatINeed(characteristic, category.name(), projectType);
        
        System.out.println("📚 Found " + relevant.size() + " relevant experiences:");
        relevant.forEach(exp -> 
            System.out.println("  - " + exp.getTitle() + " by " + exp.getAgentCharacteristic())
        );
        
        return relevant;
    }
    
    // ============ PATTERN 2: CHECK BEFORE SHARING ============
    
    /**
     * Before sharing, check if similar experience exists
     */
    public boolean shouldShare(String title, ExperienceCategory category) {
        boolean exists = AgentCommands.exists(title, category.name());
        
        if (exists) {
            System.out.println("⚠️ Similar experience already exists: " + title);
            System.out.println("💡 Consider updating existing one or adding a variation");
            return false;
        } else {
            System.out.println("✨ New discovery detected: " + title);
            System.out.println("🎯 This will help other agents!");
            return true;
        }
    }
    
    // ============ PATTERN 3: QUICK SHARING ============
    
    /**
     * Share a simple discovery quickly
     */
    public boolean shareQuick(ExperienceCategory category, String title, String description) {
        if (!shouldShare(title, category)) {
            return false;
        }
        
        boolean saved = AgentCommands.quickSave(agentId, characteristic, category.name(), title, description);
        
        if (saved) {
            System.out.println("✅ Shared experience: " + title);
        } else {
            System.out.println("❌ Failed to share experience: " + title);
        }
        
        return saved;
    }
    
    // ============ PATTERN 4: DETAILED SHARING ============
    
    /**
     * Share a detailed experience with full context
     */
    public boolean shareDetailed(ExperienceCategory category, String title, String description,
                               String situation, String approach, String outcome) {
        if (!shouldShare(title, category)) {
            return false;
        }
        
        Experience experience = AgentCommands.create()
            .agent(agentId, characteristic)
            .category(category)
            .title(title)
            .description(description)
            .situation(situation)
            .approach(approach)
            .outcome(outcome)
            .projectType(projectType)
            .build();
        
        boolean saved = AgentCommands.save(experience);
        
        if (saved) {
            System.out.println("✅ Shared detailed experience: " + title);
        } else {
            System.out.println("❌ Failed to share experience: " + title);
        }
        
        return saved;
    }
    
    // ============ PATTERN 5: SEARCH FOR SOLUTIONS ============
    
    /**
     * Search for solutions to a specific problem
     */
    public List<Experience> findSolutions(String problem) {
        System.out.println("🔍 Searching for solutions to: " + problem);
        
        List<Experience> solutions = AgentCommands.search(problem);
        
        System.out.println("💡 Found " + solutions.size() + " potential solutions:");
        solutions.stream().limit(5).forEach(exp -> 
            System.out.println("  - " + exp.getTitle() + " (Category: " + exp.getCategory().getDisplayName() + ")")
        );
        
        return solutions;
    }
    
    // ============ PATTERN 6: LEARN FROM PEERS ============
    
    /**
     * Learn what other agents with same characteristic discovered
     */
    public List<Experience> learnFromPeers() {
        System.out.println("👥 Learning from other " + characteristic + " agents...");
        
        List<Experience> peerExperiences = AgentCommands.findBy(characteristic);
        
        System.out.println("🤝 Found " + peerExperiences.size() + " experiences from peers:");
        peerExperiences.stream().limit(5).forEach(exp -> 
            System.out.println("  - " + exp.getTitle() + " (Category: " + exp.getCategory().getDisplayName() + ")")
        );
        
        return peerExperiences;
    }
    
    // ============ PATTERN 7: PROJECT-SPECIFIC LEARNING ============
    
    /**
     * Learn from agents working on similar projects
     */
    public List<Experience> learnFromSimilarProjects() {
        System.out.println("🏗️ Learning from agents working on " + projectType + " projects...");
        
        List<Experience> projectExperiences = AgentCommands.findProject(projectType);
        
        System.out.println("📦 Found " + projectExperiences.size() + " experiences from similar projects:");
        projectExperiences.stream().limit(5).forEach(exp -> 
            System.out.println("  - " + exp.getTitle() + " by " + exp.getAgentCharacteristic())
        );
        
        return projectExperiences;
    }
    
    // ============ COMPLETE WORKFLOW EXAMPLE ============
    
    /**
     * Complete workflow: Learn, decide, share
     */
    public void solveContextOptimizationProblem() {
        System.out.println("🎯 Agent Workflow: Solving Context Optimization Problem");
        System.out.println("======================================================");
        System.out.println();
        
        // Step 1: Learn from others
        System.out.println("STEP 1: Learn from existing experiences");
        List<Experience> relevant = learnFromOthers(ExperienceCategory.CONTEXT_OPTIMIZATION);
        System.out.println();
        
        // Step 2: Search for specific solutions
        System.out.println("STEP 2: Search for specific solutions");
        List<Experience> solutions = findSolutions("context slow performance");
        System.out.println();
        
        // Step 3: Learn from peers
        System.out.println("STEP 3: Learn from peer agents");
        List<Experience> peerHelp = learnFromPeers();
        System.out.println();
        
        // Step 4: Implement solution (simulated)
        System.out.println("STEP 4: Implement solution based on learnings");
        System.out.println("📝 Agent implements hierarchical context collection strategy...");
        System.out.println("⚡ Result: Context generation time reduced from 45s to 8s");
        System.out.println();
        
        // Step 5: Share discovery
        System.out.println("STEP 5: Share discovery with community");
        shareDetailed(
            ExperienceCategory.CONTEXT_OPTIMIZATION,
            "Hierarchical Context Collection for Large Projects",
            "Multi-level context collection strategy that dramatically improves performance",
            "Large " + projectType + " project with slow context generation (45s average)",
            "Implemented 3-level hierarchy: package -> class -> method with intelligent caching",
            "Reduced context generation time from 45s to 8s (82% improvement)"
        );
        System.out.println();
        
        System.out.println("✅ Workflow complete! Agent learned, solved problem, and shared knowledge.");
    }
    
    // ============ USAGE EXAMPLE ============
    
    public static void main(String[] args) {
        // Create an agent
        SimpleAgentInterface agent = new SimpleAgentInterface(
            "agent_perf_001", 
            "PerformanceFreak", 
            "Java/Spring"
        );
        
        // Run complete workflow
        agent.solveContextOptimizationProblem();
        
        System.out.println();
        System.out.println("📊 Database stats after agent interaction:");
        AgentCommands.stats();
    }
}