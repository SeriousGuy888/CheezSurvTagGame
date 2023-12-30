package io.github.seriousguy888.cheezsurvtaggame;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.UUID;

public class Game {
  private final File file;
  private final FileConfiguration gameDataStorage;

  private HashMap<OfflinePlayer, TagStatsProfile> playerTagStats;

  private OfflinePlayer it; // the player who is currently it
  private OfflinePlayer previousIt; // who to apply the tagback cooldown on
  private long lastTagTimestamp; // used for calculating the tagback cooldown


  @SuppressWarnings("ResultOfMethodCallIgnored")
  public Game(CheezSurvTagGame plugin) {
//    this.plugin = plugin;
    this.playerTagStats = new HashMap<>();

    file = new File(plugin.getDataFolder() + File.separator + "data.yml");
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
    Player newIt = onlinePlayers
        .stream()
        .skip((int) (onlinePlayers.size() * Math.random()))
        .findFirst()
        .orElse(null);

    if(newIt != null)
      setIt(newIt);
    return newIt;
  }

  private void loadState() {
    String itUuidString = gameDataStorage.getString("it");
    if(itUuidString == null) {
      pickRandomIt();
      return;
    }

    UUID itUuid = UUID.fromString(itUuidString);
    setIt(Bukkit.getOfflinePlayer(itUuid));
  }

  public void saveState() {
    OfflinePlayer it = getIt();
    if(it == null) {
      Bukkit.getLogger().warning("Current It was not saved as no player is It.");
      return;
    }
    gameDataStorage.set("it", it.getUniqueId().toString());

    try {
      gameDataStorage.save(file);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }


  public void loadTagStats() { // if no argument is supplied, load the stats for all players currently online
    Bukkit.getOnlinePlayers().forEach(this::loadTagStats);
  }
  public void loadTagStats(OfflinePlayer player) {
    String uuid = player.getUniqueId().toString();

    int tagsTaken = gameDataStorage.getInt("players." + uuid + ".stats.taken");
    int tagsGiven = gameDataStorage.getInt("players." + uuid + ".stats.given");
    playerTagStats.put(player, new TagStatsProfile(tagsTaken, tagsGiven));
  }

  public void saveTagStats() {
    // if no args are supplied, just write all the profiles loaded to the data.yml file
    playerTagStats.forEach((player, _tagStatsProfile) -> saveTagStats(player));
  }
  public void saveTagStats(OfflinePlayer player) {
    if(!playerTagStats.containsKey(player))
      return;

    String uuid = player.getUniqueId().toString();
    TagStatsProfile statsProfile = getTagStats(player);
    gameDataStorage.set("players." + uuid + ".stats.taken", statsProfile.getTagsTaken());
    gameDataStorage.set("players." + uuid + ".stats.given", statsProfile.getTagsGiven());

    try {
      gameDataStorage.save(file);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public TagStatsProfile getTagStats(OfflinePlayer player) {
    if(!playerTagStats.containsKey(player))
      loadTagStats(player);
    return playerTagStats.get(player);
  }

  public void setTagStats(OfflinePlayer player, TagStatsProfile newProfile) {
    playerTagStats.put(player, newProfile);
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