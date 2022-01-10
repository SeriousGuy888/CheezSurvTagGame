package io.github.seriousguy888.cheezsurvtaggame.commands;

import io.github.seriousguy888.cheezsurvtaggame.CheezSurvTagGame;
import io.github.seriousguy888.cheezsurvtaggame.TagStatsProfile;
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
  public boolean onCommand(@Nonnull CommandSender sender,
                           @Nonnull Command command,
                           @Nonnull String label,
                           @Nonnull String[] args) {

//    if(args[0] == null)
//      return false;

    if(!(sender instanceof Player player)) {
      sender.sendMessage("no");
      return false;
    }

    TagStatsProfile statsProfile = plugin.game.getTagStats(player);
    player.sendMessage("tags given" + statsProfile.getTagsGiven());
    player.sendMessage("tags taken" + statsProfile.getTagsTaken());
    return true;
  }
}
