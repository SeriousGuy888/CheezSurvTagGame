package io.github.seriousguy888.cheezsurvtaggame.commands;

import io.github.seriousguy888.cheezsurvtaggame.CheezSurvTagGame;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;

public class HudCommand implements CommandExecutor {

    private final CheezSurvTagGame plugin;

    public HudCommand(CheezSurvTagGame plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@Nonnull CommandSender sender,
                             @Nonnull Command command,
                             @Nonnull String label,
                             @Nonnull String[] args) {

        Player target = (sender instanceof Player) ? (Player) sender : null;
        boolean isForcingOther = false;
        if (args.length >= 1) {
            if (sender.hasPermission("survtag.hud.others")) {
                target = Bukkit.getPlayer(args[0]);
                isForcingOther = true;
            } else {
                sender.sendMessage(ChatColor.RED + "Insufficient permissions to toggle HUD for another player.");
                return true;
            }
        }

        if (target == null) {
            sender.sendMessage(ChatColor.RED + "The console must specify a target player to toggle the HUD for.");
            return true;
        }

        boolean alreadyEnabled = plugin.getTagHudManager().isHudEnabled(target);
        plugin.getTagHudManager().setHudEnabled(target, !alreadyEnabled);
        plugin.getDatabase().setHudEnabled(target, !alreadyEnabled);

        sender.sendMessage(ChatColor.GRAY + (alreadyEnabled ? "Disabled" : "Enabled") + " the Tag HUD" +
                (isForcingOther ? " for " + target.getName() : "") + ".");

        if (isForcingOther && !target.equals(sender)) {
            target.sendMessage(ChatColor.GRAY + sender.getName() + " has " +
                    (alreadyEnabled ? "disabled" : "enabled") + " the Tag HUD for you (/taghud).");
        }

        return false;
    }
}
