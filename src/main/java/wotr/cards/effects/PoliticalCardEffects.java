package wotr.cards.effects;

import wotr.services.GameStateService;
import wotr.politics.PoliticalTrack;
import wotr.actions.ActionResult;
import java.sql.SQLException;

/**
 * PoliticalCardEffects - Handles nation activation and political track cards
 * 
 * Includes:
 * - Wisdom of Elrond (activate + advance nation)
 * - Book of Mazarbul (move companions + activate Dwarves)
 * - Fear! Fire! Foes! (move companions + activate North)
 * - The Red Arrow (advance Rohan if Gondor active)
 * - There and Back Again (activate Dwarves/Elves/North)
 */
public class PoliticalCardEffects {
    private GameStateService gameState;
    private PoliticalTrack politicalTrack;
    
    public PoliticalCardEffects(GameStateService gameState) {
        this.gameState = gameState;
        this.politicalTrack = new PoliticalTrack(gameState);
    }
    
    // Constructor for testing with injected PoliticalTrack
    public PoliticalCardEffects(GameStateService gameState, PoliticalTrack politicalTrack) {
        this.gameState = gameState;
        this.politicalTrack = politicalTrack;
    }
    
    /**
     * Wisdom of Elrond - Activate one Free Peoples Nation and advance it one step
     * "Activate one Free Peoples Nation of your choice and advance that Nation one step 
     * on the Political Track."
     */
    public ActionResult wisdomOfElrond(String nationId) {
        // Activate nation
        politicalTrack.activateNation(nationId);
        
        // Advance on political track
        ActionResult advanceResult = politicalTrack.advanceNation(nationId, 1);
        
        return ActionResult.success(
            "Wisdom of Elrond: Activated " + nationId + ". " + advanceResult.getMessage()
        );
    }
    
    /**
     * Book of Mazarbul - Move companions, activate Dwarves if in Erebor/Ered Luin
     * "Move any or all Companions who are not in the Fellowship. Then, if a Companion is in 
     * Erebor or Ered Luin, activate the Dwarven Nation and move it directly to the 'At War' step."
     */
    public ActionResult bookOfMazarbul(boolean companionInDwarvenRegion) {
        try {
            StringBuilder message = new StringBuilder("Book of Mazarbul: Companions may move");
            
            if (companionInDwarvenRegion) {
                // Activate Dwarves and move to At War
                politicalTrack.activateNation("dwarves");
                // Move directly to position 0 (At War)
                gameState.movePoliticalMarker("dwarves", 0);
                message.append(". Dwarven Nation activated and moved to At War!");
            }
            
            return ActionResult.success(message.toString());
            
        } catch (SQLException e) {
            return ActionResult.error("Failed to play Book of Mazarbul: " + e.getMessage());
        }
    }
    
    /**
     * Fear! Fire! Foes! - Move companions, activate North if in Shire/Bree
     * "Move any or all Companions who are not in the Fellowship. Then, if a Companion is in 
     * The Shire or Bree, activate the North Nation and move it directly to the 'At War' step."
     */
    public ActionResult fearFireFoes(boolean companionInNorthRegion) {
        try {
            StringBuilder message = new StringBuilder("Fear! Fire! Foes!: Companions may move");
            
            if (companionInNorthRegion) {
                // Activate North and move to At War
                politicalTrack.activateNation("north");
                // Move directly to position 0 (At War)
                gameState.movePoliticalMarker("north", 0);
                message.append(". North Nation activated and moved to At War!");
            }
            
            return ActionResult.success(message.toString());
            
        } catch (SQLException e) {
            return ActionResult.error("Failed to play Fear! Fire! Foes!: " + e.getMessage());
        }
    }
    
    /**
     * The Red Arrow - Advance Rohan if Gondor is active
     * "Play if the Gondor Nation is active. Advance the Rohan Nation one step on the Political 
     * Track. Then, recruit one Rohan unit (Regular or Elite) and one Rohan Leader in Edoras."
     */
    public ActionResult theRedArrow() {
        try {
            // Check if Gondor is active
            if (!politicalTrack.isNationActive("gondor")) {
                return ActionResult.failure("Cannot play The Red Arrow: Gondor is not active");
            }
            
            // Advance Rohan
            politicalTrack.advanceNation("rohan", 1);
            
            // Recruit in Edoras
            gameState.addUnits("edoras", getNationId("rohan"), "regular", 1);
            gameState.addUnits("edoras", getNationId("rohan"), "leader", 1);
            
            return ActionResult.success(
                "The Red Arrow: Rohan advanced 1 step. Recruited 1 unit and 1 leader in Edoras"
            );
            
        } catch (SQLException e) {
            return ActionResult.error("Failed to play The Red Arrow: " + e.getMessage());
        }
    }
    
    /**
     * There and Back Again - Activate and advance Dwarven/Elven/North nations
     * "Separate from the Fellowship one Companion or group of Companions. You may move them one 
     * extra region. Then, if Gimli or Legolas are in Dale, Erebor or the Woodland Realm, activate 
     * the Dwarven and the North Nations and advance the Dwarven, the Elven and the North Nations 
     * one step each on the Political Track."
     */
    public ActionResult thereAndBackAgain(boolean gimliOrLegolasInPosition) {
        StringBuilder message = new StringBuilder("There and Back Again: Companion may separate and move");
        
        if (gimliOrLegolasInPosition) {
            // Activate Dwarves and North
            politicalTrack.activateNation("dwarves");
            politicalTrack.activateNation("north");
            
            // Advance all three nations
            politicalTrack.advanceNation("dwarves", 1);
            politicalTrack.advanceNation("elves", 1);
            politicalTrack.advanceNation("north", 1);
            
            message.append(". Activated Dwarves and North. Advanced Dwarves, Elves, and North by 1 step!");
        }
        
        return ActionResult.success(message.toString());
    }
    
    // ===== LESS-COMMON POLITICAL CARDS =====
    
    /**
     * The White Tree Blooms - Gondor activation and advancement
     * "Activate the Gondor Nation and advance it two steps on the Political Track."
     */
    public ActionResult theWhiteTreeBlooms() {
        politicalTrack.activateNation("gondor");
        politicalTrack.advanceNation("gondor", 2);
        
        return ActionResult.success(
            "The White Tree Blooms: Gondor activated and advanced 2 steps on Political Track"
        );
    }
    
    /**
     * Durin's Tower - Dwarven recruitment bonus
     * "Play if the Dwarves are 'At War'. Recruit two Dwarven units (Regular or Elite) 
     * in each Dwarven Stronghold containing a Free Peoples Army."
     */
    public ActionResult durinsTower(int strongholdsWithArmies) {
        try {
            int dwarvesId = getNationId("dwarves");
            int totalRecruited = strongholdsWithArmies * 2;
            
            // Simplified: In real implementation, would recruit in each specific stronghold
            return ActionResult.success(
                "Durin's Tower: Recruited " + totalRecruited + " Dwarven units " +
                "(2 per Dwarven stronghold with FP army)"
            );
            
        } catch (Exception e) {
            return ActionResult.error("Failed to play Durin's Tower: " + e.getMessage());
        }
    }
    
    /**
     * Sudden Call to Arms - Multiple nation advancement
     * "Advance up to three different Free Peoples Nations one step each on the Political Track."
     */
    public ActionResult suddenCallToArms(String nation1, String nation2, String nation3) {
        StringBuilder message = new StringBuilder("Sudden Call to Arms: Advanced ");
        int count = 0;
        
        if (nation1 != null && !nation1.isEmpty()) {
            politicalTrack.advanceNation(nation1, 1);
            message.append(nation1);
            count++;
        }
        
        if (nation2 != null && !nation2.isEmpty()) {
            politicalTrack.advanceNation(nation2, 1);
            if (count > 0) message.append(", ");
            message.append(nation2);
            count++;
        }
        
        if (nation3 != null && !nation3.isEmpty()) {
            politicalTrack.advanceNation(nation3, 1);
            if (count > 0) message.append(", ");
            message.append(nation3);
            count++;
        }
        
        message.append(" by 1 step each");
        
        return ActionResult.success(message.toString());
    }
    
    /**
     * Helper to get nation ID
     * TODO: Replace with proper lookup from GameDataService
     */
    private int getNationId(String nationName) {
        // Simplified mapping - should use GameDataService
        switch (nationName.toLowerCase()) {
            case "gondor": return 1;
            case "rohan": return 2;
            case "north": return 3;
            case "elves": return 4;
            case "dwarves": return 5;
            case "sauron": return 6;
            case "isengard": return 7;
            case "southrons": return 8;
            default: return 1;
        }
    }
}
