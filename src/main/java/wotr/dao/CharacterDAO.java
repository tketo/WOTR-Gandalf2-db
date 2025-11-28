package wotr.dao;

import wotr.models.Character;
import wotr.models.CharacterAbility;
import wotr.models.CharacterEffect;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CharacterDAO {
    private Connection connection;
    private CharacterAbilityDAO abilityDAO;
    private CharacterEffectDAO effectDAO;
    
    public CharacterDAO(Connection connection) {
        this.connection = connection;
        this.abilityDAO = new CharacterAbilityDAO(connection);
        this.effectDAO = new CharacterEffectDAO(connection);
    }
    
    public List<Character> findAll() throws SQLException {
        List<Character> characters = new ArrayList<Character>();
        String sql = "SELECT * FROM characters ORDER BY name";
        
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        
        while (rs.next()) {
            Character character = mapResultSetToCharacter(rs);
            character.setAbilities(abilityDAO.findByCharacterId(character.getId()));
            character.setEffects(effectDAO.findByCharacterId(character.getId()));
            characters.add(character);
        }
        
        rs.close();
        stmt.close();
        
        return characters;
    }
    
    public Character findById(String id) throws SQLException {
        String sql = "SELECT * FROM characters WHERE id = ?";
        PreparedStatement pstmt = connection.prepareStatement(sql);
        pstmt.setString(1, id);
        
        ResultSet rs = pstmt.executeQuery();
        Character character = null;
        
        if (rs.next()) {
            character = mapResultSetToCharacter(rs);
            character.setAbilities(abilityDAO.findByCharacterId(id));
            character.setEffects(effectDAO.findByCharacterId(id));
        }
        
        rs.close();
        pstmt.close();
        
        return character;
    }
    
    public List<Character> findByFaction(String faction) throws SQLException {
        List<Character> characters = new ArrayList<Character>();
        String sql = "SELECT * FROM characters WHERE faction = ? ORDER BY name";
        
        PreparedStatement pstmt = connection.prepareStatement(sql);
        pstmt.setString(1, faction);
        ResultSet rs = pstmt.executeQuery();
        
        while (rs.next()) {
            Character character = mapResultSetToCharacter(rs);
            character.setAbilities(abilityDAO.findByCharacterId(character.getId()));
            character.setEffects(effectDAO.findByCharacterId(character.getId()));
            characters.add(character);
        }
        
        rs.close();
        pstmt.close();
        
        return characters;
    }
    
    public void insert(Character character) throws SQLException {
        String sql = "INSERT INTO characters (id, name, title, faction, type, level, " +
                    "leadership, action_die_bonus, can_guide, playable_by) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        PreparedStatement pstmt = connection.prepareStatement(sql);
        pstmt.setString(1, character.getId());
        pstmt.setString(2, character.getName());
        pstmt.setString(3, character.getTitle());
        pstmt.setString(4, character.getFaction());
        pstmt.setString(5, character.getType());
        pstmt.setString(6, character.getLevel());
        pstmt.setInt(7, character.getLeadership());
        pstmt.setInt(8, character.getActionDieBonus());
        pstmt.setBoolean(9, character.isCanGuide());
        pstmt.setString(10, character.getPlayableBy());
        
        pstmt.executeUpdate();
        pstmt.close();
        
        for (CharacterAbility ability : character.getAbilities()) {
            ability.setCharacterId(character.getId());
            abilityDAO.insert(ability);
        }
        
        for (CharacterEffect effect : character.getEffects()) {
            effect.setCharacterId(character.getId());
            effectDAO.insert(effect);
        }
    }
    
    private Character mapResultSetToCharacter(ResultSet rs) throws SQLException {
        Character character = new Character();
        character.setId(rs.getString("id"));
        character.setName(rs.getString("name"));
        character.setTitle(rs.getString("title"));
        character.setFaction(rs.getString("faction"));
        character.setType(rs.getString("type"));
        character.setLevel(rs.getString("level"));
        character.setLeadership(rs.getInt("leadership"));
        character.setActionDieBonus(rs.getInt("action_die_bonus"));
        character.setCanGuide(rs.getBoolean("can_guide"));
        character.setPlayableBy(rs.getString("playable_by"));
        return character;
    }
}
