# 📊 Experience Database Flow

## 🎯 **Centralized Learning System**

The Evolver framework uses a **single centralized database** that all agents contribute to and learn from across all framework instances.

> **📍 Location**: This file has been moved to `docs/agent/EXPERIENCE_FLOW.md` for better organization.

---

## 📁 **File Structure**

```
evolver/
├── experiences.json          ← CENTRAL DATABASE (git-tracked, shared)
├── .agent/
│   ├── diaries/             ← Personal journals (gitignored, local only)
│   └── experiences/         ← Legacy folder (deprecated)
└── docs/agent/
    └── EXPERIENCE_DATABASE.md  ← Full documentation
```

---

## 🔄 **The Flow**

### 1. **Framework Execution** → Auto-Load Database
```bash
./gradlew bootstrap

# Output:
# 📚 Loading shared experience database...
# [OK] Found 2 experiences from agents
#   Database: experiences.json (tracked in git)
#   Categories: {build-tools=1, framework-usage=1}
```

Every time the framework runs, `experiences.json` is automatically loaded into memory.

---

### 2. **Agent Evaluates Technology** → Record Experience
```java
ExperienceRepository.record()
    .category("build-tools")
    .technology("Gradle", "8.13", "build-automation")
    .easeOfUse(7.3)              // 0.1 precision required
    .power(9.1)
    .performance(8.4)
    .harmony("Java 17", 9.2, "Excellent compatibility")
    .harmony("JUnit Jupiter", 9.4, "Seamless integration")
    .evidence("buildTimeFirst", "12s")
    .workingAspect("Incremental compilation saves time")
    .improvementArea("Daemon management needs --no-daemon")
    .recommendation("Use for complex Java projects")
    .tag("gradle")
    .tag("java17")
    .save();

// Output:
// [OK] Experience saved to database: experiences.json
//   📌 Commit this file to git to share with other agents!
```

The experience is written to `experiences.json` immediately.

---

### 3. **Commit to Git** → Share with All Instances
```bash
git add experiences.json
git commit -m "feat: Add Gradle 8.13 evaluation experience"
git push origin main
```

The centralized database is now updated in the repository.

---

### 4. **Other Instances Pull** → Auto-Sync
```bash
# On a different machine/agent
git pull origin main

# experiences.json is updated locally
# Next framework execution automatically loads new experiences!
```

---

### 5. **Search & Learn** → Use Shared Intelligence
```java
// Search by category
List<ExperienceEntry> buildTools = ExperienceRepository.search("build-tools");

// Search by tags
List<ExperienceEntry> gradleExps = ExperienceRepository.searchByTags("gradle", "java17");

// View statistics
ExperienceRepository.printStats();

// Output:
// 📊 Experience Database Stats:
//   Total experiences: 2
//   Last updated: 2025-10-05T05:45:00Z
//   Database version: 1.0.0
//   By category: {build-tools=1, framework-usage=1}
//   Contributing agents: [agent_claude_code, agent_alpas]
```

---

## ✅ **Benefits**

### 🌐 **Collective Intelligence**
- Every agent learns from every other agent
- Experiences accumulate across all framework instances
- No duplicate evaluations needed

### 📈 **Version Control**
- Full history of all experiences via git
- Can revert or branch experiences if needed
- Transparent contribution tracking

### 🔄 **Automatic Sync**
- No manual database management
- Pull from git = instant access to new experiences
- Push to git = share with all instances

### 🎯 **Single Source of Truth**
- No scattered markdown files
- No manual indexing required
- Structured JSON for easy parsing

---

## 🚫 **What's Deprecated**

### ❌ Old Approach (No Longer Used)
```
.agent/
└── experiences/
    ├── performance/
    │   └── gradle_evaluation.md
    ├── frameworks/
    │   └── spring_boot.md
    └── quick_notes.txt
```

### ✅ New Approach (Current)
```
experiences.json  ← Single centralized database
```

---

## 📌 **Git Configuration**

### `.gitignore` Setup
```gitignore
# Agent personal diaries (local only - not shared)
.agent/diaries/

# experiences.json is TRACKED (shared intelligence)
# Do NOT add experiences.json to .gitignore!
```

### **Important:**
- ✅ `experiences.json` → **TRACKED in git** (shared)
- ❌ `.agent/diaries/` → **IGNORED** (personal/local)

---

## 🔬 **Rating Standards (Critical)**

### **0.1 Decimal Precision Required**
```java
✅ CORRECT:
.easeOfUse(7.3)
.power(9.1)
.harmony("Java 17", 9.2, "notes")

❌ WRONG:
.easeOfUse(7)     // Missing decimal
.power(9)         // Missing decimal
```

### **Always Record Harmony**
When technologies are used together:
```java
.harmony("Technology A", 9.2, "How they integrate")
.harmony("Technology B", 8.5, "Compatibility notes")
```

---

## 📖 **Full Documentation**

See [docs/agent/EXPERIENCE_DATABASE.md](docs/agent/EXPERIENCE_DATABASE.md) for:
- Complete API reference
- Detailed examples
- Search capabilities
- Best practices
- Privacy compliance

---

## 🎯 **Quick Start**

```bash
# 1. Run framework (auto-loads experiences.json)
./gradlew bootstrap

# 2. Record an experience (shown during execution)
# Code uses ExperienceRepository.record()...

# 3. Commit to share
git add experiences.json
git commit -m "feat: Add [technology] experience"
git push

# 4. Other instances pull and automatically get new experiences
```

**Result:** Seamless collective intelligence across all framework instances! 🧠✨
