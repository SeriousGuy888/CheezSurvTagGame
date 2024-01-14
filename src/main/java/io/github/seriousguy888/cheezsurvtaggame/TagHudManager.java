package io.github.seriousguy888.cheezsurvtaggame;

import io.github.seriousguy888.cheezsurvtaggame.runnables.UpdateBossbars;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class TagHudManager implements Listener {

    private final CheezSurvTagGame plugin;
    private final Set<Player> hudEnabledPlayers; // temporary solution

    private final BossBar itBar;
    private final BossBar notItBar;
    private final UpdateBossbars displayHudRunnable;


    public TagHudManager(CheezSurvTagGame plugin) {
        this.plugin = plugin;
        hudEnabledPlayers = new HashSet<>();

        itBar = plugin.getServer().createBossBar("...", BarColor.RED, BarStyle.SOLID);
        notItBar = plugin.getServer().createBossBar("...", BarColor.BLUE, BarStyle.SOLID);

        displayHudRunnable = new UpdateBossbars(plugin, itBar, notItBar);
        displayHudRunnable.runTaskTimer(plugin, 0, 2);

        updateBossbarTitles();
    }

    public void stopDisplayingBossbars() {
        displayHudRunnable.cancel();
    }

    public void updateBossbarPlayers(OfflinePlayer... players) {
        Game game = plugin.getGame();
        if (game == null) {
            return;
        }

        UUID itUuid = game.getIt().getUniqueId();

        for (OfflinePlayer offlinePlayer : players) {
            // ugly way of converting from OfflinePlayer to Player
            Player player = Bukkit.getPlayer(offlinePlayer.getUniqueId());
            if(player == null) {
                continue;
            }

            if (isHudEnabled(player)) {
                if (player.getUniqueId().equals(itUuid)) {
                    itBar.addPlayer(player);
                    notItBar.removePlayer(player);
                } else {
                    notItBar.addPlayer(player);
                    itBar.removePlayer(player);
                }
            } else {
                itBar.removePlayer(player);
                notItBar.removePlayer(player);
            }
        }
    }

    public void updateBossbarTitles() {
        Game game = plugin.getGame();
        if (game == null) {
            return;
        }

        if(Bukkit.getOnlinePlayers().size() == 1) {
            String bossbarTitle = "Nobody else online. \"/taghud\" to toggle It Indicator.";
            itBar.setTitle(ChatColor.RED + bossbarTitle);
            notItBar.setTitle(ChatColor.AQUA + bossbarTitle);
            return;
        }

        long cooldown = game.getTagbackCooldownRemainingMs();
        boolean cooldownActive = cooldown > 0;

        if (cooldownActive) {
            // ms to sec, 1 decimal place of precision
            double cooldownSec = (Math.round((cooldown / 1000.0) * 10) / 10.0);

            itBar.setTitle(ChatColor.RED + "Tagback Cooldown: " + cooldownSec + "s");
            notItBar.setTitle(ChatColor.AQUA + game.getIt().getName() + " | Tagback: " + cooldownSec + "s");
        } else {
            itBar.setTitle(ChatColor.RED + "You are it!");
            notItBar.setTitle(ChatColor.AQUA + game.getIt().getName() + " is it.");
        }
    }

    public void setHudEnabled(Player player, boolean shouldEnable) {
        if (shouldEnable) {
            hudEnabledPlayers.add(player);
        } else {
            hudEnabledPlayers.remove(player);
        }

        updateBossbarPlayers(player);
    }

    public boolean isHudEnabled(Player player) {
        return hudEnabledPlayers.contains(player);
    }

    public void setHudEnabledFromDatabaseForAllPlayers() {
        Bukkit.getOnlinePlayers().forEach(this::setHudEnabledFromDatabase);
    }

    private void setHudEnabledFromDatabase(Player player) {
        boolean shouldEnableHud = plugin.getDatabase().getHudEnabled(player);
        setHudEnabled(player, shouldEnableHud);
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        setHudEnabledFromDatabase(event.getPlayer());
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        hudEnabledPlayers.remove(event.getPlayer());
    }
}
