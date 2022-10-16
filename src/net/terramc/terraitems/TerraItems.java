package net.terramc.terraitems;

import net.terramc.terraitems.commands.ItemSpawn;
import net.terramc.terraitems.commands.TerraConfig;
import net.terramc.terraitems.eventhandlers.OnHitListener;
import net.terramc.terraitems.shared.EquipmentMaterialType;
import net.terramc.terraitems.weapons.WeaponType;
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
    private EffectsConfig effectsConfig;

    @Override
    public void onDisable() {
        logger.info("[Terra Items] Disabled.");
    }

    @Override
    public void onEnable() {
        logger = Bukkit.getLogger();
        effectsConfig = new EffectsConfig(this);
        weaponsConfig = new WeaponsConfig(this);
        saveDefaultConfig();

        addCraftingRecipes();

        /**
         * research possibility of extending reach of glaive types (rename to spear?)
         * give maces, glaives, staffs a reason to actually be used + crafted (rename maces to blunt?)
         * custom effects!
         * investigate possibilities of a gun type
         * shields support, off-hand items support
         * cloth, leather, plate armor types
         * Reorganize to support new config scheme of weapons.yml, off-hands.yml, armor.yml, weapons.yml, effects.yml
         * Extract shareable code from WeaponsConfig to be used across the 5 yml configs
         * Mark and document optional/required fields for entry settings in the configs
         * Refactor item spawn commands to work with variation in item types
         * allow specifying amount in give command
         */

        this.getCommand("terraitem")
                .setExecutor(new ItemSpawn(this));
        this.getCommand("terraconfig")
                .setExecutor(new TerraConfig(this));

        getServer().getPluginManager().registerEvents(new OnHitListener(effectsConfig, weaponsConfig), this);

        logger.info("[Terra Items] Enabled.");
    }

    public void reloadConfigs() {
        effectsConfig = new EffectsConfig(this);
        weaponsConfig = new WeaponsConfig(this);
        logger.info("Reloaded configuration files.");
    }

    // Add base item crafting recipes for custom items - were gonna need a better way to do this
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

    public EffectsConfig getEffectsConfig() {
        return effectsConfig;
    }
}