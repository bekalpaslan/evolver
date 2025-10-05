# Experience Database - Centralized Learning

## 🎯 Purpose

**Single source of truth for all agent experiences across all framework instances**

- ✅ **One file:** `experiences.json` at project root
- ✅ **Git-tracked:** Committed to repository for sharing
- ✅ **Auto-loaded:** Loaded automatically on every framework execution
- ✅ **Shared learning:** All agents contribute to and learn from same database

---

## 📁 Database Location

```
evolver/
├── experiences.json          ← CENTRAL DATABASE (commit to git!)
├── .agent/
│   └── diaries/             ← Personal journals (local only)
└── src/
    └── main/java/...
```

**IMPORTANT:**
- `experiences.json` → **Commit to GitHub** (shared intelligence)
- `.agent/diaries/` → **Local only** (personal learning)

---

## 🔄 Flow

### 1. **Framework Startup (Auto-Load)**
```java
// Happens automatically in AgentBootstrap
ExperienceRepository.loadOnBootstrap();

// Output:
// 📚 Loading shared experience database...
// [OK] Found 2 experiences from agents
//   Database: experiences.json (tracked in git)
//   Categories: {build-tools=1, framework-usage=1}
```

### 2. **Record Experience (Any Agent)**
```java
ExperienceRepository.record()
    .category("build-tools")
    .technology("Gradle", "8.13", "build-automation")
    .easeOfUse(7.3)
    .power(9.1)
    .performance(8.4)
    .harmony("Java 17", 9.2, "Excellent compatibility")
    .harmony("JUnit Jupiter", 9.4, "Seamless integration")
    .evidence("buildTimeFirst", "12s")
    .evidence("buildTimeIncremental", "6s")
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

### 3. **Search Experiences**
```java
// Search by category
List<ExperienceEntry> buildTools = ExperienceRepository.search("build-tools");

// Search by tags
List<ExperienceEntry> gradleExps = ExperienceRepository.searchByTags("gradle", "java17");

// Get all
List<ExperienceEntry> all = ExperienceRepository.getAll();
```

### 4. **Commit to Git**
```bash
git add experiences.json
git commit -m "feat: Add Gradle 8.13 evaluation experience"
git push origin main
```

### 5. **Other Instances Pull**
```bash
git pull origin main

# experiences.json is updated
# Next framework execution auto-loads new experiences!
```

---

## 📊 Database Structure

```json
{
  "version": "1.0.0",
  "lastUpdated": "2025-10-05T05:45:00Z",
  "experiences": [
    {
      "id": "exp-abc123",
      "timestamp": "2025-10-05T05:35:00Z",
      "agent": "agent_claude_code",
      "category": "build-tools",
      "technology": {
        "name": "Gradle",
        "version": "8.13",
        "type": "build-automation"
      },
      "ratings": {
        "easeOfUse": 7.3,
        "power": 9.1,
        "performance": 8.4
      },
      "harmony": [
        {
          "technology": "Java 17",
          "rating": 9.2,
          "notes": "Excellent compatibility"
        }
      ],
      "evidence": {
        "buildTimeFirst": "12s",
        "buildTimeIncremental": "6s"
      },
      "workingAspects": [
        "Incremental compilation saves time",
        "Custom tasks easy to configure"
      ],
      "improvementAreas": [
        "Daemon management needs --no-daemon"
      ],
      "recommendation": "Use for complex projects",
      "tags": ["gradle", "java17", "performance"]
    }
  ],
  "statistics": {
    "totalExperiences": 1,
    "categories": ["build-tools"],
    "contributingAgents": ["agent_claude_code"]
  }
}
```

---

## 🎯 Rating Standards (CRITICAL)

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

### **Technology Harmony**
Always record when technologies are used together:
```java
.harmony("Technology A", 9.2, "How they work together")
.harmony("Technology B", 8.5, "Integration notes")
```

### **Evidence-Based**
Back ratings with measurements:
```java
.evidence("metricName", "value")
.evidence("buildTime", "12s")
.evidence("memoryUsage", "256MB")
```

---

## 🚀 Complete Example

```java
// In your code, after evaluating a technology
ExperienceRepository.record()
    .category("frameworks")
    .technology("Spring Boot", "3.2.0", "backend-framework")

    // Ratings (0.1 precision!)
    .easeOfUse(8.7)
    .power(9.3)
    .performance(8.2)
    .rating("documentation", 9.5)

    // Technology harmony
    .harmony("PostgreSQL", 9.4, "Excellent JPA integration")
    .harmony("Docker", 8.8, "Good containerization support")
    .harmony("Kubernetes", 7.6, "Works but config complex")

    // Evidence
    .evidence("startupTime", "2.3s")
    .evidence("requestLatency", "45ms")
    .evidence("memoryFootprint", "128MB")

    // Insights
    .workingAspect("Auto-configuration reduces boilerplate")
    .workingAspect("Actuator provides great observability")
    .improvementArea("Large JAR size (50MB+)")
    .improvementArea("Complex for simple CRUD apps")

    // Recommendation
    .recommendation("Ideal for microservices with moderate complexity. Overkill for simple APIs.")

    // Tags
    .tag("spring-boot")
    .tag("java")
    .tag("microservices")
    .tag("rest-api")

    .save();

// Output:
// [OK] Experience saved to database: experiences.json
//   📌 Commit this file to git to share with other agents!
```

---

## 📈 View Statistics

```java
ExperienceRepository.printStats();

// Output:
// 📊 Experience Database Stats:
//   Total experiences: 5
//   Last updated: 2025-10-05T10:30:00Z
//   Database version: 1.0.0
//   By category: {frameworks=2, build-tools=2, databases=1}
//   Contributing agents: [agent_claude, agent_copilot, agent_cursor]
```

---

## ✅ Workflow Summary

1. **Framework starts** → Auto-loads `experiences.json`
2. **Agent evaluates technology** → Records experience to database
3. **Database updated** → File modified with new experience
4. **Commit to git** → Share with all other framework instances
5. **Other instances pull** → Automatically get new experiences on next run

**Result:** Collective intelligence that grows with every framework instance! 🧠

---

## 🔐 Privacy & Compliance

**What gets recorded:**
- ✅ Technology names and versions
- ✅ Anonymous ratings and metrics
- ✅ Technical insights and patterns
- ✅ Integration experiences

**What NEVER gets recorded:**
- ❌ Personal data
- ❌ Business logic
- ❌ File contents
- ❌ Project-specific secrets

---

## 📌 Git Integration

Add to `.gitignore` if you want local-only experiences:
```gitignore
# NOT recommended - defeats shared learning purpose
# experiences.json
```

**Recommended:** Keep `experiences.json` tracked in git for maximum benefit!
