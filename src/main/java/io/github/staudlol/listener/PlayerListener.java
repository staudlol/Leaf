package io.github.staudlol.listener;

import io.github.staudlol.LeafPlugin;
import io.github.staudlol.item.ItemManager;
import io.github.staudlol.item.selector.ServerSelector;
import io.github.staudlol.util.CC;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.*;
import org.bukkit.event.weather.WeatherChangeEvent;

public class PlayerListener implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        final Player player = event.getPlayer();

        for (String message : LeafPlugin.getPlugin(LeafPlugin.class).getConfig().getStringList("welcome.message")) {
            player.sendMessage(CC.translate(message));
        }

        event.setJoinMessage(null);

        player.getInventory().clear();
        player.getInventory().setArmorContents(null);

        player.setGameMode(GameMode.ADVENTURE);
        player.teleport(player.getWorld().getSpawnLocation());

        ItemManager.giveItems(player.getInventory());
    }

    @EventHandler
    public void onSelectionInteraction(PlayerInteractEvent event) {
        if (event.getItem() != null) {
            if (event.getItem().getType().equals(Material.valueOf(LeafPlugin.getPlugin(LeafPlugin.class).getConfig().getString("items.selector.material")))) {
                new ServerSelector(event.getPlayer(), LeafPlugin.getPlugin(LeafPlugin.class)).updateMenu();
            }
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        event.setQuitMessage(null);
    }

    @EventHandler
    public void onHunger(FoodLevelChangeEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onWeather(WeatherChangeEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onDrop(PlayerDropItemEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onPickup(PlayerPickupItemEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onEntitySpawn(EntitySpawnEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onDamage(EntityDamageEvent event) {
        final Entity entity = event.getEntity();

        if (entity instanceof LivingEntity) {
            ((LivingEntity) entity).setHealth(((LivingEntity) entity).getMaxHealth());
        }

        if (event.getCause() == EntityDamageEvent.DamageCause.VOID) {
            entity.teleport(entity.getWorld().getSpawnLocation());
        }

        event.setCancelled(true);
    }

    @EventHandler
    public void onMoveItem(InventoryClickEvent event) {
        final Player player = (Player) event.getWhoClicked();

        if (event.getCurrentItem() == null) {
            return;
        }

        if (player.getGameMode().equals(GameMode.CREATIVE)) {
            return;
        }

        event.setCancelled(true);
    }
}
