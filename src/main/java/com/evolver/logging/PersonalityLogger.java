package com.evolver.logging;

import com.evolver.agent.AgentCharacteristic;
import java.util.Map;

/**
 * PERSONALITY-BASED LOGGER
 *
 * Provides logging with personality-specific formatting and emojis.
 * Each agent characteristic gets its own visual style.
 */
public class PersonalityLogger implements AgentLogger {

    private final AgentCharacteristic characteristic;

    public PersonalityLogger(AgentCharacteristic characteristic) {
        this.characteristic = characteristic;
    }

    @Override
    public void logInteraction(String agentId, String action, String summary, Map<String, Object> metadata) {
        String emoji = getPersonalityEmoji();
        System.out.println(emoji + " [" + agentId + "] " + action + ": " + summary);

        if (metadata != null && !metadata.isEmpty()) {
            System.out.println("   └─ Details: " + metadata);
        }
    }

    @Override
    public void logDecision(String agentId, String decision, String reasoning) {
        String emoji = getPersonalityEmoji();
        System.out.println(emoji + " [" + agentId + "] DECISION: " + decision);
        System.out.println("   └─ " + reasoning);
    }

    @Override
    public void logEvolution(String agentId, String evolution, Map<String, Object> changes) {
        String emoji = getPersonalityEmoji();
        System.out.println(emoji + " [" + agentId + "] EVOLVED: " + evolution);
        if (changes != null) {
            changes.forEach((key, value) ->
                System.out.println("   └─ " + key + ": " + value));
        }
    }

    @Override
    public void logLearning(String agentId, String concept, double confidence) {
        String emoji = getPersonalityEmoji();
        System.out.println(emoji + " [" + agentId + "] LEARNED: " + concept +
                          " (confidence: " + String.format("%.1f%%", confidence * 100) + ")");
    }

    @Override
    public void logConflict(String agentId, String issue, String resolution) {
        String emoji = getPersonalityEmoji();
        System.out.println(emoji + " [" + agentId + "] CONFLICT: " + issue);
        if (resolution != null) {
            System.out.println("   └─ RESOLVED: " + resolution);
        } else {
            System.out.println("   └─ UNRESOLVED: Requires attention");
        }
    }

    private String getPersonalityEmoji() {
        String name = characteristic.getName();
        switch (name) {
            case "DocBot": return "📚";
            case "CleanFreak": return "🧹";
            case "SpeedDemon": return "⚡";
            case "ParanoidPanda": return "🐼";
            case "ChaosMonkey": return "🐒";
            case "ZenMaster": return "🧘";
            case "SilentObserver": return "👁️";
            case "MadScientist": return "🧪";
            default: return "🤖";
        }
    }
}