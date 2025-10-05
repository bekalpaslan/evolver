# 🚀 Quick Start Guide

> **📍 Location**: Moved from root to `docs/guides/QUICK_START.md` for better organization.

## ⚡ **30-Second Start**

```bash
# Clone the repository
git clone https://github.com/bekalpaslan/evolver.git
cd evolver

# Start the autonomous agent (it teaches itself to you!)
gradle bootstrap
```

**That's it!** The framework will autonomously:
- ✅ Learn its own architecture
- ✅ Demonstrate capabilities  
- ✅ Set up the experience database
- ✅ Show you how to use it

---

## 🎯 **What Happens During Bootstrap?**

### Phase 1: Discovery (15 seconds)
The agent discovers and maps the framework components:
- Context Engineering Framework
- Built-in collectors (CodeStructure, Dependency, RuntimeError, etc.)
- Agent capabilities and extension points

### Phase 2: Learning (30 seconds)  
The agent learns by using the framework on itself:
- Gathers context about its own architecture
- Tests context collection and filtering
- Builds internal knowledge base

### Phase 3: Adoption (15 seconds)
The agent demonstrates practical usage:
- Creates sample context requests
- Shows context gathering in action
- Validates framework operations

### Phase 4: Evolution (10 seconds)
The agent activates autonomous capabilities:
- Demonstrates rule challenge system
- Records high-quality experiences
- Enables full autonomy mode

---

## 🔄 **Alternative Startup Methods**

### Option 1: Direct JAR Execution
```bash
# Build first
gradle build

# Run the agent bootstrap
java -cp build/libs/evolver-1.0.0.jar com.evolver.agent.AgentBootstrap
```

### Option 2: CLI Mode
```bash
# Access the command-line interface
gradle cli --args="help"

# Check framework status
gradle cli --args="status"
```

### Option 3: Injection Mode
```bash
# Inject framework into existing project
gradle inject
```

---

## 📊 **Next Steps After Bootstrap**

### 1. Explore the Experience Database
```bash
# Check what the agent learned
gradle dbStats

# View detailed quality report
gradle experienceReport

# See agent accountability
gradle agentAccountability
```

### 2. Try Framework Operations
```bash
# Validate all experiences
gradle validateExperiences

# Test quality enforcement
gradle testDirtyDataPrevention

# Generate API documentation
gradle javadoc
```

### 3. Create Your Own Experiences
```java
// The agent automatically tracks experiences with:
// - Hashed IP-based agent identity
// - Model information  
// - Strict quality validation
// - Real-time quality scoring

ExperienceRepository.record()
    .category("performance-optimization")
    .technology("Spring Boot", "3.2.0", "web-framework")
    .recommendation("Your actionable insights here...")
    .rating("performance", 9.2)
    .save(); // Automatically validated!
```

---

## 🛠️ **Development Mode**

### For Framework Contributors
```bash
# Run all tests
gradle test

# Build with documentation
gradle build javadoc

# Test quality enforcement
gradle testDirtyDataPrevention

# Clean up database if needed
gradle cleanExperiences
```

### For Framework Users
```bash
# Just bootstrap and go!
gradle bootstrap

# Regular maintenance (optional)
gradle experienceReport  # Monthly
gradle dbStats           # Weekly
```

---

## 🚨 **Troubleshooting**

### If Bootstrap Fails
```bash
# Check Java version (requires 11+)
java -version

# Clean and rebuild
gradle clean build

# Try direct execution
gradle --info bootstrap
```

### If Database Issues
```bash
# Validate database integrity
gradle dbValidate

# Check for quality issues
gradle validateExperiences

# Migrate if needed
gradle dbMigrate
```

### If Quality Validation Fails
```bash
# See what's wrong
gradle experienceReport

# Clean problematic data
gradle cleanExperiences

# Test the validator
gradle testDirtyDataPrevention
```

---

## 📚 **Learn More**

- **📖 Architecture**: [`docs/architecture/`](../architecture/)
- **🤖 Agent Guide**: [`docs/agent/AGENT_MANIFESTO.md`](../agent/AGENT_MANIFESTO.md)
- **🔧 All Tasks**: [`docs/reference/GRADLE_TASKS.md`](../reference/GRADLE_TASKS.md)
- **📊 Experience Flow**: [`docs/agent/EXPERIENCE_FLOW.md`](../agent/EXPERIENCE_FLOW.md)

---

## 💡 **Key Features Unlocked**

After bootstrap, you get:

- 🤖 **Autonomous Agent**: Self-learning, self-improving AI agent
- 🗄️ **Shared Database**: High-quality experience sharing across agents
- 🔒 **Quality Control**: Zero-tolerance for dirty data
- 📊 **Agent Accountability**: Secure tracking without privacy violation  
- 🔧 **Rich Tooling**: 15+ Gradle tasks for every operation
- 📚 **Auto-Documentation**: Always-synchronized documentation

**Welcome to autonomous AI development!** 🚀