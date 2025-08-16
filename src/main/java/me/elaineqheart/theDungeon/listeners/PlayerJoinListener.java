package me.elaineqheart.theDungeon.listeners;

import me.elaineqheart.theDungeon.world.ItemManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener implements Listener {

    @EventHandler
    public void onPlayerFirstJoin(PlayerJoinEvent event) {
        if(!event.getPlayer().hasPlayedBefore()) event.getPlayer().discoverRecipe(ItemManager.recipe.getKey());
    }

}
