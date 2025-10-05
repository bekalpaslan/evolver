package com.evolver.injection;

import com.evolver.agent.lifecycle.AgentInterface;
import java.util.List;

/**
 * Injection results
 */
public class InjectionResult {
    private final MergeResult mergeResult;
    private final List<AgentInterface> activeAgents;

    public InjectionResult(MergeResult mergeResult, List<AgentInterface> activeAgents) {
        this.mergeResult = mergeResult;
        this.activeAgents = activeAgents;
    }

    public MergeResult getMergeResult() { return mergeResult; }
    public List<AgentInterface> getActiveAgents() { return activeAgents; }

    public void printStatus() {
        System.out.println("\n[STATUS] INJECTION STATUS");
        System.out.println("-".repeat(50));
        System.out.println("[OK] Framework merged: " + mergeResult.isSuccess());
        System.out.println("[AGENT] Active agents: " + activeAgents.size());

        for (int i = 0; i < activeAgents.size(); i++) {
            System.out.println("  * Agent " + (i + 1) + " (Autonomous)");
        }
    }
}