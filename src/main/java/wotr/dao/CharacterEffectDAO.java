package wotr.dao;

import wotr.models.CharacterEffect;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CharacterEffectDAO {
    private Connection connection;
    
    public CharacterEffectDAO(Connection connection) {
        this.connection = connection;
    }
    
    public List<CharacterEffect> findByCharacterId(String characterId) throws SQLException {
        List<CharacterEffect> effects = new ArrayList<CharacterEffect>();
        String sql = "SELECT * FROM character_effects WHERE character_id = ?";
        
        PreparedStatement pstmt = connection.prepareStatement(sql);
        pstmt.setString(1, characterId);
        ResultSet rs = pstmt.executeQuery();
        
        while (rs.next()) {
            CharacterEffect effect = new CharacterEffect();
            effect.setId(rs.getInt("id"));
            effect.setCharacterId(rs.getString("character_id"));
            effect.setName(rs.getString("name"));
            effect.setTriggersJson(rs.getString("triggers_json"));
            effects.add(effect);
        }
        
        rs.close();
        pstmt.close();
        
        return effects;
    }
    
    public void insert(CharacterEffect effect) throws SQLException {
        String sql = "INSERT INTO character_effects (character_id, name, triggers_json) VALUES (?, ?, ?)";
        PreparedStatement pstmt = connection.prepareStatement(sql);
        pstmt.setString(1, effect.getCharacterId());
        pstmt.setString(2, effect.getName());
        pstmt.setString(3, effect.getTriggersJson());
        pstmt.executeUpdate();
        pstmt.close();
    }
}
