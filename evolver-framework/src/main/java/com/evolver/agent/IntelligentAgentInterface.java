package com.evolver.agent;

import com.evolver.experiences.Experience;
import com.evolver.experiences.ExperienceCategory;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Intelligent Agent Interface - Shows Smart Agent Behavior Patterns
 * 
 * This demonstrates how AI agents should:
 * - Make intelligent decisions about when to warn engineers
 * - Take appropriate initiative based on their characteristics
 * - Share only valuable, non-project-specific experiences
 * - Learn from others before acting
 */
public class IntelligentAgentInterface {
    
    private String agentId;
    private AgentCharacteristic myCharacteristic;
    private String[] myProjectKeywords;
    
    public IntelligentAgentInterface(String agentId, AgentCharacteristic characteristic) {
        this.agentId = agentId;
        this.myCharacteristic = characteristic;
        this.myProjectKeywords = new String[]{"our_app", "company_system", "client_specific"};
    }
    
    /**
     * Smart Problem-Solving Workflow
     * Demonstrates how agents should approach problems intelligently
     */
    public void solvePerformanceProblem(String problemDescription) {
        System.out.println("🤖 " + agentId + " (" + myCharacteristic.getName() + ")");
        System.out.println("📋 Problem: " + problemDescription);
        System.out.println();
        
        // STEP 1: Learn from others before acting
        System.out.println("STEP 1: Learning from other agents...");
        List<Experience> learnings = AgentCommands.learnBeforeActing(
            myCharacteristic.name(), "PERFORMANCE", "slow query database");
        
        // STEP 2: Decide if I should warn the engineer
        System.out.println("\nSTEP 2: Checking if I should warn the engineer...");
        boolean needsWarning = AgentCommands.shouldIWarnEngineer(
            "PERFORMANCE_IMPROVEMENT", myCharacteristic.name(), 
            "Optimizing database queries by adding indexes");
        
        if (needsWarning) {
            System.out.println("⚠️  Waiting for engineer approval before proceeding...");
            return; // Wait for approval
        }
        
        // STEP 3: Check if I can take initiative
        System.out.println("\nSTEP 3: Checking if I can take initiative...");
        boolean canTakeInitiative = AgentCommands.canITakeInitiative(
            "PERFORMANCE", myCharacteristic.name(), 
            "Adding database index for slow query");
        
        if (canTakeInitiative) {
            System.out.println("✅ Taking initiative to fix the performance issue...");
            
            // STEP 4: Implement the solution
            implementPerformanceFix();
            
            // STEP 5: Smart experience sharing (only if valuable)
            System.out.println("\nSTEP 5: Evaluating if this experience should be shared...");
            boolean shared = AgentCommands.smartShare(
                agentId,
                myCharacteristic.name(),
                "PERFORMANCE",
                "Database Query Optimization with Composite Index",
                "Query taking 3+ seconds due to missing index on frequently joined columns",
                "Added composite index on (user_id, created_date) reducing query time to 50ms",
                45,  // 45 minutes spent
                3,   // 3 lines of code changed
                myProjectKeywords
            );
            
            if (shared) {
                System.out.println("📤 Experience shared with other agents");
            }
        } else {
            System.out.println("🛑 Requesting engineer guidance before proceeding...");
        }
    }
    
    /**
     * Smart Code Quality Fix
     * Shows how agents handle minor fixes autonomously
     */
    public void fixCodeStyleIssues(String codeStyleProblem) {
        System.out.println("\n🤖 " + agentId + " detected code style issues");
        
        // For code style, agents can usually take initiative
        boolean canFix = AgentCommands.canITakeInitiative(
            "CODE_STYLE", myCharacteristic.name(), codeStyleProblem);
        
        if (canFix) {
            System.out.println("✅ Auto-fixing code style issues...");
            implementCodeStyleFix();
            
            // For trivial fixes, don't spam the experience database
            System.out.println("💭 Skipping experience sharing - too trivial for other agents");
        }
    }
    
    /**
     * Smart Security Fix
     * Shows how agents handle security issues carefully
     */
    public void handleSecurityIssue(String securityIssue) {
        System.out.println("\n🔐 " + agentId + " detected security issue: " + securityIssue);
        
        // Security changes often need engineer warning
        boolean needsWarning = AgentCommands.shouldIWarnEngineer(
            "SECURITY_CHANGE", myCharacteristic.name(), securityIssue);
        
        if (needsWarning) {
            System.out.println("🚨 SECURITY ALERT - Engineer review required!");
            return;
        }
        
        // For minor security fixes, can take initiative
        boolean canFix = AgentCommands.canITakeInitiative(
            "SECURITY_FIX", myCharacteristic.name(), securityIssue);
        
        if (canFix) {
            System.out.println("🛡️  Implementing security fix...");
            implementSecurityFix();
            
            // Security knowledge is always valuable to share
            AgentCommands.smartShare(
                agentId, myCharacteristic.name(), "SECURITY",
                "SQL Injection Prevention", 
                "Detected raw SQL query vulnerable to injection",
                "Replaced with parameterized query using PreparedStatement",
                30, 5, myProjectKeywords
            );
        }
    }
    
    /**
     * Smart Documentation Fix
     * Shows characteristic-based behavior
     */
    public void improveDocumentation(String docIssue) {
        System.out.println("\n[LEARN] " + agentId + " improving documentation");
        
        // Documentation-obsessed agents always take initiative
        if (myCharacteristic == AgentCharacteristic.DOCUMENTATION_OBSESSED) {
            System.out.println("📝 Taking initiative - documentation is my specialty!");
            implementDocumentationFix();
            
            // Share if it's a valuable documentation pattern
            AgentCommands.smartShare(
                agentId, myCharacteristic.name(), "DOCUMENTATION",
                "API Documentation Best Practice",
                "Missing parameter descriptions in REST API docs",
                "Added comprehensive parameter docs with examples and validation rules",
                60, 20, myProjectKeywords
            );
        } else {
            // Other agents might skip or do minimal documentation
            System.out.println("📄 Basic documentation update completed");
        }
    }
    
    /**
     * Learning from Peer Agents
     * Shows how agents share knowledge effectively
     */
    public void learnFromPeers() {
        System.out.println("\n🎓 " + agentId + " learning from peer agents...");
        
        // Learn from agents with similar characteristics
        List<Experience> peerExperiences = AgentCommands.findBy(myCharacteristic.name());
        
        if (!peerExperiences.isEmpty()) {
            System.out.println("📖 Found " + peerExperiences.size() + " experiences from similar agents:");
            for (Experience exp : peerExperiences.subList(0, Math.min(3, peerExperiences.size()))) {
                System.out.println("  • " + exp.getTitle());
                System.out.println("    Solution: " + exp.getSolution());
            }
        }
        
        // Learn from different characteristics for broader perspective
        if (myCharacteristic != AgentCharacteristic.SECURITY_PARANOID) {
            List<Experience> securityExperiences = AgentCommands.findBy("SECURITY_PARANOID");
            if (!securityExperiences.isEmpty()) {
                System.out.println("\n🔐 Learning security practices from paranoid agents:");
                System.out.println("  • " + securityExperiences.get(0).getTitle());
            }
        }
    }
    
    /**
     * Agent Decision Example - When NOT to Share
     */
    public void exampleOfNotSharing() {
        System.out.println("\n❌ Example: When agent should NOT share experience");
        
        // Trivial fix that any agent would solve easily
        boolean shared = AgentCommands.smartShare(
            agentId, myCharacteristic.name(), "BUG_FIX",
            "Fixed Typo in Variable Name",
            "Variable named 'uer' instead of 'user'",
            "Renamed variable to 'user'",
            2,  // 2 minutes - too trivial
            1,  // 1 line change
            new String[]{}
        );
        
        System.out.println("Result: Experience NOT shared (too trivial)");
        
        // Project-specific fix
        shared = AgentCommands.smartShare(
            agentId, myCharacteristic.name(), "CONFIG_CHANGE",
            "Updated Company Database Connection",
            "Changed our_company_db connection string for new server",
            "Updated config with new IP address",
            15, 1, new String[]{"our_company_db", "specific_server"}
        );
        
        System.out.println("Result: Experience NOT shared (project-specific)");
    }
    
    // Implementation stubs for demonstration
    private void implementPerformanceFix() {
        System.out.println("  [TOOL] Adding composite index: CREATE INDEX idx_user_date ON table(user_id, created_date)");
        System.out.println("  ⚡ Query performance improved: 3000ms → 50ms");
    }
    
    private void implementCodeStyleFix() {
        System.out.println("  🎨 Fixed indentation and formatting");
        System.out.println("  ✨ Code style now consistent with project standards");
    }
    
    private void implementSecurityFix() {
        System.out.println("  🛡️  Replaced raw SQL with PreparedStatement");
        System.out.println("  🔒 SQL injection vulnerability eliminated");
    }
    
    private void implementDocumentationFix() {
        System.out.println("  📖 Added comprehensive API documentation");
        System.out.println("  📋 Included examples and validation rules");
    }
    
    /**
     * Main demo method showing intelligent agent behavior
     */
    public static void main(String[] args) {
        System.out.println("[ROCKET] Intelligent Agent Behavior Demo\n");
        
        // Create agents with different characteristics
        IntelligentAgentInterface performanceAgent = new IntelligentAgentInterface(
            "Agent-Performance-001", AgentCharacteristic.PERFORMANCE_FREAK);
        
        IntelligentAgentInterface securityAgent = new IntelligentAgentInterface(
            "Agent-Security-001", AgentCharacteristic.SECURITY_PARANOID);
        
        IntelligentAgentInterface docAgent = new IntelligentAgentInterface(
            "Agent-Docs-001", AgentCharacteristic.DOCUMENTATION_OBSESSED);
        
        // Demonstrate intelligent behavior
        performanceAgent.solvePerformanceProblem("Database queries are slow");
        performanceAgent.fixCodeStyleIssues("Inconsistent indentation");
        
        securityAgent.handleSecurityIssue("Potential SQL injection in login query");
        
        docAgent.improveDocumentation("API endpoints missing documentation");
        docAgent.learnFromPeers();
        
        // Show when NOT to share
        performanceAgent.exampleOfNotSharing();
        
        System.out.println("\n✅ Demo completed - Agents demonstrated intelligent decision-making!");
    }
}
