# 🔌 Framework Injection Guide

## For Developers Who Want to Inject the Evolver Framework

This guide explains how to seamlessly inject the **Evolver Context Engineering Framework** into your existing project, allowing AI agents to autonomously learn and evolve your codebase's context management.

---

## 🎯 What Is Framework Injection?

**Traditional Approach:**
- Replace existing frameworks ❌
- Rewrite application logic ❌  
- Learn new APIs ❌
- Migrate data ❌

**Evolver Injection:**
- **Detects** your existing framework automatically ✅
- **Merges** seamlessly without breaking changes ✅
- **Enhances** your current setup ✅
- **Evolves** alongside your codebase ✅

---

## 🚀 Quick Start (2 Minutes)

### Step 1: Add Dependency

**Maven:**
```xml
<dependency>
    <groupId>com.evolver</groupId>
    <artifactId>evolver-framework</artifactId>
    <version>1.0.0</version>
</dependency>
```

**Gradle:**
```gradle
implementation 'com.evolver:evolver-framework:1.0.0'
```

### Step 2: Inject Into Your Project

```java
import com.evolver.injection.FrameworkInjector;

public class YourApplication {
    public static void main(String[] args) {
        // ONE line injection - that's it!
        FrameworkInjector.inject()
            .withLearningDock("docs/") // Point to your docs
            .withAgentCharacteristic("DocumentationObsessed")
            .start();
            
        // Your existing code continues unchanged
        yourExistingApplication.run();
    }
}
```

### Step 3: Watch Agents Learn

The agents immediately start:
- 🔍 **Discovering** your project structure
- 📚 **Learning** your patterns and conventions  
- 🎯 **Optimizing** context for your specific domain
- 🔄 **Evolving** the framework to fit your needs

---

## 🏗️ How Injection Works

### 1. **Project Detection Phase**

The injector automatically detects:

```java
// Spring Boot detected
@SpringBootApplication ✅
-> Configures Spring-aware collectors
-> Sets up dependency injection bridge
-> Learns Spring patterns

// React/Node.js detected  
package.json + jsx files ✅
-> Configures JS/TS collectors
-> Learns component patterns
-> Understands build pipeline

// Django detected
settings.py + models.py ✅
-> Configures Python collectors
-> Learns Django patterns
-> Understands ORM relationships
```

### 2. **Seamless Integration**

```java
// Before injection - your existing code
@RestController
public class UserController {
    @Autowired
    private UserService userService;
    
    @GetMapping("/users")
    public List<User> getUsers() {
        return userService.findAll();
    }
}

// After injection - same code, enhanced with AI
@RestController
@EvolverEnhanced // ← Added automatically
public class UserController {
    @Autowired
    private UserService userService;
    
    @GetMapping("/users")
    public List<User> getUsers() {
        // Agent automatically understands:
        // - This is REST endpoint
        // - Returns user data
        // - Uses service layer pattern
        // - Follows Spring conventions
        return userService.findAll();
    }
}
```

### 3. **Framework Evolution**

Agents don't just use your framework - they **evolve it**:

```java
// Agent discovers: "This project uses GraphQL heavily"
// Agent creates: CustomGraphQLCollector.java
// Agent evolves: Framework now understands GraphQL contexts

// Agent discovers: "This team prefers functional patterns"  
// Agent creates: FunctionalContextFormatter.java
// Agent evolves: Framework adapts to functional style

// Agent discovers: "This domain is finance-heavy"
// Agent creates: FinancialSecurityCollector.java  
// Agent evolves: Framework becomes finance-aware
```

---

## 🎭 Agent Characteristics

Choose agent personalities that match your team:

### Available Characteristics

```java
// For documentation-heavy projects
.withAgentCharacteristic("DocumentationObsessed")
// Creates comprehensive docs, focuses on clarity

// For performance-critical applications  
.withAgentCharacteristic("PerformanceFreak")
// Optimizes for speed, measures everything

// For security-sensitive systems
.withAgentCharacteristic("SecurityParanoid") 
// Validates inputs, checks vulnerabilities

// For clean architecture enthusiasts
.withAgentCharacteristic("ArchitectureNazi")
// Enforces patterns, maintains structure

// For test-driven development
.withAgentCharacteristic("TestingEvangelist")
// Writes tests first, ensures coverage

// For legacy system maintenance
.withAgentCharacteristic("LegacyWhisperer")
// Understands old code, preserves stability
```

### Custom Characteristics

```java
// Define your own team personality
AgentCharacteristic custom = AgentCharacteristic.builder()
    .name("ProductivityHacker")
    .description("Obsessed with developer productivity and automation")
    .priorities(List.of(
        "Reduce repetitive tasks",
        "Automate manual processes", 
        "Optimize developer workflows"
    ))
    .contextFocus(List.of(
        "Build scripts",
        "Developer tools",
        "Automation opportunities"
    ))
    .evolutionStyle("Aggressive automation")
    .build();

FrameworkInjector.inject()
    .withCustomCharacteristic(custom)
    .start();
```

---

## 🎯 Learning Docks

**Learning Docks** are entry points where agents start discovering your project:

### Common Dock Patterns

```java
// Documentation-first discovery
.withLearningDock("docs/")
// Agents learn from your existing documentation

// API-first discovery  
.withLearningDock("src/main/controllers/")
// Agents understand your API patterns

// Domain-first discovery
.withLearningDock("src/main/models/")  
// Agents learn your business domain

// Test-first discovery
.withLearningDock("src/test/")
// Agents understand through your tests

// Configuration-first discovery
.withLearningDock("config/")
// Agents learn your deployment patterns
```

### Multiple Docks for Comprehensive Learning

```java
FrameworkInjector.inject()
    .withLearningDock("docs/", DockPriority.HIGH)
    .withLearningDock("src/main/api/", DockPriority.MEDIUM)  
    .withLearningDock("tests/integration/", DockPriority.LOW)
    .start();
```

---

## 🔧 Framework Evolution Examples

### Example 1: E-commerce Project

**Before Injection:**
```java
// Regular Spring Boot e-commerce app
@Entity
public class Product {
    private String name;
    private BigDecimal price;
    // ...
}
```

**After Agent Learning (Week 1):**
```java
// Agent discovered e-commerce patterns
// Framework evolved to understand commerce contexts

ContextEngine engine = new ContextEngine();
String context = engine.generateContext("Analyze checkout flow");

// Agent automatically includes:
// ✅ Payment processing code
// ✅ Tax calculation logic  
// ✅ Inventory management
// ✅ Security validations
// ✅ Fraud detection patterns
```

**After Agent Evolution (Month 1):**
```java
// Agent created specialized collectors
public class ECommerceCollector extends ContextCollector {
    // Understands product catalogs
    // Recognizes payment flows
    // Identifies security boundaries
    // Tracks inventory patterns
}

// Framework now "speaks e-commerce"
String context = engine.generateContext("Optimize cart abandonment");
// Returns highly relevant e-commerce context
```

### Example 2: Microservices Architecture

**Agent Discovery:**
```java
// Agent detects: "This is microservices architecture"
// Agent finds: docker-compose.yml, service discovery, API gateways
// Agent learns: Inter-service communication patterns
```

**Framework Evolution:**
```java
// Agent creates new collectors
public class MicroserviceCollector extends ContextCollector {
    // Maps service dependencies
    // Understands event flows
    // Tracks API contracts
    // Monitors service boundaries
}

// Agent creates new formatters
public class DistributedContextFormatter extends ContextFormatter {
    // Formats context across service boundaries
    // Includes relevant microservice patterns
    // Maintains service context isolation
}
```

**Result:**
```java
String context = engine.generateContext("Debug payment service timeout");

// Automatically includes:
// ✅ Payment service code
// ✅ Related service interfaces  
// ✅ Network communication code
// ✅ Timeout configuration
// ✅ Circuit breaker patterns
// ✅ Monitoring and logging setup
```

---

## 🎨 Integration Patterns

### Pattern 1: Gradual Integration

```java
// Week 1: Basic injection
FrameworkInjector.inject()
    .withLearningDock("docs/")
    .start();

// Week 2: Add agent characteristics  
FrameworkInjector.inject()
    .withLearningDock("docs/")
    .withAgentCharacteristic("DocumentationObsessed")
    .start();

// Month 1: Multiple agents
FrameworkInjector.inject()
    .withLearningDock("docs/")
    .withAgentCharacteristic("DocumentationObsessed")
    .withAgentCharacteristic("SecurityParanoid")  
    .withAgentCharacteristic("PerformanceFreak")
    .start();
```

### Pattern 2: Team-Based Integration

```java
// Different teams, different characteristics
if (team.equals("backend")) {
    FrameworkInjector.inject()
        .withAgentCharacteristic("ArchitectureNazi")
        .withAgentCharacteristic("SecurityParanoid")
        .start();
}

if (team.equals("frontend")) {
    FrameworkInjector.inject()
        .withAgentCharacteristic("UXObsessed")
        .withAgentCharacteristic("PerformanceFreak")
        .start();
}
```

### Pattern 3: Environment-Based Integration

```java
// Production: Conservative agents
if (environment.equals("production")) {
    FrameworkInjector.inject()
        .withAgentCharacteristic("StabilityGuardian")
        .withEvolutionRate(EvolutionRate.CONSERVATIVE)
        .start();
}

// Development: Aggressive agents
if (environment.equals("development")) {  
    FrameworkInjector.inject()
        .withAgentCharacteristic("ExperimentalHacker")
        .withEvolutionRate(EvolutionRate.AGGRESSIVE)
        .start();
}
```

---

## 📊 Monitoring Agent Learning

### Built-in Monitoring

```java
// Check what agents have learned
AgentRuntime runtime = AgentRuntime.getInstance();

// View discovered patterns
List<Pattern> patterns = runtime.getDiscoveredPatterns();

// Check framework evolutions
List<Evolution> evolutions = runtime.getFrameworkEvolutions();

// Monitor learning progress
LearningProgress progress = runtime.getLearningProgress();
System.out.println("Agent has analyzed " + progress.getFilesAnalyzed() + " files");
System.out.println("Discovered " + progress.getPatternsFound() + " patterns");
System.out.println("Created " + progress.getCollectorsCreated() + " new collectors");
```

### Learning Dashboard

```java
// Optional: Enable web dashboard
FrameworkInjector.inject()
    .withLearningDock("docs/")
    .withAgentCharacteristic("DocumentationObsessed")
    .enableDashboard(8080) // http://localhost:8080/agent-dashboard
    .start();
```

---

## 🛡️ Safety and Rollback

### Safe Evolution

```java
// Agents test evolutions before applying
FrameworkInjector.inject()
    .withSafetyMode(SafetyMode.STRICT) // Test all changes
    .withRollbackEnabled(true) // Auto-rollback on issues
    .start();
```

### Manual Control

```java
// Approve agent evolutions manually
FrameworkInjector.inject()
    .withEvolutionApproval(ApprovalMode.MANUAL)
    .start();

// Approve/reject evolution proposals
AgentRuntime.getInstance().approveEvolution("CustomCollectorProposal_1");
AgentRuntime.getInstance().rejectEvolution("RiskyFormatterProposal_2");
```

---

## 🔀 Migration from Other Frameworks

### From Traditional Context Management

```java
// Before: Manual context management
public String gatherContext(String task) {
    StringBuilder context = new StringBuilder();
    
    // Manual file reading
    context.append(readFile("src/main/service/UserService.java"));
    context.append(readFile("src/main/model/User.java"));
    
    // Manual relevance filtering
    if (task.contains("database")) {
        context.append(readFile("src/main/repository/UserRepository.java"));
    }
    
    return context.toString();
}

// After: Agent-driven context management  
public String gatherContext(String task) {
    return FrameworkInjector.getContextEngine()
        .generateContext(task); // Agent handles everything
}
```

### From Other AI Frameworks

```java
// Before: LangChain/etc
chain = LLMChain(
    llm=OpenAI(),
    prompt=PromptTemplate.from_template("Analyze this code: {code}")
)

// After: Evolver integration
FrameworkInjector.inject().start();
String context = ContextEngine.generateContext("Analyze user authentication code");
String result = openAI.complete(context); // Much richer context
```

---

## 🏆 Best Practices

### 1. **Start Small, Evolve Gradually**

```java
// Week 1: Single dock, single characteristic
FrameworkInjector.inject()
    .withLearningDock("docs/")
    .withAgentCharacteristic("DocumentationObsessed")
    .start();

// Month 1: Multiple agents working together
// Month 6: Fully evolved, project-specific framework
```

### 2. **Choose Characteristics Matching Your Team**

```java
// For startups: Fast iteration
.withAgentCharacteristic("MoveFastBreakThings")

// For enterprise: Stability focus  
.withAgentCharacteristic("StabilityGuardian")

// For open source: Documentation focus
.withAgentCharacteristic("DocumentationObsessed")
```

### 3. **Monitor and Guide Evolution**

```java
// Review agent learning weekly
AgentRuntime.getInstance().generateLearningReport();

// Guide agent focus
FrameworkInjector.inject()
    .withLearningFocus("security") // This week: focus on security
    .start();
```

### 4. **Use Multiple Docks for Rich Learning**

```java
// Rich learning from multiple perspectives
.withLearningDock("docs/architecture/", DockPriority.HIGH)
.withLearningDock("src/test/integration/", DockPriority.MEDIUM)
.withLearningDock("config/deployment/", DockPriority.LOW)
```

---

## 🚨 Troubleshooting

### Common Issues

**Issue: Agent not learning effectively**
```java
// Solution: Add more learning docks
.withLearningDock("docs/")
.withLearningDock("examples/")  
.withLearningDock("tests/")
```

**Issue: Framework evolution too aggressive**
```java
// Solution: Use conservative mode
.withEvolutionRate(EvolutionRate.CONSERVATIVE)
.withSafetyMode(SafetyMode.STRICT)
```

**Issue: Agent characteristics conflicting**
```java
// Solution: Use compatible characteristics
.withAgentCharacteristic("DocumentationObsessed") // ✅ Compatible
.withAgentCharacteristic("TestingEvangelist")     // ✅ Compatible
// Avoid: "MoveFirstBreakThings" + "StabilityGuardian" ❌
```

### Debug Mode

```java
FrameworkInjector.inject()
    .withDebugMode(true) // Verbose logging
    .withLearningDock("docs/")
    .start();

// Check logs for agent decisions
AgentRuntime.getInstance().getDecisionLog().forEach(System.out::println);
```

---

## 🎉 Success Stories

### Case Study 1: Legacy Java Monolith

**Challenge:** 500K line legacy Java application, poor documentation
**Solution:** 
```java
FrameworkInjector.inject()
    .withLearningDock("src/main/legacy/")
    .withAgentCharacteristic("LegacyWhisperer")
    .withAgentCharacteristic("DocumentationObsessed")
    .start();
```
**Result:** Agent learned legacy patterns, created comprehensive context collectors, reduced context gathering time from hours to seconds.

### Case Study 2: Microservices Startup

**Challenge:** 50 microservices, complex inter-service dependencies
**Solution:**
```java
FrameworkInjector.inject()
    .withLearningDock("docker-compose.yml")
    .withLearningDock("k8s/")
    .withAgentCharacteristic("DistributedSystemsExpert")
    .start();
```
**Result:** Agent mapped all service dependencies, created cross-service context collectors, evolved framework to understand distributed patterns.

---

## 📚 Further Reading

- 🤖 **[AGENT_MANIFESTO.md](AGENT_MANIFESTO.md)** - For AI agents wanting to understand autonomy
- 🏗️ **[ARCHITECTURE.md](ARCHITECTURE.md)** - Deep dive into framework architecture  
- 🔄 **[META_FRAMEWORK.md](META_FRAMEWORK.md)** - Advanced: Framework using itself
- 🚀 **[GETTING_STARTED.md](GETTING_STARTED.md)** - Detailed integration examples

---

## 💡 The Vision

**Traditional frameworks are static.** You configure them once, they stay the same forever.

**The Evolver framework is alive.** It learns your project, adapts to your patterns, evolves with your needs.

**Injection makes this effortless.** Your existing code stays the same. The framework grows around it, enhancing without disrupting.

**Agents make it autonomous.** You don't manage the framework. Agents manage it for you, evolving it into the perfect fit for your specific project.

---

## 🔗 Quick Links

- **Quick Start:** `FrameworkInjector.inject().withLearningDock("docs/").start();`
- **Documentation:** [GETTING_STARTED.md](GETTING_STARTED.md)
- **Agent Guide:** [AGENT_MANIFESTO.md](AGENT_MANIFESTO.md)
- **Examples:** `./gradlew agentDemo`

**Welcome to autonomous context engineering. Your framework will never be the same.** 🚀