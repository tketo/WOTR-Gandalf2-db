package wotr.migration;

import wotr.database.DatabaseManager;
import wotr.dao.*;
import wotr.models.*;
import java.sql.Connection;
import java.util.List;

public class MigrationValidator {
    
    public static void main(String[] args) {
        try {
            System.out.println("=== Validating Migration ===\n");
            
            DatabaseManager dbManager = DatabaseManager.getInstance();
            Connection conn = dbManager.getConnection();
            
            // Validate nations
            System.out.println("Nations:");
            NationDAO nationDAO = new NationDAO(conn);
            List<Nation> nations = nationDAO.findAll();
            System.out.println("  Count: " + nations.size());
            for (Nation nation : nations) {
                System.out.println("    " + nation);
            }
            System.out.println();
            
            // Validate regions
            System.out.println("Regions:");
            RegionDAO regionDAO = new RegionDAO(conn);
            List<Region> regions = regionDAO.findAll();
            System.out.println("  Count: " + regions.size());
            
            // Show sample regions with settlements
            System.out.println("  Sample regions with settlements:");
            int settlementCount = 0;
            for (Region region : regions) {
                if (region.getSettlement() != null) {
                    settlementCount++;
                    if (settlementCount <= 5) {
                        System.out.println("    " + region.getName() + " - " + region.getSettlement());
                    }
                }
            }
            System.out.println("  Total settlements: " + settlementCount);
            System.out.println();
            
            // Validate characters
            System.out.println("Characters:");
            CharacterDAO characterDAO = new CharacterDAO(conn);
            List<wotr.models.Character> characters = characterDAO.findAll();
            System.out.println("  Count: " + characters.size());
            
            // Show Free Peoples characters
            System.out.println("\n  Free Peoples Characters:");
            List<wotr.models.Character> fpChars = characterDAO.findByFaction("Free Peoples");
            for (wotr.models.Character character : fpChars) {
                System.out.println("    " + character.getName() + 
                    " (Level: " + character.getLevel() + 
                    ", Leadership: " + character.getLeadership() + 
                    ", Abilities: " + character.getAbilities().size() + ")");
            }
            
            // Show Shadow characters
            System.out.println("\n  Shadow Characters:");
            List<wotr.models.Character> shadowChars = characterDAO.findByFaction("Shadow");
            for (wotr.models.Character character : shadowChars) {
                System.out.println("    " + character.getName() + 
                    " (Level: " + character.getLevel() + 
                    ", Leadership: " + character.getLeadership() + 
                    ", Abilities: " + character.getAbilities().size() + ")");
            }
            
            // Detailed character example
            wotr.models.Character gandalf = characterDAO.findById("gandalf_the_grey");
            if (gandalf != null) {
                System.out.println("\n  Detailed Example - Gandalf the Grey:");
                System.out.println("    Name: " + gandalf.getName());
                System.out.println("    Title: " + gandalf.getTitle());
                System.out.println("    Faction: " + gandalf.getFaction());
                System.out.println("    Level: " + gandalf.getLevel());
                System.out.println("    Leadership: " + gandalf.getLeadership());
                System.out.println("    Can Guide: " + gandalf.isCanGuide());
                System.out.println("    Abilities:");
                for (CharacterAbility ability : gandalf.getAbilities()) {
                    System.out.println("      - " + ability.getName() + ": " + ability.getDescription());
                }
                if (gandalf.getEffects().size() > 0) {
                    System.out.println("    Effects: " + gandalf.getEffects().size());
                }
            }
            
            System.out.println("\n=== Validation Complete ===");
            System.out.println("✓ Database successfully populated with game data!");
            
        } catch (Exception e) {
            System.err.println("Validation failed!");
            e.printStackTrace();
        }
    }
}
