package com.evolver.context.examples;

import com.evolver.context.*;
import com.evolver.context.collectors.*;

/**
 * Quick Start Example - Your first AI agent with context engineering
 *
 * This example shows the absolute minimum code needed to get started.
 */
public class QuickStartExample {

    public static void main(String[] args) {
        System.out.println("[LAUNCH] Context Engineering Framework - Quick Start\n");

        // STEP 1: Create and configure the engine (one-time setup)
        ContextEngine engine = createEngine();

        // STEP 2: Use it for different AI tasks
        example1_CodeGeneration(engine);
        example2_BugFixing(engine);
        example3_CodeReview(engine);
    }

    /**
     * STEP 1: Create the context engine (do this once)
     */
    private static ContextEngine createEngine() {
        // Simple configuration
        ContextConfig config = ContextConfig.builder()
            .minRelevanceThreshold(0.3)
            .build();

        ContextEngine engine = new ContextEngine(config);

        // Add the collectors you need
        engine.registerCollector(new CodeStructureCollector());
        engine.registerCollector(new DependencyCollector());
        engine.registerCollector(new RuntimeErrorCollector());
        engine.registerCollector(new DocumentationCollector());

        return engine;
    }

    /**
     * Example 1: Generate code with optimal context
     */
    private static void example1_CodeGeneration(ContextEngine engine) {
        System.out.println("===========================================");
        System.out.println("Example 1: Code Generation");
        System.out.println("===========================================\n");

        // What you want the AI to do
        String task = "Create a method to validate user passwords";

        // Build the request
        ContextRequest request = ContextRequest.builder()
            .taskDescription(task)
            .taskType(TaskType.CODE_GENERATION)
            .addParameter("file_path", "src/SecurityUtils.java")
            .tokenBudget(6000)
            .build();

        // Gather context
        ContextPackage context = engine.gatherContext(request).join();

        // This is what you send to your AI model
        String promptForAI = context.render();

        System.out.println("📤 Prompt for AI:");
        System.out.println(promptForAI);
        System.out.println("\n[OK] Tokens used: " + context.getEstimatedTokens() + "/6000");

        // In real usage: String result = yourAI.complete(promptForAI);
        System.out.println("\n💡 Next: Send 'promptForAI' to OpenAI/Claude/etc.\n");
    }

    /**
     * Example 2: Fix bugs with error-focused context
     */
    private static void example2_BugFixing(ContextEngine engine) {
        System.out.println("===========================================");
        System.out.println("Example 2: Bug Fixing");
        System.out.println("===========================================\n");

        // The error you want fixed
        String errorLog = """
            java.lang.NullPointerException
                at UserService.validatePassword(UserService.java:45)
                at LoginController.authenticate(LoginController.java:23)
            """;

        ContextRequest request = ContextRequest.builder()
            .taskDescription("Fix the NullPointerException in password validation")
            .taskType(TaskType.BUG_FIXING)
            .addParameter("file_path", "src/UserService.java")
            .addParameter("error_log", errorLog)
            .tokenBudget(8000)
            .build();

        ContextPackage context = engine.gatherContext(request).join();

        // Check quality
        ContextMetrics metrics = engine.analyzeContext(context);
        System.out.println("[STATUS] Context Quality:");
        System.out.println("   Relevance: " + String.format("%.2f", metrics.getRelevanceScore()));
        System.out.println("   Fragments: " + metrics.getFragmentCount());
        System.out.println("   Tokens: " + metrics.getTotalTokens());

        String promptForAI = context.render();
        System.out.println("\n💡 Send this to AI to get the bug fix\n");

        // In real usage: String fix = yourAI.complete(promptForAI);
    }

    /**
     * Example 3: Code review with best practices
     */
    private static void example3_CodeReview(ContextEngine engine) {
        System.out.println("===========================================");
        System.out.println("Example 3: Code Review");
        System.out.println("===========================================\n");

        ContextRequest request = ContextRequest.builder()
            .taskDescription("Review this code for security issues")
            .taskType(TaskType.SECURITY_ANALYSIS)
            .addFocusArea("security")
            .addParameter("file_path", "src/PaymentService.java")
            .tokenBudget(10000)
            .build();

        ContextPackage context = engine.gatherContext(request).join();

        // Custom template for code review
        String customTemplate = """
            # Security Code Review

            ## File to Review
            {task}

            ## Context
            {sections}

            ## Instructions
            Please review for:
            1. Security vulnerabilities
            2. Input validation issues
            3. Authentication/authorization flaws
            4. Data exposure risks
            """;

        String promptForAI = context.render(customTemplate);
        System.out.println("📤 Custom formatted prompt ready for AI");
        System.out.println("   Template: Security review template");
        System.out.println("   Tokens: " + context.getEstimatedTokens());

        System.out.println("\n💡 Send to AI for security analysis\n");
    }

    /**
     * BONUS: Integrate with your AI model (pseudo-code)
     */
    @SuppressWarnings("unused")
    private static void integrateWithYourAI(ContextEngine engine) {
        // This is how you'd actually use it with an AI model

        String task = "Generate a REST endpoint for user login";

        ContextRequest request = ContextRequest.builder()
            .taskDescription(task)
            .taskType(TaskType.CODE_GENERATION)
            .addParameter("file_path", "src/UserController.java")
            .tokenBudget(8000)
            .build();

        engine.gatherContext(request).thenAccept(context -> {
            String prompt = context.render();

            // Call your AI (OpenAI, Claude, etc.)
            // String result = openai.complete(prompt);
            // String result = claude.complete(prompt);
            // String result = yourCustomAI.complete(prompt);

            // System.out.println("Generated code: " + result);
        }).join();
    }
}
