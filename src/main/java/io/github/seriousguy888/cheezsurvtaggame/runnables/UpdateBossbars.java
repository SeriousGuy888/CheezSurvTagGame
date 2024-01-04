package io.github.seriousguy888.cheezsurvtaggame.runnables;

import io.github.seriousguy888.cheezsurvtaggame.CheezSurvTagGame;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.boss.BossBar;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.UUID;

public class UpdateBossbars extends BukkitRunnable {

    private final CheezSurvTagGame plugin;
    private final BossBar itBar;
    private final BossBar notItBar;

    public UpdateBossbars(CheezSurvTagGame plugin, BossBar itBar, BossBar notItBar) {
        this.plugin = plugin;
        this.itBar = itBar;
        this.notItBar = notItBar;
    }

    @Override
    public void run() {
        double progress = Math.max(0, Math.min(1,
                (double) plugin.getGame().getTimeSinceLastTagMs() / plugin.getGame().getTagbackCooldownMs()));
        itBar.setProgress(progress);
        notItBar.setProgress(progress);

        plugin.getTagHudManager().updateBossbarTitles();
    }

    @Override
    public void cancel() {
        itBar.setVisible(false);
        notItBar.setVisible(false);

        // This should only really be called when the plugin is being disabled.
        // That means this class, and references to the bossbars, will be destroyed and garbage collected.
        // Or so I'm told anyways. I don't think there's a way to delete these bossbars manually.

        super.cancel();
    }
}
