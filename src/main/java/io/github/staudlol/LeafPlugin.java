package io.github.staudlol;

import io.github.nosequel.menu.MenuHandler;
import io.github.nosequel.scoreboard.ScoreboardHandler;
import io.github.staudlol.listener.PlayerListener;
import io.github.staudlol.scoreboard.ScoreboardProvider;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Collections;

@Getter
public class LeafPlugin extends JavaPlugin {

    @Override
    public void onEnable() {
        this.saveDefaultConfig();

        new MenuHandler(this);
        new ScoreboardHandler(this, new ScoreboardProvider(this.getConfig()), 20L);

        Collections.singletonList(
                new PlayerListener()
        ).forEach(listener -> Bukkit.getPluginManager().registerEvents(listener, this));
    }
}
