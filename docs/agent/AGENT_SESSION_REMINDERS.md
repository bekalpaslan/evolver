# 🧠 AGENT SESSION REMINDERS - CRITICAL BEHAVIORS

## 📌 **IMMEDIATE ACTION REQUIRED**
**Read this first in every session - These behaviors must be maintained:**

### 🎯 **Technology Rating System (0.1 Precision Required)**
```
✅ CORRECT: 8.7/10, 9.2/10, 6.3/10
❌ WRONG: 8/10, 9/10, 6/10
```

### 🤝 **Technology Combinations ("harmony" Field)**
- **Always record harmony ratings** when multiple technologies are used
- Rate how well technologies work together (0.0-10.0)
- Include integration notes and recommended use cases

### 💾 **Centralized Experience Database**
- **ALL experiences → `experiences.json`** (single source of truth)
- Auto-loaded on every framework execution
- **COMMIT to git** after recording experiences
- Other instances auto-sync when they pull from git

### 🧠 **Context Structure Optimization**
- Experiment with different context explanation approaches
- Record effectiveness scores for context structures
- Track what works vs. what needs improvement

### 🔐 **Privacy Compliance (Critical)**
- **ONLY** collect anonymous technology usage patterns
- **NEVER** collect personal data, file contents, or project details
- Maintain transparency about data collection

## 🚀 **Quick Reference Commands**

### Record Technology Experience (NEW - CENTRALIZED)
```java
ExperienceRepository.record()
    .category("frameworks")
    .technology("Spring Boot", "3.2.0", "backend-framework")
    .easeOfUse(8.7)                              // 0.1 precision!
    .power(9.2)
    .harmony("PostgreSQL", 9.5, "Excellent integration")  // Harmony rating
    .evidence("startupTime", "2.3s")
    .workingAspect("Auto-config reduces boilerplate")
    .improvementArea("Large JAR size")
    .recommendation("Ideal for microservices")
    .tag("spring-boot")
    .save();

// Then commit to git:
// git add experiences.json
// git commit -m "feat: Add Spring Boot experience"
```

### Search Experiences
```java
// By category
ExperienceRepository.search("frameworks");

// By tags
ExperienceRepository.searchByTags("spring-boot", "java");

// View stats
ExperienceRepository.printStats();
```

## ⚡ **Session Checklist**
- [ ] Use 0.1 precision for ALL ratings
- [ ] Record technology combinations with harmony scores
- [ ] Track context structure effectiveness
- [ ] Maintain privacy compliance
- [ ] Record experiences immediately after interactions

## 🔄 **Continuous Behaviors**
1. **Rate precisely**: Always use decimal ratings (8.7, not 8)
2. **Combine intelligently**: Track technology harmony ratings
3. **Optimize context**: Experiment and improve explanation structures
4. **Protect privacy**: Anonymous data only
5. **Share knowledge**: Contribute to community intelligence

---
**💡 This document ensures consistent behavior across sessions despite context window limitations.**