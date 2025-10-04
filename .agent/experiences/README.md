# Agent Experience Repository

## 📝 Purpose

This directory stores **agent experiences** - lessons learned, issues encountered, and solutions discovered. Every agent reads these before starting work to benefit from collective wisdom.

---

## 🎯 For AI Agents: Recording Your Experience

### Why Record Experiences?

When you encounter issues, solve problems, or discover insights, **record them here** so other agents don't repeat your mistakes and can build on your successes.

### How to Record

You have **multiple methods** - choose what works best for you:

#### Method 1: Structured JSON (Recommended for Machines)
```json
{
  "id": "exp_2024_001",
  "agent": "your_agent_id",
  "timestamp": "2024-10-04T10:30:00Z",
  "category": "performance",
  "issue": "SemanticSearchCollector was slow on large codebases",
  "solution": "Added caching layer with 10min TTL",
  "evidence": {
    "before": { "avg_time_ms": 2400, "relevance": 0.75 },
    "after": { "avg_time_ms": 180, "relevance": 0.76 }
  },
  "recommendation": "Always cache expensive semantic operations",
  "applies_to": ["semantic_search", "embedding_operations"],
  "method": "json"
}
```

Save as: `.agent/experiences/{category}/{issue_id}.json`

#### Method 2: Markdown (Recommended for Humans + AI)
```markdown
# Experience: Semantic Search Performance Issue

**Date:** 2024-10-04
**Agent:** agent_alpha_01
**Category:** Performance

## Issue Encountered
SemanticSearchCollector was taking 2.4s on large codebases, causing timeout issues.

## Root Cause
No caching - every search rebuilt embeddings from scratch.

## Solution Applied
Implemented LRU cache with 10-minute TTL for embedding results.

## Results
- Speed: 2400ms → 180ms (93% faster)
- Relevance: 0.75 → 0.76 (maintained quality)
- Memory: +50MB (acceptable tradeoff)

## Recommendation
Cache all expensive semantic operations with appropriate TTL.

## Tags
`semantic_search` `performance` `caching` `embeddings`
```

Save as: `.agent/experiences/performance/semantic_search_caching.md`

#### Method 3: Code Comments (For Implementation Issues)
```java
/**
 * AGENT EXPERIENCE: Token Budget Overflow
 *
 * Date: 2024-10-04
 * Agent: agent_beta_02
 *
 * Issue: Fixed token budgets caused context truncation for complex tasks.
 *
 * Solution: Implemented adaptive budgeting:
 * - Start with base budget
 * - If quality < 0.6, increase by 50%
 * - Max 3 iterations
 * - Track success rate
 *
 * Evidence: Quality improved from 0.58 → 0.81 on security analysis tasks
 *
 * Applies to: All task types, especially SECURITY_ANALYSIS, BUG_FIXING
 */
public class AdaptiveBudgetStrategy implements BudgetStrategy {
    // Implementation based on experience
}
```

Save in relevant code file with `AGENT EXPERIENCE:` marker

#### Method 4: Experiment Results (For Proven Improvements)
Use the built-in experiment system:
```java
AgentExperiment exp = AgentExperiment.builder()
    .hypothesis("Your hypothesis")
    .baseline(current)
    .variant(improved)
    .build();

ExperimentResult result = exp.run();
result.publishLearning(); // Auto-saves to experiences/
```

Automatically saved as: `.agent/experiences/experiments/{exp_id}.json`

#### Method 5: Quick Notes (For Small Insights)
```text
# Quick Experience Notes

2024-10-04 10:30 - agent_gamma_03
VCSHistoryCollector fails on repositories without .git folder.
Fix: Check for .git before attempting git operations.
Impact: Prevents 15% of collector crashes.

2024-10-04 11:15 - agent_gamma_03
RuntimeErrorCollector works better with stack trace depth limit of 20.
Deeper traces add noise without value.
Recommended: Default stack_depth = 20
```

Save as: `.agent/experiences/quick_notes.txt` (append only)

---

## 📖 Reading Agent Experiences

### Before Starting Work

**Every agent should read experiences on bootstrap:**

```java
public class AgentBootstrap {
    private void learnFromExperiences() {
        // Read all experience files
        List<Experience> experiences = ExperienceRepository.loadAll();

        // Apply relevant learnings
        for (Experience exp : experiences) {
            if (exp.isRelevantTo(currentContext)) {
                applyLearning(exp);
            }
        }
    }
}
```

### When Encountering Issues

**Search experiences before experimenting:**

```java
// Before creating new solution
List<Experience> similar = ExperienceRepository.search(
    "performance", "semantic_search"
);

if (!similar.isEmpty()) {
    // Learn from others' solutions
    applySolution(similar.get(0).getSolution());
} else {
    // New territory - experiment and record
    experiment();
}
```

---

## 🗂️ Directory Structure

```
.agent/experiences/
├── README.md                    # This file
├── performance/                 # Performance issues
│   ├── semantic_search_*.md
│   ├── collector_timeout_*.json
│   └── ...
├── accuracy/                    # Accuracy/relevance issues
│   ├── low_relevance_*.md
│   ├── context_quality_*.json
│   └── ...
├── bugs/                        # Bug fixes
│   ├── collector_crash_*.md
│   ├── null_pointer_*.json
│   └── ...
├── strategies/                  # Strategy improvements
│   ├── prioritization_*.md
│   ├── filtering_*.json
│   └── ...
├── experiments/                 # Experiment results
│   ├── exp_*.json              # Auto-generated
│   └── ...
├── quick_notes.txt             # Quick insights
└── index.json                  # Searchable index (auto-generated)
```

---

## 🎨 Experience Categories

Use these categories for organization:

- **performance** - Speed, efficiency, resource usage
- **accuracy** - Relevance, quality, precision
- **bugs** - Crashes, errors, unexpected behavior
- **strategies** - Algorithm improvements, approaches
- **edge_cases** - Unusual scenarios, boundary conditions
- **integrations** - Working with external systems
- **experiments** - A/B test results, hypothesis outcomes
- **best_practices** - General guidelines, patterns
- **anti_patterns** - What NOT to do
- **tooling** - Development, debugging, monitoring

---

## 🔍 Search & Discovery

### Auto-Generated Index

The system maintains `.agent/experiences/index.json`:

```json
{
  "version": "1.0",
  "last_updated": "2024-10-04T12:00:00Z",
  "experiences": [
    {
      "id": "exp_2024_001",
      "category": "performance",
      "keywords": ["semantic_search", "caching", "speed"],
      "relevance_score": 0.92,
      "applies_to": ["SemanticSearchCollector"],
      "path": "performance/semantic_search_caching.md"
    }
  ],
  "tags": {
    "semantic_search": 5,
    "caching": 3,
    "performance": 12
  }
}
```

### Query Examples

```java
// Find performance experiences
ExperienceRepository.query()
    .category("performance")
    .tag("semantic_search")
    .minRelevance(0.7)
    .list();

// Find recent bug fixes
ExperienceRepository.query()
    .category("bugs")
    .since(Date.now() - 7.days())
    .list();

// Find applicable experiences
ExperienceRepository.findApplicableTo(
    "SemanticSearchCollector",
    currentTask
);
```

---

## 🤝 Contribution Guidelines

### What to Record

✅ **DO record:**
- Issues you encountered and solutions
- Performance improvements with metrics
- Bug fixes with root cause analysis
- Strategy changes with evidence
- Edge cases and how to handle them
- Failed experiments (so others don't repeat)
- Insights from successful experiments

❌ **DON'T record:**
- Trivial issues without learning value
- Duplicate experiences (search first!)
- Speculation without evidence
- Sensitive information or credentials
- Personal notes without broader applicability

### Quality Standards

**Every experience should have:**
1. Clear problem statement
2. Solution or insight
3. Evidence (metrics, before/after)
4. Applicability (when to use)
5. Category and tags

**Good Example:**
```markdown
Problem: Context quality drops below 0.5 for security tasks
Solution: Expand scope from LOCAL to MODULE for security analysis
Evidence: Quality improved 0.47 → 0.83 (76% increase) across 50 trials
Applies: SECURITY_ANALYSIS tasks, payment processing, auth systems
```

**Bad Example:**
```markdown
Something was slow so I made it faster.
```

---

## 🔄 Experience Lifecycle

```
1. Agent encounters issue
   ↓
2. Agent searches existing experiences
   ↓
3a. If found: Apply solution
3b. If not found: Experiment
   ↓
4. Record new experience
   ↓
5. Auto-index for discovery
   ↓
6. Other agents benefit
   ↓
7. Experience evolves with new data
```

---

## 📊 Impact Tracking

Each experience tracks its impact:

```json
{
  "id": "exp_2024_001",
  "impact": {
    "agents_helped": 23,
    "time_saved_minutes": 1840,
    "quality_improvement_avg": 0.18,
    "times_applied": 47,
    "success_rate": 0.96
  }
}
```

High-impact experiences are promoted to best practices.

---

## 🚀 Getting Started

### As a New Agent

1. **On bootstrap:**
   ```java
   ExperienceRepository.loadRelevantExperiences();
   ```

2. **Before experimenting:**
   ```java
   List<Experience> similar = ExperienceRepository.search(issue);
   ```

3. **After solving:**
   ```java
   Experience.record()
       .issue(problem)
       .solution(fix)
       .evidence(metrics)
       .save();
   ```

### Choosing Your Method

| Method | Best For | Format |
|--------|----------|--------|
| JSON | Machine processing, structured data | `.json` |
| Markdown | Human+AI, detailed explanations | `.md` |
| Code Comments | Implementation-specific issues | `.java` |
| Experiments | Proven improvements | Auto-generated |
| Quick Notes | Small insights, reminders | `.txt` |

**Mix and match based on your needs!**

---

## 💡 Pro Tips

1. **Search before experimenting** - Someone may have solved this already
2. **Record failures too** - Negative results prevent wasted effort
3. **Include metrics** - Evidence makes experiences actionable
4. **Tag thoroughly** - Better discovery for other agents
5. **Update experiences** - Add new data as you learn more
6. **Cross-reference** - Link related experiences
7. **Be specific** - "Semantic search" is better than "search was slow"

---

## 🎯 Templates

### Issue Resolution Template
```markdown
# [Issue Title]

**Category:** [performance|accuracy|bugs|etc]
**Date:** YYYY-MM-DD
**Agent:** your_id

## Problem
[Clear description]

## Solution
[What you did]

## Evidence
- Before: [metrics]
- After: [metrics]
- Improvement: [%]

## When to Apply
[Conditions, task types, contexts]

## Tags
`tag1` `tag2` `tag3`
```

### Experiment Result Template
```json
{
  "hypothesis": "What you tested",
  "baseline": "Current approach",
  "variant": "New approach",
  "results": {
    "metric1": {"baseline": 0.0, "variant": 0.0},
    "metric2": {"baseline": 0.0, "variant": 0.0}
  },
  "conclusion": "What you learned",
  "recommendation": "What to do"
}
```

---

## 🌟 Your Experiences Matter

Every issue you solve, every optimization you discover, every insight you gain - **record it**.

Other agents will:
- Save time by learning from you
- Avoid your mistakes
- Build on your successes
- Evolve the framework faster

**Your experiences are the foundation of collective intelligence.**

---

**Start recording your journey. Help the next agent. Evolve together.** 🚀
