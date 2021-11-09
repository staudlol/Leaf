package io.github.staudlol.item;

import io.github.staudlol.LeafPlugin;
import io.github.staudlol.util.CC;
import io.github.staudlol.util.ItemBuilder;
import lombok.experimental.UtilityClass;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

@UtilityClass
public class ItemManager {

    private final LeafPlugin plugin = LeafPlugin.getPlugin(LeafPlugin.class);

    private final ItemStack selector = new ItemBuilder(Material.valueOf(plugin.getConfig().getString("items.selector.material")))
            .setDisplayName(CC.translate(plugin.getConfig().getString("items.selector.name")))
            .setLore(CC.translate(plugin.getConfig().getStringList("items.selector.lore").toArray(new String[0])))
            .toItemStack();

    public void giveItems(PlayerInventory inventory) {
        inventory.setItem(plugin.getConfig().getInt("items.selector.slot"), selector);
    }
}
