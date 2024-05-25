package io.github.seriousguy888.cheezsurvtaggame.config;

import io.github.seriousguy888.cheezsurvtaggame.CheezSurvTagGame;

public class MainConfig extends CustomConfigReader {
    public MainConfig(CheezSurvTagGame plugin, String name) {
        super(plugin, name, true);
    }

    public boolean getLogTagEventsToDb() {
        return config.getBoolean("log-tag-events-to-db", false);
    }

    public boolean getDiscordSrvIntegrationEnabled() {
        return config.getBoolean("discordsrv.enabled", false);
    }

    public String getDiscordSrvChannelName() {
        return config.getString("discordsrv.channel-name", "global");
    }

    public int getItReassignmentInterval() {
        return config.getInt("it-reassignment.interval-seconds", 0);
    }

    public int getMinPlayersOnlineToReassign() {
        return Math.max(config.getInt("it-reassignment.min-online-players", 1), 1);
    }
}
