package io.github.seriousguy888.cheezsurvtaggame.database;

import io.github.seriousguy888.cheezsurvtaggame.CheezSurvTagGame;

import java.io.File;
import java.sql.*;
import java.util.logging.Level;

public class Database {

    private final CheezSurvTagGame plugin;
    private final String dbName;
    private Connection connection;

    public Database(CheezSurvTagGame plugin, String dbName) {
        this.plugin = plugin;
        this.dbName = dbName;
        this.connection = null;
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public Connection getConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                return connection;
            }
        } catch (SQLException e) {
            // If here, do nothing and continue below to try to create a new connection.
        }

        File folder = plugin.getDataFolder();
        if (!folder.exists()) {
            folder.mkdirs();
        }

        File dbFile = new File(folder, "data" + File.separator + dbName + ".db");
        String url = "jdbc:sqlite:" + dbFile;

        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection(url);
            return connection;
        } catch (SQLException | ClassNotFoundException e) {
            plugin.getLogger().log(Level.SEVERE, "Failed to connect to database.", e);
        }

        return null;
    }

    public void init() {
        try (Connection connection = getConnection()) {
            PreparedStatement statement = connection
                    .prepareStatement("""
                            CREATE TABLE IF NOT EXISTS
                            TagLog(
                                NewItUUID varchar(36) NOT NULL,
                                OldItUUID varchar(36),
                                Timestamp timestamp NOT NULL UNIQUE
                            )
                            """);
            statement.execute();
            statement.close();
        } catch (SQLException e) {
            plugin.getLogger().log(Level.SEVERE, "Failed to initialise database.", e);
        }
    }

}
