package net.terramc.terraitems;

import net.terramc.terraitems.weapons.*;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;

public class TerraItems extends JavaPlugin {

    private Logger logger;
    private WeaponsConfig weaponsConfig;

    @Override
    public void onDisable() {
        logger.info("[Terra Items] Disabled.");
    }

    @Override
    public void onEnable() {
        logger = Bukkit.getLogger();
        weaponsConfig = new WeaponsConfig(this);
        saveDefaultConfig();

        addCraftingRecipes();

        /**
         * investigate possibilities of a gun type

         * shields support, off-hand items support
         * cloth, leather, plate armor types
         * Reorganize to support new config scheme of weapons.yml, off-hands.yml, armor.yml, weapons.yml, effects.yml
         * Extract shareable code from WeaponsConfig to be used across the 5 yml configs
         * Mark and document optional/required fields for entry settings in the configs
         * Refactor item spawn commands to work with variation in item types
         */

        this.getCommand("terraitem")
                .setExecutor(new ItemSpawn(this, weaponsConfig.getItems()));

        logger.info("[Terra Items] Enabled.");
    }

    // Add base item crafting recipes for custom items
    private void addCraftingRecipes() {
        EquipmentMaterialType[] materials = {
                EquipmentMaterialType.IRON, EquipmentMaterialType.DIAMOND, EquipmentMaterialType.NETHERITE
        };

        for (EquipmentMaterialType material : materials) {
            Weapon dagger = new Weapon(material, WeaponType.DAGGER);
            Weapon mace = new Weapon(material, WeaponType.MACE);
            Weapon staff = new Weapon(material, WeaponType.STAFF);
            Weapon glaive = new Weapon(material, WeaponType.GLAIVE);

            ShapedRecipe[] recipes = {
                    new ShapedRecipe(
                            new NamespacedKey(this, material + "dagger"), dagger.getItemStack()),
                    new ShapedRecipe(
                            new NamespacedKey(this, material + "mace"), mace.getItemStack()),
                    new ShapedRecipe(
                            new NamespacedKey(this, material + "staff"), staff.getItemStack()),
                    new ShapedRecipe(
                            new NamespacedKey(this, material + "glaive"), glaive.getItemStack()),
            };

            recipes[0].shape("   ", " X ", " I ");
            recipes[1].shape("XIX", " I ", " I ");
            recipes[2].shape("IXI", " I ", " I ");
            recipes[3].shape("  X", " I ", "I  ");

            for (ShapedRecipe recipe : recipes) {
                recipe.setIngredient('X', material.getIngot());
                recipe.setIngredient('I', Material.STICK);
                Bukkit.addRecipe(recipe);
            }
        }
    }

    public WeaponsConfig getWeaponsConfig() {
        return weaponsConfig;
    }
}