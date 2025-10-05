package com.evolver.agent;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for RuleChallenge system
 */
class RuleChallengeTest {

    @Test
    void testEvidenceCreation() {
        RuleChallenge.Evidence evidence = new RuleChallenge.Evidence()
            .before(8.5, "seconds")
            .after(6.2, "seconds")
            .sampleSize(20)
            .confidence(9.1);

        assertEquals(8.5, evidence.beforeMetric);
        assertEquals("seconds", evidence.beforeUnit);
        assertEquals(6.2, evidence.afterMetric);
        assertEquals(20, evidence.sampleSize);
        assertEquals(9.1, evidence.confidenceLevel);
    }

    @Test
    void testImprovementCalculation() {
        RuleChallenge.Evidence evidence = new RuleChallenge.Evidence()
            .before(10.0, "seconds")
            .after(8.0, "seconds");

        assertEquals(-20.0, evidence.getImprovementPercentage()); // 20% improvement (reduction)
    }

    @Test
    void testZeroPrecisionValidation() {
        RuleChallenge.Evidence evidence = new RuleChallenge.Evidence()
            .before(8.567, "ms") // Should round to 8.6
            .after(7.123, "ms");  // Should round to 7.1

        assertEquals(8.6, evidence.beforeMetric);
        assertEquals(7.1, evidence.afterMetric);
    }

    @Test
    void testConfidenceValidation() {
        assertThrows(IllegalArgumentException.class, () -> {
            new RuleChallenge.Evidence().confidence(11.0); // Over 10.0
        });

        assertThrows(IllegalArgumentException.class, () -> {
            new RuleChallenge.Evidence().confidence(-1.0); // Under 0.0
        });

        // Valid confidence levels should work
        assertDoesNotThrow(() -> {
            new RuleChallenge.Evidence().confidence(7.5);
        });
    }

    @Test
    void testCompellingEvidence() {
        RuleChallenge.Evidence compelling = new RuleChallenge.Evidence()
            .before(10.0, "seconds")
            .after(8.0, "seconds")
            .sampleSize(10)
            .confidence(8.0);

        assertTrue(compelling.isCompelling()); // 20% improvement, confidence 8.0, samples 10

        RuleChallenge.Evidence notCompelling = new RuleChallenge.Evidence()
            .before(10.0, "seconds")
            .after(9.5, "seconds") // Only 5% improvement
            .sampleSize(3)         // Low sample size
            .confidence(6.0);      // Low confidence

        assertFalse(notCompelling.isCompelling());
    }

    @Test
    void testSuccessRateDescription() {
        RuleChallenge.Evidence evidence = new RuleChallenge.Evidence()
            .before(100.0, "ms")
            .after(75.0, "ms");

        assertEquals("100.0 ms → 75.0 ms", evidence.getSuccessRateDescription());
    }
}