package wotr.dao;

import wotr.models.Settlement;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SettlementDAO {
    private Connection connection;
    
    public SettlementDAO(Connection connection) {
        this.connection = connection;
    }
    
    public List<Settlement> findAll() throws SQLException {
        List<Settlement> settlements = new ArrayList<>();
        String sql = "SELECT * FROM settlements ORDER BY region_id";
        
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        
        while (rs.next()) {
            Settlement settlement = new Settlement();
            settlement.setId(rs.getInt("id"));
            settlement.setRegionId(rs.getString("region_id"));
            settlement.setType(rs.getString("type"));
            settlement.setVp(rs.getInt("vp"));
            settlement.setCanMuster(rs.getBoolean("can_muster"));
            settlements.add(settlement);
        }
        
        rs.close();
        stmt.close();
        
        return settlements;
    }
    
    public Settlement findByRegionId(String regionId) throws SQLException {
        String sql = "SELECT * FROM settlements WHERE region_id = ?";
        PreparedStatement pstmt = connection.prepareStatement(sql);
        pstmt.setString(1, regionId);
        ResultSet rs = pstmt.executeQuery();
        
        Settlement settlement = null;
        if (rs.next()) {
            settlement = new Settlement();
            settlement.setId(rs.getInt("id"));
            settlement.setRegionId(rs.getString("region_id"));
            settlement.setType(rs.getString("type"));
            settlement.setVp(rs.getInt("vp"));
            settlement.setCanMuster(rs.getBoolean("can_muster"));
        }
        
        rs.close();
        pstmt.close();
        
        return settlement;
    }
    
    public void insert(Settlement settlement) throws SQLException {
        String sql = "INSERT INTO settlements (region_id, type, vp, can_muster) VALUES (?, ?, ?, ?)";
        PreparedStatement pstmt = connection.prepareStatement(sql);
        pstmt.setString(1, settlement.getRegionId());
        pstmt.setString(2, settlement.getType());
        pstmt.setInt(3, settlement.getVp());
        pstmt.setBoolean(4, settlement.isCanMuster());
        pstmt.executeUpdate();
        pstmt.close();
    }
}
