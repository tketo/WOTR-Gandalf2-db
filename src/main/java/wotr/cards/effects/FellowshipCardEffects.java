package wotr.cards.effects;

import wotr.services.GameStateService;
import wotr.fellowship.FellowshipManager;
import wotr.actions.ActionResult;
import java.sql.SQLException;
import java.util.Random;

/**
 * FellowshipCardEffects - Handles Fellowship-related card effects
 * 
 * Includes:
 * - Corruption healing (Athelas, Bilbo's Song, There is Another Way)
 * - Guide changes (Mirror of Galadriel allows die changes but could affect guide)
 * - Companion separation (I Will Go Alone, We Prove the Swifter, Gwaihir)
 * - Hunt tile manipulation (Challenge of the King)
 */
public class FellowshipCardEffects {
    private GameStateService gameState;
    private FellowshipManager fellowship;
    private Random random;
    
    public FellowshipCardEffects(GameStateService gameState) {
        this.gameState = gameState;
        this.fellowship = new FellowshipManager(gameState);
        this.random = new Random();
    }
    
    // Constructor for testing with injected FellowshipManager
    public FellowshipCardEffects(GameStateService gameState, FellowshipManager fellowship) {
        this.gameState = gameState;
        this.fellowship = fellowship;
        this.random = new Random();
    }
    
    /**
     * Athelas - Heal corruption based on dice rolls
     * "Roll three dice and heal one Corruption point for each die result of 5+. 
     * If Strider is the Guide, heal one Corruption point for each die result of 3+ instead."
     */
    public ActionResult athelas() {
        try {
            String guide = fellowship.getFellowshipGuide();
            boolean striderIsGuide = guide != null && 
                (guide.equalsIgnoreCase("strider") || guide.equalsIgnoreCase("aragorn"));
            
            int threshold = striderIsGuide ? 3 : 5;
            int healedPoints = 0;
            StringBuilder rolls = new StringBuilder("Dice rolls: ");
            
            for (int i = 0; i < 3; i++) {
                int roll = random.nextInt(6) + 1; // 1-6
                rolls.append(roll).append(" ");
                if (roll >= threshold) {
                    healedPoints++;
                }
            }
            
            // Heal corruption
            if (healedPoints > 0) {
                gameState.adjustCorruption(-healedPoints);
            }
            
            String message = String.format("Athelas: %s. Healed %d corruption point%s%s",
                rolls.toString().trim(),
                healedPoints,
                healedPoints == 1 ? "" : "s",
                striderIsGuide ? " (Strider is Guide: heal on 3+)" : " (heal on 5+)"
            );
            
            return ActionResult.success(message);
            
        } catch (SQLException e) {
            return ActionResult.error("Failed to play Athelas: " + e.getMessage());
        }
    }
    
    /**
     * Bilbo's Song - Heal corruption (more if Gollum is guide)
     * "Heal one Corruption point. If Gollum is the Guide, heal two Corruption points instead."
     */
    public ActionResult bilbosSong() {
        try {
            String guide = fellowship.getFellowshipGuide();
            boolean gollumIsGuide = guide != null && guide.equalsIgnoreCase("gollum");
            
            int healAmount = gollumIsGuide ? 2 : 1;
            gameState.adjustCorruption(-healAmount);
            
            String message = String.format("Bilbo's Song: Healed %d corruption point%s%s",
                healAmount,
                healAmount == 1 ? "" : "s",
                gollumIsGuide ? " (Gollum is Guide)" : ""
            );
            
            return ActionResult.success(message);
            
        } catch (SQLException e) {
            return ActionResult.error("Failed to play Bilbo's Song: " + e.getMessage());
        }
    }
    
    /**
     * There is Another Way - Heal corruption and optionally move Fellowship if Gollum is guide
     * "Heal one Corruption point. Then, if Gollum is the Guide, you may also hide or move 
     * the Fellowship (following the normal movement rules)."
     */
    public ActionResult thereIsAnotherWay(boolean moveOrHide, String destinationRegion) {
        try {
            // Heal corruption
            gameState.adjustCorruption(-1);
            StringBuilder message = new StringBuilder("There is Another Way: Healed 1 corruption point");
            
            // Check if Gollum is guide
            String guide = fellowship.getFellowshipGuide();
            boolean gollumIsGuide = guide != null && guide.equalsIgnoreCase("gollum");
            
            if (gollumIsGuide && moveOrHide) {
                if (destinationRegion != null && !destinationRegion.isEmpty()) {
                    // Move Fellowship
                    gameState.moveFellowship(destinationRegion);
                    message.append(". Fellowship moved to ").append(destinationRegion);
                } else {
                    // Hide Fellowship
                    gameState.setFellowshipRevealed(false);
                    message.append(". Fellowship hidden");
                }
                message.append(" (Gollum is Guide)");
            }
            
            return ActionResult.success(message.toString());
            
        } catch (SQLException e) {
            return ActionResult.error("Failed to play There is Another Way: " + e.getMessage());
        }
    }
    
    /**
     * I Will Go Alone - Separate companion, allow extra movement, heal corruption
     * "Separate one Companion or one group of Companions from the Fellowship. 
     * You may move the Companions one extra region. Then, heal one Corruption point."
     */
    public ActionResult iWillGoAlone(String companionId, String destinationRegion) {
        try {
            // Separate companion
            ActionResult separateResult = fellowship.separateCompanion(companionId, destinationRegion);
            if (!separateResult.isSuccess()) {
                return separateResult;
            }
            
            // Heal corruption
            gameState.adjustCorruption(-1);
            
            return ActionResult.success(
                companionId + " separated to " + destinationRegion + 
                " (may move one extra region). Healed 1 corruption point."
            );
            
        } catch (SQLException e) {
            return ActionResult.error("Failed to play I Will Go Alone: " + e.getMessage());
        }
    }
    
    /**
     * We Prove the Swifter - Separate or move companion with extra movement
     * "Separate from the Fellowship, or move, one Companion or one group of Companions. 
     * You may move them two extra regions. The movement of these Companions is allowed 
     * to end in a Stronghold under siege."
     */
    public ActionResult weProveTheSwifter(String companionId, String destinationRegion, boolean separate) {
        try {
            if (separate) {
                ActionResult result = fellowship.separateCompanion(companionId, destinationRegion);
                if (!result.isSuccess()) {
                    return result;
                }
                return ActionResult.success(
                    companionId + " separated to " + destinationRegion + 
                    " (may move two extra regions, can enter besieged Stronghold)"
                );
            } else {
                // Move existing separated companion
                gameState.moveCharacter(companionId, destinationRegion);
                return ActionResult.success(
                    companionId + " moved to " + destinationRegion + 
                    " (may move two extra regions, can enter besieged Stronghold)"
                );
            }
            
        } catch (SQLException e) {
            return ActionResult.error("Failed to play We Prove the Swifter: " + e.getMessage());
        }
    }
    
    /**
     * Gwaihir the Windlord - Move companion as if Level 4
     * "Separate from the Fellowship, or move, one Companion or one group of Companions 
     * as if their Level were 4. This movement of these Companions is allowed to end 
     * in a Stronghold under siege."
     */
    public ActionResult gwaihirTheWindlord(String companionId, String destinationRegion, boolean separate) {
        try {
            if (separate) {
                ActionResult result = fellowship.separateCompanion(companionId, destinationRegion);
                if (!result.isSuccess()) {
                    return result;
                }
                return ActionResult.success(
                    companionId + " separated to " + destinationRegion + 
                    " (moves as Level 4, can enter besieged Stronghold)"
                );
            } else {
                gameState.moveCharacter(companionId, destinationRegion);
                return ActionResult.success(
                    companionId + " moved to " + destinationRegion + 
                    " (moves as Level 4, can enter besieged Stronghold)"
                );
            }
            
        } catch (SQLException e) {
            return ActionResult.error("Failed to play Gwaihir the Windlord: " + e.getMessage());
        }
    }
    
    /**
     * Challenge of the King - Draw Hunt tiles and eliminate Aragorn if all show Eyes
     * "Draw three Hunt tiles. If all three drawn tiles show Eyes, put them back in the 
     * Hunt Pool and eliminate Strider/Aragorn. Otherwise, discard permanently the drawn 
     * tiles bearing an Eye for the remainder of the game."
     */
    public ActionResult challengeOfTheKing() {
        try {
            // Simulate drawing 3 Hunt tiles
            int eyeCount = 0;
            int totalDrawn = 3;
            
            // Simplified simulation - in reality would draw from Hunt pool
            for (int i = 0; i < totalDrawn; i++) {
                int tileRoll = random.nextInt(6) + 1;
                if (tileRoll >= 5) { // Approximate Eye probability
                    eyeCount++;
                }
            }
            
            if (eyeCount == totalDrawn) {
                // All Eyes - eliminate Aragorn (Free Peoples character)
                fellowship.eliminateCharacter("aragorn", "free_peoples");
                return ActionResult.success(
                    "Challenge of the King: All 3 tiles showed Eyes! Aragorn eliminated."
                );
            } else {
                return ActionResult.success(
                    String.format("Challenge of the King: %d tile%s showed Eye%s. " +
                        "Eye tiles permanently discarded, others returned to Hunt Pool.",
                        eyeCount,
                        eyeCount == 1 ? "" : "s",
                        eyeCount == 1 ? "" : "s"
                    )
                );
            }
            
        } catch (Exception e) {
            return ActionResult.error("Failed to play Challenge of the King: " + e.getMessage());
        }
    }
    
    /**
     * Mirror of Galadriel - Change die to Will of the West, heal if in Lórien
     * "Change any one unused Free Peoples Character Action die result into a Will of the West 
     * die result. If the Fellowship is in Lórien, and Lórien is unconquered, also heal one 
     * Corruption point."
     */
    public ActionResult mirrorOfGaladriel(String dieId) {
        try {
            // Change die result to Will of the West
            gameState.changeDieResult(dieId, "will");
            
            StringBuilder message = new StringBuilder("Mirror of Galadriel: Changed die to Will of the West");
            
            // Check if Fellowship is in Lórien
            String fellowshipLocation = gameState.getFellowshipLocation();
            if (fellowshipLocation != null && fellowshipLocation.equalsIgnoreCase("lorien")) {
                // TODO: Check if Lórien is unconquered
                // For now, assume it is and heal corruption
                gameState.adjustCorruption(-1);
                message.append(". Fellowship in Lórien: healed 1 corruption point");
            }
            
            return ActionResult.success(message.toString());
            
        } catch (SQLException e) {
            return ActionResult.error("Failed to play Mirror of Galadriel: " + e.getMessage());
        }
    }
    
    // ===== ADDITIONAL FELLOWSHIP CARDS =====
    
    /**
     * Gandalf's Cart - Move Fellowship and hide
     * "Move the Fellowship up to two regions. The Fellowship is hidden."
     */
    public ActionResult gandalfsCart(String targetRegion) {
        try {
            gameState.moveFellowship(targetRegion);
            gameState.setFellowshipRevealed(false);
            
            return ActionResult.success(
                "Gandalf's Cart: Fellowship moved to " + targetRegion + " and hidden"
            );
            
        } catch (SQLException e) {
            return ActionResult.error("Failed to play Gandalf's Cart: " + e.getMessage());
        }
    }
    
    /**
     * Hobbit Stealth - Hide Fellowship and heal corruption
     * "The Fellowship is hidden. Then, if the Guide is a Hobbit, remove 1 Corruption from the Fellowship."
     */
    public ActionResult hobbitStealth(boolean guideIsHobbit) {
        try {
            gameState.setFellowshipRevealed(false);
            
            StringBuilder message = new StringBuilder("Hobbit Stealth: Fellowship hidden");
            
            if (guideIsHobbit) {
                gameState.adjustCorruption(-1);
                message.append(". Removed 1 corruption (Hobbit guide)");
            }
            
            return ActionResult.success(message.toString());
            
        } catch (SQLException e) {
            return ActionResult.error("Failed to play Hobbit Stealth: " + e.getMessage());
        }
    }
    
    /**
     * Use Palantír - Change action die but add corruption
     * "Change the result showing on one of your unused Action dies. Then, add 1 Corruption to the Fellowship."
     */
    public ActionResult usePalantir(String dieId, String newResult) {
        try {
            gameState.changeDieResult(dieId, newResult);
            gameState.adjustCorruption(1);
            
            return ActionResult.success(
                "Use Palantír: Changed die to " + newResult + ". Added 1 corruption"
            );
            
        } catch (SQLException e) {
            return ActionResult.error("Failed to use Palantír: " + e.getMessage());
        }
    }
    
    /**
     * Shadowfax - Gandalf movement with dice manipulation
     * "Move Gandalf the White (and any number of Companions in the same region) to Edoras or Minas Tirith. 
     * Then, you may change one of your unused Action dice to any result."
     */
    public ActionResult shadowfax(String destination, String dieId, String newResult) {
        try {
            gameState.moveCharacter("gandalf_white", destination);
            
            StringBuilder message = new StringBuilder();
            message.append("Shadowfax: Gandalf moved to ").append(destination);
            
            if (dieId != null && newResult != null) {
                gameState.changeDieResult(dieId, newResult);
                message.append(". Changed die to ").append(newResult);
            }
            
            return ActionResult.success(message.toString());
            
        } catch (SQLException e) {
            return ActionResult.error("Failed to play Shadowfax: " + e.getMessage());
        }
    }
    
    /**
     * Kindred of Glorfindel - Recruit Elves with companion
     * "Play if a Companion is in Rivendell. Recruit two Elven units (Regular or Elite) in Rivendell."
     */
    public ActionResult kindredOfGlorfindel(boolean companionInRivendell) {
        if (!companionInRivendell) {
            return ActionResult.failure("Cannot play Kindred of Glorfindel: No companion in Rivendell");
        }
        
        try {
            int elvesId = 4;
            gameState.addUnits("rivendell", elvesId, "regular", 2);
            
            return ActionResult.success(
                "Kindred of Glorfindel: Recruited 2 Elven units in Rivendell"
            );
            
        } catch (SQLException e) {
            return ActionResult.error("Failed to play Kindred of Glorfindel: " + e.getMessage());
        }
    }
    
    /**
     * Anduril, Flame of the West - Combat bonus
     * "During this Combat, Strider/Aragorn's Leadership is increased by 3 for the purposes of 
     * re-rolling combat dice. After the Combat, you may recruit one Gondor Regular unit in the 
     * region where the battle took place."
     */
    public ActionResult andurilFlameOfTheWest(String battleRegion) {
        try {
            int gondorId = 1;
            gameState.addUnits(battleRegion, gondorId, "regular", 1);
            
            return ActionResult.success(
                "Anduril, Flame of the West: Aragorn +3 Leadership this combat. " +
                "Recruited 1 Gondor unit in " + battleRegion
            );
            
        } catch (SQLException e) {
            return ActionResult.error("Failed to play Anduril: " + e.getMessage());
        }
    }
    
    // ===== LESS-COMMON FELLOWSHIP CARDS =====
    
    /**
     * The Old Thrush - Companion movement from Erebor
     * "Play if a Companion is in Erebor or Dale. Move that Companion (and the Fellowship, 
     * if appropriate) up to two regions."
     */
    public ActionResult theOldThrush(String companionId, String targetRegion, boolean moveFellowship) {
        try {
            if (moveFellowship) {
                gameState.moveFellowship(targetRegion);
                return ActionResult.success(
                    "The Old Thrush: Fellowship moved to " + targetRegion + " (up to 2 regions from Erebor/Dale)"
                );
            } else {
                gameState.moveCharacter(companionId, targetRegion);
                return ActionResult.success(
                    "The Old Thrush: " + companionId + " moved to " + targetRegion + " (up to 2 regions from Erebor/Dale)"
                );
            }
            
        } catch (SQLException e) {
            return ActionResult.error("Failed to play The Old Thrush: " + e.getMessage());
        }
    }
    
    /**
     * Fool of a Took! - Reveal Fellowship but advance towards Mordor
     * "The Fellowship is revealed. Then advance the Fellowship up to two regions 
     * towards Mordor (you may not enter Mordor in this way)."
     */
    public ActionResult foolOfATook(String targetRegion) {
        try {
            gameState.setFellowshipRevealed(true);
            gameState.moveFellowship(targetRegion);
            
            return ActionResult.success(
                "Fool of a Took!: Fellowship revealed and advanced to " + targetRegion + 
                " (up to 2 regions towards Mordor)"
            );
            
        } catch (SQLException e) {
            return ActionResult.error("Failed to play Fool of a Took: " + e.getMessage());
        }
    }
    
    /**
     * The Road Goes Ever On - Extra Fellowship movement
     * "Move the Fellowship up to two regions. The regions traversed must be free 
     * of enemy-controlled areas."
     */
    public ActionResult theRoadGoesEverOn(String targetRegion) {
        try {
            gameState.moveFellowship(targetRegion);
            
            return ActionResult.success(
                "The Road Goes Ever On: Fellowship moved to " + targetRegion + 
                " (up to 2 regions, must be free of enemy control)"
            );
            
        } catch (SQLException e) {
            return ActionResult.error("Failed to play The Road Goes Ever On: " + e.getMessage());
        }
    }
    
    /**
     * Helper to get random corruption for Bilbo's Song
     */
    private int getRandomCorruption() {
        return random.nextInt(3) + 1; // 1-3 corruption
    }
}
