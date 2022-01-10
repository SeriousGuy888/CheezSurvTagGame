package io.github.seriousguy888.cheezsurvtaggame.commands;

import io.github.seriousguy888.cheezsurvtaggame.CheezSurvTagGame;
import io.github.seriousguy888.cheezsurvtaggame.TagStatsProfile;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;

public class StatsCommand implements CommandExecutor {
  private final CheezSurvTagGame plugin;

  public StatsCommand(CheezSurvTagGame plugin) {
    this.plugin = plugin;
  }

  @Override
  @SuppressWarnings("deprecation")
  public boolean onCommand(@Nonnull CommandSender sender,
                           @Nonnull Command command,
                           @Nonnull String label,
                           @Nonnull String[] args) {

    OfflinePlayer lookupPlayer;
    if(args.length == 0) { // if no args supplied, default to the sender if the sender is not the console
      if(sender instanceof Player)
        lookupPlayer = (OfflinePlayer) sender;
      else {
        sender.sendMessage(ChatColor.RED + "Provide a player to look up the stats of.");
        return true;
      }
    } else {
      // getting an offline player by their username is deprecated, but apparently it is not
      // going to be removed any time soon. at least, I sure hope it doesn't!
      lookupPlayer = Bukkit.getOfflinePlayer(args[0]);
    }

    if(!lookupPlayer.hasPlayedBefore()) {
      sender.sendMessage(ChatColor.RED + "This player has not joined the server before.");
      return true;
    }


    TagStatsProfile statsProfile = plugin.game.getTagStats(lookupPlayer);
    sender.sendMessage("\n" + ChatColor.BLUE + ChatColor.BOLD +
        lookupPlayer.getName() + "'s CheezSurv Tag Stats" +
        ChatColor.DARK_AQUA + "\nTimes Tagged by Other Players: " + ChatColor.AQUA +
        statsProfile.getTagsGiven() +
        ChatColor.DARK_AQUA + "\nTimes Having Tagged Another Player: " + ChatColor.AQUA +
        statsProfile.getTagsTaken());
    return true;
  }
}
