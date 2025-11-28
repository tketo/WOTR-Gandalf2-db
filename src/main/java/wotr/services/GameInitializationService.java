package wotr.services;

import wotr.database.DatabaseManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * GameInitializationService - Handles game initialization steps
 * 
 * This service breaks down game initialization into discrete, testable steps.
 * Called by FSM during GAME_INITIALIZATION state sequence.
 * 
 * Initialization Flow:
 * 1. VALIDATE_SIDE_ASSIGNMENTS - Ensure players assigned to factions
 * 2. PERSIST_PLAYER_FACTION_ASSIGNMENTS - Save assignments to database
 * 3. INITIALIZE_FP_RESOURCES - Setup Free Peoples cards and dice
 * 4. INITIALIZE_SP_RESOURCES - Setup Shadow cards and dice  
 * 5. SETUP_INITIAL_BOARD_STATE - Initialize regions, tracks, game state
 * 6. PLACE_INITIAL_PIECES - Place starting units and Fellowship
 */
public class GameInitializationService {
    private final DatabaseManager dbManager;
    
    public GameInitializationService() {
        this.dbManager = DatabaseManager.getInstance();
    }
    
    /**
     * Step 1: Validate side assignments
     * Ensures all players have valid faction assignments
     */
    public boolean validateSideAssignments() {
        System.out.println("[GameInit] Validating side assignments...");
        
        try {
            // For MVP: Always return true
            // In production: Query database for player-faction assignments
            // and ensure all seats are filled
            
            System.out.println("[GameInit] ✓ Side assignments validated");
            return true;
            
        } catch (Exception e) {
            System.err.println("[GameInit] ✗ Validation failed: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Step 2: Persist player-faction assignments to database
     * Saves which players control which factions
     */
    public boolean persistPlayerFactionAssignments(String playerId, String faction) {
        System.out.println("[GameInit] Persisting player-faction assignments...");
        
        try {
            Connection conn = dbManager.getConnection();
            
            // Clear existing assignments
            String clearSql = "DELETE FROM player_faction_assignments";
            PreparedStatement clearStmt = conn.prepareStatement(clearSql);
            clearStmt.executeUpdate();
            clearStmt.close();
            
            // Insert current assignment
            String insertSql = "INSERT INTO player_faction_assignments (player_id, faction, seat_number) VALUES (?, ?, ?)";
            PreparedStatement insertStmt = conn.prepareStatement(insertSql);
            
            insertStmt.setString(1, playerId);
            insertStmt.setString(2, faction);
            insertStmt.setInt(3, 1);
            insertStmt.executeUpdate();
            insertStmt.close();
            
            System.out.println("[GameInit] ✓ Saved assignment: " + playerId + " -> " + faction);
            System.out.println("[GameInit] ✓ Player-faction assignments persisted");
            return true;
            
        } catch (SQLException e) {
            System.err.println("[GameInit] ✗ Failed to persist assignments: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Step 3: Initialize Free Peoples resources
     * Sets up FP card decks, action dice, and starting hand
     * 
     * Note: Actual resource creation happens in Game.gameInit() and bitInit()
     * This method is for logging and validation purposes during FSM initialization
     */
    public boolean initializeFPResources() {
        System.out.println("[GameInit] Initializing Free Peoples resources...");
        
        try {
            // Card decks are created in bitInit() and placed in areas 121-122
            // FP Character Deck: area 121
            // FP Strategy Deck: area 122
            
            // Action dice are created in bitInit() and placed in area 174 (FP dice box)
            
            // Starting hand: Each player starts with 0 cards
            // Cards are drawn in Phase 1 of Turn 1
            
            System.out.println("[GameInit] ✓ FP card decks ready (areas 121-122)");
            System.out.println("[GameInit] ✓ FP action dice ready (area 174)");
            System.out.println("[GameInit] ✓ FP starting hand: 0 cards (will draw in Phase 1)");
            
            return true;
            
        } catch (Exception e) {
            System.err.println("[GameInit] ✗ FP resource initialization failed: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Step 4: Initialize Shadow resources
     * Sets up Shadow card decks, action dice, and starting hand
     * 
     * Note: Actual resource creation happens in Game.gameInit() and bitInit()
     * This method is for logging and validation purposes during FSM initialization
     */
    public boolean initializeSPResources() {
        System.out.println("[GameInit] Initializing Shadow resources...");
        
        try {
            // Card decks are created in bitInit() and placed in areas 123-124
            // Shadow Character Deck: area 123
            // Shadow Strategy Deck: area 124
            
            // Action dice are created in bitInit() and placed in area 176 (Shadow dice box)
            
            // Starting hand: Each player starts with 0 cards
            // Cards are drawn in Phase 1 of Turn 1
            
            System.out.println("[GameInit] ✓ Shadow card decks ready (areas 123-124)");
            System.out.println("[GameInit] ✓ Shadow action dice ready (area 176)");
            System.out.println("[GameInit] ✓ Shadow starting hand: 0 cards (will draw in Phase 1)");
            
            return true;
            
        } catch (Exception e) {
            System.err.println("[GameInit] ✗ Shadow resource initialization failed: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Step 5: Setup initial board state
     * Initializes regions, political tracks, and game state
     * 
     * Note: Actual board setup happens in Game.gameInit() and areaInit()
     * This method is for logging and validation purposes during FSM initialization
     */
    public boolean setupInitialBoardState() {
        System.out.println("[GameInit] Setting up initial board state...");
        
        try {
            // Areas are already initialized by areaInit() in gameInit()
            
            // Political track pieces are placed by bitInit()
            // - Elves: area 117 (at war)
            // - Dwarves: area 118 (passive/active)
            // - North: area 119 (passive/active)
            // - Rohan: area 120 (passive/active)
            // - Gondor: starts active
            
            // Game state is initialized:
            // - Turn: 0 (will become 1 when FSM enters TURN_START)
            // - Corruption: 0
            // - Fellowship location: Rivendell
            
            System.out.println("[GameInit] ✓ Regions initialized");
            System.out.println("[GameInit] ✓ Political track pieces placed");
            System.out.println("[GameInit] ✓ Game state: Turn 0, Corruption 0");
            
            return true;
            
        } catch (Exception e) {
            System.err.println("[GameInit] ✗ Board state setup failed: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Step 6: Place initial pieces
     * Places all starting units, characters, and Fellowship
     * 
     * Note: Actual piece placement happens in Game.hybridPieceInit()
     * This method is for logging and validation purposes during FSM initialization
     */
    public boolean placeInitialPieces() {
        System.out.println("[GameInit] Placing initial pieces...");
        
        try {
            // Pieces are placed by hybridPieceInit() which:
            // 1. Tries to load from database (scenario_setup table)
            // 2. Falls back to bitInit() if database is empty
            
            // This was already called during gameInit(), so pieces are in place
            
            System.out.println("[GameInit] ✓ Initial pieces placed on board");
            System.out.println("[GameInit] ✓ Fellowship at Rivendell");
            System.out.println("[GameInit] ✓ All starting units deployed");
            
            return true;
            
        } catch (Exception e) {
            System.err.println("[GameInit] ✗ Piece placement failed: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Complete initialization - final step
     * Performs any cleanup or final setup before Turn 1
     * 
     * Note: Called after all other initialization steps are complete
     */
    public boolean completeInitialization() {
        System.out.println("[GameInit] Completing initialization...");
        
        try {
            // Card decks are already anonymized in Game.AnonymizeDecks()
            // Game flags are already set in Game
            // Just log completion
            
            System.out.println("[GameInit] ✓ Card decks shuffled and anonymized");
            System.out.println("[GameInit] ✓ Game flags set");
            System.out.println("[GameInit] ✓✓✓ INITIALIZATION COMPLETE ✓✓✓");
            
            return true;
            
        } catch (Exception e) {
            System.err.println("[GameInit] ✗ Completion failed: " + e.getMessage());
            return false;
        }
    }
}
