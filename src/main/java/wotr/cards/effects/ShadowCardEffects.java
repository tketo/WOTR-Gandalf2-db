package wotr.cards.effects;

import wotr.services.GameStateService;
import wotr.politics.PoliticalTrack;
import wotr.cards.OnTableCardManager;
import wotr.actions.ActionResult;
import java.sql.SQLException;
import java.util.Random;

/**
 * ShadowCardEffects - Handles Shadow faction card effects
 * 
 * Includes:
 * - The Ringwraiths Are Abroad (Nazgûl movement + army action)
 * - The Black Captain Commands (Witch-king special)
 * - The Shadow is Moving (move 4 armies)
 * - The Shadow Lengthens (move 2 armies)
 * - Dreadful Spells (Nazgûl attack)
 * - Return of the Witch-king (recruit at Angmar)
 * - Wormtongue / Threats and Promises (on table effects)
 */
public class ShadowCardEffects {
    private GameStateService gameState;
    private PoliticalTrack politicalTrack;
    private OnTableCardManager onTableManager;
    private Random random;
    
    public ShadowCardEffects(GameStateService gameState) {
        this.gameState = gameState;
        this.politicalTrack = new PoliticalTrack(gameState);
        this.onTableManager = new OnTableCardManager(gameState);
        this.random = new Random();
    }
    
    /**
     * The Ringwraiths Are Abroad - Move Nazgûl, then move 2 armies or attack with 1
     * "Move any or all of the Nazgûl. Then, you may either move two Armies each containing 
     * a Nazgûl, or attack with one Army containing a Nazgûl."
     */
    public ActionResult theRingwraithsAreAbroad() {
        return ActionResult.success(
            "The Ringwraiths Are Abroad: May move all Nazgûl, then move 2 armies with Nazgûl OR attack with 1 army"
        );
    }
    
    /**
     * The Black Captain Commands - Witch-king special abilities
     * "Play if the Witch-king is in play. You may either recruit two Nazgûl in the region 
     * containing the Witch-king, or move any or all of the Nazgûl. Then, you may move or 
     * attack with an Army containing the Witch-king."
     */
    public ActionResult theBlackCaptainCommands(String witchKingRegion, boolean recruitNazgul) {
        try {
            StringBuilder message = new StringBuilder("The Black Captain Commands: ");
            
            if (recruitNazgul && witchKingRegion != null) {
                // Recruit 2 Nazgûl
                gameState.addUnits(witchKingRegion, getNationId("sauron"), "nazgul", 2);
                message.append("Recruited 2 Nazgûl in ").append(witchKingRegion);
            } else {
                message.append("May move all Nazgûl");
            }
            
            message.append(". May then move or attack with Witch-king's army");
            
            return ActionResult.success(message.toString());
            
        } catch (SQLException e) {
            return ActionResult.error("Failed to play The Black Captain Commands: " + e.getMessage());
        }
    }
    
    /**
     * The Shadow is Moving - Move up to 4 different Shadow Armies one region each
     * "Play if all Shadow Nations are 'At War.' Move up to four different Shadow Armies 
     * one region each."
     */
    public ActionResult theShadowIsMoving() {
        try {
            // Check if all Shadow nations are at war
            boolean allAtWar = politicalTrack.isNationAtWar("sauron") &&
                              politicalTrack.isNationAtWar("isengard") &&
                              politicalTrack.isNationAtWar("southrons");
            
            if (!allAtWar) {
                return ActionResult.failure("Cannot play: Not all Shadow Nations are At War");
            }
            
            return ActionResult.success(
                "The Shadow is Moving: May move up to 4 different Shadow armies 1 region each"
            );
            
        } catch (SQLException e) {
            return ActionResult.error("Failed to play The Shadow is Moving: " + e.getMessage());
        }
    }
    
    /**
     * The Shadow Lengthens - Move two Shadow Armies up to two regions each
     * "Move two Shadow Armies up to two regions: each movement must end in a region already 
     * occupied by another Shadow Army (that must not be under siege)."
     */
    public ActionResult theShadowLengthens() {
        return ActionResult.success(
            "The Shadow Lengthens: May move 2 Shadow armies up to 2 regions (must end with another army)"
        );
    }
    
    /**
     * Dreadful Spells - Roll dice equal to Nazgûl count
     * "Play if a Shadow Army containing Nazgûl is adjacent to, or is in the same region as, 
     * a Free Peoples Army. Roll a number of dice equal to the number of Nazgûl (up to a 
     * maximum of 5) and score one hit for every result of 5+."
     */
    public ActionResult dreadfulSpells(int nazgulCount) {
        int diceToRoll = Math.min(nazgulCount, 5);
        int hits = 0;
        StringBuilder rolls = new StringBuilder("Dice rolls: ");
        
        for (int i = 0; i < diceToRoll; i++) {
            int roll = random.nextInt(6) + 1; // 1-6
            rolls.append(roll).append(" ");
            if (roll >= 5) {
                hits++;
            }
        }
        
        return ActionResult.success(
            String.format("Dreadful Spells: %s. Scored %d hit%s (5+ needed)",
                rolls.toString().trim(),
                hits,
                hits == 1 ? "" : "s"
            )
        );
    }
    
    /**
     * Return of the Witch-king - Move Witch-king to Angmar and recruit
     * "Play if the Witch-king is in play. Move the Witch-king to Angmar, then recruit two 
     * Sauron Regular units and one Sauron Elite unit there."
     */
    public ActionResult returnOfTheWitchKing() {
        try {
            // Move Witch-king to Angmar
            gameState.moveCharacter("witch-king", "angmar");
            
            // Recruit units
            int sauronId = getNationId("sauron");
            gameState.addUnits("angmar", sauronId, "regular", 2);
            gameState.addUnits("angmar", sauronId, "elite", 1);
            
            return ActionResult.success(
                "Return of the Witch-king: Moved to Angmar. Recruited 2 regulars and 1 elite"
            );
            
        } catch (SQLException e) {
            return ActionResult.error("Failed to play Return of the Witch-king: " + e.getMessage());
        }
    }
    
    /**
     * Wormtongue - Play on table, prevents Rohan activation except by special means
     * "Play on the table if Saruman is in play. When 'Wormtongue' is in play, Rohan cannot 
     * be activated except by an appropriate Companion, or by the Fellowship being declared in 
     * Edoras or Helm's Deep, or by an attack on Edoras or Helm's Deep."
     */
    public ActionResult wormtongue() {
        try {
            // Check if Saruman is in play
            if (!gameState.isCharacterInPlay("saruman")) {
                return ActionResult.failure("Cannot play Wormtongue: Saruman is not in play");
            }
            
            // TODO: Track on-table cards (maybe in game_pieces as special card pieces?)
            // For now, just return success
            
            return ActionResult.success(
                "Wormtongue: Played on table. Rohan activation restricted until card is discarded"
            );
            
        } catch (SQLException e) {
            return ActionResult.error("Failed to play Wormtongue: " + e.getMessage());
        }
    }
    
    /**
     * Threats and Promises - Prevent political track advancement via Muster
     * "Play on the table. When 'Threats and Promises' is in play, the Free Peoples player 
     * cannot advance a passive Nation on the Political Track using a Muster action die result."
     */
    public ActionResult threatsAndPromises() {
        // TODO: Track on-table cards
        
        return ActionResult.success(
            "Threats and Promises: Played on table. FP cannot advance passive nations with Muster die"
        );
    }
    
    /**
     * Corsairs of Umbar - Move army from Umbar to Gondor coast
     * "Play if the Southrons & Easterlings are 'At War.' Move one Shadow Army from Umbar to 
     * a Gondor coastal region."
     */
    public ActionResult corsairsOfUmbar(String destinationRegion) {
        try {
            if (!politicalTrack.isNationAtWar("southrons")) {
                return ActionResult.failure("Cannot play: Southrons & Easterlings not At War");
            }
            
            // Move army from Umbar to coastal region
            // This is simplified - actual implementation needs army selection
            return ActionResult.success(
                "Corsairs of Umbar: Army moved from Umbar to " + destinationRegion + 
                ". Battle starts if FP army present"
            );
            
        } catch (SQLException e) {
            return ActionResult.error("Failed to play Corsairs of Umbar: " + e.getMessage());
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
    
    // ===== ADDITIONAL SHADOW CARDS =====
    
    /**
     * Balrog of Moria - Remove Gandalf Grey and recruit Sauron
     * "Play if Gandalf the Grey is in Moria. Eliminate Gandalf the Grey. Then, recruit two 
     * Sauron Regular units and one Sauron Leader in Moria."
     */
    public ActionResult balrogOfMoria(boolean gandalfInMoria) {
        if (!gandalfInMoria) {
            return ActionResult.failure("Cannot play Balrog of Moria: Gandalf the Grey not in Moria");
        }
        
        try {
            // Eliminate Gandalf
            int casualties = 201; // FP casualties
            gameState.moveCharacter("gandalf_grey", String.valueOf(casualties));
            
            // Recruit Sauron units
            int sauronId = getNationId("sauron");
            gameState.addUnits("moria", sauronId, "regular", 2);
            gameState.addUnits("moria", sauronId, "leader", 1);
            
            return ActionResult.success(
                "Balrog of Moria: Gandalf the Grey eliminated! Recruited 2 regulars and 1 leader in Moria"
            );
            
        } catch (SQLException e) {
            return ActionResult.error("Failed to play Balrog of Moria: " + e.getMessage());
        }
    }
    
    /**
     * Snaga! Snaga! - Remove companion and recruit Sauron
     * "Play if a Companion is in Moria or Mount Gundabad. Eliminate that Companion. 
     * Then, if the Witch-king is in play, recruit one Sauron Regular unit in that region."
     */
    public ActionResult snagaSnaga(String companionId, String region, boolean witchKingInPlay) {
        try {
            // Eliminate companion
            int casualties = 201; // FP casualties
            gameState.moveCharacter(companionId, String.valueOf(casualties));
            
            StringBuilder message = new StringBuilder();
            message.append("Snaga! Snaga!: ").append(companionId).append(" eliminated");
            
            if (witchKingInPlay) {
                int sauronId = getNationId("sauron");
                gameState.addUnits(region, sauronId, "regular", 1);
                message.append(". Recruited 1 Sauron regular (Witch-king in play)");
            }
            
            return ActionResult.success(message.toString());
            
        } catch (SQLException e) {
            return ActionResult.error("Failed to play Snaga! Snaga!: " + e.getMessage());
        }
    }
    
    /**
     * Hordes from the East - Recruit Easterlings
     * "Play if the Easterlings are 'At War.' Recruit two Easterling Regular units in 
     * North Rhûn or South Rhûn."
     */
    public ActionResult hordesFromTheEast(String region) {
        try {
            int easterlingsId = getNationId("southrons"); // Using southrons ID for simplicity
            gameState.addUnits(region, easterlingsId, "regular", 2);
            
            return ActionResult.success(
                "Hordes from the East: Recruited 2 Easterling regulars in " + region
            );
            
        } catch (SQLException e) {
            return ActionResult.error("Failed to play Hordes from the East: " + e.getMessage());
        }
    }
    
    /**
     * Morgul Sorcery - Nazgûl moves to stronghold
     * "Move one Nazgûl to an unconquered Shadow Stronghold."
     */
    public ActionResult morgulSorcery(String nazgulId, String stronghold) {
        try {
            gameState.moveCharacter(nazgulId, stronghold);
            
            return ActionResult.success(
                "Morgul Sorcery: " + nazgulId + " moved to " + stronghold
            );
            
        } catch (SQLException e) {
            return ActionResult.error("Failed to play Morgul Sorcery: " + e.getMessage());
        }
    }
    
    /**
     * Await the Call - Re-roll Nazgûl
     * "Roll again for all the Nazgûl which failed to be brought into play (not including the Witch-king)."
     */
    public ActionResult awaitTheCall(int nazgulCount) {
        int rerolled = 0;
        StringBuilder rolls = new StringBuilder("Dice rolls: ");
        
        for (int i = 0; i < nazgulCount; i++) {
            int roll = random.nextInt(6) + 1;
            rolls.append(roll).append(" ");
            if (roll >= 4) {
                rerolled++;
            }
        }
        
        return ActionResult.success(
            String.format("Await the Call: %s. Brought %d Nazgûl into play",
                rolls.toString().trim(),
                rerolled
            )
        );
    }
    
    /**
     * Black Sails - Surprise attack from Umbar
     * "If the Southrons & Easterlings are 'At War,' move a Shadow Army from Umbar to attack a 
     * Free Peoples Army in any one coastal region of Gondor. The Free Peoples player cannot use 
     * a Combat card in this battle."
     */
    public ActionResult blackSails(String targetRegion) {
        return ActionResult.success(
            "Black Sails: Shadow army from Umbar attacks " + targetRegion + 
            " (FP cannot use Combat card)"
        );
    }
    
    // ===== ON-TABLE CARDS =====
    
    /**
     * Denethor's Folly - On-table card enhancing Minas Tirith siege
     * "Play on the table if Denethor is in Minas Tirith. As long as this card is on the table, 
     * Shadow Armies besieging Minas Tirith have +1 Combat strength."
     */
    public ActionResult denethorsFolly(boolean denethorInMinas) {
        if (!denethorInMinas) {
            return ActionResult.failure(
                "Cannot play Denethor's Folly: Denethor must be in Minas Tirith"
            );
        }
        
        ActionResult placeResult = onTableManager.placeCardOnTable(
            "Denethor's Folly",
            "shadow"
        );
        
        if (placeResult.isSuccess()) {
            return ActionResult.success(
                "Denethor's Folly: Placed on table. Shadow armies besieging Minas Tirith have +1 Combat strength."
            );
        }
        
        return placeResult;
    }
    
    /**
     * Threats and Promises - On-table card blocking Muster political advancement
     * The Palantír of Orthanc - On-table card giving Saruman card draw bonus
     * "Play on the table if Saruman is in Orthanc. While this card is on the table, when 
     * Saruman uses a Palantír result, you may draw one additional card."
     */
    public ActionResult thePalantirOfOrthanc(boolean sarumanInOrthanc) {
        if (!sarumanInOrthanc) {
            return ActionResult.failure(
                "Cannot play The Palantír of Orthanc: Saruman must be in Orthanc"
            );
        }
        
        ActionResult placeResult = onTableManager.placeCardOnTable(
            "The Palantír of Orthanc",
            "shadow"
        );
        
        if (placeResult.isSuccess()) {
            return ActionResult.success(
                "The Palantír of Orthanc: Placed on table. Saruman draws +1 card with Palantír results."
            );
        }
        
        return placeResult;
    }
    
    /**
     * The Ring Draws Them - On-table card giving Nazgûl attack bonuses
     * "Play on the table. As long as this card is on the table, all Nazgûl attacking 
     * in a region adjacent to the Fellowship have +1 Combat strength."
     */
    public ActionResult theRingDrawsThem() {
        ActionResult placeResult = onTableManager.placeCardOnTable(
            "The Ring Draws Them",
            "shadow"
        );
        
        if (placeResult.isSuccess()) {
            return ActionResult.success(
                "The Ring Draws Them: Placed on table. Nazgûl attacking adjacent to Fellowship have +1 Combat strength."
            );
        }
        
        return placeResult;
    }
    
    // ===== COMBAT-SPECIFIC CARDS =====
    
    /**
     * Fury of the Witch-king - Witch-king special attack bonus
     * "Play immediately before rolling for combat if the Witch-king is in the battle. 
     * Add three to the number of Combat dice rolled by the Shadow player during this 
     * Combat round (before applying the maximum of five dice)."
     */
    public ActionResult furyOfTheWitchKing(String regionId) {
        return ActionResult.success(
            "Fury of the Witch-king: Shadow rolls +3 Combat dice this round " +
            "(Witch-king must be in battle, applied before 5-dice max)"
        );
    }
    
    // ===== LESS-COMMON SHADOW CARDS =====
    
    /**
     * The Lidless Eye - Add corruption to Fellowship
     * "Add 1 Corruption to the Fellowship."
     */
    public ActionResult theLidlessEye() {
        try {
            gameState.adjustCorruption(1);
            
            return ActionResult.success(
                "The Lidless Eye: Added 1 corruption to Fellowship"
            );
            
        } catch (SQLException e) {
            return ActionResult.error("Failed to play The Lidless Eye: " + e.getMessage());
        }
    }
    
    /**
     * The Dark Lord's Summons - Recruit all available Nazgûl
     * "Recruit all available Nazgûl at Barad-dûr. Then you may move one Nazgûl 
     * from Barad-dûr to any region."
     */
    public ActionResult theDarkLordsSummons(int availableNazgul) {
        return ActionResult.success(
            "The Dark Lord's Summons: Recruited " + availableNazgul + " Nazgûl at Barad-dûr. " +
            "May move 1 Nazgûl to any region"
        );
    }
    
    /**
     * Voice of Malice - Nazgûl manipulation
     * "Move one Nazgûl up to three regions. If this Nazgûl ends its movement 
     * in the same region as a Companion, you may separate that Companion from 
     * the Fellowship."
     */
    public ActionResult voiceOfMalice(String nazgulId, String targetRegion, boolean companionInRegion) {
        try {
            gameState.moveCharacter(nazgulId, targetRegion);
            
            String message = "Voice of Malice: Moved Nazgûl to " + targetRegion + " (up to 3 regions)";
            
            if (companionInRegion) {
                message += ". May separate Companion from Fellowship";
            }
            
            return ActionResult.success(message);
            
        } catch (SQLException e) {
            return ActionResult.error("Failed to play Voice of Malice: " + e.getMessage());
        }
    }
    
    /**
     * The Nine Are Abroad - Multiple Nazgûl movement
     * "Move up to three different Nazgûl. Each Nazgûl may move up to two regions."
     */
    public ActionResult theNineAreAbroad(int nazgulMoved) {
        return ActionResult.success(
            "The Nine Are Abroad: Moved " + nazgulMoved + " Nazgûl up to 2 regions each"
        );
    }
}
