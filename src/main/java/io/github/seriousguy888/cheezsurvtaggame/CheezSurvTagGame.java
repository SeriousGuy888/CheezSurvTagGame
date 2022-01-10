package io.github.seriousguy888.cheezsurvtaggame;

import io.github.seriousguy888.cheezsurvtaggame.commands.ItCommand;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public final class CheezSurvTagGame extends JavaPlugin {
  public Game game;

  @Override
  public void onEnable() {
    this.game = new Game(this);

    this.getServer().getPluginManager().registerEvents(new TagEvents(this), this);
    Objects.requireNonNull(this.getCommand("it")).setExecutor(new ItCommand(this));
  }

  @Override
  public void onDisable() {
    game.saveState();
  }
}
