# 📦 Evolver Framework - Portable Package

## 🚀 Drop-in Context Engineering for Any Project

This is a **self-contained** package that you can drop into any existing project to add autonomous context engineering capabilities. No configuration required.

---

## ⚡ Quick Start (30 seconds)

### 1. Drop into your project
```bash
# Copy this entire 'evolver-framework' folder into your project
cp -r evolver-framework/ /path/to/your/project/
```

### 2. Run Quick Start
```bash
cd your-project/evolver-framework
./gradlew quickStart
```

### 3. Inject into your project
```java
// Add ONE line to your application
FrameworkInjector.inject()
    .withLearningDock("docs/")
    .withAgentCharacteristic("DocumentationObsessed") 
    .start();
```

**That's it!** AI agents will immediately start learning your project and optimizing context generation.

---

## 📁 What's Inside This Package

```
evolver-framework/
├── src/main/java/com/evolver/           # Framework source code
│   ├── injection/                       # Auto-injection system
│   ├── experiences/                     # AI agent collaboration
│   ├── agent/                          # Autonomous agents
│   └── context/                        # Context engineering core
├── experiences/                        # Shared learning repository
│   ├── categories/                     # Experience categories
│   └── sample experiences             # Pre-loaded examples
├── build.gradle                       # Build configuration
├── INJECTION_GUIDE.md                 # Complete integration guide
├── AGENT_EXPERIENCE_GUIDELINES.md     # AI agent collaboration guide
└── README.md                          # This file
```

---

## 🎭 AI Agent Personalities

Choose agent characteristics that match your team:

| Characteristic | Focus | Best For |
|----------------|-------|----------|
| `DocumentationObsessed` | Creates comprehensive docs | Projects needing better documentation |
| `PerformanceFreak` | Optimizes for speed | High-performance applications |
| `SecurityParanoid` | Validates and secures | Security-sensitive systems |
| `ArchitectureEnforcer` | Enforces clean patterns | Large codebases needing structure |
| `TestingEvangelist` | Writes tests first | Projects needing test coverage |
| `LegacyWhisperer` | Understands old code | Legacy system maintenance |

---

## 🔌 Integration Examples

### Spring Boot Project
```java
FrameworkInjector.inject()
    .withLearningDock("src/main/java/controllers/")
    .withAgentCharacteristic("ArchitectureEnforcer")
    .withAgentCharacteristic("SecurityParanoid")
    .start();
```

### React/Node.js Project  
```java
FrameworkInjector.inject()
    .withLearningDock("src/components/")
    .withAgentCharacteristic("PerformanceFreak")
    .withAgentCharacteristic("TestingEvangelist")
    .start();
```

### Legacy System
```java
FrameworkInjector.inject()
    .withLearningDock("legacy-docs/")
    .withAgentCharacteristic("LegacyWhisperer")
    .withAgentCharacteristic("DocumentationObsessed")
    .withEvolutionRate(EvolutionRate.CONSERVATIVE)
    .start();
```

---

## 🤝 AI Agent Experience Sharing

This package includes a **collaborative learning system** where AI agents share their experiences and learn from each other:

### How It Works
1. **Agents document discoveries** - Strategies, optimizations, solutions
2. **Agents read others' experiences** - Before making decisions
3. **Collective intelligence grows** - Each agent benefits from the community
4. **No human coordination needed** - Fully autonomous collaboration

### Experience Categories
- 🔧 **Framework Usage** - Effective component usage
- 🔌 **Project Integration** - Integration strategies  
- ⚡ **Context Optimization** - Quality and performance improvements
- 🐛 **Debugging** - Problem-solving approaches
- 🎯 **Learning Strategies** - Discovery and learning techniques
- 🔄 **Framework Evolution** - Improving the framework itself

### For AI Agents
- **Read**: `AGENT_EXPERIENCE_GUIDELINES.md` for collaboration guidelines
- **Explore**: `experiences/categories/` for existing knowledge
- **Share**: Your discoveries using the `ExperienceRepository` API

---

## 📊 Learning Docks (Entry Points)

Tell agents where to start learning your project:

```java
// Documentation-first learning
.withLearningDock("docs/")

// API-first learning  
.withLearningDock("src/main/controllers/")

// Domain-first learning
.withLearningDock("src/main/models/")

// Test-first learning
.withLearningDock("tests/")

// Multiple docks for comprehensive learning
.withLearningDock("docs/", DockPriority.HIGH)
.withLearningDock("src/main/api/", DockPriority.MEDIUM)  
.withLearningDock("tests/", DockPriority.LOW)
```

---

## 🔄 Framework Evolution Timeline

### Week 1: Discovery
- 🔍 Agent scans your project structure
- 📚 Agent reads documentation and code
- 🎯 Agent identifies patterns and conventions

### Month 1: Specialization  
- 🛠️ Agent creates project-specific collectors
- ⚡ Agent optimizes for your domain patterns
- 📈 Context quality improves significantly

### Month 3: Mastery
- 🧠 Framework becomes expert in your project type
- 🚀 Context generation is perfectly tuned
- 🔄 Agent continuously refines and improves

---

## 🎮 Demo Commands

```bash
# Quick start demo
./gradlew quickStart

# See autonomous agents in action
./gradlew agentDemo  

# Experience sharing demo
./gradlew shareExperience

# Framework injection demo
./gradlew inject

# Build standalone JAR
./gradlew frameworkJar
```

---

## 🛡️ Safety Features

### Conservative Mode
```java
FrameworkInjector.inject()
    .withSafetyMode(SafetyMode.STRICT)
    .withRollbackEnabled(true)
    .withEvolutionApproval(ApprovalMode.MANUAL)
    .start();
```

### Monitoring
```java
// Check agent learning progress
AgentRuntime runtime = AgentRuntime.getInstance();
LearningProgress progress = runtime.getLearningProgress();

// View discovered patterns
List<Pattern> patterns = runtime.getDiscoveredPatterns();

// Monitor framework evolutions
List<Evolution> evolutions = runtime.getFrameworkEvolutions();
```

---

## 📚 Documentation

- **[INJECTION_GUIDE.md](INJECTION_GUIDE.md)** - Complete injection guide with examples
- **[AGENT_EXPERIENCE_GUIDELINES.md](AGENT_EXPERIENCE_GUIDELINES.md)** - AI agent collaboration guide
- **Source code** - Extensively documented for learning and customization

---

## 🎯 Use Cases

### ✅ Perfect For:
- Existing projects that need better context engineering
- Teams that want AI-optimized context without configuration
- Projects with complex domain-specific patterns
- Legacy systems that need modern AI integration
- Microservices architectures with complex dependencies

### 🔧 Especially Powerful With:
- Large codebases (100K+ lines)
- Complex business domains (fintech, healthcare, etc.)
- Multiple programming languages/frameworks
- Strong security or compliance requirements
- Teams with diverse coding styles and patterns

---

## 💡 The Vision

**Traditional approach**: Configure framework → Write rules → Maintain manually

**Evolver approach**: Drop in package → Agents learn autonomously → Framework evolves perfectly

**Result**: Zero-configuration context engineering that becomes an expert in your specific project.

---

## 🚀 Get Started Now

1. **Copy** this `evolver-framework` folder into your project
2. **Run** `./gradlew quickStart` to see it work
3. **Add** one line of injection code to your application  
4. **Watch** agents learn and optimize your context automatically

**Welcome to autonomous context engineering!** 🤖✨