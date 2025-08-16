package me.elaineqheart.theDungeon;

import me.elaineqheart.theDungeon.commands.TestCommand;
import me.elaineqheart.theDungeon.data.PlayersConfig;
import me.elaineqheart.theDungeon.data.UFG;
import me.elaineqheart.theDungeon.listeners.EntranceKeyListener;
import me.elaineqheart.theDungeon.listeners.PotionDrinkListener;
import me.elaineqheart.theDungeon.world.ItemManager;
import me.elaineqheart.theDungeon.world.WorldManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public final class TheDungeon extends JavaPlugin {


    private static TheDungeon instance;
    public static TheDungeon getInstance() {return instance;}
    private static WorldManager worldManager;
    public static WorldManager getWorldManager() {return worldManager;}
    public static boolean firstStart = false;

    @Override
    public void onEnable() {
        instance = this;

        if(getDataFolder().mkdir()) {
            firstStart = true;
            UFG csg = new UFG();
            File temp = new File(getDataFolder().getAbsolutePath() + "/TheDungeonInPluginZip");
            csg.download("https://github.com/ElaineQheart/The-Dungeon-World/zipball/master", temp, getDataFolder());
        }

        worldManager = new WorldManager();

        getCommand("dungeontestelaine").setExecutor(new TestCommand());

        getServer().getPluginManager().registerEvents(new PotionDrinkListener(), this);
        getServer().getPluginManager().registerEvents(new EntranceKeyListener(), this);

        reloadConfig();
        getConfig().options().copyDefaults(true);
        saveConfig();
        PlayersConfig.setup();
        ItemManager.registerDungeonKeyRecipe(getConfig());

    }


}
