package wotr.dice;

import wotr.models.Nation;
import wotr.dao.GameStateDAO;
import java.sql.SQLException;
import java.util.*;

/**
 * DicePool - Manages action dice for a game
 * 
 * Each nation has a certain number of dice:
 * - Free Peoples nations: variable based on political state
 * - Shadow nations: variable based on political state
 * 
 * Dice can be: available, rolled, used, or in Hunt Box
 */
public class DicePool {
    private String gameId;
    private Map<Integer, List<ActionDie>> diceByNation;
    private List<ActionDie> huntBox;
    private int diceRetrievedFromHuntLastTurn;
    
    public DicePool(String gameId) {
        this.gameId = gameId;
        this.diceByNation = new HashMap<>();
        this.huntBox = new ArrayList<>();
        this.diceRetrievedFromHuntLastTurn = 0;
    }
    
    /**
     * Load dice from database
     */
    public void loadFromDatabase(GameStateDAO dao) throws SQLException {
        // Clear existing dice
        diceByNation.clear();
        huntBox.clear();
        
        // Load all dice for this game from action_dice table
        List<ActionDie> loadedDice = dao.getActionDice(gameId);
        
        // Organize by nation
        for (ActionDie die : loadedDice) {
            int nationId = die.getNationId();
            
            // Add to nation's dice list
            if (!diceByNation.containsKey(nationId)) {
                diceByNation.put(nationId, new ArrayList<>());
            }
            diceByNation.get(nationId).add(die);
            
            // If die is in hunt box, add to hunt box list
            if (die.isInHuntBox()) {
                huntBox.add(die);
            }
        }
        
        System.out.println("Loaded " + loadedDice.size() + " dice from database");
    }
    
    /**
     * Initialize dice for a nation
     */
    public void initializeDice(Nation nation, int count) {
        List<ActionDie> dice = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            String dieId = gameId + "_" + nation.getId() + "_" + i;
            dice.add(new ActionDie(dieId, nation.getId()));
        }
        diceByNation.put(nation.getId(), dice);
    }
    
    /**
     * Get all dice for a nation
     */
    public List<ActionDie> getDiceForNation(int nationId) {
        return diceByNation.getOrDefault(nationId, new ArrayList<>());
    }
    
    /**
     * Get available dice for a nation (not used, not in Hunt Box)
     */
    public List<ActionDie> getAvailableDice(int nationId) {
        List<ActionDie> available = new ArrayList<>();
        for (ActionDie die : getDiceForNation(nationId)) {
            if (die.isAvailable()) {
                available.add(die);
            }
        }
        return available;
    }
    
    /**
     * Get rolled dice awaiting use
     */
    public List<ActionDie> getRolledDice(int nationId) {
        List<ActionDie> rolled = new ArrayList<>();
        for (ActionDie die : getDiceForNation(nationId)) {
            if (die.getStatus() == DieStatus.ROLLED && !die.isInHuntBox()) {
                rolled.add(die);
            }
        }
        return rolled;
    }
    
    /**
     * Get dice in Hunt Box
     */
    public List<ActionDie> getHuntBoxDice() {
        return new ArrayList<>(huntBox);
    }
    
    /**
     * Get hunt box dice count
     */
    public int getHuntBoxCount() {
        return huntBox.size();
    }
    
    /**
     * Roll all available dice for a faction
     */
    public void rollDice(String faction) {
        boolean isShadow = "shadow".equals(faction);
        
        for (List<ActionDie> dice : diceByNation.values()) {
            for (ActionDie die : dice) {
                if (die.getStatus() == DieStatus.AVAILABLE && !die.isInHuntBox()) {
                    die.roll(isShadow);
                    die.setStatus(DieStatus.ROLLED);
                    
                    // Eye results automatically go to Hunt Box
                    if (die.getResult() == DieType.EYE) {
                        placeInHuntBox(die);
                    }
                }
            }
        }
    }
    
    /**
     * Place a die in the Hunt Box
     */
    public void placeInHuntBox(ActionDie die) {
        die.placeInHuntBox();
        if (!huntBox.contains(die)) {
            huntBox.add(die);
        }
    }
    
    /**
     * Remove dice from Hunt Box (at start of turn)
     */
    public List<ActionDie> retrieveFromHuntBox() {
        List<ActionDie> retrieved = new ArrayList<>(huntBox);
        diceRetrievedFromHuntLastTurn = retrieved.size();
        
        for (ActionDie die : retrieved) {
            die.removeFromHuntBox();
        }
        
        huntBox.clear();
        return retrieved;
    }
    
    /**
     * Get minimum dice that must be allocated to Hunt Box
     * Rule: Minimum 1 if FP retrieved any last turn
     */
    public int getMinimumHuntAllocation() {
        return diceRetrievedFromHuntLastTurn > 0 ? 1 : 0;
    }
    
    /**
     * Get maximum dice that can be allocated to Hunt Box
     * Rule: Number of companions in Fellowship (min 1)
     */
    public int getMaximumHuntAllocation(int fellowshipSize) {
        return Math.max(1, fellowshipSize);
    }
    
    /**
     * Use a die for an action
     */
    public boolean useDie(String dieId) {
        for (List<ActionDie> dice : diceByNation.values()) {
            for (ActionDie die : dice) {
                if (die.getId().equals(dieId) && die.getStatus() == DieStatus.ROLLED) {
                    die.use();
                    return true;
                }
            }
        }
        return false;
    }
    
    /**
     * Recover all dice (at start of turn)
     */
    public void recoverAllDice() {
        // Retrieve dice from Hunt Box first
        retrieveFromHuntBox();
        
        // Recover all dice
        for (List<ActionDie> dice : diceByNation.values()) {
            for (ActionDie die : dice) {
                die.recover();
            }
        }
    }
    
    /**
     * Get dice by result type
     */
    public List<ActionDie> getDiceByType(int nationId, DieType type) {
        List<ActionDie> matching = new ArrayList<>();
        for (ActionDie die : getRolledDice(nationId)) {
            if (die.getResult() == type) {
                matching.add(die);
            }
        }
        return matching;
    }
    
    /**
     * Count available dice by type for a nation
     */
    public Map<DieType, Integer> countDiceByType(int nationId) {
        Map<DieType, Integer> counts = new HashMap<>();
        for (DieType type : DieType.values()) {
            counts.put(type, 0);
        }
        
        for (ActionDie die : getRolledDice(nationId)) {
            if (die.getResult() != null) {
                counts.put(die.getResult(), counts.get(die.getResult()) + 1);
            }
        }
        
        return counts;
    }
    
    /**
     * Get pool summary
     */
    public String getSummary() {
        StringBuilder sb = new StringBuilder();
        sb.append("Dice Pool Summary:\n");
        
        for (Map.Entry<Integer, List<ActionDie>> entry : diceByNation.entrySet()) {
            int nationId = entry.getKey();
            List<ActionDie> dice = entry.getValue();
            
            int available = 0;
            int rolled = 0;
            int used = 0;
            
            for (ActionDie die : dice) {
                if (die.isAvailable()) available++;
                else if (die.getStatus() == DieStatus.ROLLED) rolled++;
                else if (die.getStatus() == DieStatus.USED) used++;
            }
            
            sb.append(String.format("  Nation %d: %d total (%d available, %d rolled, %d used)\n",
                nationId, dice.size(), available, rolled, used));
        }
        
        sb.append(String.format("  Hunt Box: %d dice\n", huntBox.size()));
        
        return sb.toString();
    }
}
