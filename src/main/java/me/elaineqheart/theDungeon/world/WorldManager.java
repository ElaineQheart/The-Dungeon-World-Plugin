package me.elaineqheart.theDungeon.world;

import me.elaineqheart.theDungeon.TheDungeon;
import me.elaineqheart.theDungeon.data.PlayersConfig;
import me.elaineqheart.theDungeon.data.UFG;
import org.bukkit.*;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public class WorldManager {

    private World dungeonWorld;

    public World getDungeonWorld() {
        if(dungeonWorld == null || UFG.path == null) {
            try {
                Stream<Path> stream = Files.list(TheDungeon.getInstance().getDataFolder().toPath()).filter(Files::isDirectory);
                UFG.path = stream.toList().getFirst().toFile().getPath().replace('\\', '/') + "/";
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            WorldCreator creator = new WorldCreator(UFG.path + UFG.dungeonLobby);
            dungeonWorld = creator.createWorld();
        }
        return dungeonWorld;
    }

    public static void teleportPlayer(Player p) {
        Location loc = new Location(TheDungeon.getWorldManager().getDungeonWorld(), 0, 64, 0);
        p.teleport(loc);
        p.setRespawnLocation(loc,true);
        PlayersConfig.savePlayerData(p);

        p.setGameMode(GameMode.ADVENTURE);
        p.getInventory().clear();
        p.setExp(0);
        p.setLevel(0);
        p.playSound(p, Sound.BLOCK_PORTAL_TRAVEL,1f,2f);
        p.spawnParticle(Particle.PORTAL,p.getLocation().add(0,1,0),40);
        p.spawnParticle(Particle.SWEEP_ATTACK,p.getLocation().add(0,2,0.5),20);
        p.sendTitle(ChatColor.GOLD + "The Dungeon","another prison",10,70,20);
    }



}
