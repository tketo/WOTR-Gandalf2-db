package wotr.politics;

import wotr.services.GameStateService;
import wotr.services.GameDataService;
import wotr.models.Nation;
import wotr.actions.ActionResult;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * PoliticalTrack - Manages nation political positions
 * 
 * Based on War of the Ring rules:
 * - Political Track: Position 0 = "At War", Positions 1-3 = "Not at War"
 * - Nations move DOWN the track: 3 → 2 → 1 → 0 (toward war)
 * - TwoChit State: Passive or Active
 * - Passive nations CAN advance (3→2→1) but CANNOT reach position 0 (At War)
 * - Only Active nations can advance to position 0 (At War)
 * - Being "At War" allows nation to be attacked
 * - TwoChit active/passive state does NOT affect action dice allocation
 */
public class PoliticalTrack {
    private GameStateService gameState;
    private GameDataService gameData;
    
    // Political track positions (0-3) - nations move DOWN the track toward war
    private static final int AT_WAR = 0;         // Position 0 (Area 120) = "At War" (can be attacked)
    private static final int NOT_AT_WAR_1 = 1;   // Position 1 (Area 119) = "Not at War"
    private static final int NOT_AT_WAR_2 = 2;   // Position 2 (Area 118) = "Not at War"
    private static final int NOT_AT_WAR_3 = 3;   // Position 3 (Area 117) = "Not at War" (starting position)
    
    // Nation-specific starting POLITICAL TRACK positions
    // Note: TwoChit active/passive state is separate
    private static final Map<String, Integer> STARTING_POSITIONS = new HashMap<String, Integer>() {{
        // Political Track 3 (Top Box) - furthest from war
        put("rohan", NOT_AT_WAR_3);      // FP Passive
        put("north", NOT_AT_WAR_3);      // FP Passive  
        put("elves", NOT_AT_WAR_3);      // FP Active (can advance to war)
        put("dwarves", NOT_AT_WAR_3);    // FP Passive
        
        // Political Track 2 (Second Box)
        put("gondor", NOT_AT_WAR_2);     // FP Passive
        put("southrons", NOT_AT_WAR_2);  // Shadow Active
        put("easterlings", NOT_AT_WAR_2);// Shadow Active
        
        // Political Track 1 (Third Box) - closest to war
        put("sauron", NOT_AT_WAR_1);     // Shadow Active
        put("isengard", NOT_AT_WAR_1);   // Shadow Active
        
        // Political Track 0 (At War) - NO ONE STARTS HERE
    }};
    
    public PoliticalTrack(GameStateService gameState) {
        this.gameState = gameState;
        this.gameData = GameDataService.getInstance();
    }
    
    /**
     * Get nation's current political position
     * Reads from game_pieces table (political markers)
     */
    public int getNationPosition(String nationName) throws SQLException {
        Integer startingPos = STARTING_POSITIONS.get(nationName.toLowerCase());
        if (startingPos == null) {
            return NOT_AT_WAR_3;  // Default to furthest from war
        }
        
        // TODO: Query game_pieces for political marker position
        // For now, return starting position
        return startingPos;
    }
    
    // Active/Passive initial states per official rules
    private static final Map<String, Boolean> INITIAL_ACTIVE_STATE = new HashMap<String, Boolean>() {{
        // Free Peoples - Only Elves start Active
        put("elves", true);
        put("gondor", false);
        put("rohan", false);
        put("north", false);
        put("dwarves", false);
        
        // Shadow - All start Active
        put("sauron", true);
        put("isengard", true);
        put("southrons", true);
        put("easterlings", true);
    }};
    
    /**
     * Check if nation TwoChit is Active
     * Active nations can advance to position 0 (At War)
     * Passive nations can advance on track but only to position 1 (cannot reach At War)
     * Note: Active/Passive does NOT affect action dice allocation
     * TODO: Query TwoChit piece state from game_pieces instead of hard-coded map
     */
    public boolean isNationActive(String nationName) throws SQLException {
        // TODO: Check TwoChit active/passive state from game_pieces
        // For now, use initial state map based on official rules
        Boolean isActive = INITIAL_ACTIVE_STATE.get(nationName.toLowerCase());
        return isActive != null && isActive;
    }
    
    /**
     * Check if nation is at war (can be attacked)
     * Only position 0 is "At War"
     */
    public boolean isNationAtWar(String nationName) throws SQLException {
        int position = getNationPosition(nationName);
        return position == AT_WAR;  // Position 0
    }
    
    /**
     * Advance nation on political track
     * 
     * Track positions: 3 → 2 → 1 → 0 (toward war)
     * Position 0 = "At War", Positions 1-3 = "Not at War"
     * RULE: Passive nations can advance (3→2→1) but only down to position 1
     * RULE: Only Active nations can reach position 0 (At War)
     * 
     * @param nationName Nation to advance
     * @param steps Number of steps to advance (moves DOWN the track)
     * @return Result of the advancement
     */
    public ActionResult advanceNation(String nationName, int steps) {
        try {
            boolean isActive = isNationActive(nationName);
            int currentPosition = getNationPosition(nationName);
            int newPosition = currentPosition - steps;  // Move DOWN (3→2→1→0)
            
            // Passive nations cannot reach position 0 (At War)
            if (!isActive && newPosition <= AT_WAR) {
                newPosition = AT_WAR + 1; // Cap at position 1
                setNationPosition(nationName, newPosition);
                return ActionResult.success(
                    nationName + " advanced to position " + newPosition + 
                    " (Passive nations cannot reach At War - must be Active first)"
                );
            }
            
            // Active nations can reach position 0
            newPosition = Math.max(AT_WAR, newPosition);  // Can't go below 0
            setNationPosition(nationName, newPosition);
            
            // Check if nation reached "At War"
            if (currentPosition > AT_WAR && newPosition == AT_WAR) {
                return ActionResult.success(
                    nationName + " is now AT WAR! They can be attacked."
                );
            }
            
            return ActionResult.success(
                nationName + " advanced " + steps + " step(s) on political track (position " + newPosition + ")"
            );
            
        } catch (SQLException e) {
            return ActionResult.error("Failed to advance nation: " + e.getMessage());
        }
    }
    
    /**
     * Activate a nation (flip TwoChit from Passive to Active)
     * This is done with Muster die or specific cards
     * Allows nation to advance on political track
     * Does NOT give extra action dice
     */
    public ActionResult activateNation(String nationName) {
        try {
            // Check if already active
            if (isNationActive(nationName)) {
                return ActionResult.failure(nationName + " is already Active");
            }
            
            // TODO: Flip TwoChit piece from Passive to Active side
            // UPDATE game_pieces SET properties = '{"active":true}' 
            // WHERE piece_type = 'two_chit' AND nation = nationName
            
            return ActionResult.success(
                nationName + " TwoChit activated! They can now advance on the political track."
            );
            
        } catch (SQLException e) {
            return ActionResult.error("Failed to activate nation: " + e.getMessage());
        }
    }
    
    /**
     * Set nation political position
     */
    private void setNationPosition(String nationName, int position) throws SQLException {
        // TODO: Update game_pieces table with political marker position
        // For now, just log
        System.out.println("Set " + nationName + " political position to " + position);
    }
    
    /**
     * Get action dice count for a faction (Free Peoples or Shadow)
     * Dice are allocated to the SIDE, not individual nations
     * Active/Passive state does NOT affect dice count
     * 
     * TODO: This should probably be moved to DicePool or TurnManager
     * as it's not really part of the political track system
     */
    public int getActionDiceForFaction(String faction) throws SQLException {
        // Free Peoples get 4 dice, Shadow gets 7 dice
        // (This is simplified - actual game has variable counts)
        if ("free_peoples".equalsIgnoreCase(faction)) {
            return 4;
        } else if ("shadow".equalsIgnoreCase(faction)) {
            return 7;
        }
        return 0;
    }
    
    /**
     * Check if nation can advance (not yet at war)
     */
    public boolean canAdvance(String nationName) throws SQLException {
        int currentPosition = getNationPosition(nationName);
        return currentPosition > AT_WAR;  // Not yet at position 0
    }
    
    /**
     * Get human-readable name for position
     */
    public String getPositionName(int position) {
        switch (position) {
            case 0: return "At War (Position 0)";
            case 1: return "Not at War (Position 1)";
            case 2: return "Not at War (Position 2)";
            case 3: return "Not at War (Position 3)";
            default: return "Unknown Position";
        }
    }
    
    /**
     * Get summary of all nations' political states
     */
    public String getPoliticalSummary() throws SQLException {
        StringBuilder sb = new StringBuilder();
        sb.append("=== Political Track Status ===\n\n");
        
        sb.append("Free Peoples (Total: " + getActionDiceForFaction("free_peoples") + " dice):\n");
        for (String nation : new String[]{"gondor", "rohan", "north", "elves", "dwarves"}) {
            int position = getNationPosition(nation);
            boolean active = isNationActive(nation);
            sb.append(String.format("  %s: %s [%s]\n", 
                capitalize(nation), 
                getPositionName(position),
                active ? "Active" : "Passive"
            ));
        }
        
        sb.append("\nShadow (Total: " + getActionDiceForFaction("shadow") + " dice):\n");
        for (String nation : new String[]{"sauron", "isengard", "southrons", "easterlings"}) {
            int position = getNationPosition(nation);
            boolean active = isNationActive(nation);
            sb.append(String.format("  %s: %s [%s]\n", 
                capitalize(nation), 
                getPositionName(position),
                active ? "Active" : "Passive"
            ));
        }
        
        return sb.toString();
    }
    
    private String capitalize(String str) {
        if (str == null || str.isEmpty()) return str;
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }
}
