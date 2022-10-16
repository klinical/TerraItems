package net.terramc.terraitems;

import net.terramc.terraitems.effects.TerraEffect;
import net.terramc.terraitems.shared.ConfigUtility;
import net.terramc.terraitems.shared.EquipmentMaterialType;
import net.terramc.terraitems.shared.Rarity;
import net.terramc.terraitems.weapons.WeaponType;
import net.terramc.terraitems.weapons.*;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.*;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class WeaponsConfig {
    private File configFile;
    private final FileConfiguration config;
    private final TerraItems plugin;
    private HashMap<String, Weapon> items;

    WeaponsConfig(TerraItems plugin) {
        this.plugin = plugin;

        configFile = ConfigUtility.createConfigFile("weapons.yml", plugin);
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
            // Required
            String itemMaterial = config.getString(itemName + ".material");
            String itemType = config.getString(itemName + ".type");

            if (itemMaterial == null) {
                logger.warning("Missing required attribute 'material' for weapon " + itemName);
                continue;
            } else if (itemType == null) {
                logger.warning("Missing required attribute 'itemType' for weapon " + itemName);
                continue;
            }

            try {
                EquipmentMaterialType material = EquipmentMaterialType.valueOf(itemMaterial.toUpperCase());
                WeaponType weaponType = WeaponType.valueOf(itemType.toUpperCase());

                // Switch on weapon type, create a Weapon with given material and itemName
                Weapon weapon = new Weapon(material, weaponType);

                // Set the weapon's name
                weapon.setName(itemName);

                // Optional weapon meta modifications

                // Rarity, changes title color, drop chance
                String itemRarity = config.getString(itemName + ".rarity");
                if (itemRarity != null)
                    weapon.setRarity(Rarity.valueOf(itemRarity.toUpperCase()));

                // Enchantments
                List<String> itemEnchantmentsEntry = config.getStringList(itemName + ".enchantments");
                if (!itemEnchantmentsEntry.isEmpty())
                    weapon.setEnchantments(itemEnchantmentsEntry);

                // Custom attributes (attack speed, knockback, etc)
                ConfigurationSection itemAttributesEntry = config
                        .getConfigurationSection(itemName + ".attributes");
                if (itemAttributesEntry != null)
                    weapon.setAttributes(itemAttributesEntry, config);

                // Title / Display name in-game
                String itemTitle = config.getString(itemName + ".title");
                if (itemTitle != null)
                    weapon.setTitle(itemTitle);

                // Custom model
                int model = config.getInt(itemName + ".custom-model");
                logger.info("model: "+model+" name: "+itemName);
                if (model != 0)
                    weapon.setCustomModel(model);

                List<String> effects = config.getStringList(itemName + ".effects");
                List<TerraEffect> conv = effects
                        .stream()
                        .map(effect -> plugin.getEffectsConfig().getItems().get(effect))
                        .collect(Collectors.toList());

                logger.warning(conv + " " + effects + " " + plugin.getEffectsConfig().getItems());

                weapon.setEffects(conv);


                // Meta lore lines
                List<String> itemLore = config.getStringList(itemName + ".lore");
                if (!itemLore.isEmpty())
                    weapon.setLore(itemLore);

                weapon.buildLore();

                logger.info(weapon.getItemStack().getItemMeta().getLore().toString());

                // Register item by its key in the weapons config
                Bukkit.getLogger().warning(weapon.getItemStack().getItemMeta().getAsString());
                items.put(itemName, weapon);
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

    public File getItemsConfigFile() {
        return configFile;
    }

    public FileConfiguration getConfig() {
        return config;
    }
}
