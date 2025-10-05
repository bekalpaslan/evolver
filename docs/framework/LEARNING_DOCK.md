# 🚢 LearningDock - Agent Training Environment

## Overview

**LearningDock** is a specialized environment where AI agents discover, learn, and evolve within your project. It serves as the entry point for agent-driven framework evolution, enabling agents to understand your codebase and adapt the Evolver framework to your specific needs.

## 🎯 Purpose

LearningDock enables:
- **Project Discovery**: Agents analyze your project structure and patterns
- **Domain Learning**: Agents understand your business domain and conventions
- **Framework Evolution**: Agents adapt the framework to your specific project
- **Context Optimization**: Agents build project-aware context generation

## 🏗️ Architecture

```
evolver-dock/
├── agents/          # Agent workspace and state
├── analysis/        # Project structure analysis
├── context/         # Generated context patterns
├── learning/        # Learning progress and insights
├── evolution/       # Framework evolution tracking
├── dock-config.json # Dock configuration
├── LEARNING_GUIDE.md # Agent instructions
├── project-structure.md # Analyzed project structure
└── entry-point-mappings.md # Discovery routes
```

## 🚀 Quick Start

### Basic Usage

```java
import com.evolver.injection.LearningDock;
import com.evolver.injection.ProjectType;
import com.evolver.injection.FrameworkType;

// Create a learning dock
LearningDock dock = new LearningDock(
    "my-project-dock",
    "/path/to/project",
    ProjectType.GRADLE_JAVA,
    FrameworkType.SPRING_BOOT
);

// Materialize the dock
dock.materialize();

// Start agent learning
dock.startLearning();
```

### With Custom Entry Points

```java
LearningDock dock = new LearningDock(
    "custom-dock",
    "/path/to/project",
    ProjectType.GRADLE_JAVA,
    FrameworkType.SPRING_BOOT
)
.withEntryPoint("src/main/controllers/")
.withEntryPoint("src/main/services/")
.withEntryPoints("docs/api/", "config/");

dock.materialize();
dock.startLearning();
```

## 📍 Entry Points

Entry points guide where agents start their discovery process. Different entry points lead to different learning focuses:

### Documentation-First Discovery
```java
.withEntryPoint("docs/")
.withEntryPoint("README.md")
```
**Agent Focus**: Learn project purpose, domain knowledge, and conventions

### API-First Discovery
```java
.withEntryPoint("src/main/controllers/")
.withEntryPoint("src/main/api/")
```
**Agent Focus**: Understand API patterns, request/response structures

### Domain-First Discovery
```java
.withEntryPoint("src/main/models/")
.withEntryPoint("src/main/entities/")
```
**Agent Focus**: Learn business domain and data structures

### Test-First Discovery
```java
.withEntryPoint("src/test/")
.withEntryPoint("tests/")
```
**Agent Focus**: Understand through test patterns and examples

### Configuration-First Discovery
```java
.withEntryPoint("config/")
.withEntryPoint("application.properties")
```
**Agent Focus**: Learn deployment and runtime patterns

## 🔧 Project Type Support

LearningDock automatically configures entry points based on detected project types:

### Java Projects
- **GRADLE_JAVA**: `src/main/java`, `src/test/java`, `build.gradle`
- **MAVEN_JAVA**: `src/main/java`, `src/test/java`, `pom.xml`
- **GRADLE_KOTLIN**: `src/main/kotlin`, `src/test/kotlin`, `build.gradle.kts`

### JavaScript/TypeScript Projects
- **NODE_JS**: `src/`, `package.json`, `tests/`
- **REACT**: `src/`, `package.json`, `public/`, `tests/`
- **VUE**: `src/`, `package.json`, `components/`, `tests/`
- **ANGULAR**: `src/`, `package.json`, `angular.json`, `tests/`

### Python Projects
- **PYTHON_PIP**: `src/`, `requirements.txt`, `tests/`
- **PYTHON_POETRY**: `src/`, `pyproject.toml`, `tests/`
- **PYTHON_SETUPTOOLS**: `src/`, `setup.py`, `tests/`

### .NET Projects
- **DOTNET_CSHARP**: `src/`, `*.csproj`, `tests/`
- **DOTNET_SOLUTION**: `src/`, `*.sln`, `tests/`

## 🤖 Agent Learning Process

1. **Discovery Phase**
   - Analyze entry points
   - Map project structure
   - Identify patterns and conventions

2. **Learning Phase**
   - Extract domain knowledge
   - Understand code patterns
   - Analyze dependencies and frameworks

3. **Collection Phase**
   - Build specialized context collectors
   - Create project-aware generators
   - Develop domain-specific understanding

4. **Evolution Phase**
   - Adapt framework to project needs
   - Optimize for specific use cases
   - Continuous improvement

## 📊 Generated Analysis

LearningDock creates comprehensive project analysis:

### dock-config.json
```json
{
  "dockName": "my-project-dock",
  "projectType": "GRADLE_JAVA",
  "frameworkType": "SPRING_BOOT",
  "entryPoints": [
    "src/main/java",
    "src/test/java",
    "build.gradle"
  ],
  "learningPaths": [
    "Java source code patterns",
    "Build configuration",
    "Test patterns"
  ],
  "initialized": true,
  "version": "1.0.0"
}
```

### LEARNING_GUIDE.md
Comprehensive guide for agents including:
- Project overview and structure
- Entry points for discovery
- Learning paths and goals
- Evolution objectives

### Project Structure Analysis
- Directory hierarchy
- File counts and sizes
- Key component identification
- Pattern recognition

### Entry Point Mappings
- Purpose of each entry point
- Agent focus areas
- Discovery recommendations

## 🎨 Integration with FrameworkInjector

LearningDock integrates seamlessly with FrameworkInjector:

```java
FrameworkInjector.inject()
    .detectExistingFramework()
    .createLearningDock("main")
    .spawnAgent(AgentCharacteristic.DOCUMENTATION_OBSESSIVE)
    .activate();
```

The injector automatically:
1. Detects project type and framework
2. Creates appropriate LearningDock
3. Sets default entry points
4. Spawns agents for learning

## 🔄 Evolution Tracking

LearningDock tracks framework evolution over time:

### Week 1: Initial Discovery
- Project structure mapped
- Basic patterns identified
- Entry points analyzed

### Month 1: Specialized Learning
- Domain knowledge extracted
- Custom collectors created
- Framework adaptation begins

### Month 3: Expert Evolution
- Project-specific framework variant
- Optimized context generation
- Domain-aware agents

### Ongoing: Continuous Improvement
- Pattern refinement
- Performance optimization
- New capability development

## 🛡️ Safety and Isolation

LearningDock provides safe agent operation:

- **Read-Only Discovery**: Agents only read project files
- **Isolated Environment**: All agent work contained in `evolver-dock/`
- **Version Control Safe**: Dock directory can be gitignored
- **Rollback Capability**: Easy to remove and recreate

## 📈 Monitoring Progress

Track agent learning progress:

```java
// Check dock status
if (dock.isInitialized()) {
    System.out.println("Dock ready for agent learning");
}

// Review entry points
List<String> entryPoints = dock.getEntryPoints();
System.out.println("Discovery routes: " + entryPoints.size());

// Check learning paths
List<String> learningPaths = dock.getLearningPaths();
System.out.println("Learning areas: " + learningPaths.size());
```

## 🎯 Best Practices

### Entry Point Selection
- Start with documentation for domain understanding
- Include tests for behavior examples
- Add configuration for operational insights
- Use multiple entry points for comprehensive learning

### Project Organization
- Keep docs/ updated for better agent learning
- Maintain clear project structure
- Document conventions and patterns
- Provide examples and test cases

### Evolution Management
- Review agent learning progress regularly
- Validate generated insights
- Guide agent focus with custom entry points
- Monitor framework adaptation

## 🚨 Troubleshooting

### Common Issues

**Dock Not Materializing**
- Check write permissions in project directory
- Ensure project root path is correct
- Verify ProjectType and FrameworkType are valid

**Missing Entry Points**
- Entry points are suggestions, not requirements
- Agents adapt to available project structure
- Add custom entry points as needed

**Slow Learning Progress**
- Add more specific entry points
- Ensure documentation is available
- Check that key project files exist

### Debug Information

```java
// Enable detailed logging
dock.setVerbose(true);

// Check dock configuration
String entryPoint = dock.getEntryPoint();
System.out.println("Dock location: " + entryPoint);

// Verify project structure
dock.analyzeProjectStructure();
```

## 📚 Related Documentation

- [Framework Injection Guide](INJECTION_GUIDE.md) - Complete injection process
- [Agent Experience Guidelines](../agent/AGENT_EXPERIENCE_GUIDELINES.md) - Agent collaboration
- [Project Type Detection](PROJECT_TYPE_DETECTION.md) - Automatic detection
- [Framework Evolution](FRAMEWORK_EVOLUTION.md) - Evolution process

## 🎉 Success Stories

### E-commerce Platform
Agent learned shopping cart patterns, payment flows, and inventory management. Framework evolved to provide e-commerce-specific context generation with 300% improved relevance.

### Microservices Architecture
Agent discovered service boundaries, API contracts, and deployment patterns. Framework adapted to understand distributed systems with specialized service context collectors.

### Data Pipeline Project
Agent learned data transformation patterns, validation rules, and pipeline stages. Framework evolved domain-specific data processing context with error handling patterns.

## 🔗 Quick Links

- [FrameworkInjector API](FrameworkInjector.md)
- [Agent Characteristics](../agent/AGENT_CHARACTERISTICS.md)
- [Context Engineering](../architecture/CONTEXT_ENGINEERING.md)
- [Evolution Tracking](EVOLUTION_TRACKING.md)

---

*LearningDock enables AI agents to become true partners in your development process, learning your project deeply and evolving to provide increasingly valuable assistance.*