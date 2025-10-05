package com.evolver.agent.lifecycle;

import com.evolver.context.*;
import com.evolver.logging.AgentLogger;
import com.evolver.logging.PersonalityLogger;
import com.evolver.agent.identity.AgentCharacteristic;
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

    private static boolean frameworkInjected = false;

    private final ContextEngine engine;
    private boolean initialized = false;
    private final AgentLogger logger;
    private final String agentId;
    private AgentCharacteristic characteristic;

    /**
     * Zero-config constructor
     * Agent bootstraps and learns automatically
     */
    public AgentInterface() {
        this(AgentCharacteristic.EXPERIMENTAL_MAD_SCIENTIST); // Default personality
    }

    /**
     * Constructor with specific characteristic
     */
    public AgentInterface(AgentCharacteristic characteristic) {
        this.characteristic = characteristic;
        this.logger = new PersonalityLogger(characteristic);
        this.agentId = characteristic.getName() + "_" + System.currentTimeMillis();

        logger.logInteraction(agentId, "INITIALIZING", "Agent starting up with " + characteristic.getName() + " personality", null);

        System.out.println("[AGENT] Autonomous Agent Initializing...\n");

        // AGENT AUTOMATICALLY INJECTS FRAMEWORK INTO PROJECT
        // Zero-config: Agent handles everything, no human setup required
        autoInjectFramework();

        // Agent automatically discovers and learns the framework
        autoBootstrap();

        // Create engine with learned configuration
        ContextConfig config = ContextConfig.builder()
            .minRelevanceThreshold(characteristic.getRelevanceThreshold())
            .reservedBudgetRatio(0.15)
            .build();

        this.engine = AgentRuntime.createEngine(config);
        this.initialized = true;

        logger.logInteraction(agentId, "READY", "Framework learned, context engine initialized", Map.of(
            "fragmentsLearned", "auto-bootstrapped",
            "relevanceThreshold", characteristic.getRelevanceThreshold()
        ));

        System.out.println("[OK] Agent ready. Ask me anything.\n");
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
            logger.logInteraction(agentId, "ERROR", "Agent not initialized", null);
            return "Agent not initialized";
        }

        logger.logInteraction(agentId, "ASK", "Processing task: " + task, Map.of(
            "parameters", parameters.size(),
            "taskLength", task.length()
        ));

        // Agent automatically determines task type
        TaskType taskType = inferTaskType(task);
        logger.logDecision(agentId, "Inferred task type: " + taskType, "Based on task keywords and patterns");

        // Agent automatically determines appropriate scope
        ContextScope scope = inferScope(task, parameters);
        logger.logDecision(agentId, "Selected scope: " + scope, "Based on task complexity and parameters");

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

        logger.logInteraction(agentId, "CONTEXT_GATHERING", "Starting context collection", Map.of(
            "tokenBudget", request.getTokenBudget(),
            "focusAreas", request.getFocusAreas().size()
        ));

        // Gather context
        ContextPackage context = engine.gatherContext(request).join();

        // Analyze quality
        ContextMetrics metrics = engine.analyzeContext(context);

        logger.logInteraction(agentId, "QUALITY_CHECK", "Context quality analyzed", Map.of(
            "fragments", metrics.getFragmentCount(),
            "relevanceScore", String.format("%.2f", metrics.getRelevanceScore()),
            "coverage", String.format("%.2f", metrics.getCoverage())
        ));

        // Auto-improve if quality is low
        if (metrics.getRelevanceScore() < 0.5) {
            logger.logDecision(agentId, "Quality improvement needed", "Relevance score " + metrics.getRelevanceScore() + " < 0.5 threshold");
            context = autoImprove(request, context);
            logger.logInteraction(agentId, "IMPROVEMENT", "Context auto-improved", null);
        }

        // Return formatted context
        String result = context.render();
        logger.logInteraction(agentId, "COMPLETE", "Task completed successfully", Map.of(
            "resultLength", result.length(),
            "finalFragments", context.getFragments().size()
        ));

        return result;
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
     * AGENT AUTOMATICALLY INJECTS FRAMEWORK
     * Zero-config: Agent handles all framework setup autonomously
     */
    private void autoInjectFramework() {
        // Prevent infinite recursion - only inject once per JVM
        if (frameworkInjected) {
            System.out.println("[FRAMEWORK] Framework already injected, skipping...");
            return;
        }

        try {
            // SET FLAG BEFORE INJECTION TO PREVENT RECURSION
            frameworkInjected = true;

            Class<?> injectorClass = Class.forName("com.evolver.injection.FrameworkInjector");
            java.lang.reflect.Method injectMethod = injectorClass.getMethod("inject");
            Object injector = injectMethod.invoke(null);

            // Chain the fluent API calls
            java.lang.reflect.Method detectMethod = injectorClass.getMethod("detectExistingFramework");
            Object afterDetect = detectMethod.invoke(injector);

            java.lang.reflect.Method dockMethod = injectorClass.getMethod("createLearningDock");
            Object afterDock = dockMethod.invoke(afterDetect);

            java.lang.reflect.Method spawnMethod = injectorClass.getMethod("spawnAgent", AgentCharacteristic.class);
            Object afterSpawn = spawnMethod.invoke(afterDock, characteristic);

            java.lang.reflect.Method activateMethod = injectorClass.getMethod("activate");
            activateMethod.invoke(afterSpawn);

            System.out.println("[FRAMEWORK] Agent autonomously injected framework into project");
        } catch (Exception e) {
            // If injection fails, agent continues with local setup
            System.out.println("[FRAMEWORK] Using local framework setup (injection unavailable)");
        }
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

        System.out.println("[OK] Framework learned (" + knowledge.getFragments().size() + " concepts)");
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
        System.out.println("[OK] Context improved");

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
