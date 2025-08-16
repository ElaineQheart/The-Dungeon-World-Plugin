package me.elaineqheart.theDungeon.world;

import me.elaineqheart.theDungeon.TheDungeon;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.List;
import java.util.Objects;

public class ItemManager {

    public static ShapedRecipe recipe;

    public static void registerDungeonKeyRecipe(FileConfiguration config) {
        // Parse pattern
        List<String> pattern = config.getStringList("recipe.pattern");
        // Parse ingredients
        ConfigurationSection ingredients = config.getConfigurationSection("recipe.ingredients");

        // Create result item
        ItemStack result = new ItemStack(Material.ZOMBIE_HEAD);
        ItemMeta meta = result.getItemMeta();
        assert meta != null;
        meta.setDisplayName("Dungeon Entrance Key");
        meta.setLore(List.of("A realm in between dimensions", "an arcane prison to mention", "those who bestow it alone", "will come back in honor and hone"));
        meta.getPersistentDataContainer().set(new NamespacedKey(TheDungeon.getInstance(), "dungeonKey"), PersistentDataType.BOOLEAN, true);
        result.setItemMeta(meta);

        // Create recipe
        NamespacedKey key = new NamespacedKey(TheDungeon.getInstance(), "dungeon_key");
        recipe = new ShapedRecipe(key, result);
        recipe.shape(pattern.toArray(new String[0]));

        assert ingredients != null;
        for (String k : ingredients.getKeys(false)) {
            Material mat = Material.matchMaterial(Objects.requireNonNull(ingredients.getString(k)));
            if (mat != null) {
                recipe.setIngredient(k.charAt(0), mat);
            }
        }
        Bukkit.addRecipe(recipe);
    }

    public static ItemStack getDungeonHelmet(FileConfiguration config) {
        String type = config.getString("reward.type");
        String name = config.getString("reward.name");
        int armor = config.getInt("reward.attributes.armor");

        assert type != null;
        ItemStack item = new ItemStack(Objects.requireNonNull(Material.matchMaterial(type)));
        ItemMeta meta = item.getItemMeta();
        assert meta != null;
        meta.setDisplayName(name);
        NamespacedKey key = new NamespacedKey(TheDungeon.getInstance(), "reward");
        AttributeModifier modifier = new AttributeModifier(key, armor, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.HEAD.getGroup());
        meta.addAttributeModifier(Attribute.ARMOR, modifier);

        item.setItemMeta(meta);
        return item;
    }

}
