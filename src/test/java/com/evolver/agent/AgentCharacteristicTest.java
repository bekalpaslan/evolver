package com.evolver.agent;

import com.evolver.agent.identity.AgentCharacteristic;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Comprehensive tests for AgentCharacteristic
 */
class AgentCharacteristicTest {

    @Test
    @DisplayName("Predefined characteristics should have correct properties")
    void testPredefinedCharacteristics() {
        // Test DocBot
        assertEquals("DocBot", AgentCharacteristic.DOCUMENTATION_OBSESSIVE.getName());
        assertTrue(AgentCharacteristic.DOCUMENTATION_OBSESSIVE.getPersonality().contains("documentation"));
        assertEquals(0.4, AgentCharacteristic.DOCUMENTATION_OBSESSIVE.getTokenBudgetPreference());
        assertEquals(0.2, AgentCharacteristic.DOCUMENTATION_OBSESSIVE.getRelevanceThreshold());

        // Test CleanFreak
        assertEquals("CleanFreak", AgentCharacteristic.CLEAN_CODE_FREAK.getName());
        assertTrue(AgentCharacteristic.CLEAN_CODE_FREAK.getPersonality().contains("clean code"));
        assertEquals(0.3, AgentCharacteristic.CLEAN_CODE_FREAK.getTokenBudgetPreference());

        // Test SpeedDemon
        assertEquals("SpeedDemon", AgentCharacteristic.PERFORMANCE_MANIAC.getName());
        assertTrue(AgentCharacteristic.PERFORMANCE_MANIAC.getPersonality().contains("performance"));
        assertEquals(0.25, AgentCharacteristic.PERFORMANCE_MANIAC.getTokenBudgetPreference());
    }

    @Test
    @DisplayName("Custom characteristics can be created")
    void testCustomCharacteristic() {
        Map<String, Object> traits = Map.of(
            "priorityCollectors", Arrays.asList("TestCollector"),
            "tokenBudgetPreference", 0.5,
            "relevanceThreshold", 0.6,
            "focusAreas", Arrays.asList("testing"),
            "evolutionStrategy", "test-driven",
            "catchphrase", "Test everything!"
        );

        AgentCharacteristic custom = AgentCharacteristic.custom("TestBot", "Testing personality", traits);

        assertEquals("TestBot", custom.getName());
        assertEquals("Testing personality", custom.getPersonality());
        assertEquals(0.5, custom.getTokenBudgetPreference());
        assertEquals(0.6, custom.getRelevanceThreshold());
        assertEquals("test-driven", custom.getEvolutionStrategy());
        assertEquals("Test everything!", custom.getCatchphrase());
    }

    @Test
    @DisplayName("Priority collectors are correctly retrieved")
    void testPriorityCollectors() {
        List<String> docBotCollectors = AgentCharacteristic.DOCUMENTATION_OBSESSIVE.getPriorityCollectors();
        assertTrue(docBotCollectors.contains("DocumentationCollector"));
        assertTrue(docBotCollectors.contains("CodeStructureCollector"));

        List<String> speedDemonCollectors = AgentCharacteristic.PERFORMANCE_MANIAC.getPriorityCollectors();
        assertTrue(speedDemonCollectors.contains("RuntimeErrorCollector"));
        assertTrue(speedDemonCollectors.contains("CodeStructureCollector"));
    }

    @Test
    @DisplayName("Focus areas are correctly retrieved")
    void testFocusAreas() {
        List<String> docBotFocus = AgentCharacteristic.DOCUMENTATION_OBSESSIVE.getFocusAreas();
        assertTrue(docBotFocus.contains("documentation"));
        assertTrue(docBotFocus.contains("comments"));

        List<String> paranoidFocus = AgentCharacteristic.SECURITY_PARANOID.getFocusAreas();
        assertTrue(paranoidFocus.contains("security"));
        assertTrue(paranoidFocus.contains("vulnerabilities"));
    }

    @Nested
    @DisplayName("Evolution Tests")
    class EvolutionTests {

        @Test
        @DisplayName("Agent can evolve with new traits")
        void testEvolution() {
            AgentCharacteristic original = AgentCharacteristic.DOCUMENTATION_OBSESSIVE;
            AgentCharacteristic evolved = original.evolve("Learned performance matters",
                Map.of("focusAreas", Arrays.asList("documentation", "performance")));

            assertTrue(evolved.getName().startsWith("DocBot v"));
            assertTrue(evolved.getPersonality().contains("Evolved"));
            assertTrue(evolved.getFocusAreas().contains("performance"));
        }

        @Test
        @DisplayName("Evolution history is tracked")
        void testEvolutionHistory() {
            AgentCharacteristic original = AgentCharacteristic.DOCUMENTATION_OBSESSIVE;
            AgentCharacteristic evolved = original.evolve("Test evolution",
                Map.of("tokenBudgetPreference", 0.6));

            Map<String, Object> history = evolved.getEvolutionHistory();
            assertFalse(history.isEmpty());
            assertEquals(1, history.size());
            String key = history.keySet().iterator().next();
            assertTrue(key.startsWith("evolution_"));
            @SuppressWarnings("unchecked")
            Map<String, Object> evolutionData = (Map<String, Object>) history.get(key);
            assertEquals("Test evolution", evolutionData.get("reason"));
            @SuppressWarnings("unchecked")
            Map<String, Object> changes = (Map<String, Object>) evolutionData.get("changes");
            assertEquals(0.6, changes.get("tokenBudgetPreference"));
        }

        @Test
        @DisplayName("Evolution decision logic works correctly")
        void testEvolutionDecision() {
            AgentCharacteristic docBot = AgentCharacteristic.DOCUMENTATION_OBSESSIVE;
            AgentCharacteristic speedDemon = AgentCharacteristic.PERFORMANCE_MANIAC;
            AgentCharacteristic chaosMonkey = AgentCharacteristic.CHAOS_MONKEY;

            // DocBot evolves for documentation issues
            assertTrue(docBot.shouldEvolveFramework("documentation unclear", Map.of()));
            assertFalse(docBot.shouldEvolveFramework("performance slow", Map.of()));

            // SpeedDemon evolves for performance issues
            assertTrue(speedDemon.shouldEvolveFramework("performance slow", Map.of()));
            assertFalse(speedDemon.shouldEvolveFramework("documentation unclear", Map.of()));

            // ChaosMonkey is unpredictable
            // (We can't test random behavior reliably, but we can test the method exists)
            chaosMonkey.shouldEvolveFramework("anything", Map.of());
        }
    }

    @Nested
    @DisplayName("Cross-breeding Tests")
    class CrossBreedingTests {

        @Test
        @DisplayName("Agents can cross-breed to create hybrids")
        void testCrossBreeding() {
            AgentCharacteristic parent1 = AgentCharacteristic.DOCUMENTATION_OBSESSIVE;
            AgentCharacteristic parent2 = AgentCharacteristic.PERFORMANCE_MANIAC;

            AgentCharacteristic hybrid = AgentCharacteristic.crossBreed(parent1, parent2, "DocSpeed");

            assertEquals("DocSpeed", hybrid.getName());
            assertTrue(hybrid.getPersonality().contains("Hybrid"));
            assertTrue(hybrid.getPersonality().contains("DocBot"));
            assertTrue(hybrid.getPersonality().contains("SpeedDemon"));
        }

        @Test
        @DisplayName("Hybrid traits are intelligently merged")
        void testHybridTraits() {
            AgentCharacteristic parent1 = AgentCharacteristic.custom("Parent1", "Test",
                Map.of("tokenBudgetPreference", 0.2, "focusAreas", Arrays.asList("area1")));
            AgentCharacteristic parent2 = AgentCharacteristic.custom("Parent2", "Test",
                Map.of("tokenBudgetPreference", 0.8, "focusAreas", Arrays.asList("area2")));

            AgentCharacteristic hybrid = AgentCharacteristic.crossBreed(parent1, parent2, "Hybrid");

            // Numeric values should be averaged
            assertEquals(0.5, hybrid.getTokenBudgetPreference());

            // Lists should be merged
            List<String> focusAreas = hybrid.getFocusAreas();
            assertTrue(focusAreas.contains("area1"));
            assertTrue(focusAreas.contains("area2"));
        }
    }

    @Test
    @DisplayName("ToString provides meaningful representation")
    void testToString() {
        AgentCharacteristic docBot = AgentCharacteristic.DOCUMENTATION_OBSESSIVE;
        String str = docBot.toString();

        assertTrue(str.contains("DocBot"));
        assertTrue(str.contains("documentation"));
    }

    @Test
    @DisplayName("All predefined characteristics are accessible")
    void testAllPredefinedCharacteristics() {
        AgentCharacteristic[] allCharacteristics = {
            AgentCharacteristic.DOCUMENTATION_OBSESSIVE,
            AgentCharacteristic.CLEAN_CODE_FREAK,
            AgentCharacteristic.PERFORMANCE_MANIAC,
            AgentCharacteristic.SECURITY_PARANOID,
            AgentCharacteristic.CHAOS_MONKEY,
            AgentCharacteristic.MINIMALIST_ZEN,
            AgentCharacteristic.STEALTH_LEARNER,
            AgentCharacteristic.EXPERIMENTAL_MAD_SCIENTIST
        };

        for (AgentCharacteristic characteristic : allCharacteristics) {
            assertNotNull(characteristic.getName());
            assertNotNull(characteristic.getPersonality());
            assertNotNull(characteristic.getTraits());
            assertTrue(characteristic.getTokenBudgetPreference() >= 0.0);
            assertTrue(characteristic.getTokenBudgetPreference() <= 1.0);
            assertTrue(characteristic.getRelevanceThreshold() >= 0.0);
            assertTrue(characteristic.getRelevanceThreshold() <= 1.0);
        }
    }
}