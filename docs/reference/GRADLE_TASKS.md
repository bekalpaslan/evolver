# 🔧 Gradle Tasks Reference

> **📍 Location**: Moved from root to `docs/reference/GRADLE_TASKS.md` for better organization.

## Overview

This document provides comprehensive documentation for all Gradle tasks in the Evolver Framework. The framework includes autonomous context engineering capabilities with multiple task types for different operational phases.

---

## 📋 **Task Categories**

### 🤖 **Agent Tasks** (`group = 'agent'`)
**Purpose**: Autonomous agent operations and learning

| Task | Description | Usage |
|------|-------------|--------|
| `bootstrap` | Agent learns framework automatically | `gradle bootstrap` |
| `cli` | Evolver CLI for privacy controls | `gradle cli --args="help"` |

### 💉 **Injection Tasks** (`group = 'injection'`)  
**Purpose**: Framework injection into existing projects

| Task | Description | Usage |
|------|-------------|--------|
| `inject` | Inject framework into current project | `gradle inject` |
| `learningDockDemo` | Demonstrate LearningDock functionality | `gradle learningDockDemo` |
| `learningDockLiveTest` | Live test - materialize a real LearningDock | `gradle learningDockLiveTest` |

### 🗄️ **Database Tasks** (`group = 'database'`)
**Purpose**: Experience database management and quality control

| Task | Description | Usage |
|------|-------------|--------|
| `dbMigrate` | Run database migration to latest version | `gradle dbMigrate` |
| `dbConsolidate` | Consolidate legacy experience formats | `gradle dbConsolidate` |
| `dbValidate` | Validate database schema and integrity | `gradle dbValidate` |
| `dbStats` | Show database statistics | `gradle dbStats` |
| `dbVersion` | Show database version information | `gradle dbVersion` |
| `memoryAnalyze` | Analyze memory usage of database loading | `gradle memoryAnalyze` |

### 🔒 **Quality Control Tasks** (`group = 'database'`)
**Purpose**: Strict quality enforcement and validation

| Task | Description | Usage |
|------|-------------|--------|
| `cleanExperiences` | Remove test data, duplicates, and low-quality experiences | `gradle cleanExperiences` |
| `validateExperiences` | Validate experience quality standards and report issues | `gradle validateExperiences` |
| `purgeTestData` | Remove all experiences with test categories and placeholder names | `gradle purgeTestData` |
| `experienceReport` | Generate comprehensive experience database quality report | `gradle experienceReport` |
| `agentAccountability` | Generate agent accountability and contribution quality report | `gradle agentAccountability` |

### 🧹 **Maintenance Tasks** (`group = 'maintenance'`)
**Purpose**: Project maintenance and cleanup

| Task | Description | Usage |
|------|-------------|--------|
| `cleanup` | Remove OLD files and backups from the project | `gradle cleanup` |

### ✅ **Verification Tasks** (`group = 'verification'`)
**Purpose**: Testing and verification

| Task | Description | Usage |
|------|-------------|--------|
| `testDirtyDataPrevention` | Test dirty data prevention system | `gradle testDirtyDataPrevention` |

### 🏗️ **Application Tasks** (`group = 'application'`)
**Purpose**: Standard application lifecycle

| Task | Description | Usage |
|------|-------------|--------|
| `build` | Build the application | `gradle build` |
| `test` | Run tests | `gradle test` |
| `javadoc` | Generate API documentation | `gradle javadoc` |

---

## 🚀 **Quick Start Workflow**

### First Time Setup
```bash
# 1. Bootstrap the agent (learns the framework)
gradle bootstrap

# 2. Check database status
gradle dbStats

# 3. Validate experience quality
gradle validateExperiences
```

### Regular Maintenance
```bash
# Weekly quality check
gradle experienceReport

# Monthly accountability review
gradle agentAccountability

# Clean up if quality issues found
gradle cleanExperiences
```

### Development Workflow
```bash
# Build and test
gradle build test

# Generate documentation
gradle javadoc

# Verify quality enforcement
gradle testDirtyDataPrevention
```

---

## 📚 **Related Documentation**

- **Quick Start**: `docs/guides/QUICK_START.md`
- **Architecture**: `docs/architecture/`
- **Experience Flow**: `docs/agent/EXPERIENCE_FLOW.md`
- **Agent Guidelines**: `docs/agent/AGENT_MANIFESTO.md`

---

## 🔍 **Task Details**

### Database Management
The Evolver framework maintains a centralized experience database (`experiences.json`) that requires careful management:

- **Quality Control**: All experiences undergo strict validation before entry
- **Version Management**: Automatic migration system handles schema updates
- **Agent Accountability**: Hashed IP-based tracking ensures quality without privacy violation

### Quality Enforcement
The framework implements **zero-tolerance** for dirty data:

- **Automatic Rejection**: Low-quality experiences are rejected before database entry
- **Quality Scoring**: All experiences scored 0-10.0 with minimum threshold of 7.5
- **Forbidden Content**: Test data, placeholders, and generic content automatically blocked

### Agent Operations
Autonomous agents interact with the framework through:

- **Bootstrap Process**: Agents learn the framework autonomously
- **Experience Sharing**: High-quality experiences shared across all agents
- **Rule Challenges**: Agents can challenge and evolve framework rules

---

*This documentation is automatically validated and maintained as part of the framework's documentation maintenance protocol.*