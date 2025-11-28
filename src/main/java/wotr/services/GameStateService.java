package wotr.services;

import wotr.database.DatabaseManager;
import wotr.dao.GameStateDAO;
import wotr.models.*;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

/**
 * GameStateService - Dynamic game board queries
 * 
 * This service manages the current state of an active game.
 * It wraps GameStateDAO and provides a cleaner API for game logic.
 * 
 * Think of this as querying "What's on the board right now?"
 */
public class GameStateService {
    private GameStateDAO gameStateDAO;
    private String currentGameId;
    
    public GameStateService() {
        DatabaseManager dbManager = DatabaseManager.getInstance();
        this.gameStateDAO = new GameStateDAO(dbManager.getConnection());
    }
    
    // ===== GAME MANAGEMENT =====
    
    /**
     * Create a new game and return its ID
     */
    public String createNewGame(String activePlayer) throws SQLException {
        this.currentGameId = UUID.randomUUID().toString();
        
        // 1. Create game_state record
        gameStateDAO.createGameState(currentGameId, activePlayer);
        
        // 2. Initialize action dice for all nations
        initializeActionDice(currentGameId);
        
        // 3. Initialize Fellowship
        initializeFellowship(currentGameId);
        
        // TODO: Future initialization tasks
        // - Set up initial character positions
        // - Set up initial region control
        // - Initialize nation politics
        
        System.out.println("Created new game: " + currentGameId);
        return currentGameId;
    }
    
    /**
     * Initialize action dice for all nations
     */
    private void initializeActionDice(String gameId) throws SQLException {
        // Get reference to nation data
        GameDataService gameData = GameDataService.getInstance();
        Collection<Nation> nations = gameData.getAllNations();
        
        // Track total dice created
        int totalDice = 0;
        
        for (Nation nation : nations) {
            int diceCount = getDiceCountForNation(nation);
            
            for (int i = 0; i < diceCount; i++) {
                String dieId = gameId + "_" + nation.getId() + "_" + i;
                
                // Create die in database (unrolled, available)
                gameStateDAO.createActionDie(gameId, dieId, nation.getId(), "character", false);
                totalDice++;
            }
        }
        
        System.out.println("Initialized " + totalDice + " action dice for " + nations.size() + " nations");
    }
    
    /**
     * Get the number of action dice for a nation based on standard WOTR rules
     */
    private int getDiceCountForNation(Nation nation) {
        // Standard War of the Ring setup
        String nationName = nation.getName().toLowerCase();
        
        // Free Peoples nations (active at start)
        if (nationName.contains("gondor")) {
            return 4;
        }
        if (nationName.contains("rohan")) {
            return 4;
        }
        if (nationName.contains("north")) {
            return 4;
        }
        
        // Shadow nations (active at start)
        if (nationName.contains("sauron")) {
            return 7;
        }
        if (nationName.contains("isengard")) {
            return 7;
        }
        
        // Inactive nations at start (require activation)
        // Elves, Dwarves, Southrons & Easterlings
        return 0;
    }
    
    /**
     * Initialize Fellowship for a new game
     * Sets up the Fellowship piece and adds Ring Bearers to Fellowship box
     */
    private void initializeFellowship(String gameId) throws SQLException {
        // Fellowship starts with Ring Bearers and some companions
        // Gandalf is the initial guide
        
        // 1. Create Fellowship piece with initial properties (Gandalf as initial guide)
        String fellowshipProps = "{\"revealed\":false,\"corruption\":0,\"guide\":\"gandalf\"}";
        gameStateDAO.createFellowshipPiece(gameId, 0, fellowshipProps); // Area 0 = Rivendell starting location
        
        // 2. Add Ring Bearers (unitfellowship) to Fellowship box (area 115)
        gameStateDAO.moveCharacterToArea(gameId, "unitfellowship", 115);
        
        // 3. Add initial companions for testing
        // Includes gandalf (guide), aragorn, and others but NOT strider (so addToFellowship test can add him)
        String[] initialCompanions = {"gandalf", "aragorn", "legolas", "gimli", "boromir"};
        for (String companion : initialCompanions) {
            gameStateDAO.moveCharacterToArea(gameId, companion, 115);
        }
        
        System.out.println("Initialized Fellowship with Ring Bearers and " + initialCompanions.length + " companions");
    }
    
    /**
     * Load an existing game by ID
     */
    public void loadGame(String gameId) throws SQLException {
        // Verify game exists
        GameState state = gameStateDAO.getGameState(gameId);
        if (state == null) {
            throw new IllegalArgumentException("Game not found: " + gameId);
        }
        this.currentGameId = gameId;
        System.out.println("Loaded game: " + gameId);
    }
    
    /**
     * Get current game ID
     */
    public String getCurrentGameId() {
        return currentGameId;
    }
    
    // ===== CHARACTER QUERIES =====
    
    /**
     * Where is this character?
     * Example: getCharacterLocation("strider") -> "rivendell"
     */
    public String getCharacterLocation(String characterId) throws SQLException {
        ensureGameLoaded();
        return gameStateDAO.getCharacterLocation(currentGameId, characterId);
    }
    
    /**
     * Is this character in play?
     */
    public boolean isCharacterInPlay(String characterId) throws SQLException {
        ensureGameLoaded();
        return gameStateDAO.isCharacterInPlay(currentGameId, characterId);
    }
    
    /**
     * Move a character to a new region
     */
    public void moveCharacter(String characterId, String newRegionId) throws SQLException {
        ensureGameLoaded();
        gameStateDAO.updateCharacterPosition(currentGameId, characterId, newRegionId);
        System.out.println("Moved " + characterId + " to " + newRegionId);
    }
    
    /**
     * Get all characters in a region
     */
    public List<String> getCharactersInRegion(String regionId) throws SQLException {
        ensureGameLoaded();
        return gameStateDAO.getCharactersInRegion(currentGameId, regionId);
    }
    
    // ===== ARMY QUERIES =====
    
    /**
     * What armies are in this region?
     * Example: getArmyUnits("minas_tirith") -> [3 Gondor regulars, 1 elite, 1 leader]
     */
    public List<ArmyUnit> getArmyUnits(String regionId) throws SQLException {
        ensureGameLoaded();
        return gameStateDAO.getArmyUnits(currentGameId, regionId);
    }
    
    /**
     * Add units to a region
     */
    public void addUnits(String regionId, int nationId, String unitType, int count) throws SQLException {
        ensureGameLoaded();
        gameStateDAO.addUnits(currentGameId, regionId, nationId, unitType, count);
        System.out.println("Added " + count + " " + unitType + " units to " + regionId);
    }
    
    /**
     * Remove units from a region
     */
    public void removeUnits(String regionId, int nationId, String unitType, int count) throws SQLException {
        ensureGameLoaded();
        gameStateDAO.removeUnits(currentGameId, regionId, nationId, unitType, count);
        System.out.println("Removed " + count + " " + unitType + " units from " + regionId);
    }
    
    /**
     * Move army units from one region to another
     */
    public void moveArmy(String fromRegionId, String toRegionId, int nationId, 
                        int regularCount, int eliteCount) throws SQLException {
        ensureGameLoaded();
        
        // Remove units from source
        if (regularCount > 0) {
            removeUnits(fromRegionId, nationId, "regular", regularCount);
        }
        if (eliteCount > 0) {
            removeUnits(fromRegionId, nationId, "elite", eliteCount);
        }
        
        // Add units to destination
        if (regularCount > 0) {
            addUnits(toRegionId, nationId, "regular", regularCount);
        }
        if (eliteCount > 0) {
            addUnits(toRegionId, nationId, "elite", eliteCount);
        }
        
        System.out.println("Moved army from " + fromRegionId + " to " + toRegionId);
    }
    
    // ===== REGION CONTROL =====
    
    /**
     * Who controls this region?
     * Returns: "free_peoples", "shadow", or "neutral"
     */
    public String getRegionControl(String regionId) throws SQLException {
        ensureGameLoaded();
        return gameStateDAO.getRegionControl(currentGameId, regionId);
    }
    
    /**
     * Set who controls a region
     */
    public void setRegionControl(String regionId, String controller) throws SQLException {
        ensureGameLoaded();
        gameStateDAO.setRegionControl(currentGameId, regionId, controller);
        System.out.println(regionId + " is now controlled by " + controller);
    }
    
    // ===== FELLOWSHIP STATE (Board-based queries) =====
    
    /**
     * Where is the Fellowship?
     * Queries the game board to find the Fellowship piece location
     */
    public String getFellowshipLocation() throws SQLException {
        ensureGameLoaded();
        // Query game_pieces table for fellowship piece
        return gameStateDAO.getFellowshipLocation(currentGameId);
    }
    
    /**
     * Is the Fellowship revealed?
     * Checks the revealed property on the Fellowship piece
     */
    public boolean isFellowshipRevealed() throws SQLException {
        ensureGameLoaded();
        return gameStateDAO.isFellowshipRevealed(currentGameId);
    }
    
    /**
     * Get Fellowship corruption level
     * Reads corruption from Fellowship piece properties
     */
    public int getFellowshipCorruption() throws SQLException {
        ensureGameLoaded();
        return gameStateDAO.getFellowshipCorruption(currentGameId);
    }
    
    /**
     * Get corruption level (alias for getFellowshipCorruption for UI consistency)
     */
    public int getCorruptionLevel() throws SQLException {
        return getFellowshipCorruption();
    }
    
    /**
     * Move the Fellowship to a new region
     * Updates the Fellowship piece location on the board
     */
    public void moveFellowship(String newRegionId) throws SQLException {
        ensureGameLoaded();
        // Move the fellowship piece to new area
        gameStateDAO.moveFellowshipPiece(currentGameId, newRegionId);
        System.out.println("Fellowship moved to " + newRegionId);
    }
    
    /**
     * Reveal the Fellowship
     * Sets revealed property on Fellowship piece
     */
    public void revealFellowship() throws SQLException {
        ensureGameLoaded();
        gameStateDAO.setFellowshipRevealed(currentGameId, true);
        System.out.println("Fellowship revealed!");
    }
    
    /**
     * Hide the Fellowship
     * Clears revealed property on Fellowship piece
     */
    public void hideFellowship() throws SQLException {
        ensureGameLoaded();
        gameStateDAO.setFellowshipRevealed(currentGameId, false);
        System.out.println("Fellowship hidden");
    }
    
    /**
     * Add corruption to the Fellowship
     * Increments corruption property on Fellowship piece
     */
    public void addCorruption(int amount) throws SQLException {
        ensureGameLoaded();
        gameStateDAO.addFellowshipCorruption(currentGameId, amount);
        System.out.println("Added " + amount + " corruption to Fellowship");
    }
    
    /**
     * Adjust corruption (can add or remove)
     * Positive values add corruption, negative values heal
     */
    public void adjustCorruption(int amount) throws SQLException {
        ensureGameLoaded();
        gameStateDAO.addFellowshipCorruption(currentGameId, amount);
        if (amount > 0) {
            System.out.println("Added " + amount + " corruption to Fellowship");
        } else if (amount < 0) {
            System.out.println("Healed " + Math.abs(amount) + " corruption from Fellowship");
        }
    }
    
    /**
     * Set Fellowship revealed state
     */
    public void setFellowshipRevealed(boolean revealed) throws SQLException {
        ensureGameLoaded();
        gameStateDAO.setFellowshipRevealed(currentGameId, revealed);
        System.out.println("Fellowship " + (revealed ? "revealed" : "hidden"));
    }
    
    /**
     * Set Fellowship guide
     * Updates guide property on Fellowship piece
     */
    public void setFellowshipGuide(String guideId) throws SQLException {
        ensureGameLoaded();
        gameStateDAO.setFellowshipGuide(currentGameId, guideId);
        System.out.println("Fellowship guide set to " + guideId);
    }
    
    /**
     * Get Fellowship guide
     * Reads guide from Fellowship piece properties
     */
    public String getFellowshipGuide() throws SQLException {
        ensureGameLoaded();
        return gameStateDAO.getFellowshipGuide(currentGameId);
    }
    
    /**
     * Get characters in a specific area
     * Used for Fellowship box, casualties, spare, etc.
     */
    public List<String> getCharactersInArea(int areaId) throws SQLException {
        ensureGameLoaded();
        return gameStateDAO.getCharactersInArea(currentGameId, areaId);
    }
    
    /**
     * Move character to a specific area
     * Used for moving to Fellowship box, casualties, spare, etc.
     */
    public void moveCharacterToArea(String characterId, int areaId) throws SQLException {
        ensureGameLoaded();
        gameStateDAO.moveCharacterToArea(currentGameId, characterId, areaId);
    }
    
    /**
     * Get the area ID where a character is located
     */
    public Integer getCharacterAreaId(String characterId) throws SQLException {
        ensureGameLoaded();
        return gameStateDAO.getCharacterAreaId(currentGameId, characterId);
    }
    
    // ===== POLITICAL TRACK (Board Pieces) =====
    
    /**
     * Get political marker position for a nation
     * Queries the physical political marker piece location on the board
     * Areas: 117 (Pos 3), 118 (Pos 2), 119 (Pos 1), 120 (Pos 0/At War)
     */
    public int getPoliticalMarkerPosition(String nationId) throws SQLException {
        ensureGameLoaded();
        
        // Query game_pieces for this nation's political marker
        // Political markers are in areas 117-120
        Integer areaId = gameStateDAO.getPoliticalMarkerArea(currentGameId, nationId);
        
        if (areaId == null) {
            // If no marker found, return default starting position
            return getDefaultPoliticalPosition(nationId);
        }
        
        // Convert area ID to position (117=3, 118=2, 119=1, 120=0)
        return 120 - areaId;
    }
    
    /**
     * Move political marker to new position
     * Moves the physical political marker piece on the board
     */
    public void movePoliticalMarker(String nationId, int newPosition) throws SQLException {
        ensureGameLoaded();
        
        // Convert position to area ID (pos 3=area 117, pos 2=118, pos 1=119, pos 0=120)
        int newAreaId = 120 - newPosition;
        
        // Move the political marker piece to the new area
        gameStateDAO.movePoliticalMarker(currentGameId, nationId, newAreaId);
        System.out.println("Moved " + nationId + " political marker to position " + newPosition + " (area " + newAreaId + ")");
    }
    
    /**
     * Check if nation's TwoChit is active (flipped to active side)
     * Queries the active/passive state from the TwoChit piece
     */
    public boolean isNationTwoChitActive(String nationId) throws SQLException {
        ensureGameLoaded();
        
        // Query game_pieces for TwoChit active state
        Boolean isActive = gameStateDAO.getTwoChitState(currentGameId, nationId);
        
        if (isActive == null) {
            // Default: Shadow nations start active, FP nations (except Elves) start passive
            return getDefaultTwoChitState(nationId);
        }
        
        return isActive;
    }
    
    /**
     * Activate nation's TwoChit (flip from passive to active)
     */
    public void activateNationTwoChit(String nationId) throws SQLException {
        ensureGameLoaded();
        gameStateDAO.setTwoChitActive(currentGameId, nationId, true);
        System.out.println(nationId + " TwoChit activated");
    }
    
    /**
     * Get default political position for a nation (starting position)
     */
    private int getDefaultPoliticalPosition(String nationId) {
        String nation = nationId.toLowerCase();
        // Position 3 (furthest from war)
        if (nation.contains("rohan") || nation.contains("north") || 
            nation.contains("elves") || nation.contains("dwarves")) {
            return 3;
        }
        // Position 2
        if (nation.contains("gondor") || nation.contains("southron") || 
            nation.contains("easterling")) {
            return 2;
        }
        // Position 1 (closest to war)
        if (nation.contains("sauron") || nation.contains("isengard")) {
            return 1;
        }
        return 3; // Default
    }
    
    /**
     * Get default TwoChit state (active/passive at game start)
     */
    private boolean getDefaultTwoChitState(String nationId) {
        String nation = nationId.toLowerCase();
        // Shadow nations all start active
        if (nation.contains("sauron") || nation.contains("isengard") || 
            nation.contains("southron") || nation.contains("easterling")) {
            return true;
        }
        // FP: Only Elves start active
        if (nation.contains("elves")) {
            return true;
        }
        // All other FP nations start passive
        return false;
    }
    
    // ===== GAME STATE =====
    
    /**
     * Get current game state (turn, phase, VP)
     */
    public GameState getGameState() throws SQLException {
        ensureGameLoaded();
        return gameStateDAO.getGameState(currentGameId);
    }
    
    /**
     * Get current turn number
     */
    public int getCurrentTurn() throws SQLException {
        GameState state = getGameState();
        return state != null ? state.getTurnNumber() : 1;
    }
    
    /**
     * Get current phase
     */
    public String getCurrentPhase() throws SQLException {
        GameState state = getGameState();
        return state != null ? state.getPhase() : "recover_action_dice";
    }
    
    /**
     * Get active player
     */
    public String getActivePlayer() throws SQLException {
        GameState state = getGameState();
        return state != null ? state.getActivePlayer() : "free_peoples";
    }
    
    /**
     * Advance to next phase
     */
    public void advancePhase(String newPhase) throws SQLException {
        ensureGameLoaded();
        gameStateDAO.advancePhase(currentGameId, newPhase);
        System.out.println("Advanced to phase: " + newPhase);
    }
    
    /**
     * Advance to next turn
     */
    public void nextTurn() throws SQLException {
        ensureGameLoaded();
        gameStateDAO.nextTurn(currentGameId);
        int newTurn = getCurrentTurn();
        System.out.println("Advanced to turn: " + newTurn);
    }
    
    // ===== UTILITY =====
    
    private void ensureGameLoaded() {
        if (currentGameId == null) {
            throw new IllegalStateException("No game loaded. Call createNewGame() or loadGame() first.");
        }
    }
    
    /**
     * Get summary of current game state
     */
    public String getGameSummary() throws SQLException {
        ensureGameLoaded();
        GameState state = getGameState();
        String fellowshipLocation = getFellowshipLocation();
        int corruption = getFellowshipCorruption();
        boolean revealed = isFellowshipRevealed();
        
        return String.format(
            "Game: %s\n" +
            "Turn: %d\n" +
            "Phase: %s\n" +
            "Active Player: %s\n" +
            "VP (FP/Shadow): %d/%d\n" +
            "Fellowship: %s (Corruption: %d, Revealed: %s)",
            currentGameId.substring(0, 8) + "...",
            state.getTurnNumber(),
            state.getPhase(),
            state.getActivePlayer(),
            state.getVictoryPointsFP(),
            state.getVictoryPointsShadow(),
            fellowshipLocation != null ? fellowshipLocation : "unknown",
            corruption,
            revealed ? "yes" : "no"
        );
    }
    
    // ===== ON-TABLE CARD MANAGEMENT =====
    
    /**
     * Add a card to the on-table tracking
     * @param cardName Name of the card
     * @param faction "free_peoples" or "shadow"
     * @param areaId Area ID (146-151) where card is placed
     */
    public void addOnTableCard(String cardName, String faction, int areaId) throws SQLException {
        gameStateDAO.addOnTableCard(currentGameId, cardName, faction, areaId);
    }
    
    /**
     * Remove a card from the on-table tracking
     * @param cardName Name of the card to remove
     */
    public void removeOnTableCard(String cardName) throws SQLException {
        gameStateDAO.removeOnTableCard(currentGameId, cardName);
    }
    
    /**
     * Get all cards currently on the table
     * @return List of card names
     */
    public List<String> getOnTableCards() throws SQLException {
        return gameStateDAO.getOnTableCards(currentGameId);
    }
    
    /**
     * Get on-table cards for a specific faction
     * @param faction "free_peoples" or "shadow"
     * @return List of card names
     */
    public List<String> getOnTableCardsByFaction(String faction) throws SQLException {
        return gameStateDAO.getOnTableCardsByFaction(currentGameId, faction);
    }
    
    /**
     * Check if an area is occupied by a card
     * @param areaId Area ID to check
     * @return true if area has a card
     */
    public boolean isAreaOccupied(int areaId) throws SQLException {
        return gameStateDAO.isOnTableAreaOccupied(currentGameId, areaId);
    }
    
    /**
     * Clear all on-table cards (for game reset)
     */
    public void clearOnTableCards() throws SQLException {
        gameStateDAO.clearOnTableCards(currentGameId);
    }
    
    /**
     * Change action die result
     * TODO: Implement die result changes for cards like Mirror of Galadriel
     */
    public void changeDieResult(String dieId, String newResult) throws SQLException {
        // TODO: Implement die result changes
        // This will be needed for cards like "Mirror of Galadriel" that change die results
    }
}
