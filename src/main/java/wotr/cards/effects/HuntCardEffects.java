package wotr.cards.effects;

import wotr.services.GameStateService;
import wotr.fellowship.FellowshipManager;
import wotr.cards.OnTableCardManager;
import wotr.actions.ActionResult;
import java.sql.SQLException;

/**
 * HuntCardEffects - Handles Hunt-related card effects
 * 
 * Fellowship Protection Cards (play on table when conditions met):
 * - Elven Cloaks, Elven Rope, Phial of Galadriel (special Hunt tiles)
 * - Mithril Coat and Sting (re-draw Hunt tile)
 * - Axe and Bow (reduce Hunt damage, requires Gimli or Legolas)
 * - Horn of Gondor (reduce Hunt damage, requires Boromir)
 * - Wizard's Staff (prevent Hunt tile draw, requires Gandalf Grey)
 * 
 * Shadow Hunt Cards:
 * - Shelob's Lair, The Ring is Mine!, On, On They Went (special Hunt tiles)
 * - "They Are Here!" (reveal Fellowship)
 * - Lure of the Ring (add corruption + Hunt)
 */
public class HuntCardEffects {
    private GameStateService gameState;
    private FellowshipManager fellowship;
    private OnTableCardManager onTableManager;
    
    public HuntCardEffects(GameStateService gameState) {
        this.gameState = gameState;
        this.fellowship = new FellowshipManager(gameState);
        this.onTableManager = new OnTableCardManager(gameState);
    }
    
    /**
     * Elven Cloaks - Add special Hunt tile [0] to Hunt Pool
     * "The 'Elven Cloaks' special Hunt tile [0] is now in play. Add the tile to the 
     * Hunt Pool when the Fellowship is on the Mordor Track."
     */
    public ActionResult elvenCloaks() {
        // TODO: Add special Hunt tile to pool when on Mordor track
        return ActionResult.success(
            "Elven Cloaks: Play on table. Special Hunt tile [0] will be added when Fellowship reaches Mordor Track"
        );
    }
    
    /**
     * Elven Rope - Add special Hunt tile [0] to Hunt Pool
     * "The 'Elven Rope' special Hunt tile [0] is now in play."
     */
    public ActionResult elvenRope() {
        return ActionResult.success(
            "Elven Rope: Play on table. Special Hunt tile [0] will be added when Fellowship reaches Mordor Track"
        );
    }
    
    /**
     * Phial of Galadriel - Add special Hunt tile [-2] to Hunt Pool
     * "The 'Phial of Galadriel' special Hunt tile [-2] is now in play."
     */
    public ActionResult phialOfGaladriel() {
        return ActionResult.success(
            "Phial of Galadriel: Play on table. Special Hunt tile [-2] will be added when Fellowship reaches Mordor Track"
        );
    }
    
    /**
     * Sméagol Helps Nice Master - Add special Hunt tile [-1] to Hunt Pool
     * "The 'Sméagol Helps Nice Master' special Hunt tile [-1] is now in play."
     */
    public ActionResult smeagolHelpsNiceMaster() {
        return ActionResult.success(
            "Sméagol Helps Nice Master: Play on table. Special Hunt tile [-1] will be added when Fellowship reaches Mordor Track"
        );
    }
    
    /**
     * Mithril Coat and Sting - Re-draw Hunt tile
     * "After the Shadow player draws a Hunt tile, you may discard 'Mithril Coat and Sting' 
     * to draw a second tile. Apply the effects of the second tile instead of the first one, 
     * then return the first tile to the Hunt Pool."
     */
    public ActionResult mithrilCoatAndSting() {
        return ActionResult.success(
            "Mithril Coat and Sting: Play on table. May discard to re-draw Hunt tile after Shadow draws"
        );
    }
    
    /**
     * Axe and Bow - Reduce Hunt damage
     * "After the Shadow player draws a Hunt tile, you may discard 'Axe and Bow' to reduce 
     * the Hunt damage by one (to a minimum of zero). Must discard if both Gimli and Legolas 
     * leave the Fellowship."
     */
    public ActionResult axeAndBow(boolean gimliOrLegolasInFellowship) {
        if (!gimliOrLegolasInFellowship) {
            return ActionResult.failure("Cannot play Axe and Bow: Requires Gimli or Legolas in Fellowship");
        }
        
        return ActionResult.success(
            "Axe and Bow: Play on table. May discard to reduce Hunt damage by 1 (requires Gimli or Legolas)"
        );
    }
    
    /**
     * Horn of Gondor - Reduce Hunt damage
     * "After the Shadow player draws a Hunt tile, you may discard 'Horn of Gondor' to reduce 
     * the Hunt damage by one (to a minimum of zero). Must discard if Boromir leaves the Fellowship."
     */
    public ActionResult hornOfGondor(boolean borromirInFellowship) {
        if (!borromirInFellowship) {
            return ActionResult.failure("Cannot play Horn of Gondor: Requires Boromir in Fellowship");
        }
        
        return ActionResult.success(
            "Horn of Gondor: Play on table. May discard to reduce Hunt damage by 1 (requires Boromir)"
        );
    }
    
    /**
     * Wizard's Staff - Prevent Hunt tile draw
     * "You may discard 'Wizard's Staff' to prevent the Shadow player from drawing a Hunt tile. 
     * Must discard if Gandalf the Grey leaves the Fellowship."
     */
    public ActionResult wizardsStaff(boolean gandalfGreyInFellowship) {
        if (!gandalfGreyInFellowship) {
            return ActionResult.failure("Cannot play Wizard's Staff: Requires Gandalf the Grey in Fellowship");
        }
        
        return ActionResult.success(
            "Wizard's Staff: Play on table. May discard to prevent Hunt tile draw (requires Gandalf the Grey)"
        );
    }
    
    // ===== SHADOW HUNT CARDS =====
    
    /**
     * Shelob's Lair - Add special Hunt tile [die, stop] to Hunt Pool
     * "The 'Shelob's Lair' special Hunt tile [die icon, stop] is now in play."
     */
    public ActionResult shelobsLair() {
        return ActionResult.success(
            "Shelob's Lair: Play on table. Special Hunt tile [die, stop] will be added when Fellowship reaches Mordor Track"
        );
    }
    
    /**
     * The Ring is Mine! - Add special Hunt tile [eye, reveal, stop]
     * "The 'The Ring is Mine!' special Hunt tile [eye, reveal, stop] is now in play."
     */
    public ActionResult theRingIsMine() {
        return ActionResult.success(
            "The Ring is Mine!: Play on table. Special Hunt tile [eye, reveal, stop] will be added when Fellowship reaches Mordor Track"
        );
    }
    
    /**
     * On, On They Went - Add special Hunt tile [3, stop]
     * "The 'On, On They Went' special Hunt tile [3, stop] is now in play."
     */
    public ActionResult onOnTheyWent() {
        return ActionResult.success(
            "On, On They Went: Play on table. Special Hunt tile [3, stop] will be added when Fellowship reaches Mordor Track"
        );
    }
    
    /**
     * "They Are Here!" - Reveal the Fellowship
     * "The Fellowship is revealed."
     */
    public ActionResult theyAreHere() {
        try {
            gameState.setFellowshipRevealed(true);
            return ActionResult.success("\"They Are Here!\": Fellowship revealed!");
            
        } catch (SQLException e) {
            return ActionResult.error("Failed to play \"They Are Here!\": " + e.getMessage());
        }
    }
    
    /**
     * Lure of the Ring - Add corruption and place die in Hunt Box
     * "Add one die to the Hunt box. Then, add 1 Corruption to the Fellowship."
     */
    public ActionResult lureOfTheRing() {
        try {
            // Add corruption
            gameState.adjustCorruption(1);
            
            // TODO: Add die to Hunt box
            return ActionResult.success(
                "Lure of the Ring: Added 1 corruption. Place 1 die in Hunt Box"
            );
            
        } catch (SQLException e) {
            return ActionResult.error("Failed to play Lure of the Ring: " + e.getMessage());
        }
    }
    
    // ===== ON-TABLE CARDS =====
    
    /**
     * The Torment of Gollum - On-table card enhancing Hunt re-rolls
     * "Play on the table. As long as this card is on the table, the Shadow player may 
     * re-roll one die when re-rolling the Hunt."
     */
    public ActionResult theTormentOfGollum() {
        ActionResult placeResult = onTableManager.placeCardOnTable(
            "The Torment of Gollum",
            "shadow"
        );
        
        if (placeResult.isSuccess()) {
            return ActionResult.success(
                "The Torment of Gollum: Placed on table. Shadow may re-roll one die when re-rolling the Hunt."
            );
        }
        
        return placeResult;
    }
    
    // ===== LESS-COMMON HUNT CARDS =====
    
    /**
     * A Plague on Them! - Shadow Hunt bonus
     * "Add two action dice to the Hunt box. These dice do not need to be used immediately."
     */
    public ActionResult aPlagueOnThem() {
        return ActionResult.success(
            "A Plague on Them!: Added 2 action dice to Hunt box (use at any time)"
        );
    }
    
    /**
     * Not All Who Wander Are Lost - Fellowship Hunt protection
     * "The Fellowship is hidden. Then the Free Peoples player may draw one Event card."
     */
    public ActionResult notAllWhoWanderAreLost() {
        try {
            gameState.setFellowshipRevealed(false);
            
            return ActionResult.success(
                "Not All Who Wander Are Lost: Fellowship hidden. FP may draw 1 Event card"
            );
            
        } catch (SQLException e) {
            return ActionResult.error("Failed to play Not All Who Wander Are Lost: " + e.getMessage());
        }
    }
    
    /**
     * News of Orthanc - Reveal Fellowship location
     * "The Fellowship is revealed. The Shadow player draws one Event card."
     */
    public ActionResult newsOfOrthanc() {
        try {
            gameState.setFellowshipRevealed(true);
            
            return ActionResult.success(
                "News of Orthanc: Fellowship revealed. Shadow draws 1 Event card"
            );
            
        } catch (SQLException e) {
            return ActionResult.error("Failed to play News of Orthanc: " + e.getMessage());
        }
    }
    
    /**
     * The Ring Betrays - Corruption-based Hunt damage
     * "Immediately roll one Hunt die for each point of Corruption on the Fellowship 
     * (regardless of whether the Fellowship is revealed or hidden)."
     */
    public ActionResult theRingBetrays(int corruptionLevel) {
        return ActionResult.success(
            "The Ring Betrays: Roll " + corruptionLevel + " Hunt dice " +
            "(1 die per corruption point, regardless of reveal status)"
        );
    }
}
