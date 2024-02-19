package io.github.seriousguy888.cheezsurvtaggame.commands;

import io.github.seriousguy888.cheezsurvtaggame.CheezSurvTagGame;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class SurvTagCommand implements TabExecutor {
    private final CheezSurvTagGame plugin;

    public SurvTagCommand(CheezSurvTagGame plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender,
                             @NotNull Command command,
                             @NotNull String label,
                             @NotNull String[] args) {

        if(!sender.hasPermission("survtag.admin")) {
            sender.sendMessage(ChatColor.RED + "Insufficient permission.");
            return false;
        }

        if(args.length == 0 || !args[0].equalsIgnoreCase("reload")) {
            sender.sendMessage(ChatColor.RED + "Correct usage: /survtag reload");
            return true;
        }

        sender.sendMessage(ChatColor.AQUA + "Reloading SurvTag configurations...");
        plugin.reloadConfigs();
        sender.sendMessage(ChatColor.AQUA + "Successfully reloaded configurations from disk.");

        return false;
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender,
                                      @NotNull Command command,
                                      @NotNull String label,
                                      @NotNull String[] args) {
        if(args.length == 1) {
            return List.of("reload");
        }

        return null;
    }
}
