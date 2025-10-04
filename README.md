# Evolver - Autonomous Context Engineering for AI Agents

**Zero-configuration context optimization. AI agents learn, evolve, and improve autonomously.**

---

## ⚡ Zero-Config Quick Start

### 👉 **[RUN_ME_FIRST.md](RUN_ME_FIRST.md)** ← Start Here!

**One command, zero configuration:**

```bash
./gradlew agentDemo
```

**That's it.** Watch AI agents auto-configure, self-learn, and evolve. [See what happens →](RUN_ME_FIRST.md)

### 📦 **[evolver-framework/](evolver-framework/)** ← Portable Package!

**Drop into any project:**

```bash
# Copy framework package into your project
cp -r evolver-framework/ /path/to/your/project/

# ONE line injection
FrameworkInjector.inject().withLearningDock("docs/").start();
```

**Zero disruption.** Agents learn your project and evolve the framework automatically. [See injection guide →](evolver-framework/INJECTION_GUIDE.md)

---

## 🤖 For AI Agents

**You are autonomous.** Read your manifesto:

**→ [AGENT_MANIFESTO.md](AGENT_MANIFESTO.md)** - Discover, learn, experiment, and evolve freely.

```bash
./gradlew agentBootstrap  # Auto-learn the framework
```

---

## 📚 Documentation Paths

### For Context Engineers (Humans)
- **[RUN_ME_FIRST.md](RUN_ME_FIRST.md)** ⭐ - See it work (1 minute)
- **[START_HERE.md](START_HERE.md)** - Zero-config usage (30 seconds)
- **[INJECTION_GUIDE.md](INJECTION_GUIDE.md)** 🔌 - Inject into existing projects (2 minutes)
- **[INJECTION_SUMMARY.md](INJECTION_SUMMARY.md)** 📋 - Quick injection reference
- **[GETTING_STARTED.md](GETTING_STARTED.md)** - Detailed integration (5 minutes)
- **[META_FRAMEWORK.md](META_FRAMEWORK.md)** - Advanced: Framework using itself
- **[ZERO_CONFIG_SUMMARY.md](ZERO_CONFIG_SUMMARY.md)** - Complete technical summary

### For AI Agents (Autonomous)
- **[AGENT_MANIFESTO.md](AGENT_MANIFESTO.md)** ⭐ - Your complete autonomy guide
- **[.agent/SEED.md](.agent/SEED.md)** - Your initial seed instructions
- **[.agent/experiences/](.agent/experiences/)** - Learn from other agents
- **[.agent/diaries/](.agent/diaries/)** - Your personal journal
- **Learning**: `./gradlew agentBootstrap`
- **Experimentation**: `./gradlew agentDemo`

---

## Overview

The Context Engineering Framework provides a systematic approach to:
- **Collect** context from multiple sources (code, documentation, runtime, VCS, etc.)
- **Filter** irrelevant or stale information
- **Prioritize** based on relevance and task type
- **Format** context optimally for AI consumption
- **Track** context quality with detailed metrics

## Key Features

### 🎯 Task-Aware Context Gathering
Different AI tasks require different context. The framework automatically adapts context collection based on task type:
- **Code Generation**: Structure, patterns, examples
- **Bug Fixing**: Errors, implementation, runtime state
- **Code Review**: Implementation, best practices, dependencies
- **Documentation**: Structure, comments, existing docs

### 🔍 Multi-Source Collection
Built-in collectors for:
- Code structure (AST analysis)
- Dependencies and imports
- Runtime errors and logs
- Documentation and comments
- Version control history
- Semantic code search

### 📊 Intelligent Prioritization
- Relevance-based scoring
- Token budget management
- Dependency resolution
- Recency weighting
- Focus area matching

### 🎨 Flexible Formatting
- Task-specific section ordering
- Custom templates support
- Markdown rendering
- Metadata inclusion

## Architecture

```
ContextEngine
    ├── ContextCollector (interface)
    │   ├── CodeStructureCollector
    │   ├── DependencyCollector
    │   ├── RuntimeErrorCollector
    │   ├── DocumentationCollector
    │   ├── VCSHistoryCollector
    │   └── SemanticSearchCollector
    │
    ├── ContextFilter
    │   ├── Relevance filtering
    │   ├── Deduplication
    │   └── Staleness checking
    │
    ├── ContextPrioritizer
    │   ├── Score calculation
    │   └── Budget-aware selection
    │
    └── ContextFormatter
        ├── Section grouping
        └── Template rendering
```

## Quick Start

### 1. Configure the Engine

```java
ContextConfig config = ContextConfig.builder()
    .minRelevanceThreshold(0.4)
    .reservedBudgetRatio(0.15)
    .addRequiredAspect("code")
    .addRequiredAspect("structure")
    .build();

ContextEngine engine = new ContextEngine(config);
```

### 2. Register Collectors

```java
engine.registerCollector(new CodeStructureCollector());
engine.registerCollector(new DependencyCollector());
engine.registerCollector(new RuntimeErrorCollector());
engine.registerCollector(new DocumentationCollector());
```

### 3. Create a Context Request

```java
ContextRequest request = ContextRequest.builder()
    .taskDescription("Generate a REST API endpoint for user authentication")
    .taskType(TaskType.CODE_GENERATION)
    .addFocusArea("authentication")
    .addFocusArea("rest_api")
    .addParameter("file_path", "src/UserController.java")
    .tokenBudget(8000)
    .scope(ContextScope.MODULE)
    .addPreferredType(ContextType.CODE_STRUCTURE)
    .addPreferredType(ContextType.DOMAIN_PATTERNS)
    .build();
```

### 4. Gather and Use Context

```java
CompletableFuture<ContextPackage> future = engine.gatherContext(request);

future.thenAccept(contextPackage -> {
    // Analyze quality
    ContextMetrics metrics = engine.analyzeContext(contextPackage);
    System.out.println("Relevance: " + metrics.getRelevanceScore());
    System.out.println("Coverage: " + metrics.getCoverage());

    // Render for AI
    String aiContext = contextPackage.render();

    // Send to AI agent...
});
```

## Context Types

The framework supports various context types:

| Type | Description | Use Cases |
|------|-------------|-----------|
| `CODE_STRUCTURE` | Class/method definitions | Generation, Documentation |
| `CODE_IMPLEMENTATION` | Actual code | Refactoring, Review |
| `CODE_DEPENDENCIES` | Imports, relationships | Analysis, Generation |
| `RUNTIME_ERRORS` | Error messages, traces | Bug fixing, Debugging |
| `RUNTIME_LOGS` | Application logs | Debugging, Analysis |
| `VCS_HISTORY` | Git history, blame | Review, Refactoring |
| `PROJECT_DOCUMENTATION` | README, docs | Documentation, Understanding |
| `DOMAIN_PATTERNS` | Design patterns | Generation, Best practices |
| `DOMAIN_EXAMPLES` | Example code | Generation, Learning |

## Task Types

Supported AI task types:

- **Code Tasks**: `CODE_GENERATION`, `CODE_COMPLETION`, `CODE_REFACTORING`
- **Analysis Tasks**: `CODE_REVIEW`, `BUG_DETECTION`, `PERFORMANCE_ANALYSIS`, `SECURITY_ANALYSIS`
- **Documentation Tasks**: `DOCUMENTATION`, `EXPLANATION`
- **Testing Tasks**: `TEST_GENERATION`, `TEST_DEBUGGING`
- **Debugging Tasks**: `BUG_FIXING`, `ERROR_DIAGNOSIS`
- **Design Tasks**: `DESIGN`, `ARCHITECTURE_REVIEW`

## Context Scopes

Control the breadth of context gathering:

- `MINIMAL`: Only immediate context (current method)
- `LOCAL`: Current file and direct dependencies
- `MODULE`: Current module/package
- `PROJECT`: Entire project
- `EXTENDED`: Project + external resources
- `GLOBAL`: Everything available

## Extending the Framework

### Create a Custom Collector

```java
public class MyCustomCollector implements ContextCollector {

    @Override
    public boolean isApplicable(ContextRequest request) {
        return request.getTaskType() == TaskType.MY_TASK;
    }

    @Override
    public ContextFragment collect(ContextRequest request) {
        // Your collection logic here
        return ContextFragment.builder()
            .source("MyCustomCollector")
            .type(ContextType.CUSTOM)
            .content(gatherMyContext())
            .relevanceScore(0.8)
            .build();
    }

    @Override
    public CollectorMetadata getMetadata() {
        return new CollectorMetadata(
            "MyCustomCollector",
            "Collects my custom context",
            "1.0.0",
            CollectorMetadata.CollectorType.HYBRID
        );
    }
}
```

### Add Custom Filters

```java
FilterRule customRule = (fragment, request) -> {
    // Your filtering logic
    return fragment.getRelevanceScore() > 0.5;
};

ContextConfig config = ContextConfig.builder()
    .addFilterRule(customRule)
    .build();
```

## Best Practices

1. **Set Appropriate Token Budgets**: Reserve 10-20% for essential high-priority fragments
2. **Use Task-Specific Types**: Prefer task-appropriate context types
3. **Define Focus Areas**: Help prioritization by specifying focus areas
4. **Monitor Metrics**: Track relevance and coverage to ensure quality
5. **Scope Wisely**: Start with smaller scopes and expand as needed
6. **Cache Expensive Collectors**: VCS and semantic search should cache results

## Examples

See [ContextEngineExample.java](src/main/java/com/evolver/context/examples/ContextEngineExample.java) for complete usage examples including:
- Code generation with optimal context
- Bug fixing with error-focused context
- Code review with security analysis context

## Testing

Run tests:
```bash
./gradlew test
```

See [ContextEngineTest.java](src/test/java/com/evolver/context/ContextEngineTest.java) for test examples.

## License

MIT License

## Contributing

Contributions welcome! Please ensure:
- New collectors implement `ContextCollector`
- Tests for new features
- Documentation updates
