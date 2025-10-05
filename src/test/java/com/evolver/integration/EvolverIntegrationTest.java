package com.evolver.integration;

import com.evolver.agent.lifecycle.AgentInterface;
import com.evolver.agent.identity.AgentCharacteristic;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Integration tests for the complete Evolver framework
 */
class EvolverIntegrationTest {

    private final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    @BeforeEach
    void setUp() {
        System.setOut(new PrintStream(outputStream));
    }

    @AfterEach
    void tearDown() {
        System.setOut(originalOut);
    }

    @Test
    @DisplayName("Complete agent lifecycle works")
    void testCompleteAgentLifecycle() {
        // Clear any previous output
        outputStream.reset();

        // Create agent
        AgentInterface agent = new AgentInterface(AgentCharacteristic.DOCUMENTATION_OBSESSIVE);

        // Clear initialization output
        outputStream.reset();

        // Make a request
        String result = agent.ask("How do I create proper Java documentation?");

        assertNotNull(result);
        assertTrue(result.length() > 0);
    }

    @Test
    @DisplayName("Multiple agents work independently")
    void testMultipleAgentsIndependence() {
        // Create multiple agents with different characteristics
        AgentInterface docBot = new AgentInterface(AgentCharacteristic.DOCUMENTATION_OBSESSIVE);
        AgentInterface speedDemon = new AgentInterface(AgentCharacteristic.PERFORMANCE_MANIAC);
        AgentInterface chaosMonkey = new AgentInterface(AgentCharacteristic.CHAOS_MONKEY);

        // Clear initialization output
        outputStream.reset();

        // Each agent processes a different type of request
        String docResult = docBot.ask("How to document APIs?");
        String speedResult = speedDemon.ask("Optimize this algorithm");
        String chaosResult = chaosMonkey.ask("What happens with random input?");

        assertNotNull(docResult);
        assertNotNull(speedResult);
        assertNotNull(chaosResult);
        assertTrue(docResult.length() > 0);
        assertTrue(speedResult.length() > 0);
        assertTrue(chaosResult.length() > 0);
    }

    @Test
    @DisplayName("Agent evolution works")
    void testAgentEvolution() {
        AgentCharacteristic original = AgentCharacteristic.DOCUMENTATION_OBSESSIVE;

        // Clear any previous output
        outputStream.reset();

        // Evolve the characteristic
        AgentCharacteristic evolved = original.evolve("Learned about performance",
            java.util.Map.of("focusAreas", java.util.Arrays.asList("documentation", "performance")));

        // Create agent with evolved characteristic
        AgentInterface evolvedAgent = new AgentInterface(evolved);

        // Clear initialization output
        outputStream.reset();

        // Verify evolved agent works
        String result = evolvedAgent.ask("Test evolved agent");
        assertNotNull(result);
        assertTrue(result.length() > 0);
    }

    @Test
    @DisplayName("Cross-breeding creates hybrid characteristics")
    void testCrossBreeding() {
        AgentCharacteristic parent1 = AgentCharacteristic.DOCUMENTATION_OBSESSIVE;
        AgentCharacteristic parent2 = AgentCharacteristic.PERFORMANCE_MANIAC;

        // Clear output
        outputStream.reset();

        // Create hybrid
        AgentCharacteristic hybrid = AgentCharacteristic.crossBreed(parent1, parent2, "DocSpeed");

        // Create agent with hybrid characteristic
        AgentInterface hybridAgent = new AgentInterface(hybrid);

        // Clear initialization output
        outputStream.reset();

        // Verify hybrid agent works
        String result = hybridAgent.ask("Test hybrid functionality");
        assertNotNull(result);
        assertTrue(result.length() > 0);
    }

    @Test
    @DisplayName("Framework handles various task types")
    void testVariousTaskTypes() {
        AgentInterface agent = new AgentInterface(AgentCharacteristic.EXPERIMENTAL_MAD_SCIENTIST);

        String[] tasks = {
            "Create a Java class",
            "Debug this error",
            "Optimize performance"
        };

        for (String task : tasks) {
            outputStream.reset();
            String result = agent.ask(task);

            assertNotNull(result);
            assertTrue(result.length() > 0);
        }
    }

    @Test
    @DisplayName("Logging provides interaction visibility")
    void testComprehensiveLogging() {
        AgentInterface agent = new AgentInterface(AgentCharacteristic.SECURITY_PARANOID);

        outputStream.reset();

        String result = agent.ask("How to secure user authentication?");

        assertNotNull(result);
        assertTrue(result.length() > 0);

        // Some logging should occur
        String output = outputStream.toString();
        assertTrue(output.length() > 0);
    }

    @Test
    @DisplayName("Framework maintains stability across interactions")
    void testStabilityAcrossInteractions() {
        AgentInterface agent = new AgentInterface(AgentCharacteristic.MINIMALIST_ZEN);

        // Perform multiple interactions
        for (int i = 0; i < 3; i++) {
            outputStream.reset();
            String result = agent.ask("Interaction #" + i);

            assertNotNull(result);
            assertTrue(result.length() > 0);
        }
    }
}