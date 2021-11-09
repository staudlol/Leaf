package io.github.staudlol.scoreboard;

import io.github.nosequel.scoreboard.element.ScoreboardElement;
import io.github.nosequel.scoreboard.element.ScoreboardElementHandler;
import io.github.staudlol.util.CC;
import lombok.RequiredArgsConstructor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import xyz.invisraidinq.queryapi.QueryAPI;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
public class ScoreboardProvider implements ScoreboardElementHandler {

    private final QueryAPI queryAPI = QueryAPI.getInstance();
    private final FileConfiguration config;

    @Override
    public ScoreboardElement getElement(Player player) {
        final ScoreboardElement element = new ScoreboardElement();

        element.setTitle(CC.translate(this.config.getString("scoreboard.title")));

        try {
            for (String line : this.config.getStringList("scoreboard.lines")) {
                element.add(replace(line, new HashMap<String, String>() {{
                    put("%online_players%", String.valueOf(queryAPI.getOnlinePlayers()));
                }}));
            }
        } catch (Exception ignored) {
            element.add(CC.translate("&cAn error occurred while"));
            element.add(CC.translate("&cloading your board..."));
        }

        return element;
    }

    private String replace(String string, Map<String, String> replacements) {
        for (Map.Entry<String, String> entry : replacements.entrySet()) {
            string = string.replace(entry.getKey(), entry.getValue());
        }

        return CC.translate(string);
    }
}
