package wotr.fellowship;

import wotr.services.GameStateService;
import wotr.services.GameDataService;
import wotr.actions.ActionResult;
import java.sql.SQLException;
import java.util.List;

/**
 * FellowshipManager - Manages Fellowship composition and actions
 * 
 * Board-based queries:
 * - Characters in "Fellowship Box" area are in the Fellowship
 * - Characters on the map are separated
 * - Characters in "FPCasualties" (area 175) or "SACasualties" (area 177) are eliminated
 * - Characters in "Spare" area are out of play (e.g., Gandalf the Grey → White)
 * 
 * All state inferred from game board piece positions.
 */
public class FellowshipManager {
    private GameStateService gameState;
    private GameDataService gameData;
    
    // Special area IDs (from Game.areas array indices)
    private static final int FELLOWSHIP_BOX_AREA = 115;  // Fellowship box (Game.areas[115])
    private static final int FP_CASUALTIES_AREA = 175;   // Free Peoples casualties (Game.areas[175])
    private static final int SA_CASUALTIES_AREA = 177;   // Shadow casualties (Game.areas[177])
    private static final int SPARE_AREA = 185;           // Spare pieces (Game.areas[185])
    
    public FellowshipManager(GameStateService gameState) {
        this.gameState = gameState;
        this.gameData = GameDataService.getInstance();
    }
    
    /**
     * Get current Fellowship guide
     * Queries game_pieces properties for guide character
     */
    public String getFellowshipGuide() throws SQLException {
        return gameState.getFellowshipGuide();
    }
    
    /**
     * Change Fellowship guide
     * Can only be done during Fellowship phase or with specific cards
     */
    public ActionResult changeGuide(String newGuideId) {
        try {
            // Ring bearers cannot be guides
            if (isRingBearer(newGuideId)) {
                return ActionResult.failure("Ring bearers cannot be guides");
            }
            
            // Validate new guide is in Fellowship
            if (!isInFellowship(newGuideId)) {
                return ActionResult.failure(newGuideId + " is not in the Fellowship");
            }
            
            String currentGuide = getFellowshipGuide();
            if (currentGuide.equals(newGuideId)) {
                return ActionResult.failure(newGuideId + " is already the guide");
            }
            
            // Update guide in game state
            gameState.setFellowshipGuide(newGuideId);
            
            return ActionResult.success(
                "Fellowship guide changed from " + currentGuide + " to " + newGuideId
            );
            
        } catch (SQLException e) {
            return ActionResult.error("Failed to change guide: " + e.getMessage());
        }
    }
    
    /**
     * Separate companion(s) from Fellowship
     * 
     * Rules:
     * - Timing: Action Resolution, using Character die (not on Mordor Track)
     * - Initial placement: Companions move to Ring-bearers' region
     * - Can then move up to Progress counter + highest Level
     * - Groups move together to one region per highest Level
     * - Cannot separate in Mordor (eliminated instead)
     * - Cannot separate in besieged FP Stronghold (or cannot leave if separated there)
     * - Separation is permanent - cannot rejoin
     * - If Guide is separated, highest-Level remaining Companion becomes Guide
     */
    public ActionResult separateCompanion(String companionId) {
        try {
            // Validate companion is in Fellowship
            if (!isInFellowship(companionId)) {
                return ActionResult.failure(companionId + " is not in the Fellowship");
            }
            
            // Can't separate ring bearers
            if (isRingBearer(companionId)) {
                return ActionResult.failure("Cannot separate ring bearers from Fellowship");
            }
            
            // Get Fellowship location
            String fellowshipLocation = gameState.getFellowshipLocation();
            
            // Check if in Mordor - companions eliminated if separated there
            if (isMordorRegion(fellowshipLocation)) {
                gameState.moveCharacter(companionId, "fp_casualties");
                return ActionResult.success(
                    companionId + " separated in Mordor and eliminated"
                );
            }
            
            // Check if in besieged Free Peoples Stronghold
            // TODO: Implement besieged stronghold check
            // If besieged, companion goes to stronghold and cannot leave
            
            // Move companion to Ring-bearers' region (Fellowship location)
            gameState.moveCharacter(companionId, fellowshipLocation);
            
            // Check if guide was separated - need to select new guide
            String currentGuide = gameState.getFellowshipGuide();
            if (companionId.equals(currentGuide)) {
                String newGuide = selectNewGuide();
                if (newGuide != null) {
                    gameState.setFellowshipGuide(newGuide);
                    return ActionResult.success(
                        companionId + " separated to " + fellowshipLocation + 
                        ". New guide: " + newGuide + 
                        ". Can move up to " + getCompanionMaxMovement() + " regions"
                    );
                }
            }
            
            return ActionResult.success(
                companionId + " separated to " + fellowshipLocation + 
                ". Can move up to " + getCompanionMaxMovement() + " regions"
            );
            
        } catch (SQLException e) {
            return ActionResult.error("Failed to separate companion: " + e.getMessage());
        }
    }
    
    /**
     * Get maximum movement for separated companion
     * = Progress counter + highest Level (Leadership)
     */
    private int getCompanionMaxMovement() throws SQLException {
        int trackPosition = getFellowshipTrackPosition();
        int highestLevel = getHighestLeadership();
        return trackPosition + highestLevel;
    }
    
    /**
     * Select new guide when current guide is separated
     * Returns highest-Level remaining companion
     * - If multiple with same highest level: Player chooses (TODO: implement choice)
     * - Auto-select Gollum ONLY if no other companions remain
     */
    private String selectNewGuide() throws SQLException {
        List<String> companions = getFellowshipCompanions();
        
        // Find all non-Ring-Bearer companions
        List<String> eligibleCompanions = new java.util.ArrayList<>();
        int highestLeadership = 0;
        
        for (String companionId : companions) {
            if (!isRingBearer(companionId) && !companionId.equalsIgnoreCase("gollum")) {
                int leadership = getCompanionLeadership(companionId);
                eligibleCompanions.add(companionId);
                if (leadership > highestLeadership) {
                    highestLeadership = leadership;
                }
            }
        }
        
        // If no other companions remain, auto-select Gollum
        if (eligibleCompanions.isEmpty() && companions.contains("gollum")) {
            return "gollum";
        }
        
        // Find all companions with highest leadership
        List<String> candidates = new java.util.ArrayList<>();
        for (String companionId : eligibleCompanions) {
            if (getCompanionLeadership(companionId) == highestLeadership) {
                candidates.add(companionId);
            }
        }
        
        if (candidates.isEmpty()) {
            return null;
        }
        
        // If only one candidate, auto-select
        if (candidates.size() == 1) {
            return candidates.get(0);
        }
        
        // Multiple candidates with equal leadership - player must choose
        // TODO: Implement player choice dialog/mechanism
        // For now, return first candidate
        return candidates.get(0);
    }
    
    /**
     * Separate companion to specific destination (for card effects)
     * This bypasses normal separation placement rules
     */
    public ActionResult separateCompanion(String companionId, String destinationRegion) {
        try {
            // Validate companion is in Fellowship
            if (!isInFellowship(companionId)) {
                return ActionResult.failure(companionId + " is not in the Fellowship");
            }
            
            // Can't separate ring bearers
            if (isRingBearer(companionId)) {
                return ActionResult.failure("Cannot separate ring bearers from Fellowship");
            }
            
            // Move companion directly to destination (card effect)
            gameState.moveCharacter(companionId, destinationRegion);
            
            // Check if guide was separated - need to select new guide
            String currentGuide = gameState.getFellowshipGuide();
            if (companionId.equals(currentGuide)) {
                String newGuide = selectNewGuide();
                if (newGuide != null) {
                    gameState.setFellowshipGuide(newGuide);
                    return ActionResult.success(
                        companionId + " separated to " + destinationRegion + 
                        ". New guide: " + newGuide
                    );
                }
            }
            
            return ActionResult.success(
                companionId + " separated to " + destinationRegion
            );
            
        } catch (SQLException e) {
            return ActionResult.error("Failed to separate companion: " + e.getMessage());
        }
    }
    
    /**
     * Check if region is in Mordor
     */
    private boolean isMordorRegion(String regionId) {
        // Mordor regions (simplified check)
        String id = regionId.toLowerCase();
        return id.contains("mordor") || 
               id.equals("minas_morgul") || 
               id.equals("morannon") ||
               id.equals("mount_doom") ||
               id.equals("gorgoroth") ||
               id.equals("nurn");
    }
    
    /**
     * Get list of companions in Fellowship
     * Queries game_pieces for all character pieces in Fellowship box area
     */
    public List<String> getFellowshipCompanions() throws SQLException {
        return gameState.getCharactersInArea(FELLOWSHIP_BOX_AREA);
    }
    
    /**
     * Get Fellowship size (number of companions)
     */
    public int getFellowshipSize() throws SQLException {
        return getFellowshipCompanions().size();
    }
    
    /**
     * Check if character is in Fellowship
     * Returns true if character piece is in Fellowship box area
     */
    public boolean isInFellowship(String characterId) throws SQLException {
        Integer areaId = gameState.getCharacterAreaId(characterId);
        return areaId != null && areaId == FELLOWSHIP_BOX_AREA;
    }
    
    /**
     * Check if character is the Ring Bearers unit
     * In War of the Ring, Frodo+Sam move together as "UnitFellowship"
     */
    public boolean isRingBearer(String characterId) {
        return "unitfellowship".equals(characterId);
    }
    
    /**
     * Add companion to Fellowship
     * Moves character piece from map to Fellowship box area
     */
    public ActionResult addToFellowship(String companionId) {
        try {
            if (isInFellowship(companionId)) {
                return ActionResult.failure(companionId + " is already in Fellowship");
            }
            
            // Move character piece to Fellowship box area
            gameState.moveCharacterToArea(companionId, FELLOWSHIP_BOX_AREA);
            
            return ActionResult.success(companionId + " joined the Fellowship");
            
        } catch (SQLException e) {
            return ActionResult.error("Failed to add to Fellowship: " + e.getMessage());
        }
    }
    
    /**
     * Eliminate character (move to FPCasualties or SACasualties)
     * 
     * @param characterId The character to eliminate
     * @param faction "free_peoples" or "shadow"
     */
    public ActionResult eliminateCharacter(String characterId, String faction) {
        try {
            // Determine which casualty area based on faction
            int casualtyArea = "free_peoples".equals(faction) ? FP_CASUALTIES_AREA : SA_CASUALTIES_AREA;
            String casualtyBox = "free_peoples".equals(faction) ? "FPCasualties" : "SACasualties";
            
            gameState.moveCharacterToArea(characterId, casualtyArea);
            
            return ActionResult.success(characterId + " eliminated to " + casualtyBox);
            
        } catch (SQLException e) {
            return ActionResult.error("Failed to eliminate character: " + e.getMessage());
        }
    }
    
    /**
     * Move character to spare (e.g., Gandalf the Grey → White)
     */
    public ActionResult moveToSpare(String characterId) {
        try {
            gameState.moveCharacterToArea(characterId, SPARE_AREA);
            
            return ActionResult.success(characterId + " moved to spare pieces");
            
        } catch (SQLException e) {
            return ActionResult.error("Failed to move to spare: " + e.getMessage());
        }
    }
    
    /**
     * Get Fellowship status summary
     * Shows companions in Fellowship box and separated companions on the board
     */
    public String getFellowshipSummary() {
        try {
            StringBuilder sb = new StringBuilder();
            sb.append("=== Fellowship Status ===\n");
            
            String location = gameState.getFellowshipLocation();
            int corruption = gameState.getFellowshipCorruption();
            boolean revealed = gameState.isFellowshipRevealed();
            String guide = getFellowshipGuide();
            List<String> companions = getFellowshipCompanions();
            
            sb.append("Location: ").append(location).append("\n");
            sb.append("Guide: ").append(guide).append("\n");
            sb.append("Size: ").append(companions.size()).append(" companions in Fellowship box\n");
            sb.append("Corruption: ").append(corruption).append("/12\n");
            sb.append("Revealed: ").append(revealed ? "Yes" : "No").append("\n");
            
            sb.append("\nIn Fellowship Box:\n");
            for (String companion : companions) {
                sb.append("  - ").append(companion);
                if (companion.equals(guide)) {
                    sb.append(" (Guide)");
                }
                if (isRingBearer(companion)) {
                    sb.append(" (Ring Bearer)");
                }
                sb.append("\n");
            }
            
            // Query for separated companions (characters not in Fellowship box, casualties, or spare)
            sb.append("\nSeparated Companions:\n");
            // Note: This would require querying all character pieces and filtering by area
            sb.append("  (Characters on the map - to be implemented)\n");
            
            return sb.toString();
            
        } catch (SQLException e) {
            return "Error getting Fellowship status: " + e.getMessage();
        }
    }
    
    /**
     * Can Fellowship declare position? (for Fellowship Phase)
     */
    public boolean canDeclare() throws SQLException {
        // Fellowship can only declare if not revealed
        return !gameState.isFellowshipRevealed();
    }
    
    /**
     * Declare Fellowship position (makes it revealed)
     */
    public ActionResult declare() {
        // TODO: Implement Fellowship declaration
        return ActionResult.success("Fellowship declared");
    }
    
    /**
     * Get companions currently in Fellowship (alias for getFellowshipCompanions)
     */
    public List<String> getCompanionsInFellowship() throws SQLException {
        return getFellowshipCompanions();
    }
    
    /**
     * Get separated companions (those on the map, not in Fellowship box)
     */
    public List<String> getSeparatedCompanions() throws SQLException {
        // Query all companion characters not in Fellowship box, casualties, or spare
        List<String> allCompanions = gameData.getAllCompanions();
        List<String> inFellowship = getFellowshipCompanions();
        List<String> separated = new java.util.ArrayList<>();
        
        for (String companion : allCompanions) {
            if (!inFellowship.contains(companion)) {
                // Check if companion is on the map (not in casualties or spare)
                String location = gameState.getCharacterLocation(companion);
                if (location != null && 
                    !location.equals("fp_casualties") && 
                    !location.equals("shadow_casualties") &&
                    !location.equals("spare")) {
                    separated.add(companion);
                }
            }
        }
        
        return separated;
    }
    
    // ===== MOVEMENT RULES =====
    
    /**
     * Get maximum movement distance for Fellowship
     * = Highest leadership in Fellowship + Fellowship track position
     */
    public int getMaxMovementDistance() throws SQLException {
        int highestLeadership = getHighestLeadership();
        int trackPosition = getFellowshipTrackPosition();
        return highestLeadership + trackPosition;
    }
    
    /**
     * Get highest leadership value among companions in Fellowship
     */
    public int getHighestLeadership() throws SQLException {
        List<String> companions = getFellowshipCompanions();
        int highest = 0;
        
        for (String companionId : companions) {
            int leadership = getCompanionLeadership(companionId);
            if (leadership > highest) {
                highest = leadership;
            }
        }
        
        return highest;
    }
    
    /**
     * Get leadership value for a specific companion
     * 
     * Note on character versions:
     * - Strider (in Fellowship) = Leadership 3, transforms to Aragorn (on map only)
     * - Gandalf Grey (in Fellowship) = Leadership 3, transforms to Gandalf White (on map only)
     */
    private int getCompanionLeadership(String companionId) {
        String id = companionId.toLowerCase();
        
        // Standard leadership values from the game
        switch (id) {
            // Gandalf versions (Grey can be in Fellowship, White cannot)
            case "gandalf_grey":
            case "gandalf":
            case "gandalf_white":  // Just in case, but should never be in Fellowship
                return 3;
            
            // Aragorn versions (Strider can be in Fellowship, Aragorn cannot)
            case "strider":
            case "aragorn":  // Just in case, but should never be in Fellowship
                return 3;
            
            // Level 2 companions
            case "legolas":
            case "gimli":
            case "boromir":
                return 2;
            
            // Level 1 companions
            case "merry":
            case "pippin":
                return 1;
            
            // Ring bearers
            case "frodo":
            case "sam":
                return 0; // Ring bearers don't provide leadership
            
            // Gollum (special)
            case "gollum":
                return 0; // Can be guide but no leadership value
            
            default:
                return 1; // Default for other companions
        }
    }
    
    /**
     * Get current Fellowship track position
     * This represents how far along the track the Fellowship has progressed
     */
    private int getFellowshipTrackPosition() throws SQLException {
        // TODO: Query Fellowship track position from game state
        // For now, return 0 as default
        return 0;
    }
    
    /**
     * Validate Fellowship movement from current location to target
     * Checks adjacency and distance rules
     */
    public ActionResult validateMovement(String targetRegion) {
        try {
            String currentLocation = gameState.getFellowshipLocation();
            
            // Check if regions are adjacent
            if (!gameData.areRegionsAdjacent(currentLocation, targetRegion)) {
                return ActionResult.failure(
                    "Cannot move Fellowship: " + targetRegion + " is not adjacent to " + currentLocation
                );
            }
            
            // Check maximum movement distance
            // Note: This would need path calculation for multi-region moves
            int maxDistance = getMaxMovementDistance();
            
            return ActionResult.success(
                "Movement valid. Max distance: " + maxDistance + " regions"
            );
            
        } catch (SQLException e) {
            return ActionResult.error("Failed to validate movement: " + e.getMessage());
        }
    }
    
    /**
     * Move Fellowship to adjacent region
     * Triggers Hunt and validates movement rules
     */
    public ActionResult moveFellowship(String targetRegion, boolean triggerHunt) {
        try {
            // Validate movement
            ActionResult validation = validateMovement(targetRegion);
            if (!validation.isSuccess()) {
                return validation;
            }
            
            // Move the Fellowship
            gameState.moveFellowship(targetRegion);
            
            StringBuilder message = new StringBuilder("Fellowship moved to " + targetRegion);
            
            // Trigger Hunt if requested
            if (triggerHunt) {
                // TODO: Implement Hunt system
                message.append(". Hunt triggered!");
            }
            
            return ActionResult.success(message.toString());
            
        } catch (SQLException e) {
            return ActionResult.error("Failed to move Fellowship: " + e.getMessage());
        }
    }
    
    /**
     * Check if companion can split during movement
     * Rules for splitting companions during Fellowship movement
     */
    public boolean canSplitDuringMovement(String companionId) throws SQLException {
        // Ring bearers cannot split
        if (isRingBearer(companionId)) {
            return false;
        }
        
        // Must be in Fellowship
        if (!isInFellowship(companionId)) {
            return false;
        }
        
        // Cannot split if Fellowship is in enemy territory (simplified check)
        // TODO: Add proper territory control checking
        
        return true;
    }
}
