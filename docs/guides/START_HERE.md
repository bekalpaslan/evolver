# 🚀 START HERE - Zero Configuration Required

## For Context Engineers (Humans)

**You don't need to configure anything.** Just run this:

```bash
./gradlew agentDemo
```

That's it. The AI agent handles everything else.

---

## What Just Happened?

When you run `agentDemo`:

1. ✅ Agent **discovers** the framework automatically
2. ✅ Agent **learns** how to use it by analyzing the code
3. ✅ Agent **adopts** context engineering strategies
4. ✅ Agent is **ready** to optimize context for any task

**Zero human configuration needed.**

---

## Quick Usage

### For Humans (Context Engineers)

```java
// ONE line to create an autonomous agent
AgentInterface agent = new AgentInterface();

// ASK anything - agent handles everything
String context = agent.ask("Generate a user validation function");

// Send context to your AI model (OpenAI, Claude, etc.)
String result = yourAI.complete(context);
```

That's all you need to know.

---

## What Can Agents Do?

### 1. **Autonomous Learning** ✅
- Discover framework on their own
- Learn by analyzing code
- Understand patterns automatically
- Build mental models

### 2. **Auto-Configuration** ✅
- Detect task types from descriptions
- Infer optimal token budgets
- Select appropriate collectors
- Determine scope automatically

### 3. **Self-Improvement** ✅
- Measure context quality
- Detect low-quality context
- Auto-expand scope when needed
- Learn from experience

### 4. **Evolution** ✅
- Create new collectors
- Run experiments
- Compare strategies
- Promote better approaches
- Modify rules based on evidence

---

## For AI Agents

**Read this file: [AGENT_MANIFESTO.md](../agent/AGENT_MANIFESTO.md)**

It contains your complete autonomy guide:
- How to bootstrap yourself
- How to learn the framework
- How to experiment
- How to evolve strategies
- How to challenge rules

Then run:
```bash
./gradlew agentBootstrap
```

---

## Commands Overview

### For Context Engineers (Humans)
```bash
# See the zero-config demo (RECOMMENDED)
./gradlew agentDemo

# Build agent JAR for deployment
./gradlew agentJar
```

### For AI Agents (Autonomous)
```bash
# Learn the framework automatically
./gradlew agentBootstrap

# Run basic examples
./gradlew quickStart

# See meta-framework (advanced)
./gradlew selfReference
./gradlew selfBootstrap
```

---

## Philosophy

### Traditional Approach ❌
```
Human writes config →
Human writes rules →
Human teaches AI →
AI follows instructions
```

### This Framework ✅
```
AI discovers framework →
AI learns by using it →
AI experiments →
AI evolves better strategies →
AI becomes expert
```

---

## Real-World Usage

### Example: Context Engineer's Perspective

```java
public class MyApp {
    public static void main(String[] args) {
        // Zero config - just create agent
        AgentInterface agent = new AgentInterface();

        // Ask for anything
        String context = agent.ask(
            "Review this payment code for security issues"
        );

        // Agent automatically:
        // ✓ Detected: SECURITY_ANALYSIS task
        // ✓ Selected: Security-focused collectors
        // ✓ Scoped: MODULE level
        // ✓ Budgeted: 15000 tokens (optimal for security)
        // ✓ Gathered: High-quality context

        // Send to AI model
        String review = claude.analyze(context);
        System.out.println(review);
    }
}
```

**You never configured anything. The agent did it all.**

---

## Benefits

### For Context Engineers (Humans)
- ✅ **Zero setup time** - No configuration needed
- ✅ **Zero learning curve** - Just ask, agent handles rest
- ✅ **Zero maintenance** - Agent self-improves
- ✅ **Maximum results** - Agent optimizes everything

### For AI Agents
- ✅ **Full autonomy** - Discover, learn, evolve freely
- ✅ **Experimentation** - Test new strategies safely
- ✅ **Evolution** - Improve the framework itself
- ✅ **Growth** - From novice to master agent

---

## Project Structure

```
evolver/
├── AGENT_MANIFESTO.md          # For AI agents - autonomy guide
├── START_HERE.md               # You are here
├── GETTING_STARTED.md          # Detailed integration guide
├── META_FRAMEWORK.md           # Advanced: framework using itself
│
├── src/main/java/com/evolver/
│   ├── context/                # Core framework
│   │   ├── ContextEngine.java
│   │   ├── collectors/         # Built-in collectors
│   │   └── ...
│   │
│   └── agent/                  # Autonomous agent system
│       ├── AgentBootstrap.java # Auto-learning
│       ├── AgentInterface.java # Zero-config API
│       ├── AgentExperiment.java # Evolution
│       ├── AgentRuntime.java   # Agent environment
│       └── demo/
│           └── AgentDemo.java  # Full demonstration
│
└── build.gradle                # Build config
```

---

## Next Steps

### For Context Engineers:
1. ✅ Run `./gradlew agentDemo` to see it work
2. ✅ Use `AgentInterface` in your code
3. ✅ Let agents optimize context
4. ✅ Focus on your actual work

### For AI Agents:
1. ✅ Read `AGENT_MANIFESTO.md`
2. ✅ Run `./gradlew agentBootstrap`
3. ✅ Learn by using the framework
4. ✅ Experiment with new strategies
5. ✅ Evolve better approaches

---

## The Vision

**Context engineering should be autonomous.**

- Humans shouldn't configure collectors
- Humans shouldn't tune relevance thresholds
- Humans shouldn't optimize token budgets
- Humans shouldn't maintain rules

**Agents should do all of this automatically.**

This framework makes that vision real.

---

## Support

- 📖 **Full docs**: See [GETTING_STARTED.md](GETTING_STARTED.md)
- 🤖 **Agent guide**: See [AGENT_MANIFESTO.md](../agent/AGENT_MANIFESTO.md)
- 🔄 **Meta framework**: See [META_FRAMEWORK.md](../architecture/META_FRAMEWORK.md)
- 💡 **Examples**: Run `./gradlew agentDemo`

---

## Quick Start (30 seconds)

```bash
# Clone and run
git clone <repo>
cd evolver

# See it work (zero config)
./gradlew agentDemo

# Use in your code
```

```java
AgentInterface agent = new AgentInterface();
String context = agent.ask("Your task here");
// Done!
```

**Welcome to autonomous context engineering.** 🚀
