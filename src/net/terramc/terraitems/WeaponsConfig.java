package net.terramc.terraitems;

import net.terramc.terraitems.shared.ConfigUtility;
import net.terramc.terraitems.weapons.*;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.*;
import java.util.logging.Logger;

public class WeaponsConfig {
    private final FileConfiguration config;
    private final TerraItems plugin;
    private HashMap<String, Weapon> items;

    WeaponsConfig(TerraItems plugin) {
        this.plugin = plugin;

        File configFile = ConfigUtility.createConfigFile("weapons.yml", plugin);
        this.config = YamlConfiguration.loadConfiguration(configFile);

        readWeapons();

        plugin.getLogger().info("Loaded weapons.yml.");
    }

    private void readWeapons() {
        // Initialize hashmap of item key -> item as an ItemStack
        items = new HashMap<>();

        Logger logger = plugin.getLogger();
        Set<String> sectionEntries = config.getKeys(false);

        for (String itemName : sectionEntries) {
            try {
                ConfigurationSection itemSection = Objects.requireNonNull(config.getConfigurationSection(itemName));

                WeaponBuilder builder = new WeaponBuilder();
                Weapon weapon = builder
                        .setName(itemName)
                        .setWeaponType(Objects.requireNonNull(itemSection.getString("type")))
                        .setMeta(itemSection.getConfigurationSection("meta"))
                        .setModifiers(itemSection.getConfigurationSection("modifiers"))
                        .setEffects(itemSection.getConfigurationSection("effects"))
                        .setProjectileModifiers(itemSection.getConfigurationSection("projectile"))
                        .setSpell(itemSection.getString("spell"))
                        .build();

                items.put(itemName, weapon);
                Bukkit.getLogger().info("Loaded weapon " + itemName);
            } catch (IllegalArgumentException | IllegalStateException ex) {
                logger.warning("Encountered exception when parsing weapon [" + itemName + "].");
                logger.warning(ex.getMessage());
                logger.warning(Arrays.toString(ex.getStackTrace()));
            }
        }
    }

    public HashMap<String, Weapon> getItems() {
        return items;
    }
}
