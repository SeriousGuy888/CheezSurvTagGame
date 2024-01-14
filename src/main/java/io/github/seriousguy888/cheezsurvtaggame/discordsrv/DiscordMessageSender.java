package io.github.seriousguy888.cheezsurvtaggame.discordsrv;

import github.scarsz.discordsrv.DiscordSRV;
import github.scarsz.discordsrv.dependencies.jda.api.entities.TextChannel;
import io.github.seriousguy888.cheezsurvtaggame.CheezSurvTagGame;

import javax.annotation.Nullable;

public class DiscordMessageSender {
    private final CheezSurvTagGame plugin;

    @Nullable
    private TextChannel textChannel;

    public DiscordMessageSender(CheezSurvTagGame plugin) {
        this.plugin = plugin;
        getTextChannel();
    }

    @Nullable
    private TextChannel getTextChannel() {
        if (textChannel != null) {
            return textChannel;
        }

        textChannel = DiscordSRV
                .getPlugin()
                .getDestinationTextChannelForGameChannelName(
                        plugin
                                .getMainConfig()
                                .getDiscordSrvChannelName()
                );

        if (textChannel == null) {
            plugin.getLogger().warning("Failed to get text channel from DiscordSRV.");
        }

        return textChannel;
    }

    public void sendMessage(String message) {
        TextChannel channel = getTextChannel();

        if (channel == null || !channel.canTalk()) {
            plugin.getLogger().warning("Failed to send message to Discord because it was not loaded " +
                    "or the bot does not have permission.");
            return;
        }

        channel.sendMessage(message).queue();
    }
}
