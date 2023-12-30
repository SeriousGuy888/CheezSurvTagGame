package io.github.seriousguy888.cheezsurvtaggame;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.ComponentBuilder;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.projectiles.ProjectileSource;

public class TagEvents implements Listener {
  private final CheezSurvTagGame plugin;

  public TagEvents(CheezSurvTagGame plugin) {
    this.plugin = plugin;
  }

  @EventHandler
  public void onTag(EntityDamageByEntityEvent event) {
    if (!(event.getEntity() instanceof Player victim))
      return;

    Player damager;

    Entity damagingEntity = event.getDamager();
    if (damagingEntity instanceof Player) {
      // If the direct damaging entity was a player, designate that player as the damager.
      damager = (Player) damagingEntity;
    } else if (damagingEntity instanceof Projectile) {
      // If the direct damaging entity was a projectile, test if there was a player that shot
      // said projectile. If so, that player is the damager, and we will continue as if ranged
      // attack was any other attack.

      ProjectileSource shooter = ((Projectile) damagingEntity).getShooter();
      if (shooter instanceof Player) {
        damager = (Player) shooter;
      } else {
        return;
      }
    } else {
      return;
    }

    OfflinePlayer it = plugin.game.getIt();
    if (it == null)
      return;

    // check equality of uuids because the damager is a Player and the player who is it is an OfflinePlayer
    if (!damager.getUniqueId().equals(it.getUniqueId()))
      return;
    if (!Bukkit.getOnlinePlayers().contains(victim))
      return; // crude test to try to prevent tagging npcs


    long tagbackCooldownMs = 3000; // how long players must wait before tagging back the player who tagged them
    long msSinceLastTag = plugin.game.getMsSinceLastTag();
    double cooldownRemaining = (double) (tagbackCooldownMs - msSinceLastTag) / 1000;
    if (victim == plugin.game.getPreviousIt() && msSinceLastTag < tagbackCooldownMs) {
      damager.sendMessage(ChatColor.GRAY +
          "Wait " + cooldownRemaining + " seconds to tag back the player who tagged you.");
      return;
    }

    plugin.game.setIt(victim);
    plugin.game.setPreviousIt(damager);
    plugin.game.setLastTagTimestampToNow();

    TagStatsProfile damagerStats = plugin.game.getTagStats(damager);
    damagerStats.incrementTagsGiven();
    plugin.game.setTagStats(damager, damagerStats);

    TagStatsProfile victimStats = plugin.game.getTagStats(victim);
    victimStats.incrementTagsTaken();
    plugin.game.setTagStats(victim, victimStats);

    Bukkit.broadcastMessage(ChatColor.GRAY + damager.getName() + " tagged " + victim.getName());
    victim.spigot().sendMessage(
        ChatMessageType.ACTION_BAR,
        new ComponentBuilder("Tag! You're It!").create());
  }

  @EventHandler
  public void onJoin(PlayerJoinEvent event) {
    Player player = event.getPlayer();
    plugin.game.loadTagStats(player);

    OfflinePlayer it = plugin.game.getIt();
    if (it != null)
      player.sendMessage(ChatColor.GRAY + it.getName() + " is currently It.");
  }

  @EventHandler
  public void onQuit(PlayerQuitEvent event) {
    plugin.game.saveTagStats(event.getPlayer());
  }
}
