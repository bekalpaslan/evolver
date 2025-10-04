package com.evolver.context;

/**
 * Types of context that can be collected
 */
public enum ContextType {
    // Code-related
    CODE_STRUCTURE,          // Class/method definitions, interfaces
    CODE_IMPLEMENTATION,     // Actual implementation details
    CODE_DEPENDENCIES,       // Imports, dependencies, relationships
    CODE_COMMENTS,           // Documentation and comments

    // Project-related
    PROJECT_STRUCTURE,       // Directory structure, build files
    PROJECT_CONFIGURATION,   // Config files, settings
    PROJECT_DOCUMENTATION,   // README, docs

    // Execution-related
    RUNTIME_STATE,          // Current execution state
    RUNTIME_LOGS,           // Application logs
    RUNTIME_ERRORS,         // Error messages, stack traces

    // Version control
    VCS_HISTORY,            // Git history, blame
    VCS_DIFF,               // Current changes
    VCS_BRANCHES,           // Branch information

    // Environment
    ENVIRONMENT_VARIABLES,  // Env vars
    ENVIRONMENT_SYSTEM,     // System information

    // Task-related
    TASK_DESCRIPTION,       // User's task description
    TASK_HISTORY,           // Previous related tasks
    TASK_CONSTRAINTS,       // Constraints and requirements

    // Domain knowledge
    DOMAIN_PATTERNS,        // Design patterns, conventions
    DOMAIN_BEST_PRACTICES,  // Best practices for the domain
    DOMAIN_EXAMPLES,        // Example code, templates

    // External resources
    EXTERNAL_API,           // API documentation
    EXTERNAL_LIBRARY,       // Library documentation
    EXTERNAL_WEB           // Web searches, StackOverflow
}
