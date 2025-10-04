package com.evolver.context;

import com.evolver.context.collectors.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for ContextEngine
 */
class ContextEngineTest {

    private ContextEngine engine;
    private ContextConfig config;

    @BeforeEach
    void setUp() {
        config = ContextConfig.builder()
            .minRelevanceThreshold(0.3)
            .reservedBudgetRatio(0.1)
            .addRequiredAspect("code")
            .build();

        engine = new ContextEngine(config);
        engine.registerCollector(new CodeStructureCollector());
        engine.registerCollector(new DependencyCollector());
    }

    @Test
    void testGatherContext_withValidRequest() {
        ContextRequest request = ContextRequest.builder()
            .taskDescription("Test task")
            .taskType(TaskType.CODE_GENERATION)
            .addParameter("file_path", "Test.java")
            .addParameter("project_path", "src/test")
            .tokenBudget(5000)
            .build();

        CompletableFuture<ContextPackage> future = engine.gatherContext(request);
        ContextPackage result = future.join();

        assertNotNull(result);
        assertNotNull(result.getFragments());
        assertTrue(result.getEstimatedTokens() <= 5000);
    }

    @Test
    void testAnalyzeContext_returnsMetrics() {
        ContextRequest request = ContextRequest.builder()
            .taskDescription("Test task")
            .taskType(TaskType.CODE_GENERATION)
            .addParameter("file_path", "Test.java")
            .addParameter("project_path", "src/test")
            .build();

        ContextPackage pkg = engine.gatherContext(request).join();
        ContextMetrics metrics = engine.analyzeContext(pkg);

        assertNotNull(metrics);
        assertTrue(metrics.getRelevanceScore() >= 0.0);
        assertTrue(metrics.getRelevanceScore() <= 1.0);
        assertTrue(metrics.getCoverage() >= 0.0);
        assertTrue(metrics.getCoverage() <= 1.0);
    }

    @Test
    void testContextPackage_rendering() {
        ContextRequest request = ContextRequest.builder()
            .taskDescription("Render test")
            .taskType(TaskType.CODE_GENERATION)
            .addParameter("file_path", "Test.java")
            .build();

        ContextPackage pkg = engine.gatherContext(request).join();
        String rendered = pkg.render();

        assertNotNull(rendered);
        assertTrue(rendered.contains("Render test"));
    }

    @Test
    void testContextFilter_removesIrrelevantFragments() {
        ContextFilter filter = new ContextFilter(config);
        ContextRequest request = ContextRequest.builder()
            .taskDescription("Test")
            .taskType(TaskType.CODE_GENERATION)
            .build();

        ContextFragment highRelevance = ContextFragment.builder()
            .source("test")
            .type(ContextType.CODE_STRUCTURE)
            .content("high relevance")
            .relevanceScore(0.8)
            .build();

        ContextFragment lowRelevance = ContextFragment.builder()
            .source("test")
            .type(ContextType.CODE_STRUCTURE)
            .content("low relevance")
            .relevanceScore(0.1)
            .build();

        var fragments = java.util.Arrays.asList(highRelevance, lowRelevance);
        var filtered = filter.filter(fragments, request);

        assertEquals(1, filtered.size());
        assertEquals(0.8, filtered.get(0).getRelevanceScore());
    }

    @Test
    void testContextPrioritizer_respectsTokenBudget() {
        ContextPrioritizer prioritizer = new ContextPrioritizer(config);
        ContextRequest request = ContextRequest.builder()
            .taskDescription("Test")
            .taskType(TaskType.CODE_GENERATION)
            .tokenBudget(1000)
            .build();

        ContextFragment fragment1 = ContextFragment.builder()
            .source("test1")
            .type(ContextType.CODE_STRUCTURE)
            .content("a".repeat(2000)) // ~500 tokens
            .relevanceScore(0.9)
            .build();

        ContextFragment fragment2 = ContextFragment.builder()
            .source("test2")
            .type(ContextType.CODE_STRUCTURE)
            .content("b".repeat(2000)) // ~500 tokens
            .relevanceScore(0.8)
            .build();

        ContextFragment fragment3 = ContextFragment.builder()
            .source("test3")
            .type(ContextType.CODE_STRUCTURE)
            .content("c".repeat(2000)) // ~500 tokens
            .relevanceScore(0.7)
            .build();

        var fragments = java.util.Arrays.asList(fragment1, fragment2, fragment3);
        var prioritized = prioritizer.prioritize(fragments, request);

        // Should select highest relevance fragments within budget
        assertTrue(prioritized.size() <= 2); // Can't fit all 3
        int totalTokens = prioritized.stream()
            .mapToInt(ContextFragment::getEstimatedTokens)
            .sum();
        assertTrue(totalTokens <= 1000);
    }
}
