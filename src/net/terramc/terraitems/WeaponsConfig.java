package net.terramc.terraitems;

import net.terramc.terraitems.effects.TerraEffect;
import net.terramc.terraitems.shared.ConfigUtility;
import net.terramc.terraitems.shared.EquipmentMaterialType;
import net.terramc.terraitems.shared.Rarity;
import net.terramc.terraitems.weapons.*;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.*;
import java.util.logging.Logger;
import java.util.stream.Collectors;

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
            String materialString = config.getString(itemName + ".material");
            EquipmentMaterialType weaponMaterial = (materialString == null) ?
                    weaponType.getDefaultMaterialType() :
                    EquipmentMaterialType.valueOf(materialString.toUpperCase());

            Weapon weapon = null;
            switch (weaponType.damageType()) {
                case MELEE:
                    weapon = new MeleeWeapon(itemName, weaponMaterial, weaponType);
                    break;
                case RANGED:
                    weapon = new RangedWeapon(itemName, weaponType);
                    break;
            }

            ConfigurationSection metaSection = config.getConfigurationSection(itemName + ".meta");
            if (metaSection != null)
                weapon.setWeaponMeta(new WeaponMeta(metaSection, weaponType));

            ConfigurationSection modifierSection = config.getConfigurationSection(itemName + ".modifiers");
            if (modifierSection != null)
                weapon.setWeaponModifiers(new WeaponModifiers(modifierSection));

            logger.info("Loaded " + weaponType + " " + itemName);
            items.put(itemName, weapon);
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
            String weaponTypeString = Objects.requireNonNull(config.getString(itemName + ".type"));
            WeaponType weaponType = WeaponType.valueOf(weaponTypeString.toUpperCase());

            registerWeapon(itemName, weaponType);
        }
    }

    public HashMap<String, Weapon> getItems() {
        return items;
    }
}
