package net.terramc.terraitems;

import net.terramc.terraitems.ammunition.*;
import net.terramc.terraitems.shared.ConfigUtility;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.*;
import java.util.logging.Logger;

public class AmmoConfig {
    private final FileConfiguration config;
    private final TerraItems plugin;
    private HashMap<String, Ammo> ammoMap;

    AmmoConfig(TerraItems plugin) {
        this.plugin = plugin;

        File configFile = ConfigUtility.createConfigFile("ammo.yml", plugin);
        this.config = YamlConfiguration.loadConfiguration(configFile);

        readAmmo();
        plugin.getLogger().info("Loaded ammo.yml.");
    }

    private void readAmmo() {
        ammoMap = new HashMap<>();
        Logger logger = plugin.getLogger();
        Set<String> sectionEntries = config.getKeys(false);

        for (String ammoTitle : sectionEntries) {
            // Required
            try {
                ConfigurationSection section = config.getConfigurationSection(ammoTitle);
                if (section == null)
                    return;

                String displayName = Objects.requireNonNull(section.getString("title"));
                String typeString = Objects.requireNonNull(section.getString("type"));
                AmmoType ammoType = AmmoType.valueOf(typeString.toUpperCase());
                int customModel = section.getInt("model");

                Ammo ammo;
                switch (ammoType) {
                    case BULLET:
                        ammo = new Bullet(ammoTitle, displayName);
                        break;

                    default:
                    case ARROW:
                        ammo = new Arrow(ammoTitle, displayName);
                        break;
                }

                ConfigurationSection modifierSection = section.getConfigurationSection("modifiers");
                if (modifierSection != null)
                    ammo.setModifiers(new AmmoModifiers(modifierSection));

                if (customModel != 0)
                    ammo.setCustomModel(customModel);

                ammoMap.put(ammoTitle, ammo);
            } catch (IllegalArgumentException ex) {
                logger.warning(ex.toString());
            }
        }
    }

    public HashMap<String, Ammo> getItems() {
        return ammoMap;
    }
}
