package com.evolver.context.examples;

import com.evolver.context.*;
import com.evolver.context.collectors.*;

/**
 * Meta Example: The Context Framework using itself!
 *
 * This demonstrates the framework gathering context ABOUT ITSELF
 * to help an AI agent understand how to use it.
 */
public class SelfReferencingExample {

    public static void main(String[] args) {
        System.out.println("🔄 Meta Example: Framework Using Itself!\n");
        System.out.println("Question: How can an AI agent learn to use this framework?\n");
        System.out.println("Answer: By using the framework to gather context about itself!\n");
        System.out.println("===========================================================\n");

        // The framework uses itself to explain itself
        demonstrateMetaUsage();
    }

    private static void demonstrateMetaUsage() {
        // 1. Create the context engine
        ContextEngine engine = createSelfAwareEngine();

        // 2. AI Agent asks: "How do I use the Context Framework for code generation?"
        ContextRequest request = ContextRequest.builder()
            .taskDescription("Teach me how to use the Context Engineering Framework for code generation tasks")
            .taskType(TaskType.EXPLANATION)
            .addFocusArea("usage")
            .addFocusArea("code_generation")
            .addFocusArea("tutorial")
            .addParameter("file_path", "src/main/java/com/evolver/context/ContextEngine.java")
            .addParameter("example_path", "src/main/java/com/evolver/context/examples/QuickStartExample.java")
            .addParameter("project_path", "src/main/java/com/evolver/context")
            .tokenBudget(12000)
            .scope(ContextScope.PROJECT)
            .addPreferredType(ContextType.CODE_STRUCTURE)
            .addPreferredType(ContextType.DOMAIN_EXAMPLES)
            .addPreferredType(ContextType.PROJECT_DOCUMENTATION)
            .build();

        // 3. The framework gathers context about ITSELF
        System.out.println("[LEARN] The framework is gathering context about itself...\n");

        ContextPackage context = engine.gatherContext(request).join();

        // 4. Analyze the meta-context
        ContextMetrics metrics = engine.analyzeContext(context);
        System.out.println("📊 Meta-Context Quality:");
        System.out.println("   Fragments collected: " + metrics.getFragmentCount());
        System.out.println("   Relevance score: " + String.format("%.2f", metrics.getRelevanceScore()));
        System.out.println("   Coverage: " + String.format("%.1f%%", metrics.getCoverage() * 100));
        System.out.println("   Tokens used: " + metrics.getTotalTokens() + "/12000");

        // 5. Create a learning prompt
        String learningTemplate = """
            # Learning Guide: Context Engineering Framework

            ## Your Question
            {task}

            ## Framework Documentation and Examples
            {sections}

            ## Learning Objectives
            After studying this context, you should be able to:
            1. Understand the core components of the framework
            2. Create a ContextEngine instance
            3. Build ContextRequest objects for different tasks
            4. Gather and use context in your AI workflows
            5. Monitor context quality with metrics

            ## Practice Exercise
            Now that you have the context, try to:
            1. Write code to set up the framework
            2. Create a request for a code generation task
            3. Explain how the prioritization works

            Begin your learning:
            """;

        String aiPrompt = context.render(learningTemplate);

        System.out.println("\n" + "=".repeat(60));
        System.out.println("📤 PROMPT FOR AI (Self-Learning):");
        System.out.println("=".repeat(60));
        System.out.println(aiPrompt);
        System.out.println("=".repeat(60));

        System.out.println("\n[IDEA] The AI agent now has optimal context to learn the framework!");
        System.out.println("[IDEA] It gathered this context BY USING the framework itself!");

        // 6. Simulate AI understanding
        simulateAILearning(context);
    }

    /**
     * Create an engine that can understand itself
     */
    private static ContextEngine createSelfAwareEngine() {
        ContextConfig config = ContextConfig.builder()
            .minRelevanceThreshold(0.3)
            .reservedBudgetRatio(0.15)
            .addRequiredAspect("usage")
            .addRequiredAspect("examples")
            .build();

        ContextEngine engine = new ContextEngine(config);

        // Register collectors that can analyze the framework's own code
        engine.registerCollector(new CodeStructureCollector());
        engine.registerCollector(new DependencyCollector());
        engine.registerCollector(new DocumentationCollector());
        engine.registerCollector(new SemanticSearchCollector());

        // Self-aware collector - understands framework patterns
        engine.registerCollector(new FrameworkPatternCollector());

        return engine;
    }

    /**
     * Simulate how an AI agent would learn from the gathered context
     */
    private static void simulateAILearning(ContextPackage context) {
        System.out.println("\n🤖 AI Agent Learning Process:");
        System.out.println("─".repeat(60));

        // Extract key learnings from the context
        int stepCount = 1;

        System.out.println(stepCount++ + ". Analyzed ContextEngine structure");
        System.out.println("   → Learned: Engine orchestrates collectors, filters, prioritizers");

        System.out.println(stepCount++ + ". Reviewed QuickStartExample");
        System.out.println("   → Learned: Setup is 3 steps - Config, Engine, Collectors");

        System.out.println(stepCount++ + ". Studied ContextRequest builder");
        System.out.println("   → Learned: Requests specify task, type, budget, parameters");

        System.out.println(stepCount++ + ". Examined context rendering");
        System.out.println("   → Learned: Use .render() or custom templates");

        System.out.println(stepCount++ + ". Understood quality metrics");
        System.out.println("   → Learned: Check relevance, coverage, token usage");

        System.out.println("\n✅ AI Agent is now competent with the framework!");
        System.out.println("✅ It learned by using the framework on itself!");

        // Now the AI can use the framework
        demonstrateLearnedAbility();
    }

    /**
     * Show that the AI has learned to use the framework
     */
    private static void demonstrateLearnedAbility() {
        System.out.println("\n🎓 AI Agent's First Independent Use:");
        System.out.println("─".repeat(60));

        // The AI creates its own request (it learned this!)
        ContextConfig config = ContextConfig.builder().build();
        ContextEngine engine = new ContextEngine(config);
        engine.registerCollector(new CodeStructureCollector());

        ContextRequest aiCreatedRequest = ContextRequest.builder()
            .taskDescription("Generate a user authentication function")
            .taskType(TaskType.CODE_GENERATION)
            .tokenBudget(5000)
            .build();

        System.out.println("AI successfully created this request:");
        System.out.println("  Task: " + aiCreatedRequest.getTaskDescription());
        System.out.println("  Type: " + aiCreatedRequest.getTaskType());
        System.out.println("  Budget: " + aiCreatedRequest.getTokenBudget());

        ContextPackage result = engine.gatherContext(aiCreatedRequest).join();
        System.out.println("\n✅ AI gathered context successfully!");
        System.out.println("   Tokens: " + result.getEstimatedTokens());
        System.out.println("   Fragments: " + result.getFragments().size());

        System.out.println("\n🎉 META-LEARNING COMPLETE!");
        System.out.println("   The framework taught itself to an AI agent,");
        System.out.println("   which can now use the framework independently!");
    }

    /**
     * Custom collector that understands framework-specific patterns
     */
    static class FrameworkPatternCollector implements ContextCollector {

        @Override
        public boolean isApplicable(ContextRequest request) {
            // Only for explaining the framework itself
            return request.getFocusAreas().contains("usage") ||
                   request.getFocusAreas().contains("tutorial");
        }

        @Override
        public ContextFragment collect(ContextRequest request) {
            String patterns = """
                # Common Framework Patterns

                ## Pattern 1: Basic Setup
                ```java
                ContextEngine engine = new ContextEngine(config);
                engine.registerCollector(new CodeStructureCollector());
                ```

                ## Pattern 2: Request Building
                ```java
                ContextRequest request = ContextRequest.builder()
                    .taskDescription("your task")
                    .taskType(TaskType.CODE_GENERATION)
                    .tokenBudget(8000)
                    .build();
                ```

                ## Pattern 3: Context Gathering
                ```java
                ContextPackage context = engine.gatherContext(request).join();
                String prompt = context.render();
                ```

                ## Pattern 4: Quality Checking
                ```java
                ContextMetrics metrics = engine.analyzeContext(context);
                if (metrics.getRelevanceScore() > 0.7) {
                    // Good context, proceed
                }
                ```

                ## Pattern 5: Custom Templates
                ```java
                String template = "Task: {task}\\n{sections}";
                String formatted = context.render(template);
                ```
                """;

            return ContextFragment.builder()
                .source("FrameworkPatternCollector")
                .type(ContextType.DOMAIN_PATTERNS)
                .content(patterns)
                .addAspect("usage")
                .addAspect("patterns")
                .addAspect("examples")
                .relevanceScore(0.95) // Highly relevant for learning
                .addMetadata("pattern_count", 5)
                .addMetadata("purpose", "framework_learning")
                .build();
        }

        @Override
        public int getPriority() {
            return 90; // High priority for learning tasks
        }

        @Override
        public CollectorMetadata getMetadata() {
            return new CollectorMetadata(
                "FrameworkPatternCollector",
                "Collects common usage patterns for the framework itself",
                "1.0.0",
                CollectorMetadata.CollectorType.STATIC
            );
        }
    }
}
