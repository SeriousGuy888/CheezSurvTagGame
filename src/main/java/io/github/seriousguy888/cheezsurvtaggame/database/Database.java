package io.github.seriousguy888.cheezsurvtaggame.database;

import io.github.seriousguy888.cheezsurvtaggame.CheezSurvTagGame;
import org.bukkit.OfflinePlayer;

import javax.annotation.Nullable;
import java.io.File;
import java.sql.*;
import java.util.UUID;
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

        File dbFile = new File(plugin.getDataFolder(), "data" + File.separator + dbName + ".db");
        dbFile.getParentFile().mkdirs();

        String url = "jdbc:sqlite:" + dbFile.getAbsolutePath();

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
        try (Connection connection = getConnection();
             PreparedStatement createLogTable = connection
                     .prepareStatement("""
                             CREATE TABLE IF NOT EXISTS
                             TagLog(
                                 NewItUUID varchar(36) NOT NULL,
                                 OldItUUID varchar(36),
                                 Timestamp timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP
                             )
                             """);
             PreparedStatement createPreferencesTable = connection
                     .prepareStatement("""
                             CREATE TABLE IF NOT EXISTS
                             Preferences(
                                 PlayerUUID varchar(36) PRIMARY KEY,
                                 EnableHUD boolean NOT NULL DEFAULT 0
                             )
                             """)
        ) {
            createLogTable.execute();
            createPreferencesTable.execute();
        } catch (SQLException e) {
            plugin.getLogger().log(Level.SEVERE, "Failed to initialise database.", e);
        }
    }

    /**
     * Write a new row to the TagLog table indicating that this player is now It.
     * The old It field will be left blank.
     *
     * @param newIt The player that has been made it.
     */
    public void logNewIt(OfflinePlayer newIt) {
        logNewIt(newIt, null);
    }

    /**
     * Write a new row to the TagLog table indicating the current timestamp and who is now It. If this player
     * is now it because of a tagging event, the old It can also be specified. Otherwise, the old It field can
     * be left blank, such as if they were chosen to be It manually or automatically because the previous It
     * player is offline.
     *
     * @param oldIt Previously It player who tagged the new player.
     * @param newIt The player that is now It.
     */
    public void logNewIt(OfflinePlayer newIt, @Nullable OfflinePlayer oldIt) {
        if(!plugin.getMainConfig().getLogTagEventsToDb()) {
            // Don't log anything if configured not to.
            return;
        }

        try (Connection connection = getConnection()) {
            // [!] Multiline strings break this for some reason, constructing a
            //     syntactically incorrect SQL query.
            PreparedStatement statement = connection.prepareStatement(
                    "INSERT INTO TagLog(NewItUUID, OldItUUID) VALUES (?, ?)");
            statement.setString(1, newIt.getUniqueId().toString());
            statement.setString(2, oldIt == null ? null : oldIt.getUniqueId().toString());

            statement.execute();
            statement.close();
        } catch (SQLException e) {
            plugin.getLogger().log(Level.WARNING, "Unable to update database with new entry in tag log.", e);
        }
    }

    public boolean getHudEnabled(OfflinePlayer player) {
        UUID uuid = player.getUniqueId();

        try (Connection connection = getConnection();
             PreparedStatement statement =
                     connection.prepareStatement("SELECT * FROM Preferences WHERE PlayerUUID = ?")) {
            statement.setString(1, uuid.toString());

            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getBoolean("EnableHUD");
            }

            resultSet.close();
        } catch (SQLException e) {
            plugin.getLogger().log(Level.WARNING, "Could not get whether HUD is enabled for " + uuid, e);
        }

        return false;
    }

    public void setHudEnabled(OfflinePlayer player, boolean enabled) {
        UUID uuid = player.getUniqueId();

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "REPLACE INTO Preferences (PlayerUUID, EnableHUD) VALUES (?, ?)")) {
            statement.setString(1, uuid.toString());
            statement.setBoolean(2, enabled);
            statement.executeUpdate();
        } catch (SQLException e) {
            plugin.getLogger().log(Level.WARNING, "Could not set whether HUD is enabled for " + uuid, e);
        }
    }
}
