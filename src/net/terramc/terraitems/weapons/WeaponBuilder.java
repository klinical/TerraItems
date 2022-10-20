package net.terramc.terraitems.weapons;

import net.terramc.terraitems.TerraItems;
import net.terramc.terraitems.effects.TerraEffect;
import net.terramc.terraitems.shared.AttributeConfiguration;
import net.terramc.terraitems.shared.EquipmentMaterialType;
import net.terramc.terraitems.shared.Rarity;
import net.terramc.terraitems.weapons.configuration.*;
import net.terramc.terraitems.weapons.melee.MeleeWeapon;
import net.terramc.terraitems.weapons.ranged.RangedWeapon;
import org.bukkit.ChatColor;
import org.bukkit.attribute.Attribute;
import org.bukkit.configuration.ConfigurationSection;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class WeaponBuilder {
    private WeaponType weaponType;
    private EquipmentMaterialType materialType;
    private WeaponMeta meta;
    private WeaponModifiers modifiers;

    public WeaponBuilder() {
    }

    public Weapon build() {
        Objects.requireNonNull(weaponType);

        WeaponConfiguration configuration = new WeaponConfiguration(weaponType);
        if (meta != null)
            configuration.setMeta(meta);

        if (modifiers != null)
            configuration.setModifiers(modifiers);

        if (materialType != null)
            configuration.setMaterialType(materialType);

        Weapon weapon;
        switch (weaponType.damageType()) {
            case MELEE:
                Objects.requireNonNull(materialType);

                weapon = new MeleeWeapon(configuration);
                break;
            case RANGED:
                weapon = new RangedWeapon(configuration);
                break;
            default:
                throw new IllegalStateException("Invalid damageType when building weapon.");
        }

        return weapon;
    }

    public WeaponType getWeaponType() {
        return weaponType;
    }

    public WeaponBuilder setWeaponType(String weaponType) {
        this.weaponType = WeaponType.valueOf(weaponType.toUpperCase());
        return this;
    }

    public EquipmentMaterialType getMaterialType() {
        return materialType;
    }

    public WeaponBuilder setMaterialType(@Nullable String materialType) {
        if (materialType == null)
            this.materialType = null;
        else
            this.materialType = EquipmentMaterialType.valueOf(materialType.toUpperCase());

        return this;
    }

    public WeaponMeta getMeta() {
        return meta;
    }

    public WeaponBuilder setMeta(@Nullable ConfigurationSection section) {
        if (section == null) {
            meta = new WeaponMeta(weaponType);
            return this;
        }

        meta = new WeaponMeta(weaponType);
        meta.setTitle(section.getString("title"));

        String rarityString = section.getString("rarity");
        if (rarityString != null)
            meta.setRarity(Rarity.valueOf(rarityString.toUpperCase()));

        meta.setCustomLore(section.getStringList("lore")
                .stream()
                .map(l -> ChatColor.translateAlternateColorCodes('&', l))
                .collect(Collectors.toList())
        );

        meta.setCustomModel(section.getInt("model"));
        return this;
    }

    public WeaponModifiers getModifiers() {
        return modifiers;
    }

    public WeaponBuilder setModifiers(@Nullable ConfigurationSection section) {
        if (section == null) {
            modifiers = new WeaponModifiers(weaponType);
            return this;
        }

        modifiers = new WeaponModifiers(weaponType);
        modifiers.setProjectileDamage(section.getInt("projectile-damage"));
        modifiers.setReloadSpeed(section.getInt("reload-speed"));

        List<AttributeConfiguration> attributeConfigurations = new ArrayList<>();
        List<String> attributesStringList = section.getStringList("attributes");
        if (!(attributesStringList.isEmpty())) {
            for (String attribute : attributesStringList) {
                int amount = section.getInt("value");
                List<String> slots = section.getStringList("slots");
                Attribute attributeParsed = Attribute.valueOf(attribute.toUpperCase());

                attributeConfigurations.add(new AttributeConfiguration(attributeParsed, amount, slots));
            }
        }

        List<TerraEffect> effects = new ArrayList<>();
        List<String> effectLore = new ArrayList<>();
        List<String> effectsStringList = section.getStringList("effects");
        if (!(effectsStringList.isEmpty())) {
            List<TerraEffect> parsedEffects = effectsStringList
                    .stream()
                    .map(effect -> TerraItems.lookupTerraPlugin().getEffectsConfig().getItems().get(effect))
                    .collect(Collectors.toList());

            for (TerraEffect effect : parsedEffects) {
                if (effect.getMeta() != null && effect.getMeta().getDisplay() != null) {
                    String displayLore = effect.getMeta().getDisplay();
                    String[] l = displayLore.split("\n");

                    for (String ls : l) {
                        effectLore.add(ChatColor.translateAlternateColorCodes('&', "&a" + ls));
                    }
                }

                effects.add(effect);
            }
        }

        modifiers.setAttributeConfigurations(attributeConfigurations);
        modifiers.setEnchantments(section.getStringList("enchantments"));
        modifiers.setEffects(effects);
        modifiers.setEffectLore(effectLore);

        return this;
    }
}
