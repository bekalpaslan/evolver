package com.evolver.context;

import java.util.*;

/**
 * INTELLIGENT CONTEXT REQUEST
 * 
 * Enhanced context request that includes agent characteristics,
 * task context, and intelligent context requirements.
 */
public class IntelligentContextRequest {
    
    private final String taskType;
    private final String domain;
    private final AgentCharacteristic agentCharacteristic;
    private final Map<String, Object> taskParameters;
    private final List<String> requiredInformation;
    private final ContextPriority priority;
    private final int maxTokenBudget;
    
    public IntelligentContextRequest(Builder builder) {
        this.taskType = builder.taskType;
        this.domain = builder.domain;
        this.agentCharacteristic = builder.agentCharacteristic;
        this.taskParameters = new HashMap<>(builder.taskParameters);
        this.requiredInformation = new ArrayList<>(builder.requiredInformation);
        this.priority = builder.priority;
        this.maxTokenBudget = builder.maxTokenBudget;
    }
    
    public static Builder builder() {
        return new Builder();
    }
    
    // Getters
    public String getTaskType() { return taskType; }
    public String getDomain() { return domain; }
    public AgentCharacteristic getAgentCharacteristic() { return agentCharacteristic; }
    public Map<String, Object> getTaskParameters() { return new HashMap<>(taskParameters); }
    public List<String> getRequiredInformation() { return new ArrayList<>(requiredInformation); }
    public ContextPriority getPriority() { return priority; }
    public int getMaxTokenBudget() { return maxTokenBudget; }
    
    /**
     * Convert to basic ContextRequest for compatibility
     */
    public ContextRequest toContextRequest() {
        return ContextRequest.builder()
            .requestId(UUID.randomUUID().toString())
            .query(taskType + " in " + domain)
            .maxTokens(maxTokenBudget)
            .priority(priority.getValue())
            .build();
    }
    
    public static class Builder {
        private String taskType;
        private String domain;
        private AgentCharacteristic agentCharacteristic;
        private Map<String, Object> taskParameters = new HashMap<>();
        private List<String> requiredInformation = new ArrayList<>();
        private ContextPriority priority = ContextPriority.MEDIUM;
        private int maxTokenBudget = 4000;
        
        public Builder taskType(String taskType) {
            this.taskType = taskType;
            return this;
        }
        
        public Builder domain(String domain) {
            this.domain = domain;
            return this;
        }
        
        public Builder agentCharacteristic(AgentCharacteristic agentCharacteristic) {
            this.agentCharacteristic = agentCharacteristic;
            return this;
        }
        
        public Builder taskParameter(String key, Object value) {
            this.taskParameters.put(key, value);
            return this;
        }
        
        public Builder taskParameters(Map<String, Object> parameters) {
            this.taskParameters.putAll(parameters);
            return this;
        }
        
        public Builder requiredInformation(String information) {
            this.requiredInformation.add(information);
            return this;
        }
        
        public Builder requiredInformation(List<String> information) {
            this.requiredInformation.addAll(information);
            return this;
        }
        
        public Builder priority(ContextPriority priority) {
            this.priority = priority;
            return this;
        }
        
        public Builder maxTokenBudget(int maxTokenBudget) {
            this.maxTokenBudget = maxTokenBudget;
            return this;
        }
        
        public IntelligentContextRequest build() {
            Objects.requireNonNull(taskType, "Task type is required");
            Objects.requireNonNull(domain, "Domain is required");
            Objects.requireNonNull(agentCharacteristic, "Agent characteristic is required");
            
            return new IntelligentContextRequest(this);
        }
    }
    
    public enum ContextPriority {
        LOW(1), MEDIUM(5), HIGH(10), CRITICAL(15);
        
        private final int value;
        
        ContextPriority(int value) {
            this.value = value;
        }
        
        public int getValue() {
            return value;
        }
    }
}