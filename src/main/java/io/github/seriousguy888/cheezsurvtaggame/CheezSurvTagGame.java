package io.github.seriousguy888.cheezsurvtaggame;

import io.github.seriousguy888.cheezsurvtaggame.commands.*;
import io.github.seriousguy888.cheezsurvtaggame.config.MainConfig;
import io.github.seriousguy888.cheezsurvtaggame.config.RulesConfig;
import io.github.seriousguy888.cheezsurvtaggame.database.Database;
import io.github.seriousguy888.cheezsurvtaggame.discordsrv.DiscordMessageSender;
import io.github.seriousguy888.cheezsurvtaggame.runnables.ChooseRandomIt;
import io.github.seriousguy888.cheezsurvtaggame.runnables.SaveGameData;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.TabCompleter;
import org.bukkit.plugin.java.JavaPlugin;

import javax.annotation.Nullable;

public final class CheezSurvTagGame extends JavaPlugin {
    private Game game;

    private MainConfig mainConfig;
    private RulesConfig rules;

    private Database database;
    private TagHudManager tagHudManager;

    private DiscordMessageSender discordMessageSender;

    @Override
    public void onEnable() {
        mainConfig = new MainConfig(this, "config");
        rules = new RulesConfig(this, "rules");

        reloadConfigs();

        database = new Database(this, "TagGame");
        database.init();

        tagHudManager = new TagHudManager(this);

        game = new Game(this);
        game.loadTagStats();

        tagHudManager.setHudEnabledFromDatabaseForAllPlayers();

        getServer().getPluginManager().registerEvents(new TagEvents(this), this);
        getServer().getPluginManager().registerEvents(tagHudManager, this);

        registerCommand("survtag", new SurvTagCommand(this));
        registerCommand("taghud", new HudCommand(this));
        registerCommand("it", new ItCommand(this));
        registerCommand("tagstats", new StatsCommand(this));
        registerCommand("tagrules", new RulesCommand(this));

        new SaveGameData(this).runTaskTimer(this, 0, 15 * 60 * 20);

        int reassignmentInterval = getMainConfig().getItReassignmentInterval();
        if (reassignmentInterval > 0) {
            new ChooseRandomIt(this).runTaskTimer(this, 0, reassignmentInterval * 20L);
        }
    }

    @Override
    public void onDisable() {
        new SaveGameData(this).run();

        tagHudManager.stopDisplayingBossbars();
    }

    public void reloadConfigs() {
        mainConfig.loadFromDisk();
        rules.loadFromDisk();

        if (mainConfig.getDiscordSrvIntegrationEnabled() && isDiscordSrvAvailable()) {
            getLogger().info("Successfully enabled DiscordSRV integration.");
            discordMessageSender = new DiscordMessageSender(this);
        }
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

    public boolean isDiscordSrvAvailable() {
        return getServer().getPluginManager().isPluginEnabled("DiscordSRV");
    }

    public Game getGame() {
        return game;
    }

    public MainConfig getMainConfig() {
        return mainConfig;
    }

    public RulesConfig getRules() {
        return rules;
    }

    public Database getDatabase() {
        return database;
    }

    public TagHudManager getTagHudManager() {
        return tagHudManager;
    }

    @Nullable
    public DiscordMessageSender getDiscordMessageSender() {
        return discordMessageSender;
    }
}
