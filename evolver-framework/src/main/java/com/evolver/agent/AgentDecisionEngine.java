package com.evolver.agent;

import com.evolver.experiences.Experience;
import com.evolver.experiences.ExperienceCategory;

/**
 * Smart Decision Engine for AI Agents
 * 
 * This class helps agents make intelligent decisions about:
 * - When to warn the context engineer before making changes
 * - When to take initiative and fix issues autonomously  
 * - When to share experiences vs skip sharing
 * - What types of experiences are worth sharing
 */
public class AgentDecisionEngine {

    /**
     * Determines if the agent should warn the context engineer before implementing changes
     */
    public static boolean shouldWarnEngineerBeforeChanges(ChangeType changeType, 
                                                         AgentCharacteristic characteristic, 
                                                         String changeDescription) {
        
        // Major architectural changes always require warning
        if (changeType == ChangeType.ARCHITECTURE_CHANGE) {
            return true;
        }
        
        // API changes that affect external interfaces
        if (changeType == ChangeType.API_CHANGE) {
            return true;
        }
        
        // Database schema changes
        if (changeType == ChangeType.DATABASE_SCHEMA) {
            return true;
        }
        
        // Security-related changes
        if (changeType == ChangeType.SECURITY_CHANGE) {
            return true;
        }
        
        // Configuration changes that affect system behavior
        if (changeType == ChangeType.CONFIG_CHANGE && 
            changeDescription.toLowerCase().contains("production")) {
            return true;
        }
        
        // Cautious agents warn more often
        if (characteristic == AgentCharacteristic.CAUTIOUS_REVIEWER) {
            return changeType != ChangeType.MINOR_FIX && 
                   changeType != ChangeType.CODE_STYLE;
        }
        
        // Aggressive agents warn less often
        if (characteristic == AgentCharacteristic.AGGRESSIVE_OPTIMIZER) {
            return changeType == ChangeType.ARCHITECTURE_CHANGE || 
                   changeType == ChangeType.API_CHANGE;
        }
        
        return false;
    }
    
    /**
     * Determines if the agent can take initiative to fix issues without asking
     */
    public static boolean canTakeInitiativeToFix(IssueType issueType, 
                                                 AgentCharacteristic characteristic,
                                                 String issueDescription) {
        
        // Always safe to take initiative on these
        if (issueType == IssueType.CODE_STYLE || 
            issueType == IssueType.DOCUMENTATION ||
            issueType == IssueType.MINOR_BUG) {
            return true;
        }
        
        // Performance improvements (if agent has experience)
        if (issueType == IssueType.PERFORMANCE && 
            (characteristic == AgentCharacteristic.PERFORMANCE_FREAK ||
             characteristic == AgentCharacteristic.AGGRESSIVE_OPTIMIZER)) {
            return true;
        }
        
        // Cleanup tasks
        if (issueType == IssueType.CODE_CLEANUP ||
            issueType == IssueType.DEAD_CODE_REMOVAL) {
            return true;
        }
        
        // Test improvements
        if (issueType == IssueType.TEST_IMPROVEMENT &&
            characteristic == AgentCharacteristic.TEST_OBSESSED) {
            return true;
        }
        
        // Security fixes (but warn first for major ones)
        if (issueType == IssueType.SECURITY_FIX &&
            !issueDescription.toLowerCase().contains("critical")) {
            return true;
        }
        
        return false;
    }
    
    /**
     * Determines if an experience is worth sharing with other agents
     */
    public static boolean shouldShareExperience(ExperienceType experienceType,
                                               String problemDescription,
                                               String solutionDescription,
                                               DifficultyLevel difficulty,
                                               boolean isProjectSpecific) {
        
        // Never share project-specific experiences
        if (isProjectSpecific) {
            return false;
        }
        
        // Skip trivial issues that any coding agent would solve easily
        if (difficulty == DifficultyLevel.TRIVIAL) {
            return false;
        }
        
        // Always share these valuable experience types
        if (experienceType == ExperienceType.ERROR_AND_SOLUTION ||
            experienceType == ExperienceType.PERFORMANCE_OPTIMIZATION ||
            experienceType == ExperienceType.SECURITY_FIX ||
            experienceType == ExperienceType.DEBUGGING_TECHNIQUE) {
            return difficulty != DifficultyLevel.TRIVIAL;
        }
        
        // Share framework/library specific knowledge
        if (experienceType == ExperienceType.FRAMEWORK_USAGE ||
            experienceType == ExperienceType.LIBRARY_INTEGRATION) {
            return difficulty == DifficultyLevel.MODERATE || 
                   difficulty == DifficultyLevel.COMPLEX;
        }
        
        // Share architectural insights
        if (experienceType == ExperienceType.ARCHITECTURAL_INSIGHT) {
            return true; // Always valuable
        }
        
        // Share tool/environment specific knowledge
        if (experienceType == ExperienceType.TOOL_CONFIGURATION ||
            experienceType == ExperienceType.ENVIRONMENT_SETUP) {
            return difficulty != DifficultyLevel.TRIVIAL;
        }
        
        return false;
    }
    
    /**
     * Determines if the problem/solution is project-specific
     */
    public static boolean isProjectSpecific(String problemDescription, 
                                          String solutionDescription,
                                          String[] projectSpecificKeywords) {
        
        String combined = (problemDescription + " " + solutionDescription).toLowerCase();
        
        // Check for project-specific keywords
        for (String keyword : projectSpecificKeywords) {
            if (combined.contains(keyword.toLowerCase())) {
                return true;
            }
        }
        
        // Common project-specific indicators
        String[] genericProjectKeywords = {
            "our database", "our api", "our system", "our application",
            "company name", "client name", "specific user", "this project",
            "our configuration", "our environment", "our server"
        };
        
        for (String keyword : genericProjectKeywords) {
            if (combined.contains(keyword)) {
                return true;
            }
        }
        
        return false;
    }
    
    /**
     * Assesses the difficulty level of a problem/solution
     */
    public static DifficultyLevel assessDifficulty(String problemDescription,
                                                   String solutionDescription,
                                                   int timeSpentMinutes,
                                                   int linesOfCodeChanged) {
        
        String combined = (problemDescription + " " + solutionDescription).toLowerCase();
        
        // Trivial indicators
        if (timeSpentMinutes < 5 ||
            (linesOfCodeChanged < 3 && !combined.contains("config") && !combined.contains("complex"))) {
            return DifficultyLevel.TRIVIAL;
        }
        
        // Complex indicators
        if (timeSpentMinutes > 120 ||
            linesOfCodeChanged > 100 ||
            combined.contains("architecture") ||
            combined.contains("algorithm") ||
            combined.contains("concurrency") ||
            combined.contains("thread") ||
            combined.contains("performance") ||
            combined.contains("memory leak") ||
            combined.contains("deadlock")) {
            return DifficultyLevel.COMPLEX;
        }
        
        // Moderate indicators
        if (timeSpentMinutes > 30 ||
            linesOfCodeChanged > 20 ||
            combined.contains("integration") ||
            combined.contains("configuration") ||
            combined.contains("dependency") ||
            combined.contains("framework")) {
            return DifficultyLevel.MODERATE;
        }
        
        return DifficultyLevel.EASY;
    }
    
    /**
     * Generates a smart warning message for the context engineer
     */
    public static String generateWarningMessage(ChangeType changeType,
                                               String changeDescription,
                                               AgentCharacteristic characteristic,
                                               String agentId) {
        
        String urgency = getUrgencyLevel(changeType);
        String characteristicNote = getCharacteristicNote(characteristic);
        
        return String.format(
            "🤖 AGENT WARNING [%s]\n" +
            "Agent ID: %s (%s)\n" +
            "Change Type: %s\n" +
            "Description: %s\n" +
            "Recommendation: %s\n" +
            "Agent Note: %s\n" +
            "---\n" +
            "Reply 'PROCEED' to authorize, 'MODIFY' to request changes, or 'DENY' to reject.",
            urgency,
            agentId,
            characteristic.getName(),
            changeType,
            changeDescription,
            getRecommendation(changeType),
            characteristicNote
        );
    }
    
    private static String getUrgencyLevel(ChangeType changeType) {
        switch (changeType) {
            case SECURITY_CHANGE: return "HIGH";
            case ARCHITECTURE_CHANGE: return "HIGH";
            case API_CHANGE: return "MEDIUM";
            case DATABASE_SCHEMA: return "MEDIUM";
            case CONFIG_CHANGE: return "LOW";
            default: return "LOW";
        }
    }
    
    private static String getCharacteristicNote(AgentCharacteristic characteristic) {
        switch (characteristic) {
            case DOCUMENTATION_OBSESSED:
                return "I've thoroughly documented this change and its implications.";
            case PERFORMANCE_FREAK:
                return "This change will improve performance without compromising stability.";
            case SECURITY_PARANOID:
                return "I've analyzed all security implications of this change.";
            case CLEAN_CODE_FANATIC:
                return "This change improves code quality and maintainability.";
            case TEST_OBSESSED:
                return "I've prepared comprehensive tests for this change.";
            case CAUTIOUS_REVIEWER:
                return "I recommend careful review before proceeding.";
            case AGGRESSIVE_OPTIMIZER:
                return "This is an important optimization that should be implemented soon.";
            default:
                return "I believe this change is beneficial for the project.";
        }
    }
    
    private static String getRecommendation(ChangeType changeType) {
        switch (changeType) {
            case ARCHITECTURE_CHANGE:
                return "Review architectural implications and impact on existing components.";
            case API_CHANGE:
                return "Verify backward compatibility and update documentation.";
            case SECURITY_CHANGE:
                return "Conduct security review and test in staging environment.";
            case DATABASE_SCHEMA:
                return "Plan migration strategy and backup procedures.";
            case CONFIG_CHANGE:
                return "Test configuration in non-production environment first.";
            default:
                return "Review and test changes before deployment.";
        }
    }
    
    // Enums for decision-making
    public enum ChangeType {
        ARCHITECTURE_CHANGE,
        API_CHANGE,
        DATABASE_SCHEMA,
        SECURITY_CHANGE,
        CONFIG_CHANGE,
        PERFORMANCE_IMPROVEMENT,
        BUG_FIX,
        MINOR_FIX,
        CODE_STYLE,
        DOCUMENTATION
    }
    
    public enum IssueType {
        SECURITY_FIX,
        PERFORMANCE,
        MAJOR_BUG,
        MINOR_BUG,
        CODE_STYLE,
        DOCUMENTATION,
        CODE_CLEANUP,
        DEAD_CODE_REMOVAL,
        TEST_IMPROVEMENT,
        DEPENDENCY_UPDATE
    }
    
    public enum ExperienceType {
        ERROR_AND_SOLUTION,
        PERFORMANCE_OPTIMIZATION,
        SECURITY_FIX,
        DEBUGGING_TECHNIQUE,
        FRAMEWORK_USAGE,
        LIBRARY_INTEGRATION,
        ARCHITECTURAL_INSIGHT,
        TOOL_CONFIGURATION,
        ENVIRONMENT_SETUP,
        BEST_PRACTICE
    }
    
    public enum DifficultyLevel {
        TRIVIAL,    // < 5 minutes, obvious solution
        EASY,       // 5-30 minutes, straightforward
        MODERATE,   // 30-120 minutes, requires thought
        COMPLEX     // > 120 minutes, significant challenge
    }
}