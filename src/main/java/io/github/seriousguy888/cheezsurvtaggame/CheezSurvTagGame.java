package io.github.seriousguy888.cheezsurvtaggame;

import io.github.seriousguy888.cheezsurvtaggame.commands.ItCommand;
import io.github.seriousguy888.cheezsurvtaggame.commands.StatsCommand;
import io.github.seriousguy888.cheezsurvtaggame.config.RulesetConfig;
import io.github.seriousguy888.cheezsurvtaggame.runnables.ChooseRandomIt;
import io.github.seriousguy888.cheezsurvtaggame.runnables.SaveGameData;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.TabCompleter;
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

        registerCommand("it", new ItCommand(this));
        registerCommand("tagstats", new StatsCommand(this));

        // save the tag game data to data.yml every 15 minutes
        new SaveGameData(this).runTaskTimer(this, 0, 15 * 60 * 20);
        new ChooseRandomIt(this).runTaskTimer(this, 0, 5 * 60 * 20);
    }

    @Override
    public void onDisable() {
        new SaveGameData(this).run();
        rulesetConfig.writeRulesetToConfig();
    }

    private void registerCommand(String commandName, CommandExecutor executor) {
        PluginCommand command = getCommand(commandName);

        if(command == null) {
            getLogger().severe("Failed to register command /" + commandName);
            return;
        }

        command.setExecutor(executor);

        if(executor instanceof TabCompleter) {
            command.setTabCompleter((TabCompleter) executor);
        }
    }

    public Game getGame() {
        return game;
    }

    public RulesetConfig getRulesetConfig() {
        return rulesetConfig;
    }
}
