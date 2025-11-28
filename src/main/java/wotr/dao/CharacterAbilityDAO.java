package wotr.dao;

import wotr.models.CharacterAbility;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CharacterAbilityDAO {
    private Connection connection;
    
    public CharacterAbilityDAO(Connection connection) {
        this.connection = connection;
    }
    
    public List<CharacterAbility> findByCharacterId(String characterId) throws SQLException {
        List<CharacterAbility> abilities = new ArrayList<CharacterAbility>();
        String sql = "SELECT * FROM character_abilities WHERE character_id = ?";
        
        PreparedStatement pstmt = connection.prepareStatement(sql);
        pstmt.setString(1, characterId);
        ResultSet rs = pstmt.executeQuery();
        
        while (rs.next()) {
            CharacterAbility ability = new CharacterAbility();
            ability.setId(rs.getInt("id"));
            ability.setCharacterId(rs.getString("character_id"));
            ability.setName(rs.getString("name"));
            ability.setDescription(rs.getString("description"));
            abilities.add(ability);
        }
        
        rs.close();
        pstmt.close();
        
        return abilities;
    }
    
    public void insert(CharacterAbility ability) throws SQLException {
        String sql = "INSERT INTO character_abilities (character_id, name, description) VALUES (?, ?, ?)";
        PreparedStatement pstmt = connection.prepareStatement(sql);
        pstmt.setString(1, ability.getCharacterId());
        pstmt.setString(2, ability.getName());
        pstmt.setString(3, ability.getDescription());
        pstmt.executeUpdate();
        pstmt.close();
    }
}
