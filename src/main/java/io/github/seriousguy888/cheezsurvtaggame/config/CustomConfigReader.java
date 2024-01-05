package io.github.seriousguy888.cheezsurvtaggame.config;

import io.github.seriousguy888.cheezsurvtaggame.CheezSurvTagGame;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;

public abstract class CustomConfigReader {
    protected final CheezSurvTagGame plugin;
    private final File file;
    protected FileConfiguration config;

    public CustomConfigReader(CheezSurvTagGame plugin, String name) {
        this.plugin = plugin;
        this.file = new File(plugin.getDataFolder(), name + ".yml");

        loadFromDisk();
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    private void loadFromDisk() {
        if (!file.exists()) {
            file.getParentFile().mkdirs();

            // Whether the resource exists in the plugin jar file
            boolean resourceExists = plugin.getResource(file.getName()) != null;

            if (resourceExists) {
                // Copy the file from the jar file into the plugin's folder on the server if it doesn't exist
                plugin.saveResource(file.getName(), false);
            } else {
                plugin.getLogger().severe("Resource `" + file.getName() + "` does not exist. " +
                        "Cannot initialise custom config.");
                return;
            }
        }

        config = YamlConfiguration.loadConfiguration(file);
    }

    /**
     * Should be called when changes have been made to the rules from in game.
     * Will overwrite what is on disk.
     */
    public void saveToDisk() {
        try {
            config.save(file);
        } catch (IOException e) {
            plugin.getLogger().log(Level.SEVERE, "Unable to save config file `" + file.getName() + "`!", e);
        }
    }
}
