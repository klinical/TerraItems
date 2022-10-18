package net.terramc.terraitems;

import net.terramc.terraitems.shared.ConfigUtility;
import net.terramc.terraitems.weapons.*;
import net.terramc.terraitems.weapons.configuration.WeaponConfiguration;
import net.terramc.terraitems.weapons.configuration.WeaponMeta;
import net.terramc.terraitems.weapons.configuration.WeaponModifiers;
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

    private void registerWeapon(String itemName, WeaponType weaponType) {
        Logger logger = plugin.getLogger();

        try {
            ConfigurationSection metaSection = Objects.requireNonNull(config.getConfigurationSection(itemName + ".meta"));
            WeaponMeta meta = new WeaponMeta(metaSection);

            WeaponConfiguration configuration = new WeaponConfiguration(itemName, meta);

            ConfigurationSection modifierSection = config.getConfigurationSection(itemName + ".modifiers");
            if (modifierSection != null)
                configuration.setWeaponModifiers(new WeaponModifiers(modifierSection));

            switch (weaponType.damageType()) {
                case MELEE:
                    items.put(itemName, new MeleeWeapon(configuration));
                    break;
                case RANGED:
                    items.put(itemName, new RangedWeapon(configuration));
                    break;
            }

            Bukkit.getLogger().info("Loaded " + weaponType + " " + itemName);
        } catch (IllegalArgumentException | IllegalStateException ex) {
            logger.warning("Encountered exception when parsing weapon [" + itemName + "].");
            logger.warning(ex.getMessage());
            logger.warning(Arrays.toString(ex.getStackTrace()));
        }
    }

    private void readWeapons() {
        // Initialize hashmap of item key -> item as an ItemStack
        items = new HashMap<>();
        Set<String> sectionEntries = config.getKeys(false);

        for (String itemName : sectionEntries) {
            String weaponTypeString = Objects.requireNonNull(config.getString(itemName + ".meta.type"));
            WeaponType weaponType = WeaponType.valueOf(weaponTypeString.toUpperCase());

            registerWeapon(itemName, weaponType);
        }
    }

    public HashMap<String, Weapon> getItems() {
        return items;
    }
}
