package me.elaineqheart.theDungeon.commands;

import me.elaineqheart.theDungeon.TheDungeon;
import me.elaineqheart.theDungeon.world.WorldManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class TestCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {

        if(commandSender instanceof Player p) {
            if(TheDungeon.firstStart) {
                p.sendMessage("This is the first time this plugin is loaded. You need to stop and restart the server for it to work.");
                return true;
            }
            WorldManager.teleportPlayer(p);
        }

        return true;
    }
}
