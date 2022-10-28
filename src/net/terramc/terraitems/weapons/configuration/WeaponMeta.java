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
    @Nullable private String title;
    private Rarity rarity;
    private List<String> customLore;
    private int customModel;
    private final WeaponType weaponType;

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
        return customLore.stream()
                .map(l -> {
                    String builder = "&7" + l;
                    return ChatColor.translateAlternateColorCodes('&', builder);
                })
                .collect(Collectors.toList());
    }

    public void setRarity(Rarity rarity) {
        this.rarity = rarity;
    }

    public void setCustomLore(List<String> customLore) {
        this.customLore = customLore;
    }

    public void setTitle(@Nullable String title) {
        this.title = title;
    }

    public void setCustomModel(int customModel) {
        if (customModel == 0)
            this.customModel = weaponType.getDefaultModel();
        else
            this.customModel = customModel;
    }
}
