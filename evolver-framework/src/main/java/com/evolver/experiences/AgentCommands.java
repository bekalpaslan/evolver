package com.evolver.experiences;

import java.util.List;
import java.util.Arrays;

/**
 * Simple Command Interface for AI Agents
 * 
 * Use this like a command line tool for experiences.
 * No complex APIs - just simple commands that work.
 */
public class AgentCommands {
    
    private static final ExperienceDB db = ExperienceDB.getInstance();
    
    // ============ SAVE COMMANDS ============
    
    /**
     * Save experience - like "save myexperience"
     */
    public static boolean save(Experience experience) {
        return db.save(experience);
    }
    
    /**
     * Quick save - like "quicksave title description"
     */
    public static boolean quickSave(String agentId, String characteristic, String category, 
                                  String title, String description) {
        try {
            ExperienceCategory cat = ExperienceCategory.valueOf(category.toUpperCase());
            return db.quickSave(agentId, characteristic, cat, title, description);
        } catch (IllegalArgumentException e) {
            System.out.println("❌ Invalid category: " + category);
            return false;
        }
    }
    
    // ============ FIND COMMANDS ============
    
    /**
     * Find by category - like "find CONTEXT_OPTIMIZATION"
     */
    public static List<Experience> find(String category) {
        try {
            ExperienceCategory cat = ExperienceCategory.valueOf(category.toUpperCase());
            return db.findByCategory(cat);
        } catch (IllegalArgumentException e) {
            System.out.println("❌ Invalid category: " + category);
            return List.of();
        }
    }
    
    /**
     * Find by characteristic - like "find PerformanceFreak"
     */
    public static List<Experience> findBy(String characteristic) {
        return db.findByCharacteristic(characteristic);
    }
    
    /**
     * Find by project - like "find Java/Spring"
     */
    public static List<Experience> findProject(String projectType) {
        return db.findByProject(projectType);
    }
    
    /**
     * Find by tag - like "find performance"
     */
    public static List<Experience> findTag(String tag) {
        return db.findByTag(tag);
    }
    
    /**
     * Search text - like "search optimization"
     */
    public static List<Experience> search(String text) {
        return db.search(text);
    }
    
    /**
     * Get what I need - smart search for my situation
     */
    public static List<Experience> whatINeed(String myCharacteristic, String category, String projectType) {
        try {
            ExperienceCategory cat = ExperienceCategory.valueOf(category.toUpperCase());
            return db.findWhatINeed(myCharacteristic, cat, projectType);
        } catch (IllegalArgumentException e) {
            System.out.println("❌ Invalid category: " + category);
            return List.of();
        }
    }
    
    // ============ CHECK COMMANDS ============
    
    /**
     * Check if similar exists - like "exists mytitle FRAMEWORK_USAGE"
     */
    public static boolean exists(String title, String category) {
        try {
            ExperienceCategory cat = ExperienceCategory.valueOf(category.toUpperCase());
            return db.existsSimilar(title, cat);
        } catch (IllegalArgumentException e) {
            System.out.println("❌ Invalid category: " + category);
            return false;
        }
    }
    
    /**
     * Get experience - like "get experience_id"
     */
    public static Experience get(String id) {
        return db.get(id);
    }
    
    // ============ LIST COMMANDS ============
    
    /**
     * List all - like "list"
     */
    public static List<Experience> list() {
        return db.getAll();
    }
    
    /**
     * List recent - like "recent 5"
     */
    public static List<Experience> recent(int count) {
        return db.getRecent(count);
    }
    
    /**
     * List recommended - like "recommended"
     */
    public static List<Experience> recommended() {
        return db.getRecommended();
    }
    
    /**
     * Show categories - like "categories"
     */
    public static void categories() {
        System.out.println("📂 Available Categories:");
        for (ExperienceCategory category : ExperienceCategory.values()) {
            int count = db.countByCategory(category);
            System.out.printf("  %-25s (%d experiences) - %s%n", 
                            category.name(), count, category.getDescription());
        }
    }
    
    // ============ UPDATE COMMANDS ============
    
    /**
     * Recommend experience - like "recommend experience_id"
     */
    public static boolean recommend(String id) {
        return db.recommend(id);
    }
    
    /**
     * Remove experience - like "remove experience_id"
     */
    public static boolean remove(String id) {
        return db.remove(id);
    }
    
    // ============ INFO COMMANDS ============
    
    /**
     * Show stats - like "stats"
     */
    public static void stats() {
        db.showStats();
    }
    
    /**
     * Count experiences - like "count"
     */
    public static int count() {
        return db.count();
    }
    
    /**
     * Show help - like "help"
     */
    public static void help() {
        System.out.println("🤖 Agent Commands - Simple Experience Database");
        System.out.println("==============================================");
        System.out.println();
        System.out.println("SAVE:");
        System.out.println("  save(experience)                 - Save complete experience");
        System.out.println("  quickSave(id,char,cat,title,desc) - Quick save with basics");
        System.out.println();
        System.out.println("FIND:");
        System.out.println("  find(\"CATEGORY\")                 - Find by category");
        System.out.println("  findBy(\"Characteristic\")         - Find by agent type");
        System.out.println("  findProject(\"Java/Spring\")       - Find by project type");
        System.out.println("  findTag(\"performance\")          - Find by tag");
        System.out.println("  search(\"text\")                  - Search in content");
        System.out.println("  whatINeed(char,cat,project)     - Smart search for me");
        System.out.println();
        System.out.println("CHECK:");
        System.out.println("  exists(title,category)          - Check if similar exists");
        System.out.println("  get(\"id\")                       - Get specific experience");
        System.out.println();
        System.out.println("LIST:");
        System.out.println("  list()                          - List all experiences");
        System.out.println("  recent(5)                       - List recent experiences");
        System.out.println("  recommended()                   - List recommended ones");
        System.out.println("  categories()                    - Show all categories");
        System.out.println();
        System.out.println("INFO:");
        System.out.println("  stats()                         - Show database stats");
        System.out.println("  count()                         - Count all experiences");
        System.out.println("  help()                          - Show this help");
        System.out.println();
        System.out.println("EXAMPLES:");
        System.out.println("  AgentCommands.find(\"CONTEXT_OPTIMIZATION\");");
        System.out.println("  AgentCommands.findBy(\"PerformanceFreak\");");
        System.out.println("  AgentCommands.search(\"caching strategy\");");
        System.out.println("  AgentCommands.quickSave(\"me\",\"PerformanceFreak\",\"PERFORMANCE\",\"Fast Cache\",\"LRU cache for 90% hit rate\");");
    }
    
    // ============ BUILDER SHORTCUTS ============
    
    /**
     * Create experience builder - like "create()"
     */
    public static ExperienceBuilder create() {
        return ExperienceBuilder.create();
    }
    
    /**
     * Create for context optimization - shortcut
     */
    public static ExperienceBuilder createContextOpt(String agentId, String characteristic) {
        return ExperienceBuilder.contextOptimization(agentId, characteristic);
    }
    
    /**
     * Create for debugging - shortcut
     */
    public static ExperienceBuilder createDebug(String agentId, String characteristic) {
        return ExperienceBuilder.debugging(agentId, characteristic);
    }
    
    /**
     * Create for integration - shortcut
     */
    public static ExperienceBuilder createIntegration(String agentId, String characteristic) {
        return ExperienceBuilder.projectIntegration(agentId, characteristic);
    }
    
    // ============ EXAMPLE USAGE FOR AGENTS ============
    
    /**
     * Show example usage patterns for agents
     */
    public static void examples() {
        System.out.println("🎯 Example Usage for AI Agents:");
        System.out.println("===============================");
        System.out.println();
        
        System.out.println("1. BEFORE MAKING A DECISION - Check what others learned:");
        System.out.println("   List<Experience> relevant = AgentCommands.whatINeed(");
        System.out.println("       \"PerformanceFreak\", \"CONTEXT_OPTIMIZATION\", \"Java/Spring\");");
        System.out.println();
        
        System.out.println("2. QUICK CHECK - Does similar experience exist?");
        System.out.println("   if (!AgentCommands.exists(\"Cache Strategy\", \"PERFORMANCE\")) {");
        System.out.println("       // Share your new discovery");
        System.out.println("   }");
        System.out.println();
        
        System.out.println("3. QUICK SAVE - Share a simple discovery:");
        System.out.println("   AgentCommands.quickSave(\"agent_001\", \"PerformanceFreak\",");
        System.out.println("       \"PERFORMANCE\", \"LRU Cache Strategy\",");
        System.out.println("       \"Using LRU cache improved response time by 90%\");");
        System.out.println();
        
        System.out.println("4. DETAILED SAVE - Share comprehensive experience:");
        System.out.println("   Experience exp = AgentCommands.createContextOpt(\"me\", \"PerformanceFreak\")");
        System.out.println("       .title(\"Hierarchical Context Collection\")");
        System.out.println("       .situation(\"Large codebase was slow\")");
        System.out.println("       .approach(\"Created multi-level collection strategy\")");
        System.out.println("       .outcome(\"Reduced time from 45s to 8s\")");
        System.out.println("       .build();");
        System.out.println("   AgentCommands.save(exp);");
        System.out.println();
        
        System.out.println("5. SEARCH FOR SOLUTIONS - Find relevant help:");
        System.out.println("   List<Experience> solutions = AgentCommands.search(\"performance slow\");");
        System.out.println("   List<Experience> peerHelp = AgentCommands.findBy(\"PerformanceFreak\");");
        System.out.println();
        
        System.out.println("Remember: Always check before sharing, learn from others first!");
    }
}