package net.terramc.terraitems;

import org.bukkit.Bukkit;
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

        /**
         * add weapon type to item description/meta/lore in a way that looks good? (also make lore look better in general)
         * optional custom-model attribute setting for weapons, armor, items to allow specifying model
         * glaive weapon type (spear/polearm type), investigate possibilities of a gun type
         * reduce attack speed for large weapons when held in offhand etc to discourage use?
         * shields support, off-hand items support
         * cloth, leather, plate armor types
         * Reorganize to support new config scheme of weapons.yml, off-hands.yml, armor.yml, weapons.yml, effects.yml
         * Extract shareable code from ItemsConfig to be used across the 5 yml configs
         * Mark and document optional/required fields for entry settings in the configs
         * Refactor item spawn commands to work with variation in item types
         */

        this.getCommand("terraitem").setExecutor(new ItemSpawn(this, weaponsConfig.getItems()));

        logger.info("[Terra Items] Enabled.");
    }

    public WeaponsConfig getWeaponsConfig() {
        return weaponsConfig;
    }
}