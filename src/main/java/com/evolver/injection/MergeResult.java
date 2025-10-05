package com.evolver.injection;

/**
 * Result of a framework merge operation
 */
public class MergeResult {
    private final boolean success;
    private final String message;

    public MergeResult(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }
}