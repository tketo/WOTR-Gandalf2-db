package wotr;

import wotr.database.DatabaseManager;
import wotr.dao.RegionDAO;
import wotr.models.Region;

/**
 * Add Tower Hills region (ID 69) to database
 */
public class AddTowerHills {
    
    public static void main(String[] args) {
        try {
            System.out.println("=== Adding Tower Hills Region ===\n");
            
            DatabaseManager dbManager = DatabaseManager.getInstance();
            RegionDAO regionDAO = new RegionDAO(dbManager.getConnection());
            
            // Create Tower Hills region
            Region towerHills = new Region();
            towerHills.setId("69");
            towerHills.setName("Tower Hills");
            towerHills.setNationId(null); // No controlling nation
            
            // Insert into database
            regionDAO.insert(towerHills);
            
            System.out.println("✓ Tower Hills (region 69) added to database");
            
            // Verify
            Region retrieved = regionDAO.findById("69");
            if (retrieved != null) {
                System.out.println("✓ Verified: " + retrieved.getName());
            } else {
                System.out.println("✗ Failed to verify");
            }
            
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
