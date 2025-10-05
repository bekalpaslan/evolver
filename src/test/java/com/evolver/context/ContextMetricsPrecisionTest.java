package com.evolver.context;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for 0.1 precision validation in ContextMetrics
 */
class ContextMetricsPrecisionTest {

    @Test
    void testRelevanceScorePrecisionValidation() {
        // Valid 0.1 precision values should work
        assertDoesNotThrow(() -> {
            ContextMetrics.builder()
                .relevanceScore(8.7)
                .build();
        });

        assertDoesNotThrow(() -> {
            ContextMetrics.builder()
                .relevanceScore(9.0)
                .build();
        });

        // Invalid precision should throw exception
        assertThrows(IllegalArgumentException.class, () -> {
            ContextMetrics.builder()
                .relevanceScore(8.75) // Too precise
                .build();
        });

        assertThrows(IllegalArgumentException.class, () -> {
            ContextMetrics.builder()
                .relevanceScore(8.567) // Too precise
                .build();
        });
    }

    @Test
    void testCoveragePrecisionValidation() {
        // Valid 0.1 precision values should work
        assertDoesNotThrow(() -> {
            ContextMetrics.builder()
                .coverage(0.8)
                .build();
        });

        // Invalid precision should throw exception
        assertThrows(IllegalArgumentException.class, () -> {
            ContextMetrics.builder()
                .coverage(0.856) // Too precise
                .build();
        });
    }

    @Test
    void testPrecisionErrorMessage() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            ContextMetrics.builder()
                .relevanceScore(8.756)
                .build();
        });

        String message = exception.getMessage();
        assertTrue(message.contains("must have 0.1 precision"));
        assertTrue(message.contains("got: 8.756"));
        assertTrue(message.contains("Use: 8.8")); // Should suggest rounded value
    }

    @Test
    void testValidMetricsCreation() {
        ContextMetrics metrics = ContextMetrics.builder()
            .totalTokens(1000)
            .fragmentCount(15)
            .relevanceScore(8.7)
            .coverage(0.9)
            .build();

        assertEquals(1000, metrics.getTotalTokens());
        assertEquals(15, metrics.getFragmentCount());
        assertEquals(8.7, metrics.getRelevanceScore());
        assertEquals(0.9, metrics.getCoverage());
    }
}