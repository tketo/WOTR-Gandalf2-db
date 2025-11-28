package wotr.migration;

import com.google.gson.*;
import wotr.database.DatabaseManager;
import wotr.dao.*;
import wotr.models.*;

import java.io.*;
import java.sql.Connection;

public class JSONToDBMigration {
    
    private Gson gson;
    private DatabaseManager dbManager;
    
    public JSONToDBMigration() {
        gson = new GsonBuilder().setPrettyPrinting().create();
        dbManager = DatabaseManager.getInstance();
    }
    
    public void migrateAll() {
        try {
            System.out.println("=== Starting Data Migration ===\n");
            
            migrateNations();
            migrateRegions();
            migrateCharacters();
            
            System.out.println("\n=== Migration Complete ===");
        } catch (Exception e) {
            System.err.println("Migration failed: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private void migrateNations() throws Exception {
        System.out.println("Migrating nations...");
        FileReader reader = new FileReader("data/metadata.json");
        JsonObject root = gson.fromJson(reader, JsonObject.class);
        JsonArray nationsArray = root.getAsJsonArray("nations");
        
        Connection conn = dbManager.getConnection();
        NationDAO nationDAO = new NationDAO(conn);
        
        for (JsonElement element : nationsArray) {
            JsonObject nationObj = element.getAsJsonObject();
            Nation nation = new Nation();
            nation.setId(nationObj.get("id").getAsInt());
            nation.setName(nationObj.get("name").getAsString());
            nationDAO.insert(nation);
        }
        
        reader.close();
        System.out.println("  ✓ Migrated " + nationsArray.size() + " nations");
    }
    
    private void migrateRegions() throws Exception {
        System.out.println("Migrating regions...");
        FileReader reader = new FileReader("data/metadata.json");
        JsonObject root = gson.fromJson(reader, JsonObject.class);
        JsonArray regionsArray = root.getAsJsonArray("regions");
        
        Connection conn = dbManager.getConnection();
        RegionDAO regionDAO = new RegionDAO(conn);
        
        int count = 0;
        for (JsonElement element : regionsArray) {
            JsonObject regionObj = element.getAsJsonObject();
            Region region = new Region();
            region.setId(regionObj.get("id").getAsString());
            region.setName(regionObj.get("name").getAsString());
            
            if (regionObj.has("nation") && !regionObj.get("nation").isJsonNull()) {
                String nationStr = regionObj.get("nation").getAsString();
                if (!nationStr.isEmpty()) {
                    try {
                        region.setNationId(Integer.parseInt(nationStr));
                    } catch (NumberFormatException e) {
                        // Some nations are empty strings, skip
                    }
                }
            }
            
            // Handle settlement if exists
            if (regionObj.has("settlement") && !regionObj.get("settlement").isJsonNull()) {
                JsonObject settlementObj = regionObj.getAsJsonObject("settlement");
                Settlement settlement = new Settlement();
                settlement.setRegionId(region.getId());
                settlement.setType(settlementObj.get("type").getAsString());
                settlement.setVp(settlementObj.get("vp").getAsInt());
                settlement.setCanMuster(settlementObj.get("can_muster").getAsBoolean());
                region.setSettlement(settlement);
            }
            
            regionDAO.insert(region);
            count++;
        }
        
        reader.close();
        System.out.println("  ✓ Migrated " + count + " regions");
    }
    
    private void migrateCharacters() throws Exception {
        System.out.println("Migrating characters...");
        FileReader reader = new FileReader("data/characters.json");
        JsonObject root = gson.fromJson(reader, JsonObject.class);
        JsonArray charactersArray = root.getAsJsonArray("characters");
        
        Connection conn = dbManager.getConnection();
        CharacterDAO characterDAO = new CharacterDAO(conn);
        CharacterAbilityDAO abilityDAO = new CharacterAbilityDAO(conn);
        CharacterEffectDAO effectDAO = new CharacterEffectDAO(conn);
        
        for (JsonElement element : charactersArray) {
            JsonObject charObj = element.getAsJsonObject();
            
            wotr.models.Character character = new wotr.models.Character();
            character.setId(charObj.get("id").getAsString());
            character.setName(charObj.get("name").getAsString());
            
            if (charObj.has("title") && !charObj.get("title").isJsonNull()) {
                character.setTitle(charObj.get("title").getAsString());
            }
            
            character.setFaction(charObj.get("faction").getAsString());
            character.setType(charObj.get("type").getAsString());
            
            if (charObj.has("level") && !charObj.get("level").isJsonNull()) {
                character.setLevel(charObj.get("level").getAsString());
            }
            
            if (charObj.has("leadership") && !charObj.get("leadership").isJsonNull()) {
                character.setLeadership(charObj.get("leadership").getAsInt());
            }
            
            if (charObj.has("actionDieBonus") && !charObj.get("actionDieBonus").isJsonNull()) {
                character.setActionDieBonus(charObj.get("actionDieBonus").getAsInt());
            }
            
            if (charObj.has("canGuide") && !charObj.get("canGuide").isJsonNull()) {
                character.setCanGuide(charObj.get("canGuide").getAsBoolean());
            }
            
            if (charObj.has("playableBy") && !charObj.get("playableBy").isJsonNull()) {
                character.setPlayableBy(charObj.get("playableBy").getAsString());
            }
            
            // Insert character first
            characterDAO.insert(character);
            
            // Migrate abilities
            if (charObj.has("abilities") && !charObj.get("abilities").isJsonNull()) {
                JsonArray abilities = charObj.getAsJsonArray("abilities");
                for (JsonElement abilityEl : abilities) {
                    JsonObject abilityObj = abilityEl.getAsJsonObject();
                    CharacterAbility ability = new CharacterAbility();
                    ability.setCharacterId(character.getId());
                    ability.setName(abilityObj.get("name").getAsString());
                    ability.setDescription(abilityObj.get("description").getAsString());
                    abilityDAO.insert(ability);
                }
            }
            
            // Migrate effects (store as JSON blob)
            if (charObj.has("effects") && !charObj.get("effects").isJsonNull()) {
                JsonArray effects = charObj.getAsJsonArray("effects");
                for (JsonElement effectEl : effects) {
                    JsonObject effectObj = effectEl.getAsJsonObject();
                    CharacterEffect effect = new CharacterEffect();
                    effect.setCharacterId(character.getId());
                    effect.setName(effectObj.get("name").getAsString());
                    // Store triggers as JSON string
                    if (effectObj.has("triggers") && !effectObj.get("triggers").isJsonNull()) {
                        effect.setTriggersJson(effectObj.get("triggers").toString());
                    }
                    effectDAO.insert(effect);
                }
            }
        }
        
        reader.close();
        System.out.println("  ✓ Migrated " + charactersArray.size() + " characters");
    }
    
    public static void main(String[] args) {
        JSONToDBMigration migration = new JSONToDBMigration();
        migration.migrateAll();
    }
}
