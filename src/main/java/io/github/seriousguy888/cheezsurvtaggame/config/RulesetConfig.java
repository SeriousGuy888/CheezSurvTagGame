package io.github.seriousguy888.cheezsurvtaggame.config;

import io.github.seriousguy888.cheezsurvtaggame.CheezSurvTagGame;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class RulesetConfig {
    private CheezSurvTagGame plugin;
    private FileConfiguration config;
    private Ruleset ruleset;

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public RulesetConfig(CheezSurvTagGame plugin, String fileName) {
        this.plugin = plugin;

        File file = new File(plugin.getDataFolder().toPath() + File.separator + fileName);
        if (!file.exists()) {
            try {
                file.getParentFile().mkdirs();
                file.createNewFile();
            } catch (IOException e) {
                plugin.getLogger().severe(e.getMessage());
            }
        }

        config = YamlConfiguration.loadConfiguration(file);
    }

    private Ruleset readRulesetFromConfig() {
        return new Ruleset(
                config.getBoolean("projectiles-can-tag", false),
                config.getBoolean("shields-can-block", false),
                config.getInt("tagback-cooldown-ms", 3000)
        );
    }

    private void writeRulesetToConfig(Ruleset newRuleset) {
        config.set("projectiles-can-tag", newRuleset.isProjectilesCanTag());
        config.set("shields-can-block", newRuleset.isShieldsCanBlock());
        config.set("tagback-cooldown-ms", newRuleset.getTagbackCooldownMs());
    }

    public Ruleset getRuleset() {
        return ruleset;
    }

    public void setRuleset(Ruleset ruleset) {
        this.ruleset = ruleset;
        writeRulesetToConfig(ruleset);
    }
}
