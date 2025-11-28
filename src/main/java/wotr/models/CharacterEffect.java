package wotr.models;

public class CharacterEffect {
    private int id;
    private String characterId;
    private String name;
    private String triggersJson;
    
    public CharacterEffect() {}
    
    public CharacterEffect(String characterId, String name, String triggersJson) {
        this.characterId = characterId;
        this.name = name;
        this.triggersJson = triggersJson;
    }
    
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public String getCharacterId() {
        return characterId;
    }
    
    public void setCharacterId(String characterId) {
        this.characterId = characterId;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getTriggersJson() {
        return triggersJson;
    }
    
    public void setTriggersJson(String triggersJson) {
        this.triggersJson = triggersJson;
    }
    
    public String toString() {
        return "CharacterEffect{name='" + name + "'}";
    }
}
