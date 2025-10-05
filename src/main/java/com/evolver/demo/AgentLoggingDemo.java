package com.evolver.demo;

import com.evolver.agent.AgentInterface;
import com.evolver.agent.AgentCharacteristic;

/**
 * DEMO: Agent Logging System
 *
 * Shows how agents provide "little logs" and summaries for every interaction
 */
public class AgentLoggingDemo {

    public static void main(String[] args) {
        System.out.println("🤖 AGENT LOGGING DEMO");
        System.out.println("====================");

        // Create agents with different personalities
        AgentInterface docBot = new AgentInterface(AgentCharacteristic.DOCUMENTATION_OBSESSIVE);
        AgentInterface speedDemon = new AgentInterface(AgentCharacteristic.PERFORMANCE_MANIAC);
        AgentInterface chaosMonkey = new AgentInterface(AgentCharacteristic.CHAOS_MONKEY);

        System.out.println("\n📝 Testing DocBot (Documentation Obsessive):");
        String result1 = docBot.ask("How do I create a Java class?");

        System.out.println("\n⚡ Testing SpeedDemon (Performance Maniac):");
        String result2 = speedDemon.ask("Optimize this sorting algorithm");

        System.out.println("\n🐒 Testing ChaosMonkey (Chaos Agent):");
        String result3 = chaosMonkey.ask("What happens if I divide by zero?");

        System.out.println("\n✅ Demo complete! Each agent provided personality-specific logs.");
    }
}