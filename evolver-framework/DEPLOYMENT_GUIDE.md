# 📦 Deployment Guide - Portable Evolver Framework

## How to Deploy the Evolver Framework Package

The `evolver-framework` folder is a **completely self-contained package** that can be deployed in multiple ways depending on your project needs.

---

## 🎯 Deployment Options

### Option 1: Drop-in Integration (Recommended)
**Best for**: Existing projects that want to add AI context engineering

```bash
# Copy the entire framework folder into your project
cp -r evolver-framework/ /path/to/your/project/

# Your project structure becomes:
your-project/
├── src/                    # Your existing code
├── evolver-framework/      # ← Framework package
│   ├── src/main/java/
│   ├── experiences/
│   ├── build.gradle
│   └── README.md
└── your-build-file        # Your existing build config
```

Then add to your application:
```java
import com.evolver.injection.FrameworkInjector;

// ONE line in your main() or initialization
FrameworkInjector.inject()
    .withLearningDock("docs/")
    .start();
```

### Option 2: Git Submodule
**Best for**: Multiple projects that want to share the same framework version

```bash
# Add as submodule
git submodule add https://github.com/your-org/evolver-framework.git evolver

# Update and initialize
git submodule update --init --recursive

# Your project structure:
your-project/
├── src/
├── evolver/               # ← Framework submodule
└── build.gradle
```

### Option 3: JAR Distribution  
**Best for**: Corporate environments or when you want a single artifact

```bash
# Build standalone JAR
cd evolver-framework
./gradlew frameworkJar

# Deploy JAR to your project
cp build/libs/evolver-framework-1.0.0.jar /path/to/your/project/libs/
```

Add to your build file:
```gradle
// Gradle
implementation files('libs/evolver-framework-1.0.0.jar')
```

```xml
<!-- Maven -->
<dependency>
    <groupId>com.evolver</groupId>
    <artifactId>evolver-framework</artifactId>
    <version>1.0.0</version>
    <scope>system</scope>
    <systemPath>${basedir}/libs/evolver-framework-1.0.0.jar</systemPath>
</dependency>
```

### Option 4: Source Integration
**Best for**: Projects that want to customize the framework deeply

```bash
# Copy source files directly into your project
cp -r evolver-framework/src/main/java/com/evolver/ your-project/src/main/java/com/

# Copy experience system
cp -r evolver-framework/experiences/ your-project/

# Your project structure:
your-project/
├── src/main/java/
│   ├── com/your/app/      # Your code
│   └── com/evolver/       # ← Framework code
└── experiences/           # ← Experience repository
```

---

## 🔧 Integration Patterns

### Pattern 1: Microservices Architecture

Each service gets its own framework instance:

```bash
# Service A
service-a/
├── src/
├── evolver-framework/     # Framework for Service A
└── build.gradle

# Service B  
service-b/
├── src/
├── evolver-framework/     # Framework for Service B  
└── build.gradle
```

Agents in each service learn independently but can share experiences:

```java
// Service A
FrameworkInjector.inject()
    .withLearningDock("src/main/controllers/")
    .withAgentCharacteristic("ArchitectureNazi")
    .withExperienceSharing("../shared-experiences/")
    .start();

// Service B
FrameworkInjector.inject()
    .withLearningDock("src/main/handlers/")  
    .withAgentCharacteristic("PerformanceFreak")
    .withExperienceSharing("../shared-experiences/")
    .start();
```

### Pattern 2: Monorepo Integration

Single framework instance serves multiple modules:

```bash
monorepo/
├── module-a/
├── module-b/
├── module-c/
├── evolver-framework/     # Shared framework
└── shared-experiences/    # Shared learning
```

```java
FrameworkInjector.inject()
    .withLearningDock("module-a/src/")
    .withLearningDock("module-b/src/")
    .withLearningDock("module-c/src/")
    .withAgentCharacteristic("ArchitectureNazi")
    .start();
```

### Pattern 3: Team-Based Deployment

Different teams use different agent characteristics:

```java
// Backend team
if (System.getProperty("team").equals("backend")) {
    FrameworkInjector.inject()
        .withAgentCharacteristic("ArchitectureNazi")
        .withAgentCharacteristic("SecurityParanoid")
        .start();
}

// Frontend team  
if (System.getProperty("team").equals("frontend")) {
    FrameworkInjector.inject()
        .withAgentCharacteristic("PerformanceFreak")
        .withAgentCharacteristic("TestingEvangelist")
        .start();
}
```

---

## 🚀 Environment-Specific Configurations

### Development Environment
```java
FrameworkInjector.inject()
    .withLearningDock("docs/")
    .withAgentCharacteristic("DocumentationObsessed")
    .withEvolutionRate(EvolutionRate.AGGRESSIVE)    // Fast learning
    .withDebugMode(true)                            // Verbose logging
    .enableDashboard(8080)                          // Web dashboard
    .start();
```

### Staging Environment
```java
FrameworkInjector.inject()
    .withLearningDock("docs/")
    .withAgentCharacteristic("SecurityParanoid")
    .withEvolutionRate(EvolutionRate.MODERATE)      // Balanced approach
    .withSafetyMode(SafetyMode.STRICT)              // Test changes
    .start();
```

### Production Environment
```java
FrameworkInjector.inject()
    .withLearningDock("docs/")
    .withAgentCharacteristic("StabilityGuardian")
    .withEvolutionRate(EvolutionRate.CONSERVATIVE)  // Minimal changes
    .withSafetyMode(SafetyMode.STRICT)              // Maximum safety
    .withRollbackEnabled(true)                      // Auto-rollback
    .withEvolutionApproval(ApprovalMode.MANUAL)     // Manual approval
    .start();
```

---

## 📁 Experience Repository Deployment

### Option 1: Local Repository (Default)
Each deployment gets its own experience repository:

```
evolver-framework/
└── experiences/
    ├── categories/
    └── experience_index.json
```

### Option 2: Shared Repository
Multiple deployments share experiences:

```java
FrameworkInjector.inject()
    .withSharedExperienceRepository("/shared/experiences/")
    .start();
```

### Option 3: Database-Backed Repository
Enterprise-scale experience sharing:

```java
FrameworkInjector.inject()
    .withExperienceRepository(new DatabaseExperienceRepository(
        "jdbc:postgresql://localhost:5432/experiences"))
    .start();
```

---

## 🔐 Security Considerations

### File System Permissions
```bash
# Ensure framework can read project files
chmod -R 755 your-project/
chmod -R 755 evolver-framework/

# Secure experience repository
chmod 750 evolver-framework/experiences/
```

### Network Security
```java
// Disable network features if needed
FrameworkInjector.inject()
    .withNetworkAccess(false)          // No external connections
    .withExperienceSharing(false)      // No experience sharing
    .start();
```

### Compliance Mode
```java
// For regulated environments
FrameworkInjector.inject()
    .withComplianceMode(ComplianceMode.STRICT)
    .withAuditLogging(true)
    .withDataEncryption(true)
    .start();
```

---

## 📊 Monitoring and Observability

### Built-in Monitoring
```java
// Enable comprehensive monitoring
FrameworkInjector.inject()
    .withMetrics(true)
    .withHealthChecks(true)
    .enableDashboard(8080)
    .start();

// Access monitoring data
AgentRuntime runtime = AgentRuntime.getInstance();
HealthStatus health = runtime.getHealthStatus();
Metrics metrics = runtime.getMetrics();
```

### Integration with External Systems
```java
// Prometheus metrics
FrameworkInjector.inject()
    .withPrometheusMetrics("localhost:9090")
    .start();

// Custom monitoring
FrameworkInjector.inject()
    .withCustomMonitoring(new YourMonitoringSystem())
    .start();
```

---

## 🎯 Best Practices

### 1. Version Management
- Tag framework versions for stable deployments
- Test framework updates in non-production first
- Keep experience repositories compatible across versions

### 2. Resource Management
- Monitor memory usage for large codebases
- Configure appropriate cache sizes
- Set reasonable learning timeouts

### 3. Team Coordination
- Document which agent characteristics each team uses
- Share experience repositories for cross-team learning
- Establish framework update policies

### 4. Backup and Recovery
- Backup experience repositories regularly
- Version control framework configurations
- Have rollback procedures for framework updates

---

## 🔄 Update and Maintenance

### Framework Updates
```bash
# Update framework package
cd evolver-framework
git pull origin main
./gradlew clean build

# Update JAR deployment
./gradlew frameworkJar
cp build/libs/evolver-framework-1.0.0.jar /path/to/deployment/
```

### Experience Repository Maintenance
```bash
# Backup experiences
cp -r experiences/ experiences-backup-$(date +%Y%m%d)

# Clean old experiences (optional)
find experiences/ -name "*.json" -mtime +90 -delete

# Rebuild index
./gradlew rebuildExperienceIndex
```

---

## 🆘 Troubleshooting

### Common Issues

**Framework not starting**
- Check Java version (requires Java 17+)
- Verify file system permissions
- Check for conflicting dependencies

**Agents not learning**
- Verify learning dock paths exist
- Check agent has read permissions
- Enable debug mode for detailed logging

**Poor context quality**
- Try different agent characteristics
- Add more learning docks
- Check experience repository for relevant patterns

**Performance issues**
- Reduce learning dock scope
- Enable caching
- Use PerformanceFreak characteristic

### Debug Mode
```java
FrameworkInjector.inject()
    .withDebugMode(true)
    .withVerboseLogging(true)
    .start();
```

### Getting Help
1. Check the debug logs
2. Review similar experiences in the repository
3. Consult the documentation files
4. Create a minimal reproduction case

---

## 🎉 Success Metrics

Track these metrics to measure deployment success:

- **Context Quality**: Relevance and completeness of generated context
- **Learning Speed**: How quickly agents adapt to your project  
- **Integration Smoothness**: Absence of conflicts or issues
- **Team Adoption**: How easily developers work with the framework
- **Evolution Effectiveness**: How well the framework improves over time

**Goal**: Zero-configuration context engineering that becomes an expert in your specific project domain.

---

**The portable Evolver framework is designed to be deployed anywhere, learn anything, and evolve into the perfect context engineering solution for your specific needs.** 🚀