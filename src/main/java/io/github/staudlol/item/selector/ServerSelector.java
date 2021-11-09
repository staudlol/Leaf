package io.github.staudlol.item.selector;

import io.github.nosequel.menu.Menu;
import io.github.nosequel.menu.buttons.Button;
import io.github.staudlol.LeafPlugin;
import io.github.staudlol.util.CC;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

@Getter @Setter
public class ServerSelector extends Menu {

    private final FileConfiguration config;

    public ServerSelector(Player player, LeafPlugin plugin) {
        super(player, CC.translate(plugin.getConfig().getString("selector.name")), plugin.getConfig().getInt("selector.size"));

        this.config = plugin.getConfig();
    }

    @Override
    public void tick() {
        final ConfigurationSection keySection = this.config.getConfigurationSection("servers");

        for (String key : keySection.getKeys(false)) {
            final ConfigurationSection section = this.config.getConfigurationSection("servers." + key);

            this.buttons[section.getInt("slot")] = new Button(Material.valueOf(section.getString("material")))
                    .setDisplayName(CC.translate(section.getString("name")))
                    .setData((byte) section.getInt("data"))
                    .setLore(CC.translate(section.getStringList("lore").toArray(new String[0])))
                    .setClickAction(event -> {
                        this.getPlayer().performCommand(section.getString("command"));
                        event.setCancelled(true);
                    });
        }
    }
}
