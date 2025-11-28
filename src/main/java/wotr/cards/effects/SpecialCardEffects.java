package wotr.cards.effects;

import wotr.services.GameStateService;
import wotr.fellowship.FellowshipManager;
import wotr.cards.OnTableCardManager;
import wotr.actions.ActionResult;
import java.sql.SQLException;
import java.util.Random;

/**
 * SpecialCardEffects - Handles unique and special card effects
 * 
 * Includes:
 * - The Ents Awake (3 variants: Treebeard, Huorns, Entmoot)
 * - Dead Men of Dunharrow (Aragorn special movement + attack)
 * - The Eagles are Coming! (eliminate Nazgûl)
 * - House of the Stewards (Boromir recruit + draw cards)
 * - The Grey Company (upgrade unit + draw cards)
 * - The Last Battle (on table: no Fellowship dice to Hunt)
 * - A Power too Great (on table: protect Elven strongholds)
 */
public class SpecialCardEffects {
    private GameStateService gameState;
    private FellowshipManager fellowship;
    private OnTableCardManager onTableManager;
    private Random random;
    
    public SpecialCardEffects(GameStateService gameState) {
        this.gameState = gameState;
        this.fellowship = new FellowshipManager(gameState);
        this.onTableManager = new OnTableCardManager(gameState);
        this.random = new Random();
    }
    
    // ===== THE ENTS AWAKE =====
    
    /**
     * The Ents Awake (all 3 variants) - Attack Orthanc with dice
     * "Roll three dice; for each result of 4+, score one hit against a Shadow Army in Orthanc. 
     * If the Army is destroyed, so are any Nazgûl and Minions along with it. If Saruman is in 
     * Orthanc without a Shadow Army, eliminate him. If Gandalf the White is in Fangorn or a 
     * Rohan region, you may immediately play another Character Event card from your hand without 
     * using an Action die."
     */
    public ActionResult theEntsAwake(String variant, boolean gandalfWhiteInPosition) {
        int hits = 0;
        StringBuilder rolls = new StringBuilder("Dice rolls: ");
        
        for (int i = 0; i < 3; i++) {
            int roll = random.nextInt(6) + 1;
            rolls.append(roll).append(" ");
            if (roll >= 4) {
                hits++;
            }
        }
        
        StringBuilder message = new StringBuilder();
        message.append("The Ents Awake (").append(variant).append("): ");
        message.append(rolls.toString().trim());
        message.append(". Scored ").append(hits).append(" hit").append(hits == 1 ? "" : "s");
        message.append(" against army in Orthanc");
        
        if (gandalfWhiteInPosition) {
            message.append(". May play another Character Event card without using an Action die!");
        }
        
        return ActionResult.success(message.toString());
    }
    
    /**
     * Dead Men of Dunharrow - Aragorn special movement and attack
     * "Move Strider/Aragorn (and any number of Companions in the same region) to Erech, Lamedon 
     * or Pelargir. If there is a Shadow Army in that region, roll a die. That Army takes a number 
     * of hits equal to the die result and must then retreat. If the Army is destroyed, so are any 
     * Nazgûl and Minions along with it. You may then recruit up to three Gondor Regular units in 
     * that region, taking control if necessary."
     */
    public ActionResult deadMenOfDunharrow(String destinationRegion, boolean enemyPresent) {
        try {
            // Move Aragorn
            gameState.moveCharacter("aragorn", destinationRegion);
            
            StringBuilder message = new StringBuilder();
            message.append("Dead Men of Dunharrow: Aragorn moved to ").append(destinationRegion);
            
            if (enemyPresent) {
                int dieRoll = random.nextInt(6) + 1;
                message.append(". Rolled ").append(dieRoll);
                message.append(" - Shadow army takes ").append(dieRoll).append(" hit");
                message.append(dieRoll == 1 ? "" : "s").append(" and must retreat");
            }
            
            // Recruit Gondor units
            int gondorId = getNationId("gondor");
            gameState.addUnits(destinationRegion, gondorId, "regular", 3);
            message.append(". Recruited 3 Gondor regulars");
            
            return ActionResult.success(message.toString());
            
        } catch (SQLException e) {
            return ActionResult.error("Failed to play Dead Men of Dunharrow: " + e.getMessage());
        }
    }
    
    /**
     * The Eagles are Coming! - Eliminate Nazgûl
     * "Roll a die for each Nazgûl present (up to a maximum of five dice) and eliminate a Nazgûl 
     * for each roll of 5+. All surviving Nazgûl must immediately be moved to any one unconquered 
     * Sauron Stronghold. The Witch-king is not considered a Nazgûl for the purposes of this card."
     */
    public ActionResult theEaglesAreComing(int nazgulCount) {
        int diceToRoll = Math.min(nazgulCount, 5);
        int eliminated = 0;
        StringBuilder rolls = new StringBuilder("Dice rolls: ");
        
        for (int i = 0; i < diceToRoll; i++) {
            int roll = random.nextInt(6) + 1;
            rolls.append(roll).append(" ");
            if (roll >= 5) {
                eliminated++;
            }
        }
        
        return ActionResult.success(
            String.format("The Eagles are Coming!: %s. Eliminated %d Nazgûl (of %d). " +
                "Survivors move to Sauron Stronghold",
                rolls.toString().trim(),
                eliminated,
                nazgulCount
            )
        );
    }
    
    /**
     * House of the Stewards - Recruit with Boromir and draw cards
     * "Recruit one Gondor unit (Regular or Elite) in the region with Boromir. Then, draw two 
     * Strategy Event cards."
     */
    public ActionResult houseOfTheStewards(String boromirRegion) {
        try {
            int gondorId = getNationId("gondor");
            gameState.addUnits(boromirRegion, gondorId, "regular", 1);
            
            return ActionResult.success(
                "House of the Stewards: Recruited 1 Gondor unit in " + boromirRegion + 
                ". Draw 2 Strategy Event cards"
            );
            
        } catch (SQLException e) {
            return ActionResult.error("Failed to play House of the Stewards: " + e.getMessage());
        }
    }
    
    /**
     * The Grey Company - Upgrade unit with Aragorn and draw cards
     * "Eliminate one Regular unit to recruit one Elite unit of the same Nation, in the Army 
     * with Strider/Aragorn. Then, draw two Strategy Event cards."
     */
    public ActionResult theGreyCompany(String aragornRegion, String nationName) {
        try {
            int nationId = getNationId(nationName);
            
            // Remove regular, add elite
            gameState.removeUnits(aragornRegion, nationId, "regular", 1);
            gameState.addUnits(aragornRegion, nationId, "elite", 1);
            
            return ActionResult.success(
                "The Grey Company: Upgraded 1 regular to 1 elite in " + aragornRegion + 
                ". Draw 2 Strategy Event cards"
            );
            
        } catch (SQLException e) {
            return ActionResult.error("Failed to play The Grey Company: " + e.getMessage());
        }
    }
    
    /**
     * The Last Battle - On table: no Fellowship dice to Hunt
     * "Play on the table if Aragorn is with a Free Peoples Army in a region outside of a Free 
     * Peoples Nation. While this card is in play, Action dice used to move the Fellowship are 
     * not added to the Hunt Box. You must discard this card from the table as soon as the 
     * Fellowship is declared or revealed."
     */
    public ActionResult theLastBattle(boolean aragornQualifies) {
        if (!aragornQualifies) {
            return ActionResult.failure(
                "Cannot play The Last Battle: Aragorn must be with army outside FP nations"
            );
        }
        
        return ActionResult.success(
            "The Last Battle: Play on table. Fellowship movement dice not added to Hunt Box " +
            "(discard when Fellowship declared/revealed)"
        );
    }
    
    /**
     * A Power too Great - On table: protect Elven strongholds
     * "Play on the table. Advance the Elven Nation one step on the Political Track. While this 
     * card is in play, the Shadow player cannot move an Army into or attack (either in a field 
     * battle or in a siege) Lórien, Rivendell or The Grey Havens."
     */
    public ActionResult aPowerTooGreat() {
        return ActionResult.success(
            "A Power too Great: Play on table. Advance Elves 1 step. " +
            "Shadow cannot attack Lórien, Rivendell, or Grey Havens"
        );
    }
    
    // ===== ON-TABLE CARDS =====
    
    /**
     * The Power of Tom Bombadil - On-table card protecting Shire and Buckland
     * "While this card is on the table, the Shire and Buckland cannot be attacked by Shadow Armies."
     */
    public ActionResult thePowerOfTomBombadil() {
        ActionResult placeResult = onTableManager.placeCardOnTable(
            "The Power of Tom Bombadil", 
            "free_peoples"
        );
        
        if (placeResult.isSuccess()) {
            return ActionResult.success(
                "The Power of Tom Bombadil: Placed on table. " +
                "Shire and Buckland cannot be attacked while this card is on the table."
            );
        }
        
        return placeResult;
    }
    
    // ===== LESS-COMMON SPECIAL CARDS =====
    
    /**
     * Servants of the Secret Fire - Anti-Nazgûl card
     * "Play if Gandalf the White is in play. Remove one Nazgûl from the game board 
     * (not to the Nazgûl track). The Witch-king cannot be removed in this way."
     */
    public ActionResult servantsOfTheSecretFire(String nazgulId, boolean isGandalfWhite, boolean isWitchKing) {
        if (!isGandalfWhite) {
            return ActionResult.failure(
                "Cannot play Servants of the Secret Fire: Gandalf the White must be in play"
            );
        }
        
        if (isWitchKing) {
            return ActionResult.failure(
                "Cannot remove Witch-king with Servants of the Secret Fire"
            );
        }
        
        try {
            // Remove Nazgûl from game permanently - move to FP casualties area (out of game)
            gameState.moveCharacter(nazgulId, "fp_casualties");
            
            return ActionResult.success(
                "Servants of the Secret Fire: Removed " + nazgulId + " from game " +
                "(not to Nazgûl track, permanently removed)"
            );
            
        } catch (SQLException e) {
            return ActionResult.error("Failed to play Servants of the Secret Fire: " + e.getMessage());
        }
    }
    
    /**
     * Helper to get nation ID
     */
    private int getNationId(String nationName) {
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
