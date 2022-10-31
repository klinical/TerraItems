package net.terramc.terraitems.config;

import net.terramc.terraitems.TerraItems;
import net.terramc.terraitems.ammunition.Ammo;
import net.terramc.terraitems.ammunition.AmmoBuilder;
import net.terramc.terraitems.item.Item;
import net.terramc.terraitems.shared.ConfigUtility;
import net.terramc.terraitems.shared.ItemType;
import net.terramc.terraitems.shared.Rarity;
import net.terramc.terraitems.weapons.Weapon;
import net.terramc.terraitems.weapons.WeaponBuilder;
import net.terramc.terraitems.weapons.WeaponDamageType;
import net.terramc.terraitems.weapons.WeaponType;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.*;

public class TerraItemConfiguration {
    private final FileConfiguration config;
    private final ItemType configItemType;
    private final HashMap<String, Item> itemMap;

    public TerraItemConfiguration(TerraItems plugin, ItemType configItemType) {
        this.configItemType = configItemType;
        this.itemMap = new HashMap<>();

        File configFile = ConfigUtility.createConfigFile(
                configItemType.toString().toLowerCase() + ".yml",
                plugin
        );

        this.config = YamlConfiguration.loadConfiguration(configFile);
        loadItems();

        plugin.getLogger().info("Loaded " + configItemType + ".yml");
    }

    public HashMap<String, Item> getItemMap() {
        return itemMap;
    }

    public void loadItems() {
        Set<String> sectionEntries = config.getKeys(false);
        for (String itemKey : sectionEntries) {
            try {
                ConfigurationSection section = Objects.requireNonNull(config.getConfigurationSection(itemKey));

                Item item = parseItem(section);
                itemMap.put(itemKey, item);

                Bukkit.getLogger()
                        .info("Loaded " + configItemType + " " + item.getItemName());
            } catch (Exception e) {
                Bukkit.getLogger()
                        .warning("Failed to read item " + itemKey);
                Bukkit.getLogger()
                        .warning(e.toString());
            }
        }
    }

    private Item parseItem(ConfigurationSection section) {
        Item item;
        switch (configItemType) {
            default:
            case WEAPON:
                item = createWeapon(section);
                break;

            case AMMO:
                item = createAmmo(section);
                break;
        }

        String rarityString = section.getString("rarity");
        if (rarityString != null)
            item.setRarity(Rarity.valueOf(rarityString.toUpperCase()));

        ConfigurationSection metaSection = section.getConfigurationSection("meta");
        if (metaSection != null)
            item.setMetaFromConfigurationSection(metaSection);

        ConfigurationSection modifierSection = section.getConfigurationSection("vanilla-modifiers");
        if (modifierSection != null)
            item.setVanillaAttributeModifiersFromConfigurationSection(modifierSection);

        return item;
    }

    private Weapon createWeapon(ConfigurationSection section) {
        WeaponType weaponType = WeaponType.valueOf(Objects.requireNonNull(section.getString("type")).toUpperCase());

        WeaponBuilder builder = new WeaponBuilder();
        builder.setName(section.getName())
                .setWeaponType(weaponType)
                .setEffects(section.getConfigurationSection("effects"))
                .setStatModifiers(section.getConfigurationSection("stats"));

        if (weaponType.damageType() == WeaponDamageType.RANGED)
            builder.setProjectileModifiers(section.getConfigurationSection("projectile-modifiers"));
        else if (weaponType.damageType() == WeaponDamageType.MAGIC)
            builder.setSpell(section.getString("spell"));

        return builder.build();
    }

    private Ammo createAmmo(ConfigurationSection section) {
        AmmoBuilder ammoBuilder = new AmmoBuilder();
        return ammoBuilder
                .setName(section.getName())
                .setAmmoType(Objects.requireNonNull(section.getString("type")))
                .setAmmoModifiers(section.getConfigurationSection("projectile-modifiers"))
                .build();
    }

    public ItemType getConfigItemType() {
        return configItemType;
    }
}
