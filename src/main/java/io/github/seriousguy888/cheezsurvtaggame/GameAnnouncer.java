package io.github.seriousguy888.cheezsurvtaggame;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

public class GameAnnouncer {
    private final CheezSurvTagGame plugin;

    public GameAnnouncer(CheezSurvTagGame plugin) {
        this.plugin = plugin;
    }

    private void sendToChatAndDiscord(String message) {
        Bukkit.broadcastMessage(ChatColor.GRAY + message);

        if (plugin.getDiscordMessageSender() != null) {
            plugin.getDiscordMessageSender().sendMessage("*" + message + "*");
        }
    }


    public void announceTag(String newItName, String oldItName) {
        sendToChatAndDiscord(oldItName + " tagged " + newItName);
    }

    public void announceRandomlyChosenIt(String newItName) {
        sendToChatAndDiscord(
                newItName + " has been randomly chosen to be It as the previous It player is not currently online.");
    }

    public void announceManuallyChosenIt(String newItName, String executingAdminName) {
        sendToChatAndDiscord(newItName + " is now It. (Manually set by " + executingAdminName + ")");
    }
}
