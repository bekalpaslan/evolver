package com.evolver.context;

import java.util.List;

/**
 * REQUEST FOR CONTEXT DEDUPLICATION
 * 
 * Represents a request to analyze and deduplicate context fragments
 * for a specific session and agent interaction.
 */
public class ContextDeduplicationRequest {
    private final String sessionId;
    private final String agentId;
    private final List<ContextFragment> fragments;
    private final String taskType;
    private final ContextPreferences preferences;
    
    private ContextDeduplicationRequest(Builder builder) {
        this.sessionId = builder.sessionId;
        this.agentId = builder.agentId;
        this.fragments = builder.fragments;
        this.taskType = builder.taskType;
        this.preferences = builder.preferences;
    }
    
    public String getSessionId() { return sessionId; }
    public String getAgentId() { return agentId; }
    public List<ContextFragment> getFragments() { return fragments; }
    public String getTaskType() { return taskType; }
    public ContextPreferences getPreferences() { return preferences; }
    
    public static Builder builder() {
        return new Builder();
    }
    
    public static class Builder {
        private String sessionId;
        private String agentId;
        private List<ContextFragment> fragments;
        private String taskType;
        private ContextPreferences preferences;
        
        public Builder sessionId(String sessionId) {
            this.sessionId = sessionId;
            return this;
        }
        
        public Builder agentId(String agentId) {
            this.agentId = agentId;
            return this;
        }
        
        public Builder fragments(List<ContextFragment> fragments) {
            this.fragments = fragments;
            return this;
        }
        
        public Builder taskType(String taskType) {
            this.taskType = taskType;
            return this;
        }
        
        public Builder preferences(ContextPreferences preferences) {
            this.preferences = preferences;
            return this;
        }
        
        public ContextDeduplicationRequest build() {
            return new ContextDeduplicationRequest(this);
        }
    }
}