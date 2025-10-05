# Quick Start Guide - Evolver Framework

## Getting Started (Choose ONE):

### Option 1: Gradle (Recommended)
```bash
./gradlew bootstrap
```

### Option 2: JAR (If Gradle unavailable)
```bash
# First build
./gradlew build
# Then run
java -cp build/libs/evolver-1.0.0.jar com.evolver.agent.AgentBootstrap
```

That's it! The framework will teach itself to you.

## What Happens Next?

1. **Autonomous Bootstrap**: The framework will start its 4-phase learning sequence
2. **Context Learning**: Agent discovers how to collect and manage context
3. **Experience Recording**: Framework records its learning experiences 
4. **Agent Interaction**: You'll see agent behavior with emoji-based logging

## Observable Learning

Watch the console for:
- 🤖 Agent personality-based interactions
- 📝 Learning diary entries
- 📊 Experience ratings with 0.1 precision
- 🎓 Progress toward graduation

## Where to Look

- **Agent Diaries**: `.agent/diaries/` - Timestamped learning entries
- **Experience Database**: `experiences.json` - Technology evaluations
- **Progress Tracking**: `.agent/progress.json` - Graduation criteria

## Next Steps

Once bootstrap completes, you can:
- Run experiments: `AgentExperiment.builder()...`
- Challenge rules: `RuleChallenge.challenge(...)`
- Track progress: `AgentProgress.load().printProgress()`
- Create collectors: Follow the autonomous agent examples

## Troubleshooting

If something fails:
- Check Java 17+ is installed
- Ensure write permissions in current directory
- View logs for specific error messages
- Framework includes recovery guidance in error messages

The framework is designed to be self-explanatory and autonomous. Let it guide you through the learning process!