package com.evolver.injection;

import com.evolver.injection.InjectionResult;

/**
 * Simple runner to test framework injection
 */
public class InjectionRunner {
    public static void main(String[] args) {
        System.out.println("Starting Evolver Framework Injection...");

        try {
            InjectionResult result = FrameworkInjector.quickInject();
            result.printStatus();

            System.out.println("\nInjection completed successfully!");
        } catch (Exception e) {
            System.err.println("Injection failed: " + e.getMessage());
            e.printStackTrace();
        }
    }
}