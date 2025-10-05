# Getting Started - AI Agent Integration

This guide shows how an AI agent can use the Context Engineering Framework for the first time.

## 🎯 What This Framework Does

The framework helps AI agents gather **exactly the right context** they need to complete tasks effectively, while staying within token limits.

**Without this framework:** AI agents get random, possibly irrelevant context
**With this framework:** AI agents get prioritized, task-specific, optimal context

## 🚀 First-Time Setup (5 minutes)

### Step 1: Add Dependencies

Add to your `build.gradle`:

```gradle
dependencies {
    implementation project(':evolver')
}
```

Or copy the `com.evolver.context` package into your project.

### Step 2: Create Your First Context Engine

```java
import com.evolver.context.*;
import com.evolver.context.collectors.*;

public class MyAIAgent {

    private final ContextEngine contextEngine;

    public MyAIAgent() {
        // 1. Configure the engine
        ContextConfig config = ContextConfig.builder()
            .minRelevanceThreshold(0.3)  // Filter out low-relevance context
            .reservedBudgetRatio(0.1)     // Reserve 10% for critical context
            .build();

        // 2. Create the engine
        this.contextEngine = new ContextEngine(config);

        // 3. Register collectors (choose what you need)
        contextEngine.registerCollector(new CodeStructureCollector());
        contextEngine.registerCollector(new DependencyCollector());
        contextEngine.registerCollector(new RuntimeErrorCollector());
        contextEngine.registerCollector(new DocumentationCollector());
    }
}
```

### Step 3: Request Context for a Task

```java
public String generateCode(String taskDescription, String filePath) {
    // Build a context request
    ContextRequest request = ContextRequest.builder()
        .taskDescription(taskDescription)
        .taskType(TaskType.CODE_GENERATION)
        .addParameter("file_path", filePath)
        .addParameter("project_path", "src/main/java")
        .tokenBudget(8000)  // Your AI's context window
        .scope(ContextScope.MODULE)
        .build();

    // Gather context
    ContextPackage context = contextEngine.gatherContext(request).join();

    // Get formatted context for your AI
    String aiPrompt = context.render();

    // Send to your AI model
    return callYourAIModel(aiPrompt);
}
```

## 💡 Common Use Cases

### Use Case 1: Code Generation

```java
public void generateFunction() {
    ContextRequest request = ContextRequest.builder()
        .taskDescription("Create a function to validate email addresses")
        .taskType(TaskType.CODE_GENERATION)
        .addFocusArea("validation")
        .addFocusArea("email")
        .addParameter("file_path", "src/ValidationUtils.java")
        .tokenBudget(6000)
        .scope(ContextScope.LOCAL)
        // Prefer examples and patterns for generation
        .addPreferredType(ContextType.DOMAIN_EXAMPLES)
        .addPreferredType(ContextType.DOMAIN_PATTERNS)
        .addPreferredType(ContextType.CODE_STRUCTURE)
        .build();

    ContextPackage context = contextEngine.gatherContext(request).join();

    System.out.println("=== Context for AI ===");
    System.out.println(context.render());

    // Metrics to verify quality
    ContextMetrics metrics = contextEngine.analyzeContext(context);
    System.out.println("\nRelevance: " + metrics.getRelevanceScore());
    System.out.println("Token usage: " + metrics.getTotalTokens() + "/6000");
}
```

### Use Case 2: Bug Fixing

```java
public void fixBug(String errorMessage, String stackTrace) {
    String errorLog = errorMessage + "\n" + stackTrace;

    ContextRequest request = ContextRequest.builder()
        .taskDescription("Fix the NullPointerException in user service")
        .taskType(TaskType.BUG_FIXING)
        .addParameter("file_path", "src/UserService.java")
        .addParameter("error_log", errorLog)
        .tokenBudget(10000)
        .scope(ContextScope.LOCAL)
        // Errors are most important for debugging
        .addPreferredType(ContextType.RUNTIME_ERRORS)
        .addPreferredType(ContextType.CODE_IMPLEMENTATION)
        .addPreferredType(ContextType.RUNTIME_LOGS)
        .build();

    ContextPackage context = contextEngine.gatherContext(request).join();

    // Custom template for bug fixing
    String template = """
        # Bug Fix Request

        ## Error Information
        {sections}

        ## Task
        {task}

        Please analyze the error and provide a fix.
        """;

    String prompt = context.render(template);

    // Send to AI for bug fix
    String fix = callYourAIModel(prompt);
    System.out.println("Suggested fix: " + fix);
}
```

### Use Case 3: Code Review

```java
public void reviewCode(String filePath) {
    ContextRequest request = ContextRequest.builder()
        .taskDescription("Review this code for security vulnerabilities")
        .taskType(TaskType.SECURITY_ANALYSIS)
        .addFocusArea("security")
        .addFocusArea("best_practices")
        .addParameter("file_path", filePath)
        .addParameter("project_path", "src")
        .tokenBudget(12000)
        .scope(ContextScope.MODULE)
        .addPreferredType(ContextType.CODE_IMPLEMENTATION)
        .addPreferredType(ContextType.DOMAIN_BEST_PRACTICES)
        .addPreferredType(ContextType.CODE_DEPENDENCIES)
        .build();

    ContextPackage context = contextEngine.gatherContext(request).join();

    // Analyze quality before sending
    ContextMetrics metrics = contextEngine.analyzeContext(context);

    if (metrics.getRelevanceScore() < 0.5) {
        System.out.println("Warning: Low relevance score, may need more collectors");
    }

    String review = callYourAIModel(context.render());
    System.out.println("Code review: " + review);
}
```

### Use Case 4: Documentation Generation

```java
public void generateDocumentation(String className) {
    ContextRequest request = ContextRequest.builder()
        .taskDescription("Generate API documentation for " + className)
        .taskType(TaskType.DOCUMENTATION)
        .addParameter("file_path", "src/" + className + ".java")
        .tokenBudget(8000)
        .scope(ContextScope.LOCAL)
        .addPreferredType(ContextType.CODE_STRUCTURE)
        .addPreferredType(ContextType.CODE_COMMENTS)
        .addPreferredType(ContextType.PROJECT_DOCUMENTATION)
        .build();

    ContextPackage context = contextEngine.gatherContext(request).join();
    String docs = callYourAIModel(context.render());

    // Save generated docs
    saveToFile("docs/" + className + ".md", docs);
}
```

## 🔧 Integration with Popular AI Models

### OpenAI GPT Integration

```java
import com.openai.api.*;

public class OpenAIAgentWithContext {
    private final ContextEngine contextEngine;
    private final OpenAI openai;

    public OpenAIAgentWithContext(String apiKey) {
        this.contextEngine = createContextEngine();
        this.openai = new OpenAI(apiKey);
    }

    public String chat(String userMessage, String filePath) {
        // Gather context based on the message
        ContextRequest request = ContextRequest.builder()
            .taskDescription(userMessage)
            .taskType(detectTaskType(userMessage))
            .addParameter("file_path", filePath)
            .tokenBudget(8000)  // Leave room for GPT response
            .build();

        ContextPackage context = contextEngine.gatherContext(request).join();

        // Build messages with context
        List<ChatMessage> messages = List.of(
            new ChatMessage("system", "You are a helpful coding assistant."),
            new ChatMessage("system", "Context:\n" + context.render()),
            new ChatMessage("user", userMessage)
        );

        // Call OpenAI
        ChatCompletion completion = openai.chatCompletion()
            .model("gpt-4")
            .messages(messages)
            .create();

        return completion.choices().get(0).message().content();
    }

    private TaskType detectTaskType(String message) {
        if (message.contains("bug") || message.contains("error")) {
            return TaskType.BUG_FIXING;
        } else if (message.contains("generate") || message.contains("create")) {
            return TaskType.CODE_GENERATION;
        } else if (message.contains("explain")) {
            return TaskType.EXPLANATION;
        }
        return TaskType.GENERAL;
    }
}
```

### Anthropic Claude Integration

```java
import anthropic.*;

public class ClaudeAgentWithContext {
    private final ContextEngine contextEngine;
    private final Anthropic anthropic;

    public String analyze(String task, Map<String, Object> params) {
        // Build context request
        ContextRequest.Builder requestBuilder = ContextRequest.builder()
            .taskDescription(task)
            .taskType(TaskType.CODE_REVIEW)
            .tokenBudget(150000);  // Claude has large context window

        params.forEach(requestBuilder::addParameter);
        ContextPackage context = contextEngine.gatherContext(requestBuilder.build()).join();

        // Call Claude with rich context
        Message response = anthropic.messages().create(
            MessageCreateParams.builder()
                .model("claude-3-5-sonnet-20241022")
                .maxTokens(4000)
                .system("You are an expert code reviewer.\n\nContext:\n" + context.render())
                .addMessage(Message.builder()
                    .role("user")
                    .content(task)
                    .build())
                .build()
        );

        return response.content().get(0).text();
    }
}
```

## 📊 Monitoring Context Quality

```java
public void monitorContextQuality(ContextPackage context) {
    ContextMetrics metrics = contextEngine.analyzeContext(context);

    System.out.println("=== Context Quality Metrics ===");
    System.out.println("Total tokens: " + metrics.getTotalTokens());
    System.out.println("Fragment count: " + metrics.getFragmentCount());
    System.out.println("Avg relevance: " + String.format("%.2f", metrics.getRelevanceScore()));
    System.out.println("Coverage: " + String.format("%.1f%%", metrics.getCoverage() * 100));

    // Quality checks
    if (metrics.getRelevanceScore() < 0.5) {
        System.out.println("⚠️ Warning: Low relevance - consider adjusting collectors");
    }

    if (metrics.getCoverage() < 0.7) {
        System.out.println("⚠️ Warning: Low coverage - may be missing important context");
    }

    if (metrics.getTotalTokens() > context.getRequest().getTokenBudget() * 0.95) {
        System.out.println("⚠️ Warning: Near token limit - context may be truncated");
    }
}
```

## 🎨 Custom Templates

Create reusable templates for consistent AI prompts:

```java
public class PromptTemplates {

    public static final String CODE_GENERATION = """
        # Code Generation Task

        ## Objective
        {task}

        ## Available Context
        {sections}

        ## Instructions
        1. Review the provided code structure and patterns
        2. Generate clean, well-documented code
        3. Follow existing conventions
        4. Include error handling

        Generate the code now:
        """;

    public static final String BUG_FIX = """
        # Bug Fix Request

        ## Problem
        {task}

        ## Error Details and Code Context
        {sections}

        ## Required Actions
        1. Identify the root cause
        2. Propose a fix
        3. Explain why this fix works
        4. Suggest preventive measures

        Provide your analysis and fix:
        """;

    public static String render(ContextPackage context, String template) {
        return context.render(template);
    }
}
```

Usage:
```java
ContextPackage context = contextEngine.gatherContext(request).join();
String prompt = PromptTemplates.render(context, PromptTemplates.CODE_GENERATION);
String result = callAI(prompt);
```

## 🔌 Creating Custom Collectors

If built-in collectors don't cover your needs:

```java
public class TestCoverageCollector implements ContextCollector {

    @Override
    public boolean isApplicable(ContextRequest request) {
        return request.getTaskType() == TaskType.TEST_GENERATION;
    }

    @Override
    public ContextFragment collect(ContextRequest request) {
        String filePath = (String) request.getParameter("file_path");

        // Your custom logic
        String coverageData = analyzeCoverage(filePath);

        return ContextFragment.builder()
            .source("TestCoverageCollector")
            .type(ContextType.DOMAIN_EXAMPLES)
            .content(coverageData)
            .addAspect("testing")
            .addAspect("coverage")
            .relevanceScore(0.75)
            .build();
    }

    @Override
    public CollectorMetadata getMetadata() {
        return new CollectorMetadata(
            "TestCoverageCollector",
            "Analyzes test coverage gaps",
            "1.0.0",
            CollectorMetadata.CollectorType.STATIC
        );
    }

    private String analyzeCoverage(String filePath) {
        // Your implementation
        return "Coverage analysis for: " + filePath;
    }
}

// Register it
contextEngine.registerCollector(new TestCoverageCollector());
```

## ⚡ Performance Tips

1. **Parallel Collection**: Collectors run in parallel automatically
2. **Caching**: Implement caching in expensive collectors
3. **Scope Control**: Start with smaller scopes (LOCAL) before going PROJECT-wide
4. **Budget Management**: Reserve 10-15% budget for high-priority fragments
5. **Async Usage**: Use `CompletableFuture` for non-blocking operations

```java
// Efficient parallel context gathering
List<CompletableFuture<String>> tasks = filePaths.stream()
    .map(path -> {
        ContextRequest req = buildRequest(path);
        return contextEngine.gatherContext(req)
            .thenApply(ctx -> processWithAI(ctx));
    })
    .collect(Collectors.toList());

CompletableFuture.allOf(tasks.toArray(new CompletableFuture[0]))
    .thenRun(() -> System.out.println("All tasks complete!"))
    .join();
```

## 🐛 Troubleshooting

**Q: Context relevance is too low**
A: Add more collectors or adjust `minRelevanceThreshold` in config

**Q: Running out of tokens**
A: Increase `reservedBudgetRatio` or reduce scope

**Q: Missing important context**
A: Check `ContextMetrics.coverage()` - add relevant collectors

**Q: Context is stale**
A: Set `contextMaxAge` in config to filter old data

## 📝 Complete Working Example

See [ContextEngineExample.java](src/main/java/com/evolver/context/examples/ContextEngineExample.java) for a complete runnable example.

Run it:
```bash
./gradlew runExample
```

## 🎯 Next Steps

1. ✅ Copy the setup code above
2. ✅ Choose collectors for your use case
3. ✅ Build your first `ContextRequest`
4. ✅ Monitor with `ContextMetrics`
5. ✅ Integrate with your AI model
6. ✅ Create custom collectors if needed

**You're now ready to give your AI agents optimal context!** 🚀
