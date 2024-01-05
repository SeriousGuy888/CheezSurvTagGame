package io.github.seriousguy888.cheezsurvtaggame.commands;

import io.github.seriousguy888.cheezsurvtaggame.CheezSurvTagGame;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;

import javax.annotation.Nonnull;
import java.util.List;

public class RulesCommand implements TabExecutor {

    private final CheezSurvTagGame plugin;

    private final String adminPerm = "survtag.rules.modify";
    private final List<String> validOptions = List.of(
            "projectiles_can_tag",
            "shields_can_block",
            "tagback_cooldown_milliseconds"
    );

    public RulesCommand(CheezSurvTagGame plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@Nonnull CommandSender sender,
                             @Nonnull Command command,
                             @Nonnull String label,
                             @Nonnull String[] args) {

        if (args.length == 0) {

            sender.sendMessage(
                    "",
                    "" + ChatColor.BLUE + ChatColor.BOLD + "Currently Active Rules for Tag",
                    ChatColor.DARK_AQUA + "Projectiles Can Tag: " + ChatColor.AQUA +
                            (plugin.getRules().getProjectilesCanTag() ? "Yes" : "No"),
                    ChatColor.DARK_AQUA + "Shields Can Block Tag: " + ChatColor.AQUA +
                            (plugin.getRules().getShieldsCanBlock() ? "Yes" : "No"),
                    ChatColor.DARK_AQUA + "Tagback Cooldown: " + ChatColor.AQUA +
                            (plugin.getRules().getTagbackCooldownMs() / 1000.0) + "s",
                    ""
            );

            return false;
        }

        if (!sender.hasPermission(adminPerm)) {
            sender.sendMessage(ChatColor.RED + "Insufficient permissions to modify rules.");
            return true;
        }

        if (args.length < 2) {
            sender.sendMessage("/tagrules <option> <value>");
            return true;
        }

        String optionSelected = args[0].toLowerCase();
        String valueProvided = args[1];

        if (!validOptions.contains(optionSelected)) {
            sender.sendMessage(ChatColor.RED + "Valid options: " + String.join(", ", validOptions));
            return true;
        }


        String valStr = "_";
        switch (optionSelected) {
            case "projectiles_can_tag", "shields_can_block" -> {

                boolean value;
                if (valueProvided.equalsIgnoreCase("true"))
                    value = true;
                else if (valueProvided.equalsIgnoreCase("false"))
                    value = false;
                else {
                    sender.sendMessage(ChatColor.RED + "Specify true or false.");
                    return true;
                }

                switch (optionSelected) {
                    case "projectiles_can_tag" -> plugin.getRules().setProjectilesCanTag(value);
                    case "shields_can_block" -> plugin.getRules().setShieldsCanBlock(value);
                }

                valStr = "" + value;
            }
            case "tagback_cooldown_milliseconds" -> {
                int value;
                try {
                    value = Integer.parseInt(valueProvided);
                } catch (NumberFormatException e) {
                    sender.sendMessage(ChatColor.RED + "Specify a valid whole number of milliseconds.");
                    return true;
                }

                if (value < 0) {
                    sender.sendMessage(ChatColor.RED + "Value must be >= 0.");
                    return true;
                }

                plugin.getRules().setTagbackCooldownMs(value);
                valStr = "" + value;
            }
        }

        plugin.getServer().broadcastMessage(ChatColor.DARK_AQUA + sender.getName() +
                ChatColor.DARK_AQUA + " has set the rule " +
                ChatColor.AQUA + optionSelected +
                ChatColor.DARK_AQUA + " to " +
                ChatColor.AQUA + valStr +
                ChatColor.DARK_AQUA + ". Check rules with /tagrules.");
        plugin.getRules().saveToDisk();

        return false;
    }

    @Override
    public List<String> onTabComplete(@Nonnull CommandSender sender,
                                      @Nonnull Command command,
                                      @Nonnull String label,
                                      @Nonnull String[] args) {
        if (!sender.hasPermission(adminPerm)) {
            return List.of();
        }

        // will rewrite this garbage to be cleaner if i add more subcommands in the future
        if (args.length == 1) {
            return validOptions.stream()
                    .filter(e -> e.startsWith(args[0].toLowerCase()))
                    .toList();
        } else if (args.length == 2) {
            return switch (args[0].toLowerCase()) {
                case "projectiles_can_tag", "shields_can_block" -> List.of("true", "false");
                case "tagback_cooldown_milliseconds" -> List.of("0", "1000", "2000", "3000", "4000", "5000");
                default -> List.of();
            };
        }

        return List.of();
    }
}
