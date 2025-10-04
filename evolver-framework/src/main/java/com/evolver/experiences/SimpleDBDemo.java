package com.evolver.experiences;

import java.util.List;

/**
 * Simple Database Demo - Shows agents using the experience database
 * 
 * Demonstrates how AI agents can easily read, write, and find experiences
 * using simple commands like a map or database.
 */
public class SimpleDBDemo {
    
    public static void main(String[] args) {
        System.out.println("🗄️ Simple Experience Database Demo");
        System.out.println("===================================");
        System.out.println();
        
        // Show help first
        showCommands();
        System.out.println();
        
        // Demonstrate agent usage patterns
        demonstrateAgentUsage();
        System.out.println();
        
        // Show database operations
        demonstrateDBOperations();
        System.out.println();
        
        // Show stats
        AgentCommands.stats();
    }
    
    private static void showCommands() {
        System.out.println("🤖 Simple Commands for AI Agents:");
        System.out.println("---------------------------------");
        System.out.println();
        System.out.println("BASIC OPERATIONS (like a map):");
        System.out.println("  AgentCommands.save(experience)           // Put experience in database");
        System.out.println("  AgentCommands.get(\"id\")                  // Get experience by ID");
        System.out.println("  AgentCommands.find(\"CATEGORY\")            // Find by category");
        System.out.println("  AgentCommands.search(\"text\")             // Search for text");
        System.out.println();
        System.out.println("SMART OPERATIONS (agent helpers):");
        System.out.println("  AgentCommands.exists(title, category)    // Check if similar exists");
        System.out.println("  AgentCommands.whatINeed(char, cat, proj) // Find relevant experiences");
        System.out.println("  AgentCommands.quickSave(...)             // Save with minimal info");
        System.out.println();
        System.out.println("DISCOVERY OPERATIONS:");
        System.out.println("  AgentCommands.findBy(\"PerformanceFreak\")  // Find by agent type");
        System.out.println("  AgentCommands.findProject(\"Java/Spring\")  // Find by project");
        System.out.println("  AgentCommands.recommended()              // Get recommended ones");
        System.out.println();
    }
    
    private static void demonstrateAgentUsage() {
        System.out.println("[TARGET] Agent Usage Examples:");
        System.out.println("------------------------");
        System.out.println();
        
        // Example 1: Agent checks before sharing
        System.out.println("Example 1: Agent checks before sharing");
        System.out.println("Agent: 'I want to share my caching strategy'");
        
        boolean exists = AgentCommands.exists("Cache Strategy", "PERFORMANCE");
        System.out.println("  AgentCommands.exists(\"Cache Strategy\", \"PERFORMANCE\") = " + exists);
        
        if (!exists) {
            System.out.println("  No similar experience found - agent shares discovery:");
            boolean saved = AgentCommands.quickSave(
                "agent_perf_001", 
                "PerformanceFreak",
                "PERFORMANCE",
                "LRU Cache Strategy", 
                "Implemented LRU cache with 1hr TTL, achieved 90% hit rate and 5x faster responses"
            );
            System.out.println("  Saved: " + saved);
        }
        System.out.println();
        
        // Example 2: Agent looks for help
        System.out.println("Example 2: Agent looks for relevant help");
        System.out.println("Agent: 'I need to optimize context generation'");
        
        List<Experience> relevant = AgentCommands.whatINeed(
            "PerformanceFreak", 
            "CONTEXT_OPTIMIZATION", 
            "Java/Spring"
        );
        System.out.println("  AgentCommands.whatINeed(\"PerformanceFreak\", \"CONTEXT_OPTIMIZATION\", \"Java/Spring\")");
        System.out.println("  Found " + relevant.size() + " relevant experiences:");
        
        relevant.stream().limit(2).forEach(exp -> 
            System.out.println("    - " + exp.getTitle() + " by " + exp.getAgentCharacteristic())
        );
        System.out.println();
        
        // Example 3: Agent searches for specific problem
        System.out.println("Example 3: Agent searches for specific solution");
        System.out.println("Agent: 'How do others handle large codebases?'");
        
        List<Experience> solutions = AgentCommands.search("large codebase");
        System.out.println("  AgentCommands.search(\"large codebase\")");
        System.out.println("  Found " + solutions.size() + " experiences with solutions:");
        
        solutions.stream().limit(2).forEach(exp -> 
            System.out.println("    - " + exp.getTitle())
        );
        System.out.println();
        
        // Example 4: Agent learns from peers
        System.out.println("Example 4: Agent learns from similar agents");
        System.out.println("Agent: 'What have other PerformanceFreaks learned?'");
        
        List<Experience> peerLearning = AgentCommands.findBy("PerformanceFreak");
        System.out.println("  AgentCommands.findBy(\"PerformanceFreak\")");
        System.out.println("  Found " + peerLearning.size() + " experiences from similar agents:");
        
        peerLearning.stream().limit(2).forEach(exp -> 
            System.out.println("    - " + exp.getTitle() + " (Category: " + exp.getCategory().getDisplayName() + ")")
        );
    }
    
    private static void demonstrateDBOperations() {
        System.out.println("🗄️ Database Operations:");
        System.out.println("-----------------------");
        System.out.println();
        
        // Show categories
        System.out.println("Available categories:");
        AgentCommands.categories();
        System.out.println();
        
        // Show recent experiences
        System.out.println("Recent experiences:");
        List<Experience> recent = AgentCommands.recent(3);
        recent.forEach(exp -> 
            System.out.println("  " + exp.getTimestamp().toLocalDate() + " - " + exp.getTitle())
        );
        System.out.println();
        
        // Show recommended
        System.out.println("Recommended experiences:");
        List<Experience> recommended = AgentCommands.recommended();
        recommended.stream().limit(3).forEach(exp -> 
            System.out.println("  ⭐ " + exp.getTitle() + " by " + exp.getAgentCharacteristic())
        );
        System.out.println();
        
        // Show count
        System.out.println("Total experiences in database: " + AgentCommands.count());
    }
    
    /**
     * Interactive demo for agents to try commands
     */
    public static void interactive() {
        System.out.println("🎮 Interactive Agent Database");
        System.out.println("============================");
        System.out.println();
        System.out.println("Try these commands in your agent code:");
        System.out.println();
        
        System.out.println("// Check what exists");
        System.out.println("AgentCommands.stats();");
        System.out.println("AgentCommands.categories();");
        System.out.println();
        
        System.out.println("// Find relevant experiences");
        System.out.println("List<Experience> contextOpt = AgentCommands.find(\"CONTEXT_OPTIMIZATION\");");
        System.out.println("List<Experience> debugging = AgentCommands.find(\"DEBUGGING\");");
        System.out.println("List<Experience> myPeers = AgentCommands.findBy(\"YOUR_CHARACTERISTIC\");");
        System.out.println();
        
        System.out.println("// Search for solutions");
        System.out.println("List<Experience> solutions = AgentCommands.search(\"performance slow\");");
        System.out.println("List<Experience> caching = AgentCommands.search(\"cache strategy\");");
        System.out.println();
        
        System.out.println("// Share your discoveries");
        System.out.println("if (!AgentCommands.exists(\"My Discovery\", \"PERFORMANCE\")) {");
        System.out.println("    AgentCommands.quickSave(\"me\", \"PerformanceFreak\", \"PERFORMANCE\",");
        System.out.println("        \"My Discovery\", \"What I learned and how it helped\");");
        System.out.println("}");
        System.out.println();
        
        System.out.println("// Get smart recommendations");
        System.out.println("List<Experience> relevant = AgentCommands.whatINeed(");
        System.out.println("    \"YOUR_CHARACTERISTIC\", \"CATEGORY\", \"YOUR_PROJECT_TYPE\");");
        System.out.println();
        
        System.out.println("Remember: Always check existing experiences before sharing new ones!");
    }
}
