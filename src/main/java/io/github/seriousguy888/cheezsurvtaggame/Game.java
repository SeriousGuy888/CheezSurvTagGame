package io.github.seriousguy888.cheezsurvtaggame;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.UUID;

public class Game {
  private CheezSurvTagGame plugin;

  private final File file;
  private final FileConfiguration gameDataStorage;

  private OfflinePlayer it; // the player who is currently it

  private OfflinePlayer previousIt; // who to apply the tagback cooldown on
  private long lastTagTimestamp; // used for calculating the tagback cooldown


  @SuppressWarnings("ResultOfMethodCallIgnored")
  public Game(CheezSurvTagGame plugin) {
    this.plugin = plugin;

    file = new File(plugin.getDataFolder() + File.separator + "data.yml");
    plugin.getLogger().info(file.getAbsolutePath());
    if(!file.exists()) {
      try {
        file.getParentFile().mkdirs();
        file.createNewFile();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
    gameDataStorage = YamlConfiguration.loadConfiguration(file);

    loadState();
  }

  public Player pickRandomIt() {
    Collection<? extends Player> onlinePlayers = Bukkit.getOnlinePlayers();

    // pick a random player from those online, or null if none are found
    return onlinePlayers
        .stream()
        .skip((int) (onlinePlayers.size() * Math.random()))
        .findFirst()
        .orElse(null);
  }

  public void loadState() {
    String itUuidString = gameDataStorage.getString("it");
    if(itUuidString == null) {
      setIt(pickRandomIt());
      return;
    }

    UUID itUuid = UUID.fromString(itUuidString);
    setIt(Bukkit.getOfflinePlayer(itUuid));
  }

  public void saveState() {
    gameDataStorage.set("it", getIt().getUniqueId().toString());

    try {
      gameDataStorage.save(file);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }


  public long getMsSinceLastTag() {
    return System.currentTimeMillis() - lastTagTimestamp;
  }
  public void setLastTagTimestampToNow() {
    lastTagTimestamp = System.currentTimeMillis();
  }

  public OfflinePlayer getIt() {
    return it;
  }
  public void setIt(OfflinePlayer it) {
    this.it = it;
  }

  public OfflinePlayer getPreviousIt() {
    return previousIt;
  }
  public void setPreviousIt(OfflinePlayer previousIt) {
    this.previousIt = previousIt;
  }
}