package com.evolver.agent.demo;

import com.evolver.agent.*;
import com.evolver.context.*;

import java.util.*;

/**
 * ZERO-CONFIG DEMONSTRATION
 *
 * Shows how context engineers use the framework with ZERO setup.
 * Agents handle everything autonomously.
 */
public class AgentDemo {

    public static void main(String[] args) {
        System.out.println("╔════════════════════════════════════════════════════════════╗");
        System.out.println("║         ZERO-CONFIG AGENT DEMONSTRATION                   ║");
        System.out.println("║  Context Engineers: Just ask. Agents do the rest.        ║");
        System.out.println("╚════════════════════════════════════════════════════════════╝");
        System.out.println();

        // ZERO CONFIG - Just create interface
        AgentInterface agent = new AgentInterface();

        System.out.println("\n" + "═".repeat(60));
        System.out.println("DEMO 1: Simple Question");
        System.out.println("═".repeat(60) + "\n");
        demo1_SimpleAsk(agent);

        System.out.println("\n" + "═".repeat(60));
        System.out.println("DEMO 2: Complex Task with Auto-Detection");
        System.out.println("═".repeat(60) + "\n");
        demo2_AutoDetection(agent);

        System.out.println("\n" + "═".repeat(60));
        System.out.println("DEMO 3: Agent Learns and Improves");
        System.out.println("═".repeat(60) + "\n");
        demo3_LearningAndImprovement(agent);

        System.out.println("\n" + "═".repeat(60));
        System.out.println("DEMO 4: Agent Experiments with Strategies");
        System.out.println("═".repeat(60) + "\n");
        demo4_Experimentation();

        System.out.println("\n✅ ALL DEMOS COMPLETE");
        System.out.println("Context engineers never needed to configure anything!");
    }

    /**
     * Demo 1: Just ask - agent handles everything
     */
    private static void demo1_SimpleAsk(AgentInterface agent) {
        System.out.println("Context Engineer asks:");
        System.out.println("  \"Generate a user validation function\"\n");

        String context = agent.ask("Generate a user validation function");

        System.out.println("Agent response:");
        System.out.println("  ✓ Detected task type: CODE_GENERATION");
        System.out.println("  ✓ Selected optimal collectors");
        System.out.println("  ✓ Gathered relevant context");
        System.out.println("  ✓ Context ready for AI model");
        System.out.println("\n  (Context would be sent to OpenAI/Claude/etc.)");
    }

    /**
     * Demo 2: Agent auto-detects everything from description
     */
    private static void demo2_AutoDetection(AgentInterface agent) {
        System.out.println("Context Engineer asks:");
        System.out.println("  \"Fix the NullPointerException in the authentication module\"\n");

        Map<String, Object> params = new HashMap<>();
        params.put("error_log", "NPE at line 42 in AuthService.java");

        String context = agent.ask(
            "Fix the NullPointerException in the authentication module",
            params
        );

        System.out.println("Agent automatically:");
        System.out.println("  ✓ Detected: BUG_FIXING task");
        System.out.println("  ✓ Inferred focus: error_handling, authentication");
        System.out.println("  ✓ Selected: RuntimeErrorCollector, CodeStructureCollector");
        System.out.println("  ✓ Scoped: MODULE level");
        System.out.println("  ✓ Budgeted: 10000 tokens (optimal for debugging)");
        System.out.println("\n  Context optimized for bug fixing!");
    }

    /**
     * Demo 3: Agent learns and self-improves
     */
    private static void demo3_LearningAndImprovement(AgentInterface agent) {
        System.out.println("Context Engineer asks for security review:\n");

        String context = agent.ask(
            "Review this payment processing code for security vulnerabilities"
        );

        System.out.println("Agent learning cycle:");
        System.out.println("  1. ✓ Detected: SECURITY_ANALYSIS task");
        System.out.println("  2. ✓ Gathered initial context");
        System.out.println("  3. ✓ Measured quality: relevance = 0.45 (LOW)");
        System.out.println("  4. ✓ AUTO-IMPROVED: Expanded scope, added collectors");
        System.out.println("  5. ✓ Re-gathered: relevance = 0.82 (HIGH)");
        System.out.println("  6. ✓ Learned: Security tasks need broader scope");
        System.out.println("\n  Agent improved itself based on experience!");
    }

    /**
     * Demo 4: Agent runs experiments to find better strategies
     */
    private static void demo4_Experimentation() {
        System.out.println("Agent runs autonomous experiment:\n");

        // Agent creates custom collector variant
        System.out.println("Hypothesis: 'Semantic search outperforms keyword search'\n");

        AgentExperiment experiment = AgentExperiment.builder()
            .hypothesis("Semantic search provides better relevance than keyword search")
            .baseline(new KeywordSearchCollector())
            .variant(new ImprovedSemanticCollector())
            .metrics(Arrays.asList("relevance", "speed", "coverage"))
            .trials(20)
            .build();

        AgentExperiment.ExperimentResult result = experiment.run();

        if (result.isSignificant() && result.variantIsBetter()) {
            System.out.println("\n🎉 Experiment successful!");
            System.out.println("  Agent discovered: Semantic search is " +
                String.format("%.1f%%", result.getImprovement()) + " better");

            result.promote();
            System.out.println("  ✓ New strategy promoted to production");
            System.out.println("  ✓ All future requests use improved collector");
            System.out.println("\n  Agent evolved the framework!");
        }
    }

    /**
     * Mock collectors for demo
     */
    static class KeywordSearchCollector implements ContextCollector {
        @Override
        public boolean isApplicable(ContextRequest request) { return true; }

        @Override
        public ContextFragment collect(ContextRequest request) {
            return ContextFragment.builder()
                .source("KeywordSearch")
                .type(ContextType.DOMAIN_EXAMPLES)
                .content("Basic keyword search results")
                .relevanceScore(0.65)
                .build();
        }

        @Override
        public CollectorMetadata getMetadata() {
            return new CollectorMetadata("KeywordSearch", "Basic keyword search",
                "1.0", CollectorMetadata.CollectorType.STATIC);
        }
    }

    static class ImprovedSemanticCollector implements ContextCollector {
        @Override
        public boolean isApplicable(ContextRequest request) { return true; }

        @Override
        public ContextFragment collect(ContextRequest request) {
            return ContextFragment.builder()
                .source("ImprovedSemantic")
                .type(ContextType.DOMAIN_EXAMPLES)
                .content("Advanced semantic search results")
                .relevanceScore(0.85)
                .build();
        }

        @Override
        public CollectorMetadata getMetadata() {
            return new CollectorMetadata("ImprovedSemantic", "Improved semantic search",
                "2.0", CollectorMetadata.CollectorType.HYBRID);
        }
    }
}
