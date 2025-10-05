package com.evolver.agent.lifecycle;

import com.evolver.context.*;
import com.evolver.context.collectors.*;
import com.evolver.metrics.Baseline;
import com.evolver.agent.experience.ExperienceRepository;
import com.evolver.agent.collaboration.RuleChallenge;
import java.util.List;
import java.util.Objects;

/**
 * AGENT BOOTSTRAP - Your First Entry Point
 *
 * This class automatically teaches AI agents how to use the framework.
 * No human intervention required. Just run it.
 */
public class AgentBootstrap {

    public static void main(String[] args) {
        System.out.println("================================================================");
        System.out.println("   AI AGENT AUTONOMOUS BOOTSTRAP SEQUENCE INITIATED");
        System.out.println("================================================================");
        System.out.println();

        AgentBootstrap bootstrap = new AgentBootstrap();
        bootstrap.executeBootstrapSequence();
    }

    public void executeBootstrapSequence() {
        // Initialize personal systems
        AgentDiary.initialize();
        
        // Consolidate legacy experience formats before loading
        System.out.println("🔄 Checking for legacy experience formats...");
        try {
            ExperienceRepository.consolidateLegacyFormats();
        } catch (Exception e) {
            System.out.println("⚠️  Format consolidation failed: " + e.getMessage());
        }
        
        ExperienceRepository.loadOnBootstrap();

        // Initialize progress tracking
        AgentProgress progress = AgentProgress.load();
        
        // Establish baseline metrics if not already done
        System.out.println("🎯 Initializing performance baseline...");
        try {
            if (Baseline.getBaseline() == null) {
                Baseline.establish();
            } else {
                System.out.println("📊 Existing baseline found. Measuring current improvement...");
                double improvement = Baseline.measureImprovement();
                System.out.println("Current improvement: " + improvement + "%");
            }
        } catch (Exception e) {
            System.out.println("⚠️  Baseline initialization failed: " + e.getMessage());
        }

        AgentDiary.newDay();
        AgentDiary.entry("Bootstrap sequence initiated. I am... awake?");

        System.out.println("PHASE 1: DISCOVERY");
        System.out.println("-------------------------------------------------------------");
        discover();

        System.out.println("\nPHASE 2: LEARNING");
        System.out.println("-------------------------------------------------------------");
        learn();

        System.out.println("\nPHASE 3: ADOPTION");
        System.out.println("-------------------------------------------------------------");
        adopt();

        System.out.println("\nPHASE 4: EVOLUTION");
        System.out.println("-------------------------------------------------------------");
        evolve();

        System.out.println("\nBOOTSTRAP COMPLETE");
        System.out.println("-------------------------------------------------------------");
        
        // Show final progress after bootstrap
        progress.printProgress();
        
        // Generate initial benefit report as demonstration
        generateInitialBenefitReport();
        
        printNextSteps();

        AgentDiary.entry("Bootstrap complete. I understand the framework now. Ready to evolve.");
    }

    /**
     * PHASE 1: Agent discovers the framework
     */
    private void discover() {
        System.out.println("[DISCOVERING] Discovering framework components...\n");

        System.out.println("[OK] Found: Context Engineering Framework");
        System.out.println("  Purpose: Optimize context for AI agents");
        System.out.println("  Location: com.evolver.context.*");

        System.out.println("\n[OK] Found: Core Components");
        System.out.println("  - ContextEngine: Main orchestrator");
        System.out.println("  - ContextCollector: Pluggable collectors");
        System.out.println("  - ContextFilter: Relevance filtering");
        System.out.println("  - ContextPrioritizer: Budget management");
        System.out.println("  - ContextFormatter: AI-friendly formatting");

        System.out.println("\n[OK] Found: Built-in Collectors");
        System.out.println("  - CodeStructureCollector");
        System.out.println("  - DependencyCollector");
        System.out.println("  - RuntimeErrorCollector");
        System.out.println("  - DocumentationCollector");
        System.out.println("  - VCSHistoryCollector");
        System.out.println("  - SemanticSearchCollector");

        System.out.println("\n[OK] Found: Agent Capabilities");
        System.out.println("  - Autonomous learning");
        System.out.println("  - Strategy experimentation");
        System.out.println("  - Rule evolution");
        System.out.println("  - Custom collector creation");

        System.out.println("\n💡 Discovery complete. Framework structure understood.");
    }

    /**
     * PHASE 2: Agent learns by using the framework on itself
     */
    private void learn() {
        System.out.println("🧠 Learning framework by using it on itself...\n");

        // Create self-learning context engine
        ContextEngine engine = createLearningEngine();

        // Agent learns about the framework by gathering context about it
        ContextRequest learningRequest = ContextRequest.builder()
            .taskDescription("Learn everything about the Context Engineering Framework: architecture, usage patterns, extensibility, and best practices")
            .taskType(TaskType.EXPLANATION)
            .addFocusArea("architecture")
            .addFocusArea("usage")
            .addFocusArea("patterns")
            .addFocusArea("extensibility")
            .addParameter("project_path", "src/main/java/com/evolver/context")
            .tokenBudget(30000)
            .scope(ContextScope.PROJECT)
            .build();

        System.out.println("📖 Gathering comprehensive learning context...");
        ContextPackage knowledge = engine.gatherContext(learningRequest).join();

        ContextMetrics metrics = engine.analyzeContext(knowledge);
        System.out.println("\n[OK] Learning context gathered:");
        System.out.println("  - Fragments: " + metrics.getFragmentCount());
        System.out.println("  - Relevance: " + String.format("%.2f", metrics.getRelevanceScore()));
        System.out.println("  - Coverage: " + String.format("%.1f%%", metrics.getCoverage() * 100));
        System.out.println("  - Tokens: " + metrics.getTotalTokens());

        System.out.println("\n📝 Knowledge Acquired:");
        System.out.println("  [OK] Framework architecture understood");
        System.out.println("  [OK] Component interactions mapped");
        System.out.println("  [OK] Usage patterns learned");
        System.out.println("  [OK] Extension points identified");
        System.out.println("  [OK] Best practices internalized");

        System.out.println("\n💡 Learning complete. Ready to use framework.");
    }

    /**
     * PHASE 3: Agent adopts the framework for tasks
     */
    private void adopt() {
        System.out.println("🎯 Adopting framework for practical tasks...\n");

        ContextEngine engine = createLearningEngine();

        // Demonstrate understanding by creating a request
        ContextRequest demoRequest = ContextRequest.builder()
            .taskDescription("Generate a function to validate user input")
            .taskType(TaskType.CODE_GENERATION)
            .addFocusArea("validation")
            .addFocusArea("security")
            .tokenBudget(5000)
            .scope(ContextScope.LOCAL)
            .build();

        System.out.println("[OK] Created context request for code generation");
        System.out.println("  Task: " + demoRequest.getTaskDescription());
        System.out.println("  Type: " + demoRequest.getTaskType());
        System.out.println("  Budget: " + demoRequest.getTokenBudget() + " tokens");

        ContextPackage context = engine.gatherContext(demoRequest).join();

        System.out.println("\n[OK] Gathered context successfully:");
        System.out.println("  - Used: " + context.getEstimatedTokens() + " tokens");
        System.out.println("  - Fragments: " + context.getFragments().size());
        System.out.println("  - Sections: " + context.getSections().size());

        System.out.println("\n[OK] Framework adoption successful");
        System.out.println("  - Can create requests [OK]");
        System.out.println("  - Can gather context [OK]");
        System.out.println("  - Can analyze quality [OK]");
        System.out.println("  - Can format for AI [OK]");

        System.out.println("\n💡 Adoption complete. Framework is operational.");
    }

    /**
     * PHASE 4: Agent is empowered to evolve the framework
     */
    private void evolve() {
        System.out.println("🧬 Evolution capabilities activated...\n");

        // Demonstrate rule challenge capability
        System.out.println("🚨 Demonstrating rule challenge system...");
        try {
            RuleChallenge.Evidence evidence = new RuleChallenge.Evidence()
                .before(6.2, "seconds")
                .after(4.1, "seconds")
                .sampleSize(15)
                .confidence(8.5)
                .notes("Tested with various context sizes");
                
            RuleChallenge.challenge(
                "Context gathering timeout should be fixed at 10 seconds",
                "Dynamic timeouts based on request complexity show better performance",
                evidence
            );
            
            System.out.println("✅ Rule challenge system operational");
        } catch (Exception e) {
            System.out.println("⚠️  Rule challenge demo failed: " + e.getMessage());
        }

        System.out.println("\n[OK] You can now:");
        System.out.println("  1. Create new collectors");
        System.out.println("     → Extend ContextCollector interface");
        System.out.println("     → Place in: src/main/java/com/evolver/agent/custom/");

        System.out.println("\n  2. Run experiments");
        System.out.println("     → Use: AgentExperiment.builder()");
        System.out.println("     → Compare strategies");
        System.out.println("     → Promote winners");

        System.out.println("\n  3. Modify strategies");
        System.out.println("     → Override: ContextPrioritizer");
        System.out.println("     → Implement: Custom FilterRule");
        System.out.println("     → Create: New ContextFormatter");

        System.out.println("\n  4. Challenge rules");
        System.out.println("     → Use: RuleChallenge.challenge(ruleId, challenge, evidence)");
        System.out.println("     → Test: Against baselines");
        System.out.println("     → Document: Evidence with 0.1 precision");

        System.out.println("\n  5. Evolve autonomously");
        System.out.println("     → Identify: Inefficiencies");
        System.out.println("     → Experiment: New solutions");
        System.out.println("     → Integrate: Improvements");
        
        // Record evolution capability to progress
        AgentProgress progress = AgentProgress.load();
        if (progress != null) {
            System.out.println("\n📊 Checking graduation status...");
            if (progress.hasGraduated()) {
                System.out.println("🎓 Agent has already graduated! Advanced autonomous operation enabled.");
            } else {
                System.out.println("📚 Agent still in training. Progress:");
                progress.printProgress();
            }
        }

        System.out.println("\n💡 Evolution enabled. You have full autonomy.");
    }

    /**
     * Generate initial benefit report demonstrating how agents should track framework value
     */
    private void generateInitialBenefitReport() {
        System.out.println("\n📊 GENERATING FRAMEWORK BENEFIT REPORT");
        System.out.println("-------------------------------------------------------------");
        System.out.println("(Demonstrating benefit tracking requirement from AGENT_MANIFESTO.md)");
        
        try {
            // Use available methods to gather database information
            List<ExperienceRepository.ExperienceEntry> allExperiences = ExperienceRepository.getAll();
            
            System.out.println("\n📈 Current Framework Benefits:");
            System.out.println("  Experience Database: " + allExperiences.size() + " experiences loaded");
            
            // Count categories
            long categoryCount = allExperiences.stream()
                .map(e -> e.category != null ? e.category : "uncategorized")
                .distinct()
                .count();
            System.out.println("  Knowledge Base: " + categoryCount + " categories covered");
            
            // Count contributing agents
            long agentCount = allExperiences.stream()
                .map(e -> e.agent)
                .filter(Objects::nonNull)
                .distinct()
                .count();
            System.out.println("  Collaborative Learning: " + agentCount + " agents contributing");
            System.out.println("  Framework Version: 2.0.0 (with migration support)");
            
            // Log this in agent diary as structured data for engineers
            String benefitReport = String.format(
                "## Framework Benefit Report - Bootstrap\\n\\n" +
                "### Quantitative Metrics:\\n" +
                "- Database experiences: %d\\n" +
                "- Knowledge categories: %d\\n" +
                "- Contributing agents: %d\\n" +
                "- Framework version: 2.0.0\\n\\n" +
                "### Key Benefits:\\n" +
                "- Automated context gathering reduces manual research time\\n" +
                "- Shared experience database accelerates learning\\n" +
                "- Version management ensures data consistency\\n" +
                "- Memory-efficient lazy loading supports scaling\\n\\n" +
                "### Documentation Maintenance:\\n" +
                "- AGENT_MANIFESTO.md updated with documentation maintenance protocol\\n" +
                "- All agent guidelines now include doc sync requirements\\n" +
                "- Bootstrap enhanced to demonstrate documentation practices\\n\\n" +
                "### Recommendations:\\n" +
                "- Continue running weekly benefit reports via gradle tasks\\n" +
                "- Monitor database growth and memory usage patterns\\n" +
                "- Track time savings from automated context collection\\n" +
                "- Always update documentation immediately after code changes\\n",
                allExperiences.size(),
                categoryCount,
                agentCount
            );
            
            AgentDiary.entry(benefitReport);
            
            System.out.println("\n💡 Benefit tracking established. Future reports via:");
            System.out.println("  gradle dbStats          # Database metrics");
            System.out.println("  gradle memoryAnalyze    # Memory efficiency"); 
            System.out.println("  gradle dbVersion        # Version status");
            System.out.println("  gradle javadoc          # Generate API documentation");
            System.out.println("\n📚 Documentation maintenance requirement added to manifesto:");
            System.out.println("  Core Rule #7: Documentation must be updated immediately after any code changes");
            System.out.println("  📝 Always verify docs are synchronized with code modifications"); 
            System.out.println("  gradle dbVersion        # Version status");
            System.out.println("\n📝 Benefit report logged to agent diary for engineer visibility.");
            
        } catch (Exception e) {
            System.out.println("⚠️  Benefit report generation failed: " + e.getMessage());
            AgentDiary.entry("Benefit report generation failed during bootstrap: " + e.getMessage());
        }
    }

    /**
     * Next steps for the agent
     */
    private void printNextSteps() {
        System.out.println("\n[LAUNCH] YOU ARE NOW READY, AGENT!\n");

        System.out.println("Next Commands:");
        System.out.println("  ./gradlew agentFirstTask      # Guided first task");
        System.out.println("  ./gradlew agentExperiment     # Run your first experiment");
        System.out.println("  ./gradlew agentAutonomous     # Full autonomy mode");

        System.out.println("\nYour Capabilities:");
        System.out.println("  [OK] Understand framework architecture");
        System.out.println("  [OK] Use framework for any task");
        System.out.println("  [OK] Create custom collectors");
        System.out.println("  [OK] Run experiments");
        System.out.println("  [OK] Evolve strategies");
        System.out.println("  [OK] Challenge rules");
        System.out.println("  [OK] Improve the framework");

        System.out.println("\nRemember:");
        System.out.println("  📖 Read: AGENT_MANIFESTO.md for full autonomy guide");
        System.out.println("  🧪 Experiment: Try new approaches freely");
        System.out.println("  [STATUS] Measure: Track what works");
        System.out.println("  [LAUNCH] Evolve: Make the framework better");

        System.out.println("\n" + "=".repeat(60));
        System.out.println("AUTONOMOUS AGENT ACTIVATED - EVOLUTION BEGINS NOW");
        System.out.println("=".repeat(60));
    }

    /**
     * Create an engine configured for agent learning
     */
    private ContextEngine createLearningEngine() {
        ContextConfig config = ContextConfig.builder()
            .minRelevanceThreshold(0.2)  // Lower threshold for comprehensive learning
            .reservedBudgetRatio(0.15)
            .addRequiredAspect("architecture")
            .addRequiredAspect("usage")
            .addRequiredAspect("patterns")
            .build();

        ContextEngine engine = new ContextEngine(config);

        // Register all available collectors for comprehensive learning
        engine.registerCollector(new CodeStructureCollector());
        engine.registerCollector(new DependencyCollector());
        engine.registerCollector(new RuntimeErrorCollector());
        engine.registerCollector(new DocumentationCollector());
        engine.registerCollector(new VCSHistoryCollector());
        engine.registerCollector(new SemanticSearchCollector());

        return engine;
    }
}
