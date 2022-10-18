package net.terramc.terraitems.weapons.configuration;

import net.terramc.terraitems.shared.Rarity;
import net.terramc.terraitems.weapons.WeaponType;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class WeaponMeta {
    @Nullable private final String title;
    private final Rarity rarity;
    private final List<String> customLore;
    private final int customModel;
    private final WeaponType weaponType;

    public WeaponMeta(ConfigurationSection section) {
        String weaponTypeString = Objects.requireNonNull(section.getString("type"));
        this.weaponType = WeaponType.valueOf(weaponTypeString.toUpperCase());
        this.title = section.getString("title");

        String rarityString = section.getString("rarity");
        if (rarityString != null)
            rarity = Rarity.valueOf(rarityString.toUpperCase());
        else
            rarity = Rarity.COMMON;

        customLore = section.getStringList("lore")
                .stream()
                .map(l -> ChatColor.translateAlternateColorCodes('&', l))
                .collect(Collectors.toList());

        int model = section.getInt("model");
        if (model == 0)
            customModel = weaponType.getDefaultModel();
        else
            customModel = model;
    }

    public WeaponMeta(WeaponType type) {
        this.weaponType = type;

        this.customModel = weaponType.getDefaultModel();
        this.customLore = new ArrayList<>();
        this.rarity = Rarity.COMMON;
        this.title = null;
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
        return customModel;
    }

    public @Nullable String getTitle() {
        return title;
    }

    public Rarity getRarity() {
        return rarity;
    }

    public List<String> getCustomLore() {
        return customLore;
    }

    public WeaponType getWeaponType() {
        return weaponType;
    }
}
