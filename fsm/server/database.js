const sqlite3 = require('sqlite3').verbose();

/**
 * Database Helper - Read-only queries for FSM guard evaluation
 * 
 * All queries are SELECT only - no INSERT/UPDATE/DELETE
 * Game client has write authority
 */
class Database {
  constructor(dbPath) {
    console.log('[DB] Opening database:', dbPath);
    this.db = new sqlite3.Database(dbPath, sqlite3.OPEN_READONLY, (err) => {
      if (err) {
        console.error('[DB] Error opening database:', err);
      } else {
        console.log('[DB] Database opened successfully');
      }
    });
  }
  
  /**
   * Helper to execute query and return single value
   */
  async query(sql, params = []) {
    return new Promise((resolve, reject) => {
      this.db.get(sql, params, (err, row) => {
        if (err) {
          console.error('[DB] Query error:', err);
          reject(err);
        } else {
          // Return first column value or null
          resolve(row ? Object.values(row)[0] : null);
        }
      });
    });
  }
  
  /**
   * Helper to execute query and return all rows
   */
  async queryAll(sql, params = []) {
    return new Promise((resolve, reject) => {
      this.db.all(sql, params, (err, rows) => {
        if (err) {
          console.error('[DB] Query error:', err);
          reject(err);
        } else {
          resolve(rows || []);
        }
      });
    });
  }
  
  // ===== FSM Guard Queries =====
  
  /**
   * Get current corruption level
   */
  async getCorruption() {
    const corruption = await this.query(
      "SELECT corruption FROM fellowship_state LIMIT 1"
    );
    return corruption || 0;
  }
  
  /**
   * Get fellowship location
   */
  async getFellowshipLocation() {
    const location = await this.query(
      "SELECT region_id FROM fellowship_state LIMIT 1"
    );
    return location || null;
  }
  
  /**
   * Get remaining dice for faction
   */
  async getDiceRemaining(faction) {
    const count = await this.query(
      "SELECT COUNT(*) FROM action_dice WHERE faction=? AND used=0",
      [faction]
    );
    return count || 0;
  }
  
  /**
   * Check if faction has passed
   */
  async hasPassed(faction) {
    // This would require a game_state table to track passed flags
    // For now, return false (will implement when game client adds tracking)
    return false;
  }
  
  /**
   * Get victory points for faction
   */
  async getVictoryPoints(faction) {
    const vp = await this.query(
      `SELECT SUM(r.victory_points) 
       FROM regions r
       JOIN region_control rc ON r.region_id = rc.region_id
       WHERE rc.control = ?`,
      [faction]
    );
    return vp || 0;
  }
  
  /**
   * Check if combat is active
   */
  async isCombatActive() {
    // Would query combat_state table if it exists
    // For now, return false
    return false;
  }
  
  /**
   * Check if fellowship moved this turn
   */
  async hasFellowshipMoved() {
    // Would query turn_state table if it exists
    // For now, return false
    return false;
  }
  
  /**
   * Set passed flag for faction (in-memory for now)
   */
  async setPassedFlag(faction, value) {
    // This would write to database if we had write permissions
    // For MVP, we'll track in memory in FSM engine
    console.log(`[DB] Would set ${faction}_PASSED_FLAG = ${value}`);
  }
  
  /**
   * Clear action flags
   */
  async clearActionFlags() {
    console.log('[DB] Would clear action flags');
  }
  
  /**
   * Get hand size for a faction
   */
  async getHandSize(faction) {
    // Query cards in hand for this faction
    // For now, return mock data
    return faction === 'FP' ? 4 : 5;
  }
  
  /**
   * Get Fellowship location and status
   */
  async getFellowshipLocation() {
    // Query fellowship position
    return {
      location: 'Rivendell',
      regionId: 30
    };
  }
  
  /**
   * Check if Fellowship is revealed
   */
  async isFellowshipRevealed() {
    // Query fellowship revealed status
    return false;
  }
  
  /**
   * Get current corruption level
   */
  async getCorruption() {
    // Query corruption from database
    return 0;
  }
  
  /**
   * Get count of Eye dice results
   */
  async getEyeDiceCount(faction) {
    // Count Eye results from action_dice table
    return 2; // Mock
  }
  
  /**
   * Get hunt dice currently allocated
   */
  async getHuntDiceAllocated() {
    // Query hunt box
    return 0; // Mock
  }
  
  /**
   * Get total action dice count for faction
   */
  async getDiceCount(faction) {
    // Count action_dice for faction
    return faction === 'FP' ? 4 : 7;
  }
  
  /**
   * Close database connection
   */
  close() {
    this.db.close((err) => {
      if (err) {
        console.error('[DB] Error closing database:', err);
      } else {
        console.log('[DB] Database closed');
      }
    });
  }
}

module.exports = Database;
