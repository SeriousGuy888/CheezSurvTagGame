package io.github.seriousguy888.cheezsurvtaggame.discordsrv;

import github.scarsz.discordsrv.DiscordSRV;
import github.scarsz.discordsrv.dependencies.jda.api.entities.TextChannel;
import io.github.seriousguy888.cheezsurvtaggame.CheezSurvTagGame;

public class DiscordMessageSender {
    private final CheezSurvTagGame plugin;

    private final TextChannel textChannel;

    public DiscordMessageSender(CheezSurvTagGame plugin) {
        this.plugin = plugin;

        String channelName = plugin.getMainConfig().getDiscordSrvChannelName();
        textChannel = DiscordSRV.getPlugin().getDestinationTextChannelForGameChannelName(channelName);

        if (textChannel == null) {
            plugin.getLogger().warning(
                    "Configured DiscordSRV channel name in `config.yml` is not a valid channel.");
        }
    }

    public void sendMessage(String message) {
        if (!textChannel.canTalk()) {
            plugin.getLogger().warning(
                    "Could not send message to text channel in Discord (DiscordSRV integration) because " +
                            "the bot does not have permission.");
            return;
        }

        textChannel.sendMessage(message).queue();
    }
}
