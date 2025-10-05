# Framework Separation Strategies

## Architectural Patterns for Logical Separation

### 1. Interface-Based Abstraction
Use interfaces to define contracts, allowing framework injection without coupling.

```java
// Business interface - framework agnostic
public interface UserService {
    User createUser(CreateUserRequest request);
    User getUser(String id);
}

// Framework-enhanced implementation
public class FrameworkEnhancedUserService implements UserService {
    private final UserService businessLogic;
    private final ContextEngine contextEngine;

    public FrameworkEnhancedUserService(UserService businessLogic, ContextEngine contextEngine) {
        this.businessLogic = businessLogic;
        this.contextEngine = contextEngine;
    }

    @Override
    public User createUser(CreateUserRequest request) {
        // Framework optimizes context gathering
        ContextPackage context = contextEngine.gatherContext(
            ContextRequest.builder()
                .taskDescription("Creating user: " + request.getEmail())
                .taskType(TaskType.CREATION)
                .build()
        ).join();

        // Business logic remains unchanged
        return businessLogic.createUser(request);
    }
}
```

### 2. Decorator Pattern
Wrap business logic with framework capabilities as decorators.

```java
// Base business service
public class SimpleUserService implements UserService {
    @Override
    public User createUser(CreateUserRequest request) {
        // Pure business logic
        return userRepository.save(new User(request));
    }
}

// Framework decorator
public class ContextOptimizedUserService implements UserService {
    private final UserService delegate;
    private final ContextEngine contextEngine;

    public ContextOptimizedUserService(UserService delegate, ContextEngine contextEngine) {
        this.delegate = delegate;
        this.contextEngine = contextEngine;
    }

    @Override
    public User createUser(CreateUserRequest request) {
        // Framework optimization layer
        optimizeContextForUserCreation(request);

        // Delegate to business logic
        return delegate.createUser(request);
    }

    private void optimizeContextForUserCreation(CreateUserRequest request) {
        contextEngine.gatherContext(/* optimization logic */).join();
    }
}
```

### 3. Aspect-Oriented Programming (AOP)
Apply framework concerns as aspects that intercept method calls.

```java
// Business service - no framework awareness
@Service
public class OrderService {
    public Order processOrder(OrderRequest request) {
        validateOrder(request);
        calculatePricing(request);
        saveOrder(request);
        return createOrderResponse(request);
    }
}

// Framework aspect - applied transparently
@Aspect
@Component
public class ContextOptimizationAspect {
    @Autowired
    private ContextEngine contextEngine;

    @Around("@annotation(OptimizeContext)")
    public Object optimizeContext(ProceedingJoinPoint joinPoint) throws Throwable {
        // Gather optimized context before method execution
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        OptimizeContext annotation = signature.getMethod().getAnnotation(OptimizeContext.class);

        ContextPackage context = contextEngine.gatherContext(
            ContextRequest.builder()
                .taskDescription(annotation.value())
                .taskType(annotation.taskType())
                .build()
        ).join();

        // Proceed with business logic
        return joinPoint.proceed();
    }
}

// Usage - just add annotation
@Service
public class OrderService {
    @OptimizeContext(value = "Processing customer order", taskType = TaskType.PROCESSING)
    public Order processOrder(OrderRequest request) {
        // Pure business logic
        return processOrderLogic(request);
    }
}
```

### 4. Plugin Architecture
Framework as pluggable components loaded at runtime.

```java
// Plugin interface
public interface FrameworkPlugin {
    void initialize(PluginContext context);
    void optimize(OperationContext operation);
    void shutdown();
}

// Plugin manager
public class FrameworkPluginManager {
    private final List<FrameworkPlugin> plugins = new ArrayList<>();

    public void loadPlugins() {
        // Load plugins dynamically (could be from classpath, config, etc.)
        ServiceLoader<FrameworkPlugin> serviceLoader = ServiceLoader.load(FrameworkPlugin.class);
        for (FrameworkPlugin plugin : serviceLoader) {
            plugins.add(plugin);
            plugin.initialize(new PluginContext());
        }
    }

    public void applyOptimizations(OperationContext operation) {
        for (FrameworkPlugin plugin : plugins) {
            plugin.optimize(operation);
        }
    }
}

// Business service uses plugin manager
@Service
public class ProductService {
    @Autowired
    private FrameworkPluginManager pluginManager;

    public Product createProduct(ProductRequest request) {
        OperationContext operation = new OperationContext("createProduct", request);

        // Apply framework optimizations
        pluginManager.applyOptimizations(operation);

        // Business logic
        return productRepository.save(new Product(request));
    }
}
```

### 5. Event-Driven Architecture
Framework listens to application events without being part of main flow.

```java
// Application events - framework agnostic
public class OrderEvents {
    public static class OrderCreatedEvent {
        private final Order order;
        // constructor, getters...
    }

    public static class OrderProcessedEvent {
        private final Order order;
        // constructor, getters...
    }
}

// Business service publishes events
@Service
public class OrderProcessingService {
    @Autowired
    private ApplicationEventPublisher eventPublisher;

    public Order processOrder(OrderRequest request) {
        Order order = createOrder(request);
        processPayment(order);
        updateInventory(order);

        // Publish event - framework can listen
        eventPublisher.publishEvent(new OrderEvents.OrderCreatedEvent(order));

        return order;
    }
}

// Framework event listener
@Component
public class ContextOptimizationEventListener {
    @Autowired
    private ContextEngine contextEngine;

    @EventListener
    public void onOrderCreated(OrderEvents.OrderCreatedEvent event) {
        // Framework optimizes context based on event
        contextEngine.gatherContext(
            ContextRequest.builder()
                .taskDescription("Order created: " + event.getOrder().getId())
                .taskType(TaskType.CREATION)
                .build()
        ).join();
    }
}
```

### 6. Configuration-Driven Separation
Framework behavior controlled by external configuration files.

```yaml
# application.yml - framework configuration separate from business config
evolver:
  enabled: true
  optimization:
    level: "adaptive"
    strategies:
      - "context_deduplication"
      - "garbage_collection"
  agents:
    characteristics:
      - "DocumentationObsessed"
      - "PerformanceFreak"
  learning:
    dock: "docs/"
    experiences: "experiences/"
```

```java
// Configuration class
@ConfigurationProperties(prefix = "evolver")
public class FrameworkConfig {
    private boolean enabled = true;
    private OptimizationConfig optimization;
    private List<String> agentCharacteristics;
    // getters, setters...
}

// Framework bootstrap - conditional loading
@Configuration
@ConditionalOnProperty(name = "evolver.enabled", havingValue = "true")
public class FrameworkAutoConfiguration {
    @Bean
    public ContextEngine contextEngine(FrameworkConfig config) {
        return new ContextEngine(config);
    }
}

// Business service - no framework code
@Service
public class InventoryService {
    public void updateStock(Product product, int quantity) {
        // Pure business logic
        inventoryRepository.updateStock(product.getId(), quantity);
    }
}
```

### 7. Proxy Pattern
Framework acts as transparent proxy around business services.

```java
// Proxy interface
public interface ServiceProxy<T> {
    T createProxy(T target);
}

// Framework proxy implementation
public class ContextOptimizingProxy implements ServiceProxy<Object> {
    private final ContextEngine contextEngine;

    @Override
    public Object createProxy(Object target) {
        return Proxy.newProxyInstance(
            target.getClass().getClassLoader(),
            target.getClass().getInterfaces(),
            new ContextOptimizingInvocationHandler(target, contextEngine)
        );
    }
}

// Invocation handler
public class ContextOptimizingInvocationHandler implements InvocationHandler {
    private final Object target;
    private final ContextEngine contextEngine;

    public ContextOptimizingInvocationHandler(Object target, ContextEngine contextEngine) {
        this.target = target;
        this.contextEngine = contextEngine;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        // Pre-method optimization
        optimizeContextForMethod(method, args);

        // Invoke business logic
        Object result = method.invoke(target, args);

        // Post-method optimization
        optimizeContextAfterMethod(method, result);

        return result;
    }

    private void optimizeContextForMethod(Method method, Object[] args) {
        contextEngine.gatherContext(/* method-specific optimization */).join();
    }
}
```

### 8. Module System Separation
Framework as separate module with clear boundaries.

```
project/
├── business-logic/           # Pure business modules
│   ├── user-service/
│   ├── order-service/
│   └── inventory-service/
├── framework-integration/    # Framework integration layer
│   ├── evolver-bridge/
│   └── optimization-config/
└── infrastructure/           # Database, messaging, etc.
```

```java
// Framework integration module
module framework.integration {
    requires business.logic;
    requires evolver.framework;

    exports com.company.framework.bridge;
}

// Business logic module - framework agnostic
module business.logic {
    exports com.company.business.user;
    exports com.company.business.order;
    // No framework dependencies
}
```

### 9. Service Locator Pattern
Framework services discovered at runtime.

```java
// Service locator interface
public interface FrameworkServiceLocator {
    <T> T getService(Class<T> serviceType);
    <T> Optional<T> findService(Class<T> serviceType);
}

// Implementation
public class RuntimeFrameworkServiceLocator implements FrameworkServiceLocator {
    private final Map<Class<?>, Object> services = new ConcurrentHashMap<>();

    @Override
    public <T> T getService(Class<T> serviceType) {
        return findService(serviceType)
            .orElseThrow(() -> new ServiceNotFoundException(serviceType));
    }

    @Override
    public <T> Optional<T> findService(Class<T> serviceType) {
        return Optional.ofNullable(services.get(serviceType))
            .map(serviceType::cast);
    }

    public void registerService(Class<?> serviceType, Object service) {
        services.put(serviceType, service);
    }
}

// Business service uses locator
@Service
public class PaymentService {
    @Autowired
    private FrameworkServiceLocator serviceLocator;

    public Payment processPayment(PaymentRequest request) {
        // Get framework service if available
        Optional<ContextEngine> contextEngine = serviceLocator.findService(ContextEngine.class);

        // Apply optimization if framework is present
        contextEngine.ifPresent(engine -> {
            engine.gatherContext(/* payment optimization */).join();
        });

        // Business logic
        return paymentProcessor.process(request);
    }
}
```

### 10. Annotation-Based Configuration
Declarative framework integration with annotations.

```java
// Framework annotations
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ContextOptimized {
    String value() default "";
    TaskType taskType() default TaskType.GENERAL;
    int priority() default 5;
}

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface FrameworkAware {
    boolean enabled() default true;
}

// Annotation processor
public class FrameworkAnnotationProcessor {
    public void processAnnotations(Object bean, String beanName) {
        Class<?> beanClass = bean.getClass();

        // Process class-level annotations
        if (beanClass.isAnnotationPresent(FrameworkAware.class)) {
            FrameworkAware annotation = beanClass.getAnnotation(FrameworkAware.class);
            if (annotation.enabled()) {
                registerFrameworkIntegration(bean);
            }
        }

        // Process method-level annotations
        for (Method method : beanClass.getMethods()) {
            if (method.isAnnotationPresent(ContextOptimized.class)) {
                ContextOptimized annotation = method.getAnnotation(ContextOptimized.class);
                registerMethodOptimization(method, annotation);
            }
        }
    }
}

// Business service with annotations
@Service
@FrameworkAware
public class ShippingService {
    @ContextOptimized(value = "Calculate shipping cost", taskType = TaskType.CALCULATION)
    public ShippingCost calculateShipping(Order order, Address destination) {
        // Pure business logic
        return shippingCalculator.calculate(order, destination);
    }

    @ContextOptimized(value = "Process shipment", taskType = TaskType.PROCESSING, priority = 8)
    public Shipment processShipment(Order order) {
        // Business logic
        return shipmentProcessor.process(order);
    }
}
```

## Implementation Recommendations

### Choose Strategy Based on Context:

1. **Interface-Based Abstraction**: Good for new projects, clean architecture
2. **Decorator Pattern**: Excellent for existing codebases, minimal changes
3. **AOP**: Best for cross-cutting concerns, transparent integration
4. **Plugin Architecture**: Ideal for extensible systems, runtime flexibility
5. **Event-Driven**: Perfect for loosely coupled systems
6. **Configuration-Driven**: Great for environment-specific behavior
7. **Proxy Pattern**: Transparent integration, no code changes required
8. **Module System**: Strong boundaries, compile-time separation
9. **Service Locator**: Runtime flexibility, optional dependencies
10. **Annotation-Based**: Declarative, easy to understand

### Best Practices:

- **Single Responsibility**: Framework concerns separate from business concerns
- **Dependency Inversion**: Business code depends on abstractions, not framework
- **Configuration Externalization**: Framework behavior configurable externally
- **Conditional Loading**: Framework components loaded only when needed
- **Testing Isolation**: Business logic testable without framework dependencies
- **Gradual Adoption**: Framework integration can be added incrementally

### Framework Injection Pattern:

```java
// Recommended injection approach
@Configuration
public class FrameworkIntegrationConfig {
    @Bean
    @ConditionalOnClass(name = "com.evolver.framework.ContextEngine")
    public FrameworkInjector frameworkInjector() {
        return FrameworkInjector.inject()
            .withLearningDock("docs/")
            .withAgentCharacteristic("DocumentationObsessed")
            .start();
    }
}

// Business code remains unchanged
@Service
public class BusinessService {
    // No framework imports or dependencies
    public void businessMethod() {
        // Pure business logic
    }
}
```

These strategies ensure the evolver framework remains a transparent optimization layer while keeping production code focused on business value.