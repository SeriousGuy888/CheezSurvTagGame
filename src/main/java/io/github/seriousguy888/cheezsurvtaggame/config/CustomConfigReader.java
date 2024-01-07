package io.github.seriousguy888.cheezsurvtaggame.config;

import io.github.seriousguy888.cheezsurvtaggame.CheezSurvTagGame;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.logging.Level;

public abstract class CustomConfigReader {
    protected final CheezSurvTagGame plugin;
    private final File file;
    protected FileConfiguration config;

    private final boolean mustRetainComments;

    public CustomConfigReader(CheezSurvTagGame plugin, String name, boolean mustRetainComments) {
        this.plugin = plugin;
        this.file = new File(plugin.getDataFolder(), name + ".yml");
        this.mustRetainComments = mustRetainComments;

        loadFromDisk();
    }

    public CustomConfigReader(CheezSurvTagGame plugin, String name) {
        this(plugin, name, false);
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    private void loadFromDisk() {
        InputStream defaultConfigStream = plugin.getResource(file.getName());

        if (!file.exists()) {
            file.getParentFile().mkdirs();

            if (defaultConfigStream != null) {
                // Copy the file from the jar file into the plugin's folder on the server if it doesn't exist
                plugin.saveResource(file.getName(), false);
            } else {
                plugin.getLogger().severe("Resource `" + file.getName() + "` does not exist. " +
                        "Cannot initialise custom config.");
                return;
            }
        }

        config = YamlConfiguration.loadConfiguration(file);

        if (defaultConfigStream != null) {
            // Write any keys present in the default config (the one included in the jar file)
            // but absent in the one currently on disk in the plugin folder.

            // Grab the default config included inside the jar file
            var defaultConfig = YamlConfiguration.loadConfiguration(new InputStreamReader(defaultConfigStream));

            // Copy default key values and comments to the keys that aren't set
            defaultConfig
                    .getKeys(true)
                    .stream()
                    .filter(key -> !config.contains(key))
                    .forEach(key -> config.set(key, defaultConfig.get(key)));

            if (mustRetainComments) {
                // Keep the comments at the top up to date
                config.options().setHeader(defaultConfig.options().getHeader());

                // Copy all the other comments as well from the default config
                config
                        .getKeys(true)
                        .forEach(key -> {
                            config.setComments(key, defaultConfig.getComments(key));
                            config.setInlineComments(key, defaultConfig.getInlineComments(key));
                        });
            }

            saveToDisk();
        }
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
