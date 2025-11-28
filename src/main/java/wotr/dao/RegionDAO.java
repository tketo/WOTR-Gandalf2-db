package wotr.dao;

import wotr.models.Region;
import wotr.models.Settlement;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RegionDAO {
    private Connection connection;
    private SettlementDAO settlementDAO;
    
    public RegionDAO(Connection connection) {
        this.connection = connection;
        this.settlementDAO = new SettlementDAO(connection);
    }
    
    public List<Region> findAll() throws SQLException {
        List<Region> regions = new ArrayList<Region>();
        String sql = "SELECT * FROM regions ORDER BY name";
        
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        
        while (rs.next()) {
            Region region = mapResultSetToRegion(rs);
            Settlement settlement = settlementDAO.findByRegionId(region.getId());
            region.setSettlement(settlement);
            regions.add(region);
        }
        
        rs.close();
        stmt.close();
        
        return regions;
    }
    
    public Region findById(String id) throws SQLException {
        String sql = "SELECT * FROM regions WHERE id = ?";
        PreparedStatement pstmt = connection.prepareStatement(sql);
        pstmt.setString(1, id);
        ResultSet rs = pstmt.executeQuery();
        
        Region region = null;
        if (rs.next()) {
            region = mapResultSetToRegion(rs);
            Settlement settlement = settlementDAO.findByRegionId(id);
            region.setSettlement(settlement);
        }
        
        rs.close();
        pstmt.close();
        
        return region;
    }
    
    public List<Region> findByNation(int nationId) throws SQLException {
        List<Region> regions = new ArrayList<Region>();
        String sql = "SELECT * FROM regions WHERE nation_id = ? ORDER BY name";
        
        PreparedStatement pstmt = connection.prepareStatement(sql);
        pstmt.setInt(1, nationId);
        ResultSet rs = pstmt.executeQuery();
        
        while (rs.next()) {
            Region region = mapResultSetToRegion(rs);
            Settlement settlement = settlementDAO.findByRegionId(region.getId());
            region.setSettlement(settlement);
            regions.add(region);
        }
        
        rs.close();
        pstmt.close();
        
        return regions;
    }
    
    public void insert(Region region) throws SQLException {
        String sql = "INSERT INTO regions (id, name, nation_id) VALUES (?, ?, ?)";
        PreparedStatement pstmt = connection.prepareStatement(sql);
        pstmt.setString(1, region.getId());
        pstmt.setString(2, region.getName());
        
        if (region.getNationId() != null) {
            pstmt.setInt(3, region.getNationId());
        } else {
            pstmt.setNull(3, Types.INTEGER);
        }
        
        pstmt.executeUpdate();
        pstmt.close();
        
        if (region.getSettlement() != null) {
            region.getSettlement().setRegionId(region.getId());
            settlementDAO.insert(region.getSettlement());
        }
    }
    
    /**
     * Check if two regions are adjacent
     * Uses the v_region_adjacency view for bidirectional lookup
     */
    public boolean areAdjacent(String regionId1, String regionId2) throws SQLException {
        if (regionId1 == null || regionId2 == null) {
            return false;
        }
        
        // Same region = adjacent to itself
        if (regionId1.equals(regionId2)) {
            return true;
        }
        
        String sql = "SELECT 1 FROM v_region_adjacency " +
                    "WHERE region_a = ? AND region_b = ? LIMIT 1";
        
        PreparedStatement pstmt = connection.prepareStatement(sql);
        pstmt.setString(1, regionId1);
        pstmt.setString(2, regionId2);
        ResultSet rs = pstmt.executeQuery();
        
        boolean adjacent = rs.next();
        
        rs.close();
        pstmt.close();
        
        return adjacent;
    }
    
    /**
     * Get all regions adjacent to a given region
     */
    public List<String> getAdjacentRegions(String regionId) throws SQLException {
        List<String> adjacent = new ArrayList<>();
        String sql = "SELECT region_b FROM v_region_adjacency WHERE region_a = ?";
        
        PreparedStatement pstmt = connection.prepareStatement(sql);
        pstmt.setString(1, regionId);
        ResultSet rs = pstmt.executeQuery();
        
        while (rs.next()) {
            adjacent.add(rs.getString("region_b"));
        }
        
        rs.close();
        pstmt.close();
        
        return adjacent;
    }
    
    private Region mapResultSetToRegion(ResultSet rs) throws SQLException {
        Region region = new Region();
        region.setId(rs.getString("id"));
        region.setName(rs.getString("name"));
        
        int nationId = rs.getInt("nation_id");
        if (!rs.wasNull()) {
            region.setNationId(nationId);
        }
        
        return region;
    }
}
