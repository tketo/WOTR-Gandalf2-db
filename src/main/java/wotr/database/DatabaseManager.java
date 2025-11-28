package wotr.database;

import java.io.*;
import java.sql.*;
import org.flywaydb.core.Flyway;

public class DatabaseManager {
    private static final String DB_PATH = "wotr_game.db";
    private static final String SCHEMA_DIR = "database/schema/";
    private static DatabaseManager instance;
    private Connection connection;
    
    private DatabaseManager() {
        initialize();
    }
    
    public static synchronized DatabaseManager getInstance() {
        if (instance == null) {
            instance = new DatabaseManager();
        }
        return instance;
    }
    
    private void initialize() {
        try {
            // Load SQLite JDBC driver
            Class.forName("org.sqlite.JDBC");
            
            // Check if database exists BEFORE creating connection
            File dbFile = new File(DB_PATH);
            boolean fileExisted = dbFile.exists();
            
            System.err.println("[DB Init] Database file: " + dbFile.getAbsolutePath());
            System.err.println("[DB Init] File exists BEFORE connection: " + fileExisted);
            System.err.flush();
            
            // Create connection (this may create an empty file)
            connection = DriverManager.getConnection("jdbc:sqlite:" + DB_PATH);
            connection.setAutoCommit(true);
            
            System.err.println("[DB Init] File exists AFTER connection: " + dbFile.exists());
            System.err.flush();
            
            // Enable foreign keys
            Statement stmt = connection.createStatement();
            stmt.execute("PRAGMA foreign_keys = ON");
            stmt.close();
            
            // Check if core tables exist (more reliable than file existence check)
            boolean needsInitialization = !tablesExist();
            
            if (needsInitialization) {
                System.out.println("[DB Init] Core tables missing, initializing schema...");
                initializeSchema();
            } else {
                System.out.println("[DB Init] Database already initialized: " + DB_PATH);
            }
            
            // Run Flyway migrations (applies V008, V009, V010, etc.)
            runMigrations();
            
        } catch (ClassNotFoundException e) {
            System.err.println("SQLite JDBC driver not found!");
            e.printStackTrace();
            throw new RuntimeException("Failed to load SQLite driver", e);
        } catch (SQLException e) {
            System.err.println("Failed to initialize database!");
            e.printStackTrace();
            throw new RuntimeException("Database initialization failed", e);
        }
    }
    
    /**
     * Check if core database tables exist
     * @return true if game_state table exists (indicates schema is initialized)
     */
    private boolean tablesExist() {
        try {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(
                "SELECT name FROM sqlite_master WHERE type='table' AND name='game_state'"
            );
            boolean exists = rs.next();
            rs.close();
            stmt.close();
            return exists;
        } catch (SQLException e) {
            System.err.println("[DB Init] Error checking table existence: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Check if scenario data has been imported
     * @return true if scenario_setup table has data
     */
    private boolean hasScenarioData() {
        try {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM scenario_setup");
            int count = 0;
            if (rs.next()) {
                count = rs.getInt(1);
            }
            rs.close();
            stmt.close();
            return count > 0;
        } catch (SQLException e) {
            System.err.println("[DB Init] Error checking scenario data: " + e.getMessage());
            return false;
        }
    }
    
    private void initializeSchema() throws SQLException {
        System.err.println("[DB Init] Initializing database schema...");
        System.err.flush();
        
        // Execute schema creation scripts with error checking
        String[] schemaFiles = {
            "01_create_tables.sql",
            "02_create_indexes.sql",
            "03_create_game_state_tables.sql",
            "04_create_scenario_tables.sql",
            "05_create_adjacency_tables.sql",
            "06_add_nation_factions.sql",
            "07_create_unified_game_pieces.sql"
        };
        
        for (String file : schemaFiles) {
            try {
                executeSQLFile(SCHEMA_DIR + file);
            } catch (Exception e) {
                System.err.println("[DB Init] ✗ CRITICAL: Failed to execute " + file);
                e.printStackTrace();
                throw new SQLException("Schema initialization failed at: " + file, e);
            }
        }
        
        System.err.println("[DB Init] Database schema initialized successfully!");
        System.err.flush();
        
        // Import scenario data if table is empty
        if (!hasScenarioData()) {
            try {
                System.err.println("[DB Init] Importing scenario data...");
                executeSQLFile("database/imports/scenario_imports.sql");
                System.err.println("[DB Init] Scenario data imported successfully!");
            } catch (Exception e) {
                System.err.println("[DB Init] Warning: Failed to import scenario data: " + e.getMessage());
                // Don't fail initialization if imports fail - game can use hardcoded fallback
            }
        } else {
            System.err.println("[DB Init] Scenario data already exists, skipping import");
        }
    }
    
    /**
     * Run Flyway migrations from src/main/resources/db/migration
     * This applies V008, V009, V010 and future migrations automatically
     * 
     * Baseline version 7 matches the old schema version (database/schema/*.sql files)
     */
    private void runMigrations() {
        System.out.println("[Flyway] Running database migrations...");
        
        Flyway flyway = Flyway.configure()
            .dataSource("jdbc:sqlite:" + DB_PATH, null, null)
            .locations("classpath:db/migration")
            .baselineOnMigrate(true)
            .baselineVersion("7")  // Old schema files were version 1-7
            .load();
        
        int pendingCount = flyway.info().pending().length;
        System.out.println("[Flyway] Found " + pendingCount + " pending migrations");
        
        if (pendingCount > 0) {
            flyway.migrate();
            System.out.println("[Flyway] Migrations complete - current version: " + 
                flyway.info().current().getVersion());
        } else {
            System.out.println("[Flyway] No pending migrations");
        }
    }
    
    private void checkSchemaVersion() throws SQLException {
        try {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(
                "SELECT version, description FROM schema_version " +
                "ORDER BY version DESC LIMIT 1"
            );
            
            if (rs.next()) {
                int version = rs.getInt("version");
                String desc = rs.getString("description");
                System.out.println("Schema version: " + version + " - " + desc);
            }
            
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            System.err.println("Warning: Could not check schema version");
        }
    }
    
    private void executeSQLFile(String filePath) throws SQLException {
        // Read from classpath resources (works inside JAR)
        System.out.println("[DB Init] Loading SQL file: " + filePath);
        InputStream is = getClass().getClassLoader().getResourceAsStream(filePath);
        if (is == null) {
            System.err.println("[DB Init] ✗ SQL file not found in resources: " + filePath);
            return;
        }
        System.out.println("[DB Init] ✓ SQL file found, executing...");
        
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            StringBuilder sql = new StringBuilder();
            String line;
            
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                
                // Skip comments and empty lines
                if (line.isEmpty() || line.startsWith("--")) {
                    continue;
                }
                
                sql.append(line).append(" ");
                
                // Execute when we hit a semicolon
                if (line.endsWith(";")) {
                    String statement = sql.toString();
                    try {
                        Statement stmt = connection.createStatement();
                        stmt.execute(statement);
                        stmt.close();
                    } catch (SQLException e) {
                        System.err.println("Error executing: " + statement);
                        throw e;
                    }
                    sql = new StringBuilder();
                }
            }
            
            reader.close();
            System.out.println("Executed SQL file: " + filePath);
            
        } catch (IOException e) {
            System.err.println("Error reading SQL file: " + filePath);
            throw new SQLException("Failed to read SQL file", e);
        }
    }
    
    public Connection getConnection() {
        return connection;
    }
    
    public void close() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("Database connection closed");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    // Backup utility
    public void createBackup(String backupPath) throws IOException {
        File sourceFile = new File(DB_PATH);
        File destFile = new File(backupPath);
        
        FileInputStream fis = new FileInputStream(sourceFile);
        FileOutputStream fos = new FileOutputStream(destFile);
        
        byte[] buffer = new byte[1024];
        int length;
        while ((length = fis.read(buffer)) > 0) {
            fos.write(buffer, 0, length);
        }
        
        fis.close();
        fos.close();
        
        System.out.println("Database backed up to: " + backupPath);
    }
}
