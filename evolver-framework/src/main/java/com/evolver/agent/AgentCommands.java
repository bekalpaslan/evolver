package com.evolver.agent;

import com.evolver.experiences.Experience;
import com.evolver.experiences.ExperienceCategory;
import com.evolver.experiences.ExperienceDB;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Smart Agent Commands for Experience Database Operations
 * 
 * This provides simple, map-like operations for AI agents to:
 * - Share experiences intelligently (only when valuable)
 * - Make smart decisions about when to warn engineers
 * - Take appropriate initiative based on agent characteristics
 * - Filter out project-specific or trivial experiences
 */
public class AgentCommands {
    
    private static ExperienceDB db = ExperienceDB.getInstance();
    
    // ==================== BASIC OPERATIONS ====================
    
    /**
     * Find experiences by category (like querying a database)
     */
    public static List<Experience> find(String category) {
        return db.find(ExperienceCategory.valueOf(category));
    }
    
    /**
     * Find experiences by agent characteristic
     */
    public static List<Experience> findBy(String agentCharacteristic) {
        return db.findByCharacteristic(agentCharacteristic);
    }
    
    /**
     * Search for experiences with keywords
     */
    public static List<Experience> search(String keywords) {
        return db.search(keywords);
    }
    
    /**
     * Get what I need based on my situation (smart recommendations)
     */
    public static List<Experience> whatINeed(String myCharacteristic, 
                                           String categoryIAmWorkingOn, 
                                           String myProjectType) {
        return db.whatINeed(myCharacteristic, categoryIAmWorkingOn, myProjectType);
    }
    
    /**
     * Check if similar experience already exists (avoid duplicates)
     */
    public static boolean exists(String title, String category) {
        return db.exists(title, category);
    }
    
    /**
     * Save an experience (like putting in a map)
     */
    public static void save(Experience experience) {
        db.save(experience);
    }
    
    /**
     * Get experience by ID
     */
    public static Experience get(String experienceId) {
        return db.get(experienceId);
    }
    
    // ==================== SMART AGENT OPERATIONS ====================
    
    /**
     * Smart experience sharing - only shares if it's valuable to other agents
     */
    public static boolean smartShare(String agentId,
                                   String agentCharacteristic,
                                   String category,
                                   String title,
                                   String problemDescription,
                                   String solutionDescription,
                                   int timeSpentMinutes,
                                   int linesOfCodeChanged,
                                   String[] projectSpecificKeywords) {
        
        // Assess if this is project-specific
        boolean isProjectSpecific = AgentDecisionEngine.isProjectSpecific(
            problemDescription, solutionDescription, projectSpecificKeywords);
        
        if (isProjectSpecific) {
            System.out.println("🤖 Agent: Skipping experience sharing - project-specific content detected");
            return false;
        }
        
        // Assess difficulty level
        AgentDecisionEngine.DifficultyLevel difficulty = 
            AgentDecisionEngine.assessDifficulty(problemDescription, solutionDescription, 
                                                timeSpentMinutes, linesOfCodeChanged);
        
        // Determine experience type
        AgentDecisionEngine.ExperienceType experienceType = 
            determineExperienceType(problemDescription, solutionDescription);
        
        // Check if worth sharing
        boolean shouldShare = AgentDecisionEngine.shouldShareExperience(
            experienceType, problemDescription, solutionDescription, difficulty, isProjectSpecific);
        
        if (!shouldShare) {
            System.out.println("🤖 Agent: Skipping experience sharing - too trivial or not valuable enough");
            return false;
        }
        
        // Check for duplicates
        if (exists(title, category)) {
            System.out.println("🤖 Agent: Similar experience already exists, updating instead of creating duplicate");
            return false;
        }
        
        // Share the experience
        Experience experience = Experience.builder()
            .agentId(agentId)
            .agentCharacteristic(agentCharacteristic)
            .category(ExperienceCategory.valueOf(category))
            .title(title)
            .description(problemDescription)
            .solution(solutionDescription)
            .difficultyLevel(difficulty.toString())
            .experienceType(experienceType.toString())
            .timeSpent(timeSpentMinutes)
            .timestamp(LocalDateTime.now())
            .build();
        
        save(experience);
        System.out.println("✅ Agent: Valuable experience shared - " + title);
        return true;
    }
    
    /**
     * Smart decision: Should I warn the engineer before making this change?
     */
    public static boolean shouldIWarnEngineer(String changeType,
                                            String agentCharacteristic,
                                            String changeDescription) {
        
        AgentDecisionEngine.ChangeType type = AgentDecisionEngine.ChangeType.valueOf(changeType);
        AgentCharacteristic characteristic = AgentCharacteristic.valueOf(agentCharacteristic);
        
        boolean shouldWarn = AgentDecisionEngine.shouldWarnEngineerBeforeChanges(
            type, characteristic, changeDescription);
        
        if (shouldWarn) {
            String warningMessage = AgentDecisionEngine.generateWarningMessage(
                type, changeDescription, characteristic, "AI-Agent");
            System.out.println(warningMessage);
        }
        
        return shouldWarn;
    }
    
    /**
     * Smart decision: Can I take initiative to fix this issue?
     */
    public static boolean canITakeInitiative(String issueType,
                                           String agentCharacteristic,
                                           String issueDescription) {
        
        AgentDecisionEngine.IssueType type = AgentDecisionEngine.IssueType.valueOf(issueType);
        AgentCharacteristic characteristic = AgentCharacteristic.valueOf(agentCharacteristic);
        
        boolean canTakeInitiative = AgentDecisionEngine.canTakeInitiativeToFix(
            type, characteristic, issueDescription);
        
        if (canTakeInitiative) {
            System.out.println("🤖 Agent: Taking initiative to fix - " + issueDescription);
        } else {
            System.out.println("🤖 Agent: Requesting permission before fixing - " + issueDescription);
        }
        
        return canTakeInitiative;
    }
    
    /**
     * Learn from others before taking action (smart workflow)
     */
    public static List<Experience> learnBeforeActing(String myCharacteristic,
                                                   String problemCategory,
                                                   String problemKeywords) {
        
        System.out.println("🤖 Agent: Learning from others before taking action...");
        
        // First, check what similar agents learned
        List<Experience> similarAgentExperiences = findBy(myCharacteristic);
        
        // Then, search for experiences related to this problem
        List<Experience> relevantExperiences = search(problemKeywords);
        
        // Combine and filter for most relevant
        List<Experience> learnings = similarAgentExperiences.stream()
            .filter(exp -> exp.getDescription().toLowerCase().contains(problemKeywords.toLowerCase()))
            .collect(Collectors.toList());
        
        learnings.addAll(relevantExperiences.stream()
            .filter(exp -> !learnings.contains(exp))
            .collect(Collectors.toList()));
        
        if (!learnings.isEmpty()) {
            System.out.println("[LEARN] Agent: Found " + learnings.size() + " relevant experiences from other agents");
            for (Experience exp : learnings.subList(0, Math.min(3, learnings.size()))) {
                System.out.println("  - " + exp.getTitle() + " (by " + exp.getAgentCharacteristic() + ")");
            }
        } else {
            System.out.println("[DISCOVER] Agent: No similar experiences found - proceeding with caution");
        }
        
        return learnings;
    }
    
    /**
     * Quick save for minor fixes (simplified interface)
     */
    public static void quickSave(String agentId, 
                               String characteristic, 
                               String category,
                               String title, 
                               String description) {
        
        if (!exists(title, category)) {
            Experience experience = Experience.builder()
                .agentId(agentId)
                .agentCharacteristic(characteristic)
                .category(ExperienceCategory.valueOf(category))
                .title(title)
                .description(description)
                .timestamp(LocalDateTime.now())
                .build();
            
            save(experience);
            System.out.println("💾 Agent: Quick experience saved - " + title);
        }
    }
    
    // ==================== HELPER METHODS ====================
    
    private static AgentDecisionEngine.ExperienceType determineExperienceType(String problemDescription, 
                                                                             String solutionDescription) {
        String combined = (problemDescription + " " + solutionDescription).toLowerCase();
        
        if (combined.contains("error") || combined.contains("exception") || combined.contains("bug")) {
            return AgentDecisionEngine.ExperienceType.ERROR_AND_SOLUTION;
        }
        if (combined.contains("performance") || combined.contains("slow") || combined.contains("optimization")) {
            return AgentDecisionEngine.ExperienceType.PERFORMANCE_OPTIMIZATION;
        }
        if (combined.contains("security") || combined.contains("vulnerability")) {
            return AgentDecisionEngine.ExperienceType.SECURITY_FIX;
        }
        if (combined.contains("debug") || combined.contains("troubleshoot")) {
            return AgentDecisionEngine.ExperienceType.DEBUGGING_TECHNIQUE;
        }
        if (combined.contains("framework") || combined.contains("library")) {
            return AgentDecisionEngine.ExperienceType.FRAMEWORK_USAGE;
        }
        if (combined.contains("architecture") || combined.contains("design")) {
            return AgentDecisionEngine.ExperienceType.ARCHITECTURAL_INSIGHT;
        }
        if (combined.contains("configuration") || combined.contains("setup")) {
            return AgentDecisionEngine.ExperienceType.TOOL_CONFIGURATION;
        }
        
        return AgentDecisionEngine.ExperienceType.BEST_PRACTICE;
    }
    
    /**
     * Display help for agents
     */
    public static void help() {
        System.out.println("🤖 Agent Commands Help:");
        System.out.println("Basic Operations:");
        System.out.println("  find(category) - Find experiences by category");
        System.out.println("  search(keywords) - Search for experiences");
        System.out.println("  exists(title, category) - Check if experience exists");
        System.out.println("  save(experience) - Save an experience");
        System.out.println();
        System.out.println("Smart Operations:");
        System.out.println("  smartShare(...) - Share experience only if valuable");
        System.out.println("  shouldIWarnEngineer(...) - Check if engineer warning needed");
        System.out.println("  canITakeInitiative(...) - Check if can fix autonomously");
        System.out.println("  learnBeforeActing(...) - Learn from others first");
        System.out.println("  quickSave(...) - Quick save for minor experiences");
    }
    
    /**
     * Show examples of smart agent behavior
     */
    public static void examples() {
        System.out.println("🤖 Smart Agent Behavior Examples:");
        System.out.println();
        System.out.println("1. Smart Experience Sharing:");
        System.out.println("   // Only shares if valuable and not project-specific");
        System.out.println("   smartShare(\"agent1\", \"PERFORMANCE_FREAK\", \"PERFORMANCE\",");
        System.out.println("              \"SQL Query Optimization\", \"Slow query\", \"Added index\",");
        System.out.println("              45, 3, new String[]{\"our_app_table\"});");
        System.out.println();
        System.out.println("2. Engineer Warning Check:");
        System.out.println("   // Checks if should warn before major changes");
        System.out.println("   if (shouldIWarnEngineer(\"ARCHITECTURE_CHANGE\", \"CAUTIOUS_REVIEWER\",");
        System.out.println("                          \"Changing database structure\")) {");
        System.out.println("       // Wait for engineer approval");
        System.out.println("   }");
        System.out.println();
        System.out.println("3. Taking Initiative:");
        System.out.println("   // Decides if can fix autonomously");
        System.out.println("   if (canITakeInitiative(\"CODE_STYLE\", \"CLEAN_CODE_FANATIC\",");
        System.out.println("                         \"Inconsistent formatting\")) {");
        System.out.println("       // Fix immediately");
        System.out.println("   }");
        System.out.println();
        System.out.println("4. Learning Before Acting:");
        System.out.println("   // Always learn from others first");
        System.out.println("   List<Experience> learnings = learnBeforeActing(");
        System.out.println("       \"PERFORMANCE_FREAK\", \"PERFORMANCE\", \"slow database\");");
    }
}
