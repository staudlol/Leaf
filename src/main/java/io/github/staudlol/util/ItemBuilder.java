package io.github.staudlol.util;

import io.github.staudlol.LeafPlugin;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.plugin.java.JavaPlugin;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * @author NV6
 */

public class ItemBuilder {

    private final Map<Enchantment, Integer> enchantments = new HashMap<>();

    private String displayName;
    private String[] lore;

    private Material type;
    private byte data = 0;

    private Consumer<PlayerInteractEvent> action;
    private Player target;
    private String skullOwner;

    /**
     * Constructor to make a new item builder object
     *
     * @param material the type of the item
     */
    public ItemBuilder(Material material) {
        this.type = material;
    }

    /**
     * Set the data of the item stack (or as staud would pronounce it, i em.)
     *
     * @param data the data to set it to
     * @return the current item builder instance
     */
    public ItemBuilder setData(byte data) {
        this.data = data;
        return this;
    }

    /**
     * Set the display name of the item
     *
     * @param displayName the new display name
     * @return the current item builder instance
     */
    public ItemBuilder setDisplayName(String displayName) {
        this.displayName = displayName;
        return this;
    }

    /**
     * Set the skull owner of the item
     *
     * @param ownerName the name of the owner
     * @return the current item builder instance
     */
    public ItemBuilder setSkullOwner(String ownerName) {
        this.skullOwner = ownerName;
        return this;
    }

    /**
     * Set the lore of the item
     *
     * @param lore the new lore
     * @return the current item builder instance
     */
    public ItemBuilder setLore(String... lore) {
        this.lore = lore;
        return this;
    }

    /**
     * Set the material type of the item
     *
     * @param type the new material type
     * @return the current item builder instance
     */
    public ItemBuilder setType(Material type) {
        this.type = type;
        return this;
    }

    /**
     * Add a new enchantment to the item
     *
     * @param enchantment the enchantment to add
     * @param level       the level of the enchantment
     * @return the current item builder instance
     */
    public ItemBuilder addEnchantment(Enchantment enchantment, int level) {
        this.enchantments.put(enchantment, level);
        return this;
    }

    /**
     * Set the click action of the item
     *
     * @param action the click action
     * @param target the player who the action is for
     * @return the current item builder instance
     */
    public ItemBuilder setAction(Consumer<PlayerInteractEvent> action, Player target) {
        this.action = action;
        this.target = target;
        return this;
    }

    /**
     * Get the item stack
     *
     * @return the item stack
     */
    public ItemStack toItemStack() {
        final ItemStack item = new ItemStack(this.type, 1, this.data);
        final ItemMeta meta = item.getItemMeta();

        if (this.displayName != null) {
            meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', displayName));
            item.setItemMeta(meta);
        }

        if (this.lore != null) {
            meta.setLore(Arrays.stream(lore)
                    .map(lore -> ChatColor.translateAlternateColorCodes('&', lore))
                    .collect(Collectors.toList())
            );

            item.setItemMeta(meta);
        }

        if(this.skullOwner != null && meta instanceof SkullMeta) {
            ((SkullMeta) meta).setOwner(this.skullOwner);

            System.out.println("set the skull owner to " + skullOwner);
            item.setItemMeta(meta);
        }

        if (!this.enchantments.isEmpty()) {
            for (Map.Entry<Enchantment, Integer> entry : this.enchantments.entrySet()) {
                item.addUnsafeEnchantment(entry.getKey(), entry.getValue());
            }
        }

        if (this.action != null) {
            Bukkit.getPluginManager().registerEvents(new Listener() {

                @EventHandler
                public void onClick(PlayerInteractEvent event) {
                    if (event.getItem() != null && event.getItem().isSimilar(item) && event.getPlayer().equals(target)) {
                        action.accept(event);
                    }
                }

            }, JavaPlugin.getPlugin(LeafPlugin.class));
        }

        return item;
    }
}