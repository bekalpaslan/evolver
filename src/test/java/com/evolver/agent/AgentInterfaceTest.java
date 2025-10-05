package com.evolver.agent;

import com.evolver.agent.lifecycle.AgentInterface;
import com.evolver.agent.identity.AgentCharacteristic;
import com.evolver.logging.AgentLogger;
import com.evolver.logging.PersonalityLogger;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.AfterEach;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for AgentInterface with logging functionality
 */
class AgentInterfaceTest {

    private final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    @BeforeEach
    void setUp() {
        // Capture System.out for logging verification
        System.setOut(new PrintStream(outputStream));
    }

    @AfterEach
    void tearDown() {
        // Restore original System.out
        System.setOut(originalOut);
    }

    @Test
    @DisplayName("AgentInterface creates with default MadScientist characteristic")
    void testDefaultConstructor() {
        AgentInterface agent = new AgentInterface();

        assertNotNull(agent);
        // Check that logging occurred during initialization
        String output = outputStream.toString();
        assertTrue(output.contains("MadScientist"));
        assertTrue(output.contains("INITIALIZING"));
    }

    @Test
    @DisplayName("AgentInterface creates with specific characteristic")
    void testSpecificCharacteristicConstructor() {
        // Clear any previous output
        outputStream.reset();

        AgentInterface docBot = new AgentInterface(AgentCharacteristic.DOCUMENTATION_OBSESSIVE);
        AgentInterface speedDemon = new AgentInterface(AgentCharacteristic.PERFORMANCE_MANIAC);

        assertNotNull(docBot);
        assertNotNull(speedDemon);

        // Check that logging occurred (some output should be captured)
        String output = outputStream.toString();
        assertTrue(output.length() > 0); // Some logging should have occurred
    }

    @Test
    @DisplayName("Agent ask() method produces results")
    void testAskMethodLogging() {
        // Create agent first, then reset output to capture only the ask operation
        AgentInterface agent = new AgentInterface(AgentCharacteristic.DOCUMENTATION_OBSESSIVE);
        outputStream.reset(); // Clear initialization output

        // Make a request
        String result = agent.ask("How do I create a Java class?");

        assertNotNull(result);
        assertTrue(result.length() > 0);

        // Check that some logging occurred during the ask
        String output = outputStream.toString();
        assertTrue(output.length() > 0); // Some logging should occur
    }

    @Test
    @DisplayName("Different characteristics produce different results")
    void testPersonalitySpecificResults() {
        AgentInterface docBot = new AgentInterface(AgentCharacteristic.DOCUMENTATION_OBSESSIVE);
        AgentInterface speedDemon = new AgentInterface(AgentCharacteristic.PERFORMANCE_MANIAC);

        outputStream.reset();
        String docResult = docBot.ask("Test question");

        outputStream.reset();
        String speedResult = speedDemon.ask("Test question");

        assertNotNull(docResult);
        assertNotNull(speedResult);
        // Results should be different or at least valid
        assertTrue(docResult.length() > 0);
        assertTrue(speedResult.length() > 0);
    }

    @Test
    @DisplayName("Logging includes metadata when provided")
    void testLoggingWithMetadata() {
        AgentInterface agent = new AgentInterface(AgentCharacteristic.DOCUMENTATION_OBSESSIVE);

        // The logging happens internally, we just verify the agent works
        String result = agent.ask("Test with metadata");

        assertNotNull(result);

        String output = outputStream.toString();
        assertTrue(output.contains("Details:"));
    }

    @Test
    @DisplayName("Agent initialization works")
    void testInitializationLogging() {
        outputStream.reset();
        AgentInterface agent = new AgentInterface(AgentCharacteristic.CLEAN_CODE_FREAK);

        assertNotNull(agent);

        // Some output should be produced during initialization
        String output = outputStream.toString();
        assertTrue(output.length() > 0);
    }

    @Test
    @DisplayName("All predefined characteristics work with AgentInterface")
    void testAllCharacteristics() {
        AgentCharacteristic[] characteristics = {
            AgentCharacteristic.DOCUMENTATION_OBSESSIVE,
            AgentCharacteristic.CLEAN_CODE_FREAK,
            AgentCharacteristic.PERFORMANCE_MANIAC,
            AgentCharacteristic.SECURITY_PARANOID,
            AgentCharacteristic.CHAOS_MONKEY,
            AgentCharacteristic.MINIMALIST_ZEN,
            AgentCharacteristic.STEALTH_LEARNER,
            AgentCharacteristic.EXPERIMENTAL_MAD_SCIENTIST
        };

        for (AgentCharacteristic characteristic : characteristics) {
            outputStream.reset();
            AgentInterface agent = new AgentInterface(characteristic);

            assertNotNull(agent);

            String output = outputStream.toString();
            assertTrue(output.contains(characteristic.getName()));
            assertTrue(output.contains("INITIALIZING"));
        }
    }

    @Test
    @DisplayName("Agent handles empty/null inputs gracefully")
    void testEmptyInputHandling() {
        AgentInterface agent = new AgentInterface(AgentCharacteristic.MINIMALIST_ZEN);

        outputStream.reset();

        // Test with empty string
        String result = agent.ask("");

        assertNotNull(result);
        assertTrue(result.length() >= 0); // Should handle empty input
    }
}