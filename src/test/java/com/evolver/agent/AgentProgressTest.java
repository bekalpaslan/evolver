package com.evolver.agent;

import com.evolver.agent.lifecycle.AgentProgress;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for AgentProgress tracking system
 */
class AgentProgressTest {

    private AgentProgress progress;

    @BeforeEach
    void setUp() {
        progress = new AgentProgress();
    }

    @Test
    void testCollectorCreationTracking() {
        assertFalse(progress.hasGraduated());
        assertEquals(0, progress.getCollectorsCreated());

        progress.recordCollectorCreation("TestCollector");
        assertEquals(1, progress.getCollectorsCreated());
    }

    @Test
    void testExperimentTracking() {
        assertEquals(0, progress.getExperimentsRun());

        progress.recordExperimentCompletion("exp-001", true);
        assertEquals(1, progress.getExperimentsRun());

        progress.recordExperimentCompletion("exp-002", false);
        assertEquals(2, progress.getExperimentsRun());
    }

    @Test
    void testBaselineImprovementTracking() {
        assertEquals(0.0, progress.getBaselineImprovement());

        progress.recordBaselineImprovement(15.5, "context relevance");
        assertEquals(15.5, progress.getBaselineImprovement());

        // Should only update if improvement is higher
        progress.recordBaselineImprovement(12.0, "other area");
        assertEquals(15.5, progress.getBaselineImprovement());

        progress.recordBaselineImprovement(22.3, "efficiency");
        assertEquals(22.3, progress.getBaselineImprovement());
    }

    @Test
    void testRuleChallengeTracking() {
        assertEquals(0, progress.getRulesChallenged());

        progress.recordRuleChallenge("rule-001", "Should be dynamic");
        assertEquals(1, progress.getRulesChallenged());
    }

    @Test
    void testGraduationCriteria() {
        assertFalse(progress.hasGraduated());

        // Partially meet criteria
        progress.recordCollectorCreation("TestCollector");
        progress.recordBaselineImprovement(25.0, "test");
        assertFalse(progress.hasGraduated());

        // Meet all criteria
        for (int i = 0; i < 10; i++) {
            progress.recordExperimentCompletion("exp-" + i, true);
        }
        progress.recordRuleChallenge("rule-001", "Challenge 1");
        progress.recordRuleChallenge("rule-002", "Challenge 2");
        progress.recordRuleChallenge("rule-003", "Challenge 3");

        assertTrue(progress.hasGraduated());
    }

    @Test
    void testMilestoneTracking() {
        assertTrue(progress.getMilestones().isEmpty());

        progress.recordCollectorCreation("TestCollector");
        assertEquals(1, progress.getMilestones().size());
        assertTrue(progress.getMilestones().get(0).contains("TestCollector"));
    }
}