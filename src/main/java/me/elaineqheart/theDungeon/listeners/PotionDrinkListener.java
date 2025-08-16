package me.elaineqheart.theDungeon.listeners;

import me.elaineqheart.theDungeon.TheDungeon;
import me.elaineqheart.theDungeon.data.PlayersConfig;
import me.elaineqheart.theDungeon.world.ItemManager;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;

import java.util.Objects;

public class PotionDrinkListener implements Listener {

    @EventHandler
    public void onPotionDrink(PlayerItemConsumeEvent event) {
        Player p = event.getPlayer();
        World dungeon = TheDungeon.getWorldManager().getDungeonWorld();
        if(!Objects.equals(p.getLocation().getWorld(), dungeon)) return;
        ItemMeta meta = event.getItem().getItemMeta();
        if (meta == null) return;
        if(!(meta instanceof PotionMeta)) return;
        if(meta.getItemName().equals("§dThe Potion of Freedom")) {
            World world = Bukkit.getWorld("world");
            if(world == null) {
                p.sendMessage(ChatColor.RED + "[ERROR] The default world doesn't exist.");
                TheDungeon.getInstance().getServer().getScheduler().runTaskLater(TheDungeon.getInstance(), () ->
                        p.sendMessage(ChatColor.RED + "Teleport failed."),60);
                TheDungeon.getInstance().getServer().getScheduler().runTaskLater(TheDungeon.getInstance(), () ->
                        p.sendMessage(ChatColor.RED + "No escape route found. Keeping player in this dimension."),120);
                return;
            }
            p.setRespawnLocation(world.getSpawnLocation());
            p.teleport(world.getSpawnLocation());
            p.playSound(p, Sound.BLOCK_PORTAL_TRAVEL,1f,2f);
            p.spawnParticle(Particle.PORTAL,p.getLocation().add(0,1,0),40);
            p.getInventory().clear();
            PlayersConfig.loadPlayerData(p);
            if(p.getInventory().firstEmpty() == -1) {
                world.dropItem(p.getLocation(), ItemManager.getDungeonHelmet(TheDungeon.getInstance().getConfig()));
            } else {
                p.getInventory().addItem(ItemManager.getDungeonHelmet(TheDungeon.getInstance().getConfig()));
            }
            p.setExp(0);
            p.setLevel(0);

        }
    }
}
