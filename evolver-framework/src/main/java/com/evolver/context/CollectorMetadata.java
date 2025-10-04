package com.evolver.context;

/**
 * Metadata about a context collector
 */
public class CollectorMetadata {
    private final String name;
    private final String description;
    private final String version;
    private final CollectorType type;

    public CollectorMetadata(String name, String description, String version, CollectorType type) {
        this.name = name;
        this.description = description;
        this.version = version;
        this.type = type;
    }

    public String getName() { return name; }
    public String getDescription() { return description; }
    public String getVersion() { return version; }
    public CollectorType getType() { return type; }

    public enum CollectorType {
        STATIC,      // Analyzes static code
        DYNAMIC,     // Analyzes runtime behavior
        EXTERNAL,    // Fetches external resources
        HYBRID       // Combination of multiple approaches
    }
}
