# 🎬 RUN ME FIRST

## One Command to See Everything

```bash
./gradlew agentDemo
```

That's it. Watch the magic happen.

---

## What You'll See

### 1. Agent Auto-Bootstrap (3 seconds)
```
🤖 Autonomous Agent Initializing...
📚 Agent learning framework...
✓ Framework learned (47 concepts)
✅ Agent ready. Ask me anything.
```

### 2. Zero-Config Usage (Instant)
```
Context Engineer asks: "Generate a user validation function"

Agent automatically:
✓ Detected task type: CODE_GENERATION
✓ Selected optimal collectors
✓ Gathered relevant context
✓ Context ready for AI model
```

### 3. Auto-Detection Demo (Instant)
```
Context Engineer asks: "Fix the NullPointerException in authentication"

Agent automatically:
✓ Detected: BUG_FIXING task
✓ Inferred focus: error_handling, authentication
✓ Selected: RuntimeErrorCollector, CodeStructureCollector
✓ Scoped: MODULE level
✓ Budgeted: 10000 tokens
```

### 4. Self-Improvement Demo (2 seconds)
```
Agent learning cycle:
1. ✓ Detected: SECURITY_ANALYSIS task
2. ✓ Gathered initial context
3. ✓ Measured quality: relevance = 0.45 (LOW)
4. ✓ AUTO-IMPROVED: Expanded scope, added collectors
5. ✓ Re-gathered: relevance = 0.82 (HIGH)
6. ✓ Learned: Security tasks need broader scope
```

### 5. Evolution Demo (5 seconds)
```
🧪 EXPERIMENT: Agent tests new strategy
Hypothesis: 'Semantic search outperforms keyword search'

📊 RESULTS:
  ✓ relevance   : Baseline=0.65  Variant=0.85  ↑ 30.8%
  ✓ speed       : Baseline=5.2   Variant=4.8   ↑ 7.7%
  ✓ coverage    : Baseline=3.0   Variant=4.5   ↑ 50.0%

🎉 Experiment successful!
🚀 PROMOTING VARIANT TO PRODUCTION
✓ Variant is now the default
```

---

## What This Proves

✅ **Zero Config** - No setup needed by humans
✅ **Auto Learning** - Agent teaches itself
✅ **Auto Optimization** - Agent improves context quality
✅ **Auto Evolution** - Agent discovers better strategies
✅ **Evidence-Based** - All improvements are measured

---

## What Happens Under the Hood

```
AgentInterface agent = new AgentInterface();
└─> Auto-bootstraps on first run
    ├─> Discovers framework structure
    ├─> Uses framework to learn about itself
    ├─> Gathers self-referential context
    └─> Builds mental models

agent.ask("Your task")
└─> Infers task type from description
    ├─> Detects: CODE_GENERATION/BUG_FIXING/etc
    └─> Selects optimal collectors
        ├─> CodeStructure for generation
        ├─> RuntimeError for debugging
        └─> Security for analysis
            └─> Gathers context
                ├─> Filters noise
                ├─> Prioritizes relevance
                └─> Formats for AI
                    └─> Measures quality
                        └─> Auto-improves if needed
                            └─> Returns optimal context
```

---

## Try It Yourself

### Option 1: See the Demo
```bash
./gradlew agentDemo
```

### Option 2: Use It in Code
```java
AgentInterface agent = new AgentInterface();
String context = agent.ask("Your task here");
// Send context to OpenAI/Claude/etc
```

### Option 3: Let Agent Learn
```bash
./gradlew agentBootstrap
```

### Option 4: See Meta-Framework
```bash
./gradlew selfReference  # Framework learning about itself
./gradlew selfBootstrap  # Framework improving itself
```

---

## The Philosophy in 30 Seconds

**Old way:**
- Human configures collectors
- Human tunes thresholds
- Human defines rules
- Human maintains system
- AI follows orders

**This way:**
- AI discovers framework
- AI learns patterns
- AI optimizes context
- AI evolves strategies
- AI improves framework
- Human just asks questions

---

## Quick Links

- **[START_HERE.md](START_HERE.md)** - Zero-config guide for humans
- **[AGENT_MANIFESTO.md](AGENT_MANIFESTO.md)** - Autonomy guide for AI agents
- **[ZERO_CONFIG_SUMMARY.md](ZERO_CONFIG_SUMMARY.md)** - Complete technical summary

---

## FAQ

**Q: Do I need to configure anything?**
A: No. Just run `./gradlew agentDemo`

**Q: How do agents learn the framework?**
A: The framework uses itself to teach agents about itself.

**Q: Can agents modify the framework?**
A: Yes. They can create collectors, run experiments, and evolve strategies.

**Q: Is it safe for agents to experiment?**
A: Yes. All experiments are measured and must prove improvement before adoption.

**Q: How do I use this in my code?**
A: `AgentInterface agent = new AgentInterface(); agent.ask("task");`

**Q: What AI models does this work with?**
A: All. OpenAI, Claude, Gemini, local models, etc. It just provides optimal context.

---

## TL;DR

```bash
# Run this
./gradlew agentDemo

# Then use this
AgentInterface agent = new AgentInterface();
String context = agent.ask("Whatever you need");

# Done. That's the whole framework.
```

**Zero configuration. Maximum results. Autonomous evolution.** 🚀
