package com.evolver.context;

/**
 * Types of tasks that AI agents perform
 */
public enum TaskType {
    // Code generation
    CODE_GENERATION,        // Generate new code
    CODE_COMPLETION,        // Complete partial code
    CODE_REFACTORING,       // Refactor existing code

    // Analysis
    CODE_REVIEW,           // Review code quality
    BUG_DETECTION,         // Find bugs
    PERFORMANCE_ANALYSIS,  // Analyze performance
    SECURITY_ANALYSIS,     // Security audit

    // Documentation
    DOCUMENTATION,         // Generate documentation
    EXPLANATION,           // Explain code

    // Testing
    TEST_GENERATION,       // Generate tests
    TEST_DEBUGGING,        // Debug failing tests

    // Debugging
    BUG_FIXING,           // Fix bugs
    ERROR_DIAGNOSIS,      // Diagnose errors

    // Architecture
    DESIGN,               // Design solutions
    ARCHITECTURE_REVIEW,  // Review architecture

    // General
    GENERAL,              // General purpose task
    QUESTION_ANSWERING    // Answer questions
}
