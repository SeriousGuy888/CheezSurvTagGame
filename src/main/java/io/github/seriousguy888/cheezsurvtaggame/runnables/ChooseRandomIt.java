package io.github.seriousguy888.cheezsurvtaggame.runnables;

import io.github.seriousguy888.cheezsurvtaggame.CheezSurvTagGame;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class ChooseRandomIt extends BukkitRunnable {
  private final CheezSurvTagGame plugin;
  public ChooseRandomIt(CheezSurvTagGame plugin) {
    this.plugin = plugin;
  }

  @Override
  public void run() {
    OfflinePlayer currentIt = plugin.game.getIt();
    if(currentIt != null && currentIt.isOnline())
      return;

    Player newIt = plugin.game.pickRandomIt();
    if(newIt != null) {
      Bukkit.broadcastMessage(ChatColor.GRAY + newIt.getName() +
          " has been randomly chosen to be It as the previous It player is not currently online.");
    }
  }
}
