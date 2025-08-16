package me.elaineqheart.theDungeon.listeners;

import me.elaineqheart.theDungeon.TheDungeon;
import me.elaineqheart.theDungeon.world.WorldManager;
import org.bukkit.NamespacedKey;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;

public class EntranceKeyListener implements Listener {

    @EventHandler
    public void onKeyPlace(BlockPlaceEvent event) {
        ItemStack item = event.getItemInHand();
        if (item.getItemMeta() != null && item.getItemMeta().getPersistentDataContainer().has(
                new NamespacedKey(TheDungeon.getInstance(), "dungeonKey"))) {
            event.setCancelled(true);
            item.setAmount(item.getAmount()-1);
            WorldManager.teleportPlayer(event.getPlayer());
        }
    }

}
