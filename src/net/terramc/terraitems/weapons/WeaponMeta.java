package net.terramc.terraitems.weapons;

import net.terramc.terraitems.shared.Rarity;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;

import java.util.List;
import java.util.stream.Collectors;

public class WeaponMeta {
    private final String title;
    private final Rarity rarity;
    private final List<String> customLore;
    private int customModel;
    private WeaponType weaponType;

    public WeaponMeta(ConfigurationSection section, WeaponType weaponType) {
        this.title = section.getString("title");
        this.weaponType = weaponType;

        String rarityString = section.getString("rarity");
        if (rarityString != null)
            rarity = Rarity.valueOf(rarityString.toUpperCase());
        else
            rarity = Rarity.COMMON;

        customLore = section.getStringList("lore")
                .stream()
                .map(l -> ChatColor.translateAlternateColorCodes('&', l))
                .collect(Collectors.toList());

        customModel = section.getInt("model");
    }

    public boolean hasTitle() {
        return title != null;
    }

    public boolean hasCustomLore() {
        return !(customLore.isEmpty());
    }

    /**
     * If custom model is 0, it may have not been specified in config, default to weapon-type custom model
     * which could be 0 or 1
     */
    public int getCustomModel() {
        return (customModel == 0) ? weaponType.getDefaultModel() : customModel;
    }

    public String getTitle() {
        return title;
    }

    public Rarity getRarity() {
        return rarity;
    }

    public List<String> getCustomLore() {
        return customLore;
    }
}
