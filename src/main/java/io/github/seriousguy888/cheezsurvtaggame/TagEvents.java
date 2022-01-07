package io.github.seriousguy888.cheezsurvtaggame;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.ComponentBuilder;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerJoinEvent;

public class TagEvents implements Listener {
  private final CheezSurvTagGame plugin;
  public TagEvents(CheezSurvTagGame plugin) {
    this.plugin = plugin;
  }

  @EventHandler
  public void onTag(EntityDamageByEntityEvent event) {
    if(!(event.getEntity() instanceof Player victim))
      return;
    if(!(event.getDamager() instanceof Player damager))
      return;

    if(damager != plugin.game.getIt())
      return;
    if(!Bukkit.getOnlinePlayers().contains(victim))
      return; // crude test to try to prevent tagging npcs


    long tagbackCooldownMs = 3000; // how long players must wait before tagging back the player who tagged them
    long msSinceLastTag = plugin.game.getMsSinceLastTag();
    double cooldownRemaining = (double) (tagbackCooldownMs - msSinceLastTag) / 1000;
    if(victim == plugin.game.getPreviousIt() && msSinceLastTag < tagbackCooldownMs) {
      damager.sendMessage(ChatColor.GRAY +
          "Wait " + cooldownRemaining + " seconds to tag back the player who tagged you.");
      return;
    }

    plugin.game.setIt(victim);
    plugin.game.setPreviousIt(damager);
    plugin.game.setLastTagTimestampToNow();

    Bukkit.broadcastMessage(ChatColor.GRAY + damager.getName() + " tagged " + victim.getName());
    victim.spigot().sendMessage(
        ChatMessageType.ACTION_BAR,
        new ComponentBuilder("Tag! You're It!").create());
  }

  @EventHandler
  public void onJoin(PlayerJoinEvent event) {
    OfflinePlayer it = plugin.game.getIt();
    event.getPlayer().sendMessage(ChatColor.GRAY + it.getName() + " is currently It.");
  }
}
