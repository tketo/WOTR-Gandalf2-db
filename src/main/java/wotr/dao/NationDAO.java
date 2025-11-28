package wotr.dao;

import wotr.models.Nation;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class NationDAO {
    private Connection connection;
    
    public NationDAO(Connection connection) {
        this.connection = connection;
    }
    
    public List<Nation> findAll() throws SQLException {
        List<Nation> nations = new ArrayList<Nation>();
        String sql = "SELECT * FROM nations ORDER BY id";
        
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        
        while (rs.next()) {
            Nation nation = new Nation();
            nation.setId(rs.getInt("id"));
            nation.setName(rs.getString("name"));
            nation.setFaction(rs.getString("faction"));
            nations.add(nation);
        }
        
        rs.close();
        stmt.close();
        
        return nations;
    }
    
    public Nation findById(int id) throws SQLException {
        String sql = "SELECT * FROM nations WHERE id = ?";
        PreparedStatement pstmt = connection.prepareStatement(sql);
        pstmt.setInt(1, id);
        ResultSet rs = pstmt.executeQuery();
        
        Nation nation = null;
        if (rs.next()) {
            nation = new Nation();
            nation.setId(rs.getInt("id"));
            nation.setName(rs.getString("name"));
            nation.setFaction(rs.getString("faction"));
        }
        
        rs.close();
        pstmt.close();
        
        return nation;
    }
    
    public void insert(Nation nation) throws SQLException {
        String sql = "INSERT INTO nations (id, name) VALUES (?, ?)";
        PreparedStatement pstmt = connection.prepareStatement(sql);
        pstmt.setInt(1, nation.getId());
        pstmt.setString(2, nation.getName());
        pstmt.executeUpdate();
        pstmt.close();
    }
}
