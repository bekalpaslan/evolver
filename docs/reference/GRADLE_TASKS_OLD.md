# Evolver Framework - Gradle Tasks Documentation

## Overview

This document provides comprehensive documentation for all Gradle tasks created for the Evolver Framework. The framework includes autonomous context engineering capabilities with multiple task types for different phases of operation.

---

## 📋 **Task Categories**

### 🤖 **Agent Tasks** (`group = 'agent'`)
Tasks for autonomous agent operations and learning.

### 💉 **Injection Tasks** (`group = 'injection'`)  
Tasks for injecting the framework into projects.

### 🏗️ **Application Tasks** (`group = 'application'`)
Standard application lifecycle tasks.

---

## 🎯 **Core Tasks**

### `bootstrap`
**Group:** `agent`  
**Description:** Bootstrap - Agent learns framework automatically  
**Main Class:** `com.evolver.agent.AgentBootstrap`

**Purpose:**
- Initiates the 4-phase autonomous learning sequence
- Agent discovers framework components automatically
- Establishes baseline performance metrics
- Records learning experiences to database
- Graduates agent for autonomous operation

**Usage:**
```bash
gradle bootstrap
# or
./gradlew bootstrap
```

**What It Does:**
1. **Phase 1 - Discovery:** Scans and understands framework architecture
2. **Phase 2 - Learning:** Uses framework on itself to learn patterns
3. **Phase 3 - Adoption:** Creates practical usage scenarios
4. **Phase 4 - Evolution:** Enables rule challenges and improvements

**Output:**
- Console logging with agent personalities and emojis
- Experience database updates (`experiences.json`)
- Learning diary entries (`.agent/diaries/`)
- Graduation status and capabilities unlocked

---

### `inject`
**Group:** `injection`  
**Description:** Inject framework into current project  
**Main Class:** `com.evolver.injection.InjectionRunner`

**Purpose:**
- Applies Evolver framework to external projects
- Spawns autonomous agents for project analysis
- Creates learning docks for project-specific optimization
- Enables continuous improvement for target codebase

**Usage:**
```bash
gradle inject --args="<project_path>"
# Example:
gradle inject --args="C:\\Users\\alpas\\IdeaProjects\\fbtahmin"
```

**Parameters:**
- `<project_path>`: Absolute path to the target project directory

**What It Does:**
1. Scans target project for existing frameworks
2. Detects project type (Maven, Gradle, etc.)
3. Creates learning dock in evolver-dock directory
4. Spawns multiple autonomous agents (DocBot, CleanFreak, SpeedDemon, MadScientist)
5. Begins continuous learning and optimization

**Output:**
- Multiple active autonomous agents
- Project-specific learning data
- Continuous optimization suggestions
- Framework integration status

---

### `cli`
**Group:** `agent`  
**Description:** Evolver CLI for privacy controls and configuration  
**Main Class:** `com.evolver.cli.EvolverCLI`

**Purpose:**
- Provides command-line interface for framework configuration
- Manages privacy settings for technology intelligence
- Handles experience database operations
- Shows system status and metrics

**Usage:**
```bash
gradle cli --args="<command>"

# Available commands:
gradle cli --args="status"                    # Show current status
gradle cli --args="config --disable-technology-intelligence"
gradle cli --args="config --technology-intelligence-local-only"
gradle cli --args="export --technology-experiences ./my-data.json"
gradle cli --args="purge --technology-experiences --confirm"
```

**Commands:**

#### `status`
Shows current framework status including:
- Privacy settings (Technology Intelligence, Community Sharing)
- Experience database metrics (size, location, count)
- Available configuration commands

#### `config`
- `--disable-technology-intelligence`: Disables technology learning
- `--technology-intelligence-local-only`: Keeps learning local only

#### `export`
- `--technology-experiences <path>`: Exports experience database to specified file

#### `purge`
- `--technology-experiences --confirm`: Removes technology experiences (requires confirmation)

---

## 🚀 **Standard Application Tasks**

### `run`
**Group:** `application`  
**Description:** Runs the main application  
**Main Class:** `com.evolver.agent.AgentBootstrap`

**Usage:**
```bash
gradle run
# or with arguments:
gradle run --args="com.evolver.context.examples.QuickStartExample"
```

---

## 📋 **Referenced Tasks** (from documentation)

These tasks are mentioned in documentation but may not be currently implemented:

### `agentDemo`
**Status:** Referenced in README.md  
**Purpose:** Demonstrates zero-config framework usage

### `agentFirstTask`  
**Status:** Suggested by bootstrap output but not implemented
**Purpose:** Guided first task for new users

### `agentExperiment`
**Status:** Suggested by bootstrap output but not implemented  
**Purpose:** Run your first experiment

### `agentAutonomous`
**Status:** Suggested by bootstrap output but not implemented
**Purpose:** Full autonomy mode

---

## 💡 **Task Usage Patterns**

### 🔄 **Typical Workflow**

1. **Initial Setup:**
   ```bash
   gradle bootstrap
   ```

2. **Apply to Project:**
   ```bash
   gradle inject --args="<your_project_path>"
   ```

3. **Monitor Status:**
   ```bash
   gradle cli --args="status"
   ```

4. **Export Learning:**
   ```bash
   gradle cli --args="export --technology-experiences ./backup.json"
   ```

### 🛠️ **Development Workflow**

1. **Run Examples:**
   ```bash
   gradle run --args="com.evolver.context.examples.QuickStartExample"
   gradle run --args="com.evolver.context.examples.ContextEngineExample"
   ```

2. **Test Framework:**
   ```bash
   gradle test
   ```

3. **Build Distribution:**
   ```bash
   gradle build
   ```

---

## 🔧 **Task Implementation Details**

### Task Configuration
All custom tasks are configured with:
- **Classpath:** `sourceSets.main.runtimeClasspath`
- **Encoding:** UTF-8 for Java compilation
- **Java Version:** 17

### Argument Handling
Tasks that accept arguments use:
```gradle
if (project.hasProperty('args')) {
    args project.property('args').split(' ')
}
```

### Groups and Descriptions
- **agent**: Core autonomous agent functionality
- **injection**: Framework injection and integration  
- **application**: Standard application tasks

---

## 📊 **Experience Tracking**

All tasks that involve learning contribute to:
- **Experience Database:** `experiences.json`
- **Categories:** build-tools, framework-usage, graduation, rule-challenges
- **Metrics:** Confidence scores, performance improvements, rule challenges

---

## 🚀 **Advanced Usage**

### Custom Task Creation
To add new agent tasks, follow the pattern:
```gradle
tasks.register('customTask', JavaExec) {
    group = 'agent'
    description = 'Your custom agent task'
    classpath = sourceSets.main.runtimeClasspath
    mainClass = 'com.evolver.your.MainClass'
}
```

### Framework Extension
The framework supports:
- Custom collectors implementation
- Rule challenge system
- Experimental comparison
- Autonomous strategy evolution

---

## 📋 **Summary Table**

| Task | Group | Status | Purpose |
|------|-------|--------|---------|
| `bootstrap` | agent | ✅ Implemented | Autonomous framework learning |
| `inject` | injection | ✅ Implemented | Project framework injection |
| `cli` | agent | ✅ Implemented | CLI configuration interface |
| `run` | application | ✅ Implemented | Run main application |
| `agentDemo` | agent | 📝 Documented | Zero-config demonstration |
| `agentFirstTask` | agent | 💭 Suggested | Guided first task |
| `agentExperiment` | agent | 💭 Suggested | First experiment runner |
| `agentAutonomous` | agent | 💭 Suggested | Full autonomy mode |

---

## 📖 **Related Documentation**

- **Framework Overview:** `README.md`
- **Quick Start:** `QUICK_START.md`
- **Architecture:** `docs/architecture/`
- **Experience Flow:** `EXPERIENCE_FLOW.md`
- **Agent Guidelines:** `docs/agent/AGENT_MANIFESTO.md`

---

*Generated for Evolver Framework v1.0.0 - Autonomous Context Engineering*