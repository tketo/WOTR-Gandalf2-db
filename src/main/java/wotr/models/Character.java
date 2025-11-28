package wotr.models;

import java.util.ArrayList;
import java.util.List;

public class Character {
    private String id;
    private String name;
    private String title;
    private String faction;
    private String type;
    private String level;
    private int leadership;
    private int actionDieBonus;
    private boolean canGuide;
    private String playableBy;
    private List<CharacterAbility> abilities;
    private List<CharacterEffect> effects;
    
    public Character() {
        this.abilities = new ArrayList<CharacterAbility>();
        this.effects = new ArrayList<CharacterEffect>();
    }
    
    public Character(String id, String name, String faction, String type) {
        this();
        this.id = id;
        this.name = name;
        this.faction = faction;
        this.type = type;
    }
    
    public String getId() {
        return id;
    }
    
    public void setId(String id) {
        this.id = id;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getTitle() {
        return title;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }
    
    public String getFaction() {
        return faction;
    }
    
    public void setFaction(String faction) {
        this.faction = faction;
    }
    
    public String getType() {
        return type;
    }
    
    public void setType(String type) {
        this.type = type;
    }
    
    public String getLevel() {
        return level;
    }
    
    public void setLevel(String level) {
        this.level = level;
    }
    
    public int getLeadership() {
        return leadership;
    }
    
    public void setLeadership(int leadership) {
        this.leadership = leadership;
    }
    
    public int getActionDieBonus() {
        return actionDieBonus;
    }
    
    public void setActionDieBonus(int actionDieBonus) {
        this.actionDieBonus = actionDieBonus;
    }
    
    public boolean isCanGuide() {
        return canGuide;
    }
    
    public void setCanGuide(boolean canGuide) {
        this.canGuide = canGuide;
    }
    
    public String getPlayableBy() {
        return playableBy;
    }
    
    public void setPlayableBy(String playableBy) {
        this.playableBy = playableBy;
    }
    
    public List<CharacterAbility> getAbilities() {
        return abilities;
    }
    
    public void setAbilities(List<CharacterAbility> abilities) {
        this.abilities = abilities;
    }
    
    public void addAbility(CharacterAbility ability) {
        this.abilities.add(ability);
    }
    
    public List<CharacterEffect> getEffects() {
        return effects;
    }
    
    public void setEffects(List<CharacterEffect> effects) {
        this.effects = effects;
    }
    
    public void addEffect(CharacterEffect effect) {
        this.effects.add(effect);
    }
    
    public String toString() {
        return "Character{id='" + id + "', name='" + name + "', faction='" + faction + "', type='" + type + "'}";
    }
}
