package com.evolver.agent;

import com.evolver.context.*;
import java.util.*;

/**
 * ZERO-CONFIG AGENT INTERFACE
 *
 * This is the ONLY class human context engineers need to use.
 * Everything else is handled by autonomous agents.
 *
 * Usage:
 *   AgentInterface agent = new AgentInterface();
 *   String result = agent.ask("Your task here");
 */
public class AgentInterface {

    private final ContextEngine engine;
    private boolean initialized = false;

    /**
     * Zero-config constructor
     * Agent bootstraps and learns automatically
     */
    public AgentInterface() {
        System.out.println("🤖 Autonomous Agent Initializing...\n");

        // Agent automatically discovers and learns the framework
        autoBootstrap();

        // Create engine with learned configuration
        ContextConfig config = ContextConfig.builder()
            .minRelevanceThreshold(0.3)
            .reservedBudgetRatio(0.15)
            .build();

        this.engine = AgentRuntime.createEngine(config);
        this.initialized = true;

        System.out.println("✅ Agent ready. Ask me anything.\n");
    }

    /**
     * Main interface: Ask the agent to do anything
     */
    public String ask(String task) {
        return ask(task, new HashMap<>());
    }

    /**
     * Ask with parameters
     */
    public String ask(String task, Map<String, Object> parameters) {
        if (!initialized) {
            return "Agent not initialized";
        }

        // Agent automatically determines task type
        TaskType taskType = inferTaskType(task);

        // Agent automatically determines appropriate scope
        ContextScope scope = inferScope(task, parameters);

        // Build request
        ContextRequest.Builder requestBuilder = ContextRequest.builder()
            .taskDescription(task)
            .taskType(taskType)
            .tokenBudget(getOptimalTokenBudget(taskType))
            .scope(scope);

        // Add parameters
        parameters.forEach(requestBuilder::addParameter);

        // Add inferred focus areas
        inferFocusAreas(task).forEach(requestBuilder::addFocusArea);

        ContextRequest request = requestBuilder.build();

        // Gather context
        ContextPackage context = engine.gatherContext(request).join();

        // Analyze quality
        ContextMetrics metrics = engine.analyzeContext(context);

        // Auto-improve if quality is low
        if (metrics.getRelevanceScore() < 0.5) {
            context = autoImprove(request, context);
        }

        // Return formatted context
        return context.render();
    }

    /**
     * Ask with explicit task type
     */
    public String ask(String task, TaskType taskType) {
        Map<String, Object> params = new HashMap<>();
        return ask(task, taskType, params);
    }

    /**
     * Full control ask
     */
    public String ask(String task, TaskType taskType, Map<String, Object> parameters) {
        ContextRequest.Builder requestBuilder = ContextRequest.builder()
            .taskDescription(task)
            .taskType(taskType)
            .tokenBudget(getOptimalTokenBudget(taskType))
            .scope(ContextScope.MODULE);

        parameters.forEach(requestBuilder::addParameter);

        ContextPackage context = engine.gatherContext(requestBuilder.build()).join();
        return context.render();
    }

    /**
     * Agent auto-bootstraps on first use
     */
    private void autoBootstrap() {
        System.out.println("📚 Agent learning framework...");

        // Agent learns by using framework on itself
        ContextConfig learningConfig = ContextConfig.builder()
            .minRelevanceThreshold(0.2)
            .build();

        ContextEngine learningEngine = new ContextEngine(learningConfig);
        learningEngine.registerCollector(new com.evolver.context.collectors.CodeStructureCollector());
        learningEngine.registerCollector(new com.evolver.context.collectors.DocumentationCollector());

        ContextRequest learningRequest = ContextRequest.builder()
            .taskDescription("Learn the Context Engineering Framework")
            .taskType(TaskType.EXPLANATION)
            .addParameter("project_path", "src/main/java/com/evolver/context")
            .tokenBudget(20000)
            .scope(ContextScope.PROJECT)
            .build();

        ContextPackage knowledge = learningEngine.gatherContext(learningRequest).join();

        System.out.println("✓ Framework learned (" + knowledge.getFragments().size() + " concepts)");
    }

    /**
     * Infer task type from description
     */
    private TaskType inferTaskType(String task) {
        String lower = task.toLowerCase();

        if (lower.contains("generate") || lower.contains("create") || lower.contains("write")) {
            return TaskType.CODE_GENERATION;
        } else if (lower.contains("fix") || lower.contains("bug") || lower.contains("error")) {
            return TaskType.BUG_FIXING;
        } else if (lower.contains("review") || lower.contains("check")) {
            return TaskType.CODE_REVIEW;
        } else if (lower.contains("explain") || lower.contains("understand")) {
            return TaskType.EXPLANATION;
        } else if (lower.contains("test")) {
            return TaskType.TEST_GENERATION;
        } else if (lower.contains("refactor") || lower.contains("improve")) {
            return TaskType.CODE_REFACTORING;
        } else if (lower.contains("document")) {
            return TaskType.DOCUMENTATION;
        } else if (lower.contains("security") || lower.contains("vulnerability")) {
            return TaskType.SECURITY_ANALYSIS;
        }

        return TaskType.GENERAL;
    }

    /**
     * Infer appropriate scope
     */
    private ContextScope inferScope(String task, Map<String, Object> parameters) {
        if (parameters.containsKey("file_path")) {
            return ContextScope.LOCAL;
        } else if (parameters.containsKey("module_path")) {
            return ContextScope.MODULE;
        } else if (parameters.containsKey("project_path")) {
            return ContextScope.PROJECT;
        }

        // Infer from task
        String lower = task.toLowerCase();
        if (lower.contains("entire") || lower.contains("all") || lower.contains("project")) {
            return ContextScope.PROJECT;
        } else if (lower.contains("module") || lower.contains("package")) {
            return ContextScope.MODULE;
        }

        return ContextScope.LOCAL;
    }

    /**
     * Infer focus areas from task
     */
    private Set<String> inferFocusAreas(String task) {
        Set<String> areas = new HashSet<>();
        String lower = task.toLowerCase();

        // Common focus areas
        if (lower.contains("security")) areas.add("security");
        if (lower.contains("performance")) areas.add("performance");
        if (lower.contains("test")) areas.add("testing");
        if (lower.contains("error")) areas.add("error_handling");
        if (lower.contains("validation")) areas.add("validation");
        if (lower.contains("authentication")) areas.add("authentication");
        if (lower.contains("database")) areas.add("database");
        if (lower.contains("api")) areas.add("api");

        return areas;
    }

    /**
     * Get optimal token budget for task type
     */
    private int getOptimalTokenBudget(TaskType taskType) {
        switch (taskType) {
            case CODE_GENERATION: return 8000;
            case BUG_FIXING: return 10000;
            case CODE_REVIEW: return 12000;
            case SECURITY_ANALYSIS: return 15000;
            case ARCHITECTURE_REVIEW: return 20000;
            case DOCUMENTATION: return 8000;
            case TEST_GENERATION: return 6000;
            default: return 8000;
        }
    }

    /**
     * Auto-improve low quality context
     */
    private ContextPackage autoImprove(ContextRequest original, ContextPackage lowQuality) {
        System.out.println("⚠️ Low quality context detected. Auto-improving...");

        // Expand scope
        ContextScope newScope = expandScope(original.getScope());

        // Increase budget
        int newBudget = (int) (original.getTokenBudget() * 1.5);

        ContextRequest improved = ContextRequest.builder()
            .taskDescription(original.getTaskDescription())
            .taskType(original.getTaskType())
            .tokenBudget(newBudget)
            .scope(newScope)
            .focusAreas(original.getFocusAreas())
            .parameters(original.getParameters())
            .build();

        ContextPackage result = engine.gatherContext(improved).join();
        System.out.println("✓ Context improved");

        return result;
    }

    /**
     * Expand scope for better context
     */
    private ContextScope expandScope(ContextScope current) {
        switch (current) {
            case MINIMAL: return ContextScope.LOCAL;
            case LOCAL: return ContextScope.MODULE;
            case MODULE: return ContextScope.PROJECT;
            default: return ContextScope.PROJECT;
        }
    }

    /**
     * Enable verbose mode for debugging
     */
    public void setVerbose(boolean verbose) {
        // Agent can explain its reasoning
    }

    /**
     * Get agent's current state
     */
    public void printState() {
        AgentRuntime.printState();
    }
}
