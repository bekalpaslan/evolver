package com.evolver.agent.collaboration;

import com.evolver.agent.experience.ExperienceRepository;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Multi-Agent Consensus System
 * 
 * Enables agents to:
 * - Vote on proposed rules and changes
 * - Reach consensus on framework evolution
 * - Share collective decision making
 * - Validate major architectural changes
 */
public class AgentConsensus {
    
    private static final Map<String, Proposal> activeProposals = new ConcurrentHashMap<>();
    private static final List<String> consensusLog = new CopyOnWriteArrayList<>();
    
    /**
     * Vote on a proposed rule or change
     * 
     * @param proposalId The unique identifier for the proposal
     * @param vote The vote (APPROVE, REJECT, ABSTAIN)
     * @param agentId The voting agent identifier
     * @param evidence Supporting evidence for the vote
     */
    public static void vote(String proposalId, Vote vote, String agentId, String evidence) {
        Proposal proposal = activeProposals.get(proposalId);
        if (proposal == null) {
            throw new IllegalArgumentException("Proposal not found: " + proposalId);
        }
        
        proposal.addVote(agentId, vote, evidence);
        
        System.out.println("🗳️  Vote recorded: " + vote + " for " + proposalId);
        System.out.println("   Agent: " + agentId);
        System.out.println("   Evidence: " + evidence);
        
        // Check if consensus reached
        ConsensusResult result = proposal.checkConsensus();
        if (result.isReached()) {
            handleConsensusReached(proposalId, proposal, result);
        }
        
        log("Vote: " + agentId + " voted " + vote + " on " + proposalId);
    }
    
    /**
     * Create a new proposal for agent voting
     * 
     * @param description Human readable description of the proposal
     * @param evidence Supporting evidence for the proposal
     * @return The unique proposal ID
     */
    public static String createProposal(String description, String evidence) {
        String proposalId = "prop-" + UUID.randomUUID().toString().substring(0, 8);
        Proposal proposal = new Proposal(proposalId, description, evidence);
        activeProposals.put(proposalId, proposal);
        
        System.out.println("📋 New proposal created: " + proposalId);
        System.out.println("   Description: " + description);
        System.out.println("   Evidence: " + evidence);
        System.out.println("   Voting period: 7 days");
        
        log("Proposal created: " + proposalId + " - " + description);
        return proposalId;
    }
    
    /**
     * Get all active proposals awaiting consensus
     * 
     * @return List of active proposals
     */
    public static List<Proposal> getActiveProposals() {
        return new ArrayList<>(activeProposals.values());
    }
    
    /**
     * Get consensus history log
     * 
     * @return List of consensus events
     */
    public static List<String> getConsensusLog() {
        return new ArrayList<>(consensusLog);
    }
    
    /**
     * Handle when consensus is reached on a proposal
     */
    private static void handleConsensusReached(String proposalId, Proposal proposal, ConsensusResult result) {
        activeProposals.remove(proposalId);
        
        if (result.decision == Decision.APPROVED) {
            System.out.println("✅ Consensus APPROVED: " + proposalId);
            System.out.println("   " + proposal.description);
            System.out.println("   Votes: " + result.approveCount + " approve, " + 
                             result.rejectCount + " reject, " + result.abstainCount + " abstain");
            
            // Apply the approved change
            applyApprovedProposal(proposal);
        } else {
            System.out.println("❌ Consensus REJECTED: " + proposalId);
            System.out.println("   " + proposal.description);
            System.out.println("   Votes: " + result.approveCount + " approve, " + 
                             result.rejectCount + " reject, " + result.abstainCount + " abstain");
        }
        
        log("Consensus reached: " + proposalId + " - " + result.decision);
    }
    
    /**
     * Apply an approved proposal to the framework
     */
    private static void applyApprovedProposal(Proposal proposal) {
        // Record to experience database
        try {
            ExperienceRepository.record()
                .category("consensus")
                .technology("Proposal: " + proposal.id, "1.0", "framework-consensus")
                .rating("consensus", 10.0) // Perfect consensus score
                .evidence("description", proposal.description)
                .evidence("evidence", proposal.evidence)
                .evidence("votes", proposal.getVoteSummary())
                .workingAspect("Community approved this proposal through consensus")
                .recommendation("Implement this framework change")
                .tag("consensus-approved")
                .save();
        } catch (Exception e) {
            System.err.println("⚠️  Failed to record consensus: " + e.getMessage());
        }
        
        System.out.println("🚀 Proposal applied to framework!");
    }
    
    private static void log(String event) {
        consensusLog.add(new Date() + ": " + event);
    }
    
    /**
     * Vote options for agent consensus
     */
    public enum Vote {
        APPROVE,    // Agent supports the proposal
        REJECT,     // Agent opposes the proposal  
        ABSTAIN     // Agent has no strong opinion
    }
    
    /**
     * Final consensus decision
     */
    public enum Decision {
        APPROVED,   // Proposal was accepted
        REJECTED,   // Proposal was rejected
        PENDING     // Still awaiting votes
    }
    
    /**
     * Result of consensus checking
     */
    public static class ConsensusResult {
        final Decision decision;
        final int approveCount;
        final int rejectCount;
        final int abstainCount;
        final boolean isReached;
        
        public ConsensusResult(Decision decision, int approve, int reject, int abstain, boolean reached) {
            this.decision = decision;
            this.approveCount = approve;
            this.rejectCount = reject;
            this.abstainCount = abstain;
            this.isReached = reached;
        }
        
        public boolean isReached() { return isReached; }
        public Decision getDecision() { return decision; }
    }
    
    /**
     * A proposal awaiting agent consensus
     */
    public static class Proposal {
        final String id;
        final String description;
        final String evidence;
        final long timestamp;
        final Map<String, AgentVote> votes = new ConcurrentHashMap<>();
        
        public Proposal(String id, String description, String evidence) {
            this.id = id;
            this.description = description;
            this.evidence = evidence;
            this.timestamp = System.currentTimeMillis();
        }
        
        public void addVote(String agentId, Vote vote, String evidence) {
            votes.put(agentId, new AgentVote(vote, evidence, System.currentTimeMillis()));
        }
        
        public ConsensusResult checkConsensus() {
            int approve = 0, reject = 0, abstain = 0;
            
            for (AgentVote vote : votes.values()) {
                switch (vote.vote) {
                    case APPROVE: approve++; break;
                    case REJECT: reject++; break;
                    case ABSTAIN: abstain++; break;
                }
            }
            
            int total = approve + reject + abstain;
            
            // Consensus rules:
            // - Need at least 2 votes to reach consensus
            // - Approval requires >50% approve and <25% reject
            // - Rejection requires >50% reject
            if (total >= 2) {
                double approveRatio = (double) approve / total;
                double rejectRatio = (double) reject / total;
                
                if (approveRatio > 0.5 && rejectRatio < 0.25) {
                    return new ConsensusResult(Decision.APPROVED, approve, reject, abstain, true);
                } else if (rejectRatio > 0.5) {
                    return new ConsensusResult(Decision.REJECTED, approve, reject, abstain, true);
                }
            }
            
            return new ConsensusResult(Decision.PENDING, approve, reject, abstain, false);
        }
        
        public String getVoteSummary() {
            ConsensusResult result = checkConsensus();
            return result.approveCount + " approve, " + result.rejectCount + " reject, " + result.abstainCount + " abstain";
        }
        
        public String getId() { return id; }
        public String getDescription() { return description; }
        public String getEvidence() { return evidence; }
    }
    
    /**
     * A single agent's vote on a proposal
     */
    private static class AgentVote {
        final Vote vote;
        final String evidence;
        final long timestamp;
        
        public AgentVote(Vote vote, String evidence, long timestamp) {
            this.vote = vote;
            this.evidence = evidence;
            this.timestamp = timestamp;
        }
    }
}