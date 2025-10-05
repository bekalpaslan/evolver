package com.evolver.test;

import com.evolver.agent.ExperienceRepository;

/**
 * Test dirty data prevention by attempting to save low-quality experience
 */
public class TestDirtyDataPrevention {
    public static void main(String[] args) {
        System.out.println("🧪 TESTING DIRTY DATA PREVENTION");
        System.out.println("Attempting to save low-quality experience...");
        
        try {
            // This should be REJECTED by the strict validation
            ExperienceRepository.record()
                .category("test")  // FORBIDDEN category
                .technology("TestTech", "unknown", "test-framework")  // FORBIDDEN tech
                .recommendation("This is a test")  // Generic content
                .save();
                
            System.out.println("❌ FAILURE: Low-quality experience was saved (should have been rejected)");
        } catch (Exception e) {
            System.out.println("✅ SUCCESS: Low-quality experience was rejected as expected");
            System.out.println("Rejection reason: " + e.getMessage());
        }
        
        try {
            // This should be ACCEPTED
            ExperienceRepository.record()
                .category("performance-optimization")
                .technology("Spring Boot", "3.2.0", "web-framework")
                .recommendation("Implementing connection pooling with HikariCP significantly improved database performance, reducing average query time from 150ms to 45ms in our production environment with 10,000+ concurrent users")
                .rating("easeOfUse", 8.5)
                .rating("performance", 9.2)
                .save();
                
            System.out.println("✅ SUCCESS: High-quality experience was accepted");
        } catch (Exception e) {
            System.out.println("❌ FAILURE: High-quality experience was rejected");
            System.out.println("Error: " + e.getMessage());
        }
    }
}