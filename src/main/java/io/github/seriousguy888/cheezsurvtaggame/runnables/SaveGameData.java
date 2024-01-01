package io.github.seriousguy888.cheezsurvtaggame.runnables;

import io.github.seriousguy888.cheezsurvtaggame.CheezSurvTagGame;
import org.bukkit.scheduler.BukkitRunnable;

public class SaveGameData extends BukkitRunnable {
  private final CheezSurvTagGame plugin;
  public SaveGameData(CheezSurvTagGame plugin) {
    this.plugin = plugin;
  }

  @Override
  public void run() {
    plugin.getGame().saveState();
    plugin.getGame().saveTagStats();
  }
}
