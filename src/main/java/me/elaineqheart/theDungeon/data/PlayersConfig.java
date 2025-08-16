package me.elaineqheart.theDungeon.data;

import me.elaineqheart.theDungeon.TheDungeon;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PlayersConfig {

    private static File file;
    private static FileConfiguration customFile;

    //Finds or generates the custom config file
    public static void setup(){
        file = new File(TheDungeon.getInstance().getDataFolder(), "players.yml");

        if (!file.exists()){
            try{
                file.createNewFile();
            }catch (IOException e){
                //uwu
            }
        }
        customFile = YamlConfiguration.loadConfiguration(file);
    }

    public static FileConfiguration get(){
        return customFile;
    }

    public static void save(){
        try {
            customFile.save(file);
        }catch (IOException e){
            TheDungeon.getInstance().getLogger().severe("Couldn't save displays.yml file");
        }
    }

    public static void reload(){
        customFile = YamlConfiguration.loadConfiguration(file);
    }



    public static void savePlayerData(Player player) {
        FileConfiguration config = PlayersConfig.get();
        String uuid = player.getUniqueId().toString();

        List<Map<String, Object>> items = new ArrayList<>();
        for (ItemStack item : player.getInventory().getContents()) {
            items.add(item == null ? null : item.serialize());
        }
        config.set(uuid + ".inventory", items);

        config.set(uuid + ".gamemode", player.getGameMode().name());

        config.set(uuid + ".xp", player.getLevel());

        PlayersConfig.save();
    }

    public static void loadPlayerData(Player player) {
        FileConfiguration config = PlayersConfig.get();
        String uuid = player.getUniqueId().toString();

        List<?> items = config.getList(uuid + ".inventory");
        if (items != null) {
            ItemStack[] contents = new ItemStack[items.size()];
            for (int i = 0; i < items.size(); i++) {
                Object item = items.get(i);
                contents[i] = (item == null) ? null : ItemStack.deserialize((Map<String, Object>) item);
            }
            player.getInventory().setContents(contents);
        }

        String gamemode = config.getString(uuid + ".gamemode");
        if (gamemode != null) {
            player.setGameMode(org.bukkit.GameMode.valueOf(gamemode));
        }

        int xp = config.getInt(uuid + ".xp", player.getLevel());
        player.setLevel(xp);
    }
}
