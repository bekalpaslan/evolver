package com.evolver.agent;

import java.net.InetAddress;
import java.security.MessageDigest;
import java.util.Base64;

/**
 * Secure agent identification system using hashed IP addresses.
 * Provides unique, trackable agent IDs without compromising privacy.
 */
public class AgentIdentity {
    
    private static final String SALT = "evolver-agent-salt-2025";
    private static String cachedAgentId = null;
    
    /**
     * Get the current agent's unique ID based on hashed IP address
     * @return Secure hash-based agent identifier
     */
    public static String getAgentId() {
        if (cachedAgentId != null) {
            return cachedAgentId;
        }
        
        try {
            // Get local IP address
            InetAddress localHost = InetAddress.getLocalHost();
            String ipAddress = localHost.getHostAddress();
            
            // Create secure hash
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            String input = ipAddress + SALT;
            byte[] hashBytes = digest.digest(input.getBytes());
            
            // Encode to base64 and take first 12 characters for readability
            String hash = Base64.getEncoder().encodeToString(hashBytes);
            cachedAgentId = "agent_" + hash.substring(0, 12).toLowerCase().replace("+", "x").replace("/", "y");
            
            return cachedAgentId;
            
        } catch (Exception e) {
            // Fallback to system-based ID if network detection fails
            String fallback = "agent_" + System.getProperty("user.name", "unknown");
            cachedAgentId = fallback.toLowerCase().replaceAll("[^a-z0-9_]", "");
            return cachedAgentId;
        }
    }
    
    /**
     * Get detailed agent information for experience tracking
     * @return AgentInfo object with all tracking details
     */
    public static AgentInfo getAgentInfo() {
        return new AgentInfo(
            getAgentId(),
            getModelInfo(),
            getSystemInfo(),
            getIPHash()
        );
    }
    
    /**
     * Get model information for the current AI agent
     * @return Model identification string
     */
    public static String getModelInfo() {
        // Detect model based on environment variables or system properties
        String model = System.getProperty("ai.model", System.getenv("AI_MODEL"));
        
        if (model != null && !model.trim().isEmpty()) {
            return model;
        }
        
        // Try to detect from common AI service indicators
        if (System.getenv("ANTHROPIC_API_KEY") != null) {
            return "Claude-3.5-Sonnet";
        } else if (System.getenv("OPENAI_API_KEY") != null) {
            return "GPT-4";
        } else if (System.getenv("GEMINI_API_KEY") != null) {
            return "Gemini-Pro";
        }
        
        // Default fallback
        return "Unknown-AI-Model";
    }
    
    /**
     * Get hashed IP for accountability without privacy violation
     * @return Hashed IP address
     */
    private static String getIPHash() {
        try {
            InetAddress localHost = InetAddress.getLocalHost();
            String ipAddress = localHost.getHostAddress();
            
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest((ipAddress + SALT).getBytes());
            
            return Base64.getEncoder().encodeToString(hashBytes).substring(0, 16);
        } catch (Exception e) {
            return "unknown_ip_hash";
        }
    }
    
    /**
     * Get system information for debugging and tracking
     * @return System identification string
     */
    private static String getSystemInfo() {
        return String.format("%s_%s_%s",
            System.getProperty("os.name", "unknown").toLowerCase().replaceAll("\\s+", "_"),
            System.getProperty("java.version", "unknown"),
            System.getProperty("user.name", "unknown").toLowerCase()
        );
    }
    
    /**
     * Agent information container
     */
    public static class AgentInfo {
        public final String agentId;
        public final String model;
        public final String systemInfo;
        public final String ipHash;
        public final long timestamp;
        
        public AgentInfo(String agentId, String model, String systemInfo, String ipHash) {
            this.agentId = agentId;
            this.model = model;
            this.systemInfo = systemInfo;
            this.ipHash = ipHash;
            this.timestamp = System.currentTimeMillis();
        }
        
        @Override
        public String toString() {
            return String.format("AgentInfo{id=%s, model=%s, system=%s, hash=%s}", 
                agentId, model, systemInfo, ipHash);
        }
    }
}