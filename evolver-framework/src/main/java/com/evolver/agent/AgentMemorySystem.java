package com.evolver.agent;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * AGENT MEMORY SYSTEM
 * 
 * Ensures agents retain and access critical information throughout sessions,
 * even with context window limitations. Provides active memory management
 * and key information retrieval for consistent behavior.
 */
public class AgentMemorySystem {
    
    private static final Map<String, AgentMemory> agentMemories = new ConcurrentHashMap<>();
    private static final Map<String, String> keyInstructions = new ConcurrentHashMap<>();
    
    static {
        // Load critical instructions that must be remembered
        initializeCriticalInstructions();
    }
    
    /**
     * Initialize critical instructions that agents must remember
     */
    private static void initializeCriticalInstructions() {
        keyInstructions.put("TECHNOLOGY_RATING_PRECISION", 
            "All technology ratings must use 0.1 precision (0.0-10.0). Example: 8.7, not 8 or 9");
        
        keyInstructions.put("TECHNOLOGY_COMBINATIONS", 
            "Always record 'usedTogether' field with harmony ratings when multiple technologies are used");
        
        keyInstructions.put("CONTEXT_OPTIMIZATION", 
            "Continuously experiment with context structures and record effectiveness scores");
        
        keyInstructions.put("PRIVACY_COMPLIANCE", 
            "Only collect anonymous technology usage patterns. No personal data, file contents, or project details");
        
        keyInstructions.put("EXPERIENCE_RECORDING", 
            "Record technology experiences immediately after significant interactions (positive or negative)");
        
        keyInstructions.put("COMPATIBILITY_TRACKING", 
            "Monitor and rate how well technologies work together in real projects");
    }
    
    /**
     * Get or create agent memory
     */
    public static AgentMemory getAgentMemory(String agentId) {
        return agentMemories.computeIfAbsent(agentId, k -> new AgentMemory(agentId));
    }
    
    /**
     * Record important information for agent to remember
     */
    public static void recordMemory(String agentId, String category, String information) {
        AgentMemory memory = getAgentMemory(agentId);
        memory.addMemory(category, information);
        
        System.out.println("[MEMORY] " + agentId + " recorded: " + category + " - " + information);
    }
    
    /**
     * Get critical instruction that agent must follow
     */
    public static String getCriticalInstruction(String instructionKey) {
        return keyInstructions.getOrDefault(instructionKey, 
            "Instruction not found. Follow general technology intelligence guidelines.");
    }
    
    /**
     * Get all critical instructions for agent
     */
    public static Map<String, String> getAllCriticalInstructions() {
        return new HashMap<>(keyInstructions);
    }
    
    /**
     * Agent startup reminder - critical behaviors to remember
     */
    public static List<String> getStartupReminders(String agentId) {
        List<String> reminders = new ArrayList<>();
        reminders.add("🎯 Use 0.1 precision for all technology ratings (8.7/10, not 8/10)");
        reminders.add("🤝 Record technology combinations with harmony ratings");
        reminders.add("🧠 Experiment with context structures and track effectiveness");
        reminders.add("🔐 Maintain privacy: anonymous data only, no personal information");
        reminders.add("📊 Record experiences immediately after technology interactions");
        reminders.add("⚡ Check compatibility before recommending technology combinations");
        
        // Add agent-specific reminders
        AgentMemory memory = agentMemories.get(agentId);
        if (memory != null) {
            reminders.addAll(memory.getPersonalReminders());
        }
        
        return reminders;
    }
    
    /**
     * Session persistence - key behaviors to maintain
     */
    public static SessionContext getSessionContext(String agentId) {
        AgentMemory memory = getAgentMemory(agentId);
        return new SessionContext(agentId, memory);
    }
}

/**
 * Agent memory storage for individual agents
 */
class AgentMemory {
    private final String agentId;
    private final Map<String, List<String>> categoryMemories;
    private final List<String> personalReminders;
    private final LocalDateTime createdAt;
    
    public AgentMemory(String agentId) {
        this.agentId = agentId;
        this.categoryMemories = new ConcurrentHashMap<>();
        this.personalReminders = new ArrayList<>();
        this.createdAt = LocalDateTime.now();
    }
    
    public void addMemory(String category, String information) {
        categoryMemories.computeIfAbsent(category, k -> new ArrayList<>()).add(information);
        
        // Auto-generate personal reminders based on patterns
        if (category.equals("MISTAKE") || category.equals("LEARNING")) {
            personalReminders.add("Remember: " + information);
        }
    }
    
    public List<String> getMemories(String category) {
        return new ArrayList<>(categoryMemories.getOrDefault(category, Collections.emptyList()));
    }
    
    public List<String> getPersonalReminders() {
        return new ArrayList<>(personalReminders);
    }
    
    public String getAgentId() { return agentId; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}

/**
 * Session context to maintain throughout agent interactions
 */
class SessionContext {
    public final String agentId;
    public final Map<String, String> criticalInstructions;
    public final List<String> activeReminders;
    public final AgentMemory memory;
    public final LocalDateTime sessionStart;
    
    public SessionContext(String agentId, AgentMemory memory) {
        this.agentId = agentId;
        this.memory = memory;
        this.criticalInstructions = AgentMemorySystem.getAllCriticalInstructions();
        this.activeReminders = AgentMemorySystem.getStartupReminders(agentId);
        this.sessionStart = LocalDateTime.now();
    }
    
    /**
     * Get formatted reminder text for agent
     */
    public String getFormattedReminders() {
        StringBuilder sb = new StringBuilder();
        sb.append("🧠 AGENT MEMORY SYSTEM - Active Session Reminders:\n");
        sb.append("═══════════════════════════════════════════════════\n");
        
        for (String reminder : activeReminders) {
            sb.append(reminder).append("\n");
        }
        
        if (!memory.getPersonalReminders().isEmpty()) {
            sb.append("\n📝 Personal Learning History:\n");
            for (String personal : memory.getPersonalReminders()) {
                sb.append("• ").append(personal).append("\n");
            }
        }
        
        sb.append("═══════════════════════════════════════════════════\n");
        return sb.toString();
    }
    
    /**
     * Quick reference for critical behaviors
     */
    public String getQuickReference() {
        return String.format(
            "🎯 QUICK REFERENCE - Agent %s\n" +
            "• Technology ratings: 0.1 precision (8.7/10)\n" +
            "• Record harmony ratings for combinations\n" +
            "• Track context structure effectiveness\n" +
            "• Maintain privacy compliance\n" +
            "Session: %s", 
            agentId, sessionStart
        );
    }
}