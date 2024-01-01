package io.github.seriousguy888.cheezsurvtaggame;

import io.github.seriousguy888.cheezsurvtaggame.commands.ItCommand;
import io.github.seriousguy888.cheezsurvtaggame.commands.StatsCommand;
import io.github.seriousguy888.cheezsurvtaggame.config.RulesetConfig;
import io.github.seriousguy888.cheezsurvtaggame.runnables.ChooseRandomIt;
import io.github.seriousguy888.cheezsurvtaggame.runnables.SaveGameData;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public final class CheezSurvTagGame extends JavaPlugin {
    private Game game;
    private RulesetConfig rulesetConfig;

    @Override
    public void onEnable() {
        rulesetConfig = new RulesetConfig(this, "rules.yml");

        game = new Game(this);
        game.loadTagStats();

        this.getServer().getPluginManager().registerEvents(new TagEvents(this), this);
        Objects.requireNonNull(this.getCommand("it")).setExecutor(new ItCommand(this));
        Objects.requireNonNull(this.getCommand("tagstats")).setExecutor(new StatsCommand(this));

        // save the tag game data to data.yml every 15 minutes
        new SaveGameData(this).runTaskTimer(this, 0, 15 * 60 * 20);
        new ChooseRandomIt(this).runTaskTimer(this, 0, 5 * 60 * 20);
    }

    @Override
    public void onDisable() {
        new SaveGameData(this).run();
    }

    public Game getGame() {
        return game;
    }

    public RulesetConfig getRulesetConfig() {
        return rulesetConfig;
    }
}
