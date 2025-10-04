package com.evolver.context.examples;

import com.evolver.context.*;
import com.evolver.context.collectors.*;

import java.util.concurrent.CompletableFuture;

/**
 * Example usage of the Context Engineering Framework
 */
public class ContextEngineExample {

    public static void main(String[] args) {
        // 1. Configure the context engine
        ContextConfig config = ContextConfig.builder()
            .minRelevanceThreshold(0.4)
            .reservedBudgetRatio(0.15)
            .addRequiredAspect("code")
            .addRequiredAspect("structure")
            .build();

        // 2. Create and configure the engine
        ContextEngine engine = new ContextEngine(config);

        // 3. Register collectors
        engine.registerCollector(new CodeStructureCollector());
        engine.registerCollector(new DependencyCollector());
        engine.registerCollector(new RuntimeErrorCollector());
        engine.registerCollector(new DocumentationCollector());
        engine.registerCollector(new VCSHistoryCollector());
        engine.registerCollector(new SemanticSearchCollector());

        // Example 1: Code Generation Task
        demonstrateCodeGeneration(engine);

        // Example 2: Bug Fixing Task
        demonstrateBugFixing(engine);

        // Example 3: Code Review Task
        demonstrateCodeReview(engine);
    }

    private static void demonstrateCodeGeneration(ContextEngine engine) {
        System.out.println("=== Example 1: Code Generation ===\n");

        ContextRequest request = ContextRequest.builder()
            .taskDescription("Generate a REST API endpoint for user authentication")
            .taskType(TaskType.CODE_GENERATION)
            .addFocusArea("authentication")
            .addFocusArea("rest_api")
            .addParameter("file_path", "src/main/java/UserController.java")
            .addParameter("project_path", "src/main/java")
            .tokenBudget(8000)
            .scope(ContextScope.MODULE)
            .addPreferredType(ContextType.CODE_STRUCTURE)
            .addPreferredType(ContextType.DOMAIN_PATTERNS)
            .addPreferredType(ContextType.DOMAIN_EXAMPLES)
            .build();

        CompletableFuture<ContextPackage> future = engine.gatherContext(request);

        future.thenAccept(contextPackage -> {
            System.out.println("Context gathered successfully!");
            System.out.println("Estimated tokens: " + contextPackage.getEstimatedTokens());
            System.out.println("Number of fragments: " + contextPackage.getFragments().size());
            System.out.println("Number of sections: " + contextPackage.getSections().size());

            // Analyze context quality
            ContextMetrics metrics = engine.analyzeContext(contextPackage);
            System.out.println("\nContext Metrics:");
            System.out.println(metrics);

            // Render context for AI
            System.out.println("\n--- Rendered Context ---");
            System.out.println(contextPackage.render());
            System.out.println("------------------------\n");
        }).join();
    }

    private static void demonstrateBugFixing(ContextEngine engine) {
        System.out.println("=== Example 2: Bug Fixing ===\n");

        String errorLog = "Exception in thread \"main\" java.lang.NullPointerException\n" +
                         "    at UserService.authenticateUser(UserService.java:42)\n" +
                         "    at LoginController.login(LoginController.java:28)";

        ContextRequest request = ContextRequest.builder()
            .taskDescription("Fix NullPointerException in user authentication")
            .taskType(TaskType.BUG_FIXING)
            .addFocusArea("error_handling")
            .addFocusArea("authentication")
            .addParameter("file_path", "src/main/java/UserService.java")
            .addParameter("error_log", errorLog)
            .tokenBudget(10000)
            .scope(ContextScope.LOCAL)
            .addPreferredType(ContextType.RUNTIME_ERRORS)
            .addPreferredType(ContextType.CODE_IMPLEMENTATION)
            .build();

        CompletableFuture<ContextPackage> future = engine.gatherContext(request);

        future.thenAccept(contextPackage -> {
            System.out.println("Bug fixing context gathered!");
            ContextMetrics metrics = engine.analyzeContext(contextPackage);
            System.out.println("Context Metrics: " + metrics);

            // Custom template for bug fixing
            String template = "# Bug Fix Context\n\n" +
                            "## Task: {task}\n\n" +
                            "## Type: {task_type}\n\n" +
                            "{sections}";

            System.out.println("\n--- Formatted for AI ---");
            System.out.println(contextPackage.render(template));
            System.out.println("------------------------\n");
        }).join();
    }

    private static void demonstrateCodeReview(ContextEngine engine) {
        System.out.println("=== Example 3: Code Review ===\n");

        ContextRequest request = ContextRequest.builder()
            .taskDescription("Review payment processing implementation for security issues")
            .taskType(TaskType.SECURITY_ANALYSIS)
            .addFocusArea("security")
            .addFocusArea("payment")
            .addFocusArea("validation")
            .addParameter("file_path", "src/main/java/PaymentService.java")
            .addParameter("project_path", "src/main/java")
            .tokenBudget(12000)
            .scope(ContextScope.MODULE)
            .addPreferredType(ContextType.CODE_IMPLEMENTATION)
            .addPreferredType(ContextType.DOMAIN_BEST_PRACTICES)
            .addPreferredType(ContextType.CODE_DEPENDENCIES)
            .build();

        CompletableFuture<ContextPackage> future = engine.gatherContext(request);

        future.thenAccept(contextPackage -> {
            System.out.println("Code review context gathered!");
            ContextMetrics metrics = engine.analyzeContext(contextPackage);
            System.out.println("Context Metrics: " + metrics);
            System.out.println("\nRelevance Score: " + String.format("%.2f", metrics.getRelevanceScore()));
            System.out.println("Coverage: " + String.format("%.2f%%", metrics.getCoverage() * 100));
            System.out.println("------------------------\n");
        }).join();
    }
}
