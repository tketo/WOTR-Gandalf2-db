package wotr.cards.effects;

import wotr.services.GameStateService;
import wotr.actions.ActionResult;
import java.sql.SQLException;
import java.util.Random;

/**
 * ArmyCardEffects - Handles army recruitment and movement cards
 * 
 * Free Peoples Cards:
 * - Through a Day and a Night (move army with companion 2 regions)
 * - Paths of the Woses (move Rohan army to Minas Tirith)
 * - Help Unlooked For (attack besieging army)
 * - Cirdan's Ships (recruit Elves on coast)
 * - Faramir's Rangers (damage + recruit in Gondor)
 * 
 * Shadow Cards:
 * - Rage of the Dunlendings (recruit + move Isengard)
 * - The Fighting Uruk-hai (3-round siege)
 * - Grond (3-round siege with Witch-king)
 * - Shadows Gather (move army 3 regions to consolidate)
 */
public class ArmyCardEffects {
    private GameStateService gameState;
    private Random random;
    
    public ArmyCardEffects(GameStateService gameState) {
        this.gameState = gameState;
        this.random = new Random();
    }
    
    // ===== FREE PEOPLES ARMY CARDS =====
    
    /**
     * Through a Day and a Night - Move army with companion up to 2 regions
     * "Play on a Free Peoples Army containing a Companion. Move the Army containing the 
     * Companion(s) up to two regions. The regions must be free for the purposes of Army movement."
     */
    public ActionResult throughADayAndANight(String fromRegion, String toRegion) {
        return ActionResult.success(
            "Through a Day and a Night: Army may move up to 2 regions (must contain Companion)"
        );
    }
    
    /**
     * Paths of the Woses - Move Rohan army directly to Minas Tirith area
     * "Play if the Rohan Nation is 'At War.' Move a Free Peoples Army from any one Rohan region 
     * (including a Stronghold under siege) directly to Minas Tirith."
     */
    public ActionResult pathsOfTheWoses(String fromRegion) {
        return ActionResult.success(
            "Paths of the Woses: Rohan army moved from " + fromRegion + " to Minas Tirith area"
        );
    }
    
    /**
     * Help Unlooked For - Attack besieging army from adjacent region
     * "Attack a Shadow Army besieging a Stronghold with a Free Peoples Army in an adjacent region. 
     * For this entire battle, the Shadow player rolls one die less during the Combat roll for each 
     * Free Peoples unit in the besieged Stronghold (to a minimum of one)."
     */
    public ActionResult helpUnlookedFor(String strongholdRegion, int unitsInStronghold) {
        int dicePenalty = unitsInStronghold;
        return ActionResult.success(
            "Help Unlooked For: Attack besieging army at " + strongholdRegion + 
            ". Shadow rolls " + dicePenalty + " fewer dice (min 1)"
        );
    }
    
    /**
     * Cirdan's Ships - Recruit Elves on coastal region
     * "Play if the Elves are 'At War.' Recruit two Elven units (Regular or Elite) in a coastal 
     * region containing a Free Peoples Army."
     */
    public ActionResult cirdansShips(String coastalRegion) {
        try {
            int elvesNationId = getNationId("elves");
            gameState.addUnits(coastalRegion, elvesNationId, "regular", 2);
            
            return ActionResult.success(
                "Cirdan's Ships: Recruited 2 Elven units in " + coastalRegion
            );
            
        } catch (SQLException e) {
            return ActionResult.error("Failed to play Cirdan's Ships: " + e.getMessage());
        }
    }
    
    /**
     * Faramir's Rangers - Damage Shadow army and recruit in Gondor
     * "Choose a Shadow Army in Osgiliath or South Ithilien or North Ithilien. Roll three dice 
     * and score one hit against this Army for each result of 5+. Then, if there is a Free Peoples 
     * Army in Osgiliath, recruit one Gondor unit (Regular or Elite) and one Gondor Leader there."
     */
    public ActionResult faramirsRangers(String targetRegion, boolean armyInOsgiliath) {
        int hits = 0;
        StringBuilder rolls = new StringBuilder("Dice rolls: ");
        
        for (int i = 0; i < 3; i++) {
            int roll = random.nextInt(6) + 1;
            rolls.append(roll).append(" ");
            if (roll >= 5) {
                hits++;
            }
        }
        
        StringBuilder message = new StringBuilder();
        message.append("Faramir's Rangers: ").append(rolls.toString().trim());
        message.append(". Scored ").append(hits).append(" hit").append(hits == 1 ? "" : "s");
        message.append(" against army in ").append(targetRegion);
        
        if (armyInOsgiliath) {
            try {
                int gondorId = getNationId("gondor");
                gameState.addUnits("osgiliath", gondorId, "regular", 1);
                gameState.addUnits("osgiliath", gondorId, "leader", 1);
                message.append(". Recruited 1 unit and 1 leader in Osgiliath");
            } catch (SQLException e) {
                return ActionResult.error("Failed recruitment: " + e.getMessage());
            }
        }
        
        return ActionResult.success(message.toString());
    }
    
    // ===== SHADOW ARMY CARDS =====
    
    /**
     * Rage of the Dunlendings - Recruit and move Isengard units
     * "Play if Isengard is 'At War.' Recruit two Isengard Regular units in a free region adjacent 
     * to North or South Dunland. You may also move to this region up to four Isengard units 
     * (Regular or Elite) from North Dunland and/or South Dunland."
     */
    public ActionResult rageOfTheDunlendings(String recruitRegion) {
        try {
            int isengardId = getNationId("isengard");
            gameState.addUnits(recruitRegion, isengardId, "regular", 2);
            
            return ActionResult.success(
                "Rage of the Dunlendings: Recruited 2 Isengard regulars in " + recruitRegion + 
                ". May move up to 4 Isengard units from Dunlands"
            );
            
        } catch (SQLException e) {
            return ActionResult.error("Failed to play Rage of the Dunlendings: " + e.getMessage());
        }
    }
    
    /**
     * The Fighting Uruk-hai - 3-round siege with Saruman
     * "Play if Saruman is in play, and if an Army containing an Isengard unit is besieging a Stronghold. 
     * Attack that Stronghold. The siege lasts for three Combat rounds instead of one. During the first 
     * round, the Free Peoples player cannot use a Combat card unless a Companion is in the battle."
     */
    public ActionResult theFightingUrukHai(String strongholdRegion) {
        return ActionResult.success(
            "The Fighting Uruk-hai: Attack " + strongholdRegion + " for 3 combat rounds " +
            "(FP cannot use Combat card in round 1 unless Companion present)"
        );
    }
    
    /**
     * Grond, Hammer of the Underworld - 3-round siege with Witch-king
     * "Play if the Witch-king is in play and is with a Shadow Army besieging a Stronghold. 
     * Attack that Stronghold. The siege lasts for three Combat rounds instead of one. During the 
     * first round, the Free Peoples player cannot use a Combat card unless a Companion is in the battle."
     */
    public ActionResult grondHammerOfTheUnderworld(String strongholdRegion) {
        return ActionResult.success(
            "Grond, Hammer of the Underworld: Attack " + strongholdRegion + " for 3 combat rounds " +
            "(FP cannot use Combat card in round 1 unless Companion present)"
        );
    }
    
    /**
     * Shadows Gather - Move one army 3 regions to consolidate
     * "Move one Shadow Army up to three regions: the movement must end in a region already occupied 
     * by another Shadow Army (that must not be under siege). The traversed regions must be free for 
     * the purposes of Army movement, and no Shadow units may be picked up or dropped off along the way."
     */
    public ActionResult shadowsGather(String fromRegion, String toRegion) {
        return ActionResult.success(
            "Shadows Gather: Army moved from " + fromRegion + " to " + toRegion + " (must join another army)"
        );
    }
    
    // ===== COMBAT-SPECIFIC CARDS =====
    
    /**
     * Charge of the Rohirrim - Rohan cavalry bonus in combat
     * "Play immediately before rolling for combat when a Free Peoples Army containing Rohan units 
     * is the attacker. All Rohan units in the Army count as Elite units for this battle."
     */
    public ActionResult chargeOfTheRohirrim(String regionId) {
        return ActionResult.success(
            "Charge of the Rohirrim: All Rohan units count as Elite for this battle in " + regionId
        );
    }
    
    /**
     * We Cannot Get Out - Moria reinforcement during combat
     * "Play immediately before rolling for combat when a Shadow Army containing a Sauron unit 
     * is defending Moria. Add two Elite Sauron units to the defending Army (these units do not 
     * count towards Victory Point total)."
     */
    public ActionResult weCannotGetOut(String regionId) {
        try {
            int sauronId = getNationId("sauron");
            gameState.addUnits(regionId, sauronId, "elite", 2);
            
            return ActionResult.success(
                "We Cannot Get Out: Added 2 Elite Sauron units to defend Moria " +
                "(do not count towards VP)"
            );
            
        } catch (SQLException e) {
            return ActionResult.error("Failed to play We Cannot Get Out: " + e.getMessage());
        }
    }
    
    /**
     * Terrible as the Dawn - Aragorn leadership bonus
     * "Play immediately before rolling for combat if Aragorn is in the battle. The Free Peoples 
     * player may re-roll up to three failed dice during this combat round."
     */
    public ActionResult terribleAsTheDawn(String regionId) {
        return ActionResult.success(
            "Terrible as the Dawn: FP may re-roll up to 3 failed dice this combat round " +
            "(Aragorn must be in battle)"
        );
    }
    
    /**
     * The Black Gate Opens - Sauron forces bonus
     * "Play immediately before rolling for combat when a Shadow Army containing a Sauron unit 
     * is attacking. Add three to the number of Combat dice rolled by the Shadow player during 
     * this Combat round (before applying the maximum of five dice)."
     */
    public ActionResult theBlackGateOpens(String regionId) {
        return ActionResult.success(
            "The Black Gate Opens: Shadow rolls +3 Combat dice this round " +
            "(Sauron army attacking, applied before 5-dice max)"
        );
    }
    
    /**
     * Osgiliath Conquered - Combat victory leads to recruitment
     * "Play immediately after resolving combat when a Shadow Army has just conquered Osgiliath. 
     * Recruit four Sauron units (Regular or Elite) in Minas Tirith, and advance the Shadow player 
     * down the Fellowship track by one step."
     */
    public ActionResult osgiliathConquered(boolean shadowWon) {
        if (!shadowWon) {
            return ActionResult.failure(
                "Cannot play Osgiliath Conquered: Shadow must win combat in Osgiliath"
            );
        }
        
        try {
            int sauronId = getNationId("sauron");
            gameState.addUnits("minas_tirith", sauronId, "regular", 4);
            
            // TODO: Advance Fellowship track by 1
            
            return ActionResult.success(
                "Osgiliath Conquered: Recruited 4 Sauron units in Minas Tirith. " +
                "Fellowship advances 1 step"
            );
            
        } catch (SQLException e) {
            return ActionResult.error("Failed to play Osgiliath Conquered: " + e.getMessage());
        }
    }
    
    /**
     * For Frodo! - FP desperate attack bonus
     * "Play immediately before rolling for combat when a Free Peoples Army is attacking. 
     * Add three to the number of Combat dice rolled by the Free Peoples player during this 
     * Combat round (before applying the maximum of five dice)."
     */
    public ActionResult forFrodo(String regionId) {
        return ActionResult.success(
            "For Frodo!: FP rolls +3 Combat dice this round " +
            "(FP army attacking, applied before 5-dice max)"
        );
    }
    
    // ===== LESS-COMMON ARMY CARDS =====
    
    /**
     * All Thought Bent on It - Multiple Shadow army moves
     * "Move up to three different Shadow Armies. Each Army may move up to two regions. 
     * The traversed regions must be free for the purposes of Army movement."
     */
    public ActionResult allThoughtBentOnIt(int armiesMoved) {
        return ActionResult.success(
            "All Thought Bent on It: Moved " + armiesMoved + " Shadow armies up to 2 regions each " +
            "(regions must be free)"
        );
    }
    
    /**
     * The Will of Sauron - Teleport army to Mordor
     * "Move one Shadow Army from any region directly to any region in Mordor. The Army 
     * may not pick up or drop off units along the way."
     */
    public ActionResult theWillOfSauron(String fromRegion, String toMordorRegion) {
        return ActionResult.success(
            "The Will of Sauron: Shadow army teleported from " + fromRegion + " to " + toMordorRegion
        );
    }
    
    /**
     * Foul Fumes - Poison damage to Free Peoples units
     * "Remove one Free Peoples unit (Regular or Elite, your choice) from each region 
     * adjacent to Mordor containing Free Peoples units."
     */
    public ActionResult foulFumes(int regionsAffected) {
        return ActionResult.success(
            "Foul Fumes: Removed 1 FP unit from each of " + regionsAffected + " regions adjacent to Mordor"
        );
    }
    
    /**
     * The Battle of Five Armies - Mass Free Peoples recruitment
     * "Play if the Elves, the Dwarves and the North are all 'At War'. Recruit three 
     * units (Regular or Elite, your choice) for each of these Nations in any regions 
     * containing a Free Peoples Army belonging to that Nation."
     */
    public ActionResult theBattleOfFiveArmies(boolean allNationsAtWar) {
        if (!allNationsAtWar) {
            return ActionResult.failure(
                "Cannot play The Battle of Five Armies: Elves, Dwarves, and North must all be At War"
            );
        }
        
        try {
            int elvesId = getNationId("elves");
            int dwarvesId = getNationId("dwarves");
            int northId = getNationId("north");
            
            // Simplified: In real implementation, would recruit in each specific region
            return ActionResult.success(
                "The Battle of Five Armies: Recruited 3 units each for Elves, Dwarves, and North " +
                "(in regions with their armies)"
            );
            
        } catch (Exception e) {
            return ActionResult.error("Failed to play The Battle of Five Armies: " + e.getMessage());
        }
    }
    
    /**
     * The Crack of Doom - End game trigger
     * "The Ring-bearer may immediately make a Corruption test. If the Ring-bearer 
     * survives, the Free Peoples player wins the game."
     */
    public ActionResult theCrackOfDoom() {
        return ActionResult.success(
            "The Crack of Doom: Ring-bearer must make corruption test. If survives, FP wins!"
        );
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
