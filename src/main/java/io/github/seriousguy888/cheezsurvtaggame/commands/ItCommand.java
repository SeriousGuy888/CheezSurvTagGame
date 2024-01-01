package io.github.seriousguy888.cheezsurvtaggame.commands;

import io.github.seriousguy888.cheezsurvtaggame.CheezSurvTagGame;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;

public class ItCommand implements CommandExecutor { //  implements TabExecutor
  private final CheezSurvTagGame plugin;

  public ItCommand(CheezSurvTagGame plugin) {
    this.plugin = plugin;
  }

  @Override
  public boolean onCommand(@Nonnull CommandSender sender,
                           @Nonnull Command command,
                           @Nonnull String label,
                           @Nonnull String[] args) {
    if(args.length == 0) {
      OfflinePlayer it = plugin.getGame().getIt();
      if(it == null)
        sender.sendMessage(ChatColor.GRAY + "Nobody is It right now.");
      else
        sender.sendMessage(ChatColor.GRAY + it.getName() + " is currently It.");
      return true;
    }

    if(!sender.hasPermission("tag_game.set_it")) {
      sender.sendMessage(ChatColor.RED +
          "Insufficient permission to set who is it. Run the command without arguments to see who is it.");
      return true;
    }
    if(args[0] == null) {
      sender.sendMessage(ChatColor.RED + "Specify a player to make It.");
      return true;
    }

    Player player = Bukkit.getPlayer(args[0]);
    if(player == null) {
      sender.sendMessage(ChatColor.RED + "Player not found.");
      return true;
    }

    plugin.getGame().setIt(player);
    Bukkit.broadcastMessage(ChatColor.GRAY +
        player.getName() + " is now It. (Manually set by " + sender.getName() + ")");
    return true;
  }

//  @Override
//  public List<String> onTabComplete(@Nonnull CommandSender sender,
//                                    @Nonnull Command command,
//                                    @Nonnull String alias,
//                                    @Nonnull String[] args) {
//    List<String> results = new ArrayList<>();
//    if(args.length == 1) {
//      results.add("set_it");
//    }
//
//    return results;
//  }
}
