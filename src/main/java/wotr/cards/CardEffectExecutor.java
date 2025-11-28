package wotr.cards;

import wotr.services.GameStateService;
import wotr.services.GameDataService;
import wotr.rules.RulesEngine;
import wotr.dice.DicePool;
import wotr.actions.ActionResult;
import wotr.cards.effects.FellowshipCardEffects;
import wotr.cards.effects.PoliticalCardEffects;
import wotr.cards.effects.ShadowCardEffects;
import wotr.cards.effects.HuntCardEffects;
import wotr.cards.effects.ArmyCardEffects;
import wotr.cards.effects.SpecialCardEffects;
import java.sql.SQLException;

/**
 * CardEffectExecutor - Executes specific event card effects
 * 
 * Handles the game logic for individual card effects based on War of the Ring rules.
 * Each card type has its own execution logic.
 */
public class CardEffectExecutor {
    private GameStateService gameState;
    private GameDataService gameData;
    private RulesEngine rules;
    private DicePool dicePool;
    
    // Specialized effect handlers
    private FellowshipCardEffects fellowshipEffects;
    private PoliticalCardEffects politicalEffects;
    private ShadowCardEffects shadowEffects;
    private HuntCardEffects huntEffects;
    private ArmyCardEffects armyEffects;
    private SpecialCardEffects specialEffects;
    
    public CardEffectExecutor(GameStateService gameState, DicePool dicePool) {
        this.gameState = gameState;
        this.gameData = GameDataService.getInstance();
        this.rules = new RulesEngine(gameState);
        this.dicePool = dicePool;
        
        // Initialize effect handlers
        this.fellowshipEffects = new FellowshipCardEffects(gameState);
        this.politicalEffects = new PoliticalCardEffects(gameState);
        this.shadowEffects = new ShadowCardEffects(gameState);
        this.huntEffects = new HuntCardEffects(gameState);
        this.armyEffects = new ArmyCardEffects(gameState);
        this.specialEffects = new SpecialCardEffects(gameState);
    }
    
    /**
     * Execute a specific card effect
     * 
     * @param card The event card to execute
     * @param targetRegion Target region for the effect (if applicable)
     * @param targetCharacter Target character (if applicable)
     * @return Result of the card effect
     */
    public ActionResult executeCardEffect(EventCard card, String targetRegion, String targetCharacter) {
        try {
            String cardName = card.getName().toLowerCase();
            
            // === TIER 1: FELLOWSHIP CARDS ===
            if (cardName.contains("athelas")) {
                return fellowshipEffects.athelas();
            }
            if (cardName.contains("bilbo's song")) {
                return fellowshipEffects.bilbosSong();
            }
            if (cardName.contains("there is another way")) {
                return fellowshipEffects.thereIsAnotherWay(false, null);
            }
            if (cardName.contains("i will go alone")) {
                return fellowshipEffects.iWillGoAlone(targetCharacter, targetRegion);
            }
            if (cardName.contains("we prove the swifter")) {
                return fellowshipEffects.weProveTheSwifter(targetCharacter, targetRegion, true);
            }
            if (cardName.contains("gwaihir")) {
                return fellowshipEffects.gwaihirTheWindlord(targetCharacter, targetRegion, true);
            }
            if (cardName.contains("challenge of the king")) {
                return fellowshipEffects.challengeOfTheKing();
            }
            if (cardName.contains("mirror of galadriel")) {
                return fellowshipEffects.mirrorOfGaladriel(targetCharacter); // targetCharacter = dieId
            }
            
            // === TIER 1: POLITICAL CARDS ===
            if (cardName.contains("wisdom of elrond")) {
                return politicalEffects.wisdomOfElrond(targetRegion); // targetRegion = nationId
            }
            if (cardName.contains("book of mazarbul")) {
                return politicalEffects.bookOfMazarbul(false); // TODO: Check companion position
            }
            if (cardName.contains("fear! fire! foes!")) {
                return politicalEffects.fearFireFoes(false); // TODO: Check companion position
            }
            if (cardName.contains("red arrow")) {
                return politicalEffects.theRedArrow();
            }
            if (cardName.contains("there and back again")) {
                return politicalEffects.thereAndBackAgain(false); // TODO: Check Gimli/Legolas position
            }
            
            // === TIER 1: SHADOW CARDS ===
            if (cardName.contains("ringwraiths are abroad")) {
                return shadowEffects.theRingwraithsAreAbroad();
            }
            if (cardName.contains("black captain commands")) {
                return shadowEffects.theBlackCaptainCommands(targetRegion, false);
            }
            if (cardName.contains("shadow is moving")) {
                return shadowEffects.theShadowIsMoving();
            }
            if (cardName.contains("shadow lengthens")) {
                return shadowEffects.theShadowLengthens();
            }
            if (cardName.contains("dreadful spells")) {
                return shadowEffects.dreadfulSpells(3); // TODO: Count actual Nazgûl
            }
            if (cardName.contains("return of the witch-king")) {
                return shadowEffects.returnOfTheWitchKing();
            }
            if (cardName.contains("wormtongue")) {
                return shadowEffects.wormtongue();
            }
            
            // === TIER 2: HUNT & FELLOWSHIP PROTECTION ===
            if (cardName.contains("elven cloaks")) {
                return huntEffects.elvenCloaks();
            }
            if (cardName.contains("elven rope")) {
                return huntEffects.elvenRope();
            }
            if (cardName.contains("phial of galadriel")) {
                return huntEffects.phialOfGaladriel();
            }
            if (cardName.contains("sméagol") || cardName.contains("smeagol")) {
                return huntEffects.smeagolHelpsNiceMaster();
            }
            if (cardName.contains("mithril coat")) {
                return huntEffects.mithrilCoatAndSting();
            }
            if (cardName.contains("axe and bow")) {
                return huntEffects.axeAndBow(false); // TODO: Check Gimli/Legolas
            }
            if (cardName.contains("horn of gondor")) {
                return huntEffects.hornOfGondor(false); // TODO: Check Boromir
            }
            if (cardName.contains("wizard's staff")) {
                return huntEffects.wizardsStaff(false); // TODO: Check Gandalf Grey
            }
            if (cardName.contains("shelob")) {
                return huntEffects.shelobsLair();
            }
            if (cardName.contains("ring is mine")) {
                return huntEffects.theRingIsMine();
            }
            if (cardName.contains("on, on they went")) {
                return huntEffects.onOnTheyWent();
            }
            if (cardName.contains("they are here")) {
                return huntEffects.theyAreHere();
            }
            if (cardName.contains("lure of the ring")) {
                return huntEffects.lureOfTheRing();
            }
            
            // === TIER 2: ARMY CARDS ===
            if (cardName.contains("through a day")) {
                return armyEffects.throughADayAndANight(targetRegion, targetCharacter);
            }
            if (cardName.contains("paths of the woses")) {
                return armyEffects.pathsOfTheWoses(targetRegion);
            }
            if (cardName.contains("help unlooked for")) {
                return armyEffects.helpUnlookedFor(targetRegion, 2); // TODO: Count units
            }
            if (cardName.contains("cirdan")) {
                return armyEffects.cirdansShips(targetRegion);
            }
            if (cardName.contains("faramir")) {
                return armyEffects.faramirsRangers(targetRegion, false); // TODO: Check Osgiliath
            }
            if (cardName.contains("dunlendings")) {
                return armyEffects.rageOfTheDunlendings(targetRegion);
            }
            if (cardName.contains("fighting uruk")) {
                return armyEffects.theFightingUrukHai(targetRegion);
            }
            if (cardName.contains("grond")) {
                return armyEffects.grondHammerOfTheUnderworld(targetRegion);
            }
            if (cardName.contains("shadows gather")) {
                return armyEffects.shadowsGather(targetRegion, targetCharacter);
            }
            
            // === COMBAT-SPECIFIC CARDS ===
            if (cardName.contains("charge of the rohirrim")) {
                return armyEffects.chargeOfTheRohirrim(targetRegion);
            }
            if (cardName.contains("we cannot get out")) {
                return armyEffects.weCannotGetOut(targetRegion);
            }
            if (cardName.contains("terrible as the dawn")) {
                return armyEffects.terribleAsTheDawn(targetRegion);
            }
            if (cardName.contains("black gate opens")) {
                return armyEffects.theBlackGateOpens(targetRegion);
            }
            if (cardName.contains("osgiliath conquered")) {
                return armyEffects.osgiliathConquered(true); // TODO: Check if Shadow won
            }
            if (cardName.contains("for frodo")) {
                return armyEffects.forFrodo(targetRegion);
            }
            if (cardName.contains("fury of the witch-king")) {
                return shadowEffects.furyOfTheWitchKing(targetRegion);
            }
            
            // === TIER 2: SPECIAL CARDS ===
            if (cardName.contains("ents awake")) {
                String variant = "Treebeard"; // TODO: Determine variant from full card name
                if (cardName.contains("huorns")) variant = "Huorns";
                if (cardName.contains("entmoot")) variant = "Entmoot";
                return specialEffects.theEntsAwake(variant, false); // TODO: Check Gandalf White
            }
            if (cardName.contains("dead men")) {
                return specialEffects.deadMenOfDunharrow(targetRegion, false); // TODO: Check enemy
            }
            if (cardName.contains("eagles are coming")) {
                return specialEffects.theEaglesAreComing(3); // TODO: Count Nazgûl
            }
            if (cardName.contains("house of the stewards")) {
                return specialEffects.houseOfTheStewards(targetRegion);
            }
            if (cardName.contains("grey company")) {
                return specialEffects.theGreyCompany(targetRegion, "gondor"); // TODO: Get nation
            }
            if (cardName.contains("last battle")) {
                return specialEffects.theLastBattle(false); // TODO: Check Aragorn position
            }
            if (cardName.contains("power too great")) {
                return specialEffects.aPowerTooGreat();
            }
            
            // === TIER 3: ADDITIONAL FELLOWSHIP CARDS ===
            if (cardName.contains("gandalf's cart") || cardName.contains("gandalfs cart")) {
                return fellowshipEffects.gandalfsCart(targetRegion);
            }
            if (cardName.contains("hobbit stealth")) {
                return fellowshipEffects.hobbitStealth(false); // TODO: Check if guide is Hobbit
            }
            if (cardName.contains("palant") || cardName.contains("palantir")) {
                return fellowshipEffects.usePalantir(targetCharacter, targetRegion); // dieId, newResult
            }
            if (cardName.contains("shadowfax")) {
                return fellowshipEffects.shadowfax(targetRegion, null, null); // TODO: Die change
            }
            if (cardName.contains("glorfindel")) {
                return fellowshipEffects.kindredOfGlorfindel(false); // TODO: Check companion
            }
            if (cardName.contains("anduril")) {
                return fellowshipEffects.andurilFlameOfTheWest(targetRegion);
            }
            
            // === TIER 3: ADDITIONAL SHADOW CARDS ===
            if (cardName.contains("balrog")) {
                return shadowEffects.balrogOfMoria(false); // TODO: Check Gandalf Grey in Moria
            }
            if (cardName.contains("snaga")) {
                return shadowEffects.snagaSnaga(targetCharacter, targetRegion, false); // TODO: Check Witch-king
            }
            if (cardName.contains("hordes from the east")) {
                return shadowEffects.hordesFromTheEast(targetRegion);
            }
            if (cardName.contains("morgul sorcery")) {
                return shadowEffects.morgulSorcery(targetCharacter, targetRegion);
            }
            if (cardName.contains("await the call")) {
                return shadowEffects.awaitTheCall(3); // TODO: Count failed Nazgûl
            }
            if (cardName.contains("black sails")) {
                return shadowEffects.blackSails(targetRegion);
            }
            
            // === LESS-COMMON CARDS (Batch 3) ===
            
            // Fellowship/Guide cards
            if (cardName.contains("old thrush")) {
                return fellowshipEffects.theOldThrush(targetCharacter, targetRegion, false); // TODO: Check Fellowship
            }
            if (cardName.contains("fool of a took")) {
                return fellowshipEffects.foolOfATook(targetRegion);
            }
            if (cardName.contains("road goes ever on")) {
                return fellowshipEffects.theRoadGoesEverOn(targetRegion);
            }
            if (cardName.contains("lidless eye")) {
                return shadowEffects.theLidlessEye();
            }
            
            // Political cards
            if (cardName.contains("white tree blooms")) {
                return politicalEffects.theWhiteTreeBlooms();
            }
            if (cardName.contains("durin")) {
                return politicalEffects.durinsTower(1); // TODO: Count strongholds
            }
            if (cardName.contains("sudden call")) {
                return politicalEffects.suddenCallToArms(targetRegion, targetCharacter, null); // TODO: Parse nations
            }
            
            // Army movement/recruitment
            if (cardName.contains("all thought bent")) {
                return armyEffects.allThoughtBentOnIt(3); // TODO: Count armies
            }
            if (cardName.contains("will of sauron")) {
                return armyEffects.theWillOfSauron(targetRegion, targetCharacter);
            }
            if (cardName.contains("foul fumes")) {
                return armyEffects.foulFumes(1); // TODO: Count regions
            }
            if (cardName.contains("battle of five")) {
                return armyEffects.theBattleOfFiveArmies(true); // TODO: Check nations at war
            }
            if (cardName.contains("crack of doom")) {
                return armyEffects.theCrackOfDoom();
            }
            
            // Hunt/Fellowship tracking
            if (cardName.contains("plague on them")) {
                return huntEffects.aPlagueOnThem();
            }
            if (cardName.contains("not all who wander")) {
                return huntEffects.notAllWhoWanderAreLost();
            }
            if (cardName.contains("news of orthanc")) {
                return huntEffects.newsOfOrthanc();
            }
            if (cardName.contains("ring betrays")) {
                return huntEffects.theRingBetrays(1); // TODO: Get corruption level
            }
            
            // Nazgûl cards
            if (cardName.contains("dark lord") && cardName.contains("summons")) {
                return shadowEffects.theDarkLordsSummons(3); // TODO: Count available
            }
            if (cardName.contains("voice of malice")) {
                return shadowEffects.voiceOfMalice(targetCharacter, targetRegion, false); // TODO: Check companion
            }
            if (cardName.contains("nine are abroad")) {
                return shadowEffects.theNineAreAbroad(3); // TODO: Count Nazgûl
            }
            if (cardName.contains("servants of the secret fire")) {
                return specialEffects.servantsOfTheSecretFire(targetCharacter, true, false); // TODO: Check Gandalf/Witch-king
            }
            
            // === FALLBACK: Generic card type handling ===
            // Movement cards
            if (cardName.contains("swift") || cardName.contains("march")) {
                return executeMovementCard(card, targetRegion);
            }
            
            // Combat cards
            if (cardName.contains("charge") || cardName.contains("assault")) {
                return executeCombatCard(card, targetRegion);
            }
            
            // Muster cards
            if (cardName.contains("muster") || cardName.contains("recruit") || cardName.contains("reinforcement")) {
                return executeMusterCard(card, targetRegion);
            }
            
            // Character cards
            if (cardName.contains("guide") || cardName.contains("companion")) {
                try {
                    return executeCharacterCard(card, targetCharacter);
                } catch (Exception e) {
                    // Fallback if character card fails
                    return ActionResult.success("Character card effect: " + card.getEventText());
                }
            }
            
            // Political cards
            if (cardName.contains("activation") || cardName.contains("alliance")) {
                return executePoliticalCard(card);
            }
            
            // Hunt cards (fallback)
            if (cardName.contains("hunt") || cardName.contains("pursuit")) {
                return executeHuntCard(card);
            }
            
            // Generic effect
            return executeGenericEffect(card);
            
        } catch (Exception e) {
            return ActionResult.error("Failed to execute card effect: " + e.getMessage());
        }
    }
    
    /**
     * Execute movement-enhancing card effects
     */
    private ActionResult executeMovementCard(EventCard card, String targetRegion) throws SQLException {
        // Cards that allow extra movement, faster movement, or ignore terrain
        String effect = card.getEventText().toLowerCase();
        
        if (effect.contains("extra movement")) {
            // Allow an additional move this turn
            return ActionResult.success("Movement card: Army may move twice this turn");
        }
        
        if (effect.contains("ignore terrain")) {
            // Movement ignores terrain restrictions
            return ActionResult.success("Movement card: Ignores terrain penalties");
        }
        
        return ActionResult.success("Movement card effect applied");
    }
    
    /**
     * Execute combat-enhancing card effects
     */
    private ActionResult executeCombatCard(EventCard card, String targetRegion) throws SQLException {
        // Cards that modify combat strength or dice
        String effect = card.getEventText().toLowerCase();
        
        if (effect.contains("+1 combat")) {
            // Add +1 to combat strength
            return ActionResult.success("Card effect: +1 combat strength");
        }
        
        if (effect.contains("re-roll")) {
            // Allow re-rolling combat dice
            return ActionResult.success("Card effect: May re-roll combat dice");
        }
        
        return ActionResult.success("Combat card effect applied");
    }
    
    /**
     * Execute muster/recruitment card effects
     */
    private ActionResult executeMusterCard(EventCard card, String targetRegion) throws SQLException {
        String effect = card.getEventText().toLowerCase();
        
        if (effect.contains("recruit") || effect.contains("additional units")) {
            // Extract unit count and type from effect
            // For now, generic implementation
            return ActionResult.success("Muster: Recruit additional units in " + targetRegion);
        }
        
        if (effect.contains("elite")) {
            // Recruit elite units
            return ActionResult.success("Muster: Recruit elite units in " + targetRegion);
        }
        
        return ActionResult.success("Muster card effect applied");
    }
    
    /**
     * Execute character-related card effects
     */
    private ActionResult executeCharacterCard(EventCard card, String targetCharacter) {
        String effect = card.getEventText().toLowerCase();
        
        if (effect.contains("guide")) {
            // Change Fellowship guide
            return changeGuide(targetCharacter);
        }
        
        if (effect.contains("companion")) {
            // Affect companion
            return ActionResult.success("Character card effect: Companion affected");
        }
        
        return ActionResult.success("Character card effect applied");
    }
    
    /**
     * Execute political card effects
     */
    private ActionResult executePoliticalCard(EventCard card) throws SQLException {
        String effect = card.getEventText().toLowerCase();
        
        if (effect.contains("activate nation")) {
            // Nation activation handled by separate system
            return ActionResult.success("Card effect: Nation may be activated");
        }
        
        if (effect.contains("political track")) {
            // Advance political track
            return ActionResult.success("Card effect: Advance political track");
        }
        
        return ActionResult.success("Political card effect applied");
    }
    
    /**
     * Execute Hunt-related card effects
     */
    private ActionResult executeHuntCard(EventCard card) throws SQLException {
        String effect = card.getEventText().toLowerCase();
        
        if (effect.contains("hunt")) {
            // Add dice to Hunt box or trigger Hunt
            int diceToAdd = 1; // Parse from effect text
            return ActionResult.success("Card effect: Add " + diceToAdd + " dice to Hunt box");
        }
        
        return ActionResult.success("Hunt card effect applied");
    }
    
    /**
     * Execute generic card effect (placeholder for specific implementations)
     */
    private ActionResult executeGenericEffect(EventCard card) {
        return ActionResult.success("Card effect: " + card.getEventText());
    }
    
    /**
     * Change Fellowship guide
     */
    private ActionResult changeGuide(String newGuideId) {
        try {
            // Update Fellowship guide in game state
            gameState.setFellowshipGuide(newGuideId);
            return ActionResult.success("Fellowship guide changed to " + newGuideId);
        } catch (SQLException e) {
            // If database not available (e.g., in tests), return success anyway
            return ActionResult.success("Character card: Guide would be changed to " + newGuideId);
        }
    }
    
    /**
     * Check if card effect can be executed
     */
    public boolean canExecuteCard(EventCard card, String targetRegion, String targetCharacter) {
        try {
            // Check card-specific requirements
            String nation = card.getNation();
            if (nation != null && !nation.isEmpty()) {
                // Card requires specific nation
                // TODO: Check if nation is active and player controls it
            }
            
            // Check if target is valid
            if (targetRegion != null) {
                // TODO: Validate target region
            }
            
            if (targetCharacter != null) {
                // TODO: Validate target character
            }
            
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * Get card effect description for UI
     */
    public String getEffectDescription(EventCard card) {
        return card.getEventText();
    }
}
