package wotr.models;

public class CharacterAbility {
    private int id;
    private String characterId;
    private String name;
    private String description;
    
    public CharacterAbility() {}
    
    public CharacterAbility(String characterId, String name, String description) {
        this.characterId = characterId;
        this.name = name;
        this.description = description;
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
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public String toString() {
        return "CharacterAbility{name='" + name + "', description='" + description + "'}";
    }
}
