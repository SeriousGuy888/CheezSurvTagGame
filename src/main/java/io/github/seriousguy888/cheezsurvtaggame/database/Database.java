package io.github.seriousguy888.cheezsurvtaggame.database;

import io.github.seriousguy888.cheezsurvtaggame.CheezSurvTagGame;

import java.io.File;
import java.sql.*;
import java.util.logging.Level;

public class Database {

    private final CheezSurvTagGame plugin;
    private final String dbName;

    public Database(CheezSurvTagGame plugin, String dbName) {
        this.plugin = plugin;
        this.dbName = dbName;
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public Connection getConnection() {

        File folder = plugin.getDataFolder();
        if (!folder.exists()) {
            folder.mkdirs();
        }


        String url = "jdbc:h2:file:"
                + folder.getAbsolutePath() + File.separator + "data" + File.separator + dbName;

        Connection connection = null;
        try {
            Class.forName("org.h2.Driver");
            connection = DriverManager.getConnection(url);
        } catch (SQLException | ClassNotFoundException e) {
            plugin.getLogger().log(Level.SEVERE, "Failed to connect to database.", e);
        }

        return connection;

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
