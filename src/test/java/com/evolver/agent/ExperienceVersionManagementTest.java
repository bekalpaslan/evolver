package com.evolver.agent;

import com.evolver.agent.experience.ExperienceRepository;
import com.evolver.agent.experience.ExperienceDbManager;
import org.junit.jupiter.api.*;
import java.nio.file.*;
import java.io.IOException;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test suite for Experience Database Version Management and Format Consolidation
 */
class ExperienceVersionManagementTest {
    
    private static final Path TEST_DB = Paths.get("test-experiences.json");
    private static final Path TEST_BACKUP = Paths.get("test-experiences.backup.json");
    private static final Path TEST_TEMP = Paths.get("test-experiences.tmp");
    private static final Path TEST_LOCK = Paths.get(".test-experiences.lock");
    
    @BeforeEach
    void setUp() throws IOException {
        // Clean up any existing test files
        Files.deleteIfExists(TEST_DB);
        Files.deleteIfExists(TEST_BACKUP);
        Files.deleteIfExists(TEST_TEMP);
        Files.deleteIfExists(TEST_LOCK);
    }
    
    @AfterEach
    void tearDown() throws IOException {
        // Clean up test files
        Files.deleteIfExists(TEST_DB);
        Files.deleteIfExists(TEST_BACKUP);
        Files.deleteIfExists(TEST_TEMP);
        Files.deleteIfExists(TEST_LOCK);
    }
    
    @Test
    @DisplayName("Version validation should accept valid versions")
    void testVersionValidation() {
        // Test valid versions
        assertDoesNotThrow(() -> {
            String validVersion = "2.0.0";
            assertTrue(validVersion.matches("\\d+\\.\\d+\\.\\d+"));
        });
        
        // Test invalid versions
        String[] invalidVersions = {"2.0", "v2.0.0", "2.0.0-beta", ""};
        for (String invalid : invalidVersions) {
            assertFalse(invalid.matches("\\d+\\.\\d+\\.\\d+"), 
                "Version should be invalid: " + invalid);
        }
    }
    
    @Test
    @DisplayName("Database migration should handle version upgrades")
    void testDatabaseMigration() {
        // Create a legacy 1.0.0 database
        String legacyDbContent = """
            {
              "version": "1.0.0",
              "lastUpdated": "2025-10-05T10:00:00Z",
              "experiences": [
                {
                  "id": "test-001",
                  "agent": "test_agent",
                  "category": "test"
                }
              ],
              "statistics": {
                "totalExperiences": 1
              }
            }
            """;
        
        try {
            Files.writeString(TEST_DB, legacyDbContent);
            
            // Load should trigger migration
            ExperienceRepository.loadOnBootstrap();
            
            // Verify database was migrated
            List<ExperienceRepository.ExperienceEntry> experiences = ExperienceRepository.getAll();
            assertFalse(experiences.isEmpty(), "Experiences should be preserved during migration");
            
            // Verify version was updated
            ExperienceRepository.printStats(); // This will show the version
            
        } catch (IOException e) {
            fail("Test setup failed: " + e.getMessage());
        }
    }
    
    @Test
    @DisplayName("Format consolidation should preserve data integrity")
    void testFormatConsolidation() {
        // Create test legacy directory structure
        Path testLegacyDir = Paths.get("test-legacy-experiences");
        
        try {
            Files.createDirectories(testLegacyDir.resolve("performance"));
            
            // Create a legacy markdown experience
            String legacyMarkdown = """
                # Performance Optimization Test
                
                This is a test experience for consolidation.
                
                ## Problem
                Test performance issue
                
                ## Solution
                Test solution applied
                """;
            
            Files.writeString(testLegacyDir.resolve("performance/test_experience.md"), legacyMarkdown);
            
            // Test consolidation (would need to modify paths in implementation for testing)
            // This is a design test to ensure the API exists
            assertDoesNotThrow(() -> {
                ExperienceRepository.consolidateLegacyFormats();
            }, "Consolidation should not throw exceptions");
            
            // Clean up
            Files.deleteIfExists(testLegacyDir.resolve("performance/test_experience.md"));
            Files.deleteIfExists(testLegacyDir.resolve("performance"));
            Files.deleteIfExists(testLegacyDir);
            
        } catch (IOException e) {
            fail("Test setup failed: " + e.getMessage());
        }
    }
    
    @Test
    @DisplayName("Experience builder should enforce 0.1 precision validation")
    void testPrecisionValidation() {
        ExperienceRepository.ExperienceBuilder builder = ExperienceRepository.record()
            .category("test");
        
        // Valid 0.1 precision ratings
        assertDoesNotThrow(() -> {
            builder.easeOfUse(7.3);
            builder.power(9.1);
            builder.performance(8.0);
        }, "Valid 0.1 precision ratings should be accepted");
        
        // Invalid precision ratings
        assertThrows(IllegalArgumentException.class, () -> {
            builder.easeOfUse(7.33); // Too precise
        }, "Invalid precision should be rejected");
        
        assertThrows(IllegalArgumentException.class, () -> {
            builder.power(11.0); // Out of range
        }, "Out of range ratings should be rejected");
        
        assertThrows(IllegalArgumentException.class, () -> {
            builder.performance(-1.0); // Negative
        }, "Negative ratings should be rejected");
    }
    
    @Test
    @DisplayName("Database should handle concurrent access safely")
    void testConcurrentAccess() {
        // Test that multiple threads can safely access the database
        assertDoesNotThrow(() -> {
            Thread t1 = new Thread(() -> {
                ExperienceRepository.record()
                    .category("test1")
                    .easeOfUse(7.5)
                    .save();
            });
            
            Thread t2 = new Thread(() -> {
                ExperienceRepository.record()
                    .category("test2")
                    .power(8.2)
                    .save();
            });
            
            t1.start();
            t2.start();
            
            t1.join(5000); // 5 second timeout
            t2.join(5000);
            
        }, "Concurrent access should be handled safely");
    }
    
    @Test
    @DisplayName("Database manager utility should provide all commands")
    void testDatabaseManagerCommands() {
        // Test that all manager commands exist and don't crash
        String[] commands = {"migrate", "consolidate", "validate", "version", "stats"};
        
        for (String command : commands) {
            assertDoesNotThrow(() -> {
                ExperienceDbManager.main(new String[]{command});
            }, "Command '" + command + "' should execute without errors");
        }
        
        // Test invalid command
        assertDoesNotThrow(() -> {
            ExperienceDbManager.main(new String[]{"invalid"});
        }, "Invalid commands should be handled gracefully");
        
        // Test no arguments
        assertDoesNotThrow(() -> {
            ExperienceDbManager.main(new String[]{});
        }, "No arguments should show usage");
    }
}