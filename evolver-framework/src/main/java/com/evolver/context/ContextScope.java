package com.evolver.context;

/**
 * Defines the scope of context gathering
 */
public enum ContextScope {
    /**
     * Only the immediate context (current file, method)
     */
    MINIMAL,

    /**
     * Local context (current file and direct dependencies)
     */
    LOCAL,

    /**
     * Module context (current module/package)
     */
    MODULE,

    /**
     * Project context (entire project)
     */
    PROJECT,

    /**
     * Extended context (project + external resources)
     */
    EXTENDED,

    /**
     * Global context (everything available)
     */
    GLOBAL
}
