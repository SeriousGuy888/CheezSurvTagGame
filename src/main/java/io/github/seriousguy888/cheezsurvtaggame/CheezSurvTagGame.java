package io.github.seriousguy888.cheezsurvtaggame;

import io.github.seriousguy888.cheezsurvtaggame.commands.HudCommand;
import io.github.seriousguy888.cheezsurvtaggame.commands.ItCommand;
import io.github.seriousguy888.cheezsurvtaggame.commands.RulesCommand;
import io.github.seriousguy888.cheezsurvtaggame.commands.StatsCommand;
import io.github.seriousguy888.cheezsurvtaggame.config.RulesConfig;
import io.github.seriousguy888.cheezsurvtaggame.database.Database;
import io.github.seriousguy888.cheezsurvtaggame.runnables.ChooseRandomIt;
import io.github.seriousguy888.cheezsurvtaggame.runnables.SaveGameData;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.TabCompleter;
import org.bukkit.plugin.java.JavaPlugin;

public final class CheezSurvTagGame extends JavaPlugin {
    private Game game;
    private Database database;
    private RulesConfig rules;
    private TagHudManager tagHudManager;

    @Override
    public void onEnable() {
        database = new Database(this, "TagGame");
        database.init();

        rules = new RulesConfig(this, "rules");
        tagHudManager = new TagHudManager(this);

        game = new Game(this);
        game.loadTagStats();

        tagHudManager.setHudEnabledFromDatabaseForAllPlayers();

        getServer().getPluginManager().registerEvents(new TagEvents(this), this);
        getServer().getPluginManager().registerEvents(tagHudManager, this);

        registerCommand("taghud", new HudCommand(this));
        registerCommand("it", new ItCommand(this));
        registerCommand("tagstats", new StatsCommand(this));
        registerCommand("tagrules", new RulesCommand(this));

        new SaveGameData(this).runTaskTimer(this, 0, 15 * 60 * 20);
        new ChooseRandomIt(this).runTaskTimer(this, 0, 5 * 60 * 20);
    }

    @Override
    public void onDisable() {
        new SaveGameData(this).run();

        tagHudManager.stopDisplayingBossbars();
    }

    private void registerCommand(String commandName, CommandExecutor executor) {
        PluginCommand command = getCommand(commandName);

        if (command == null) {
            getLogger().severe("Failed to register command /" + commandName);
            return;
        }

        command.setExecutor(executor);

        if (executor instanceof TabCompleter) {
            command.setTabCompleter((TabCompleter) executor);
        }
    }

    public Game getGame() {
        return game;
    }

    public Database getDatabase() {
        return database;
    }

    public RulesConfig getRules() {
        return rules;
    }

    public TagHudManager getTagHudManager() {
        return tagHudManager;
    }
}
