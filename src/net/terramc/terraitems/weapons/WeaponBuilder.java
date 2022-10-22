package net.terramc.terraitems.weapons;

import net.terramc.terraitems.TerraItems;
import net.terramc.terraitems.effects.EffectTriggerType;
import net.terramc.terraitems.effects.TerraEffect;
import net.terramc.terraitems.effects.configuration.EffectTrigger;
import net.terramc.terraitems.shared.AttributeConfiguration;
import net.terramc.terraitems.shared.EquipmentMaterialType;
import net.terramc.terraitems.shared.Rarity;
import net.terramc.terraitems.weapons.configuration.*;
import net.terramc.terraitems.weapons.melee.MeleeWeapon;
import net.terramc.terraitems.weapons.ranged.RangedWeapon;
import org.bukkit.ChatColor;
import org.bukkit.attribute.Attribute;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.meta.ItemMeta;

import javax.annotation.Nullable;
import java.util.*;
import java.util.stream.Collectors;

public class WeaponBuilder {
    private WeaponType weaponType;
    private EquipmentMaterialType materialType;
    private WeaponMeta meta;
    private WeaponModifiers modifiers;
    private ProjectileModifiers projectileModifiers;
    private HashMap<String, EffectTrigger> effects;
    private String weaponName;

    public Weapon build() {
        Objects.requireNonNull(weaponName);
        Objects.requireNonNull(weaponType);

        Weapon weapon;
        switch (weaponType.damageType()) {
            case MELEE:
                if (materialType != null)
                    weapon = new MeleeWeapon(weaponName, weaponType, materialType);
                else
                    weapon = new MeleeWeapon(weaponName, weaponType);
                break;

            case RANGED:
                RangedWeapon rangedWeapon = new RangedWeapon(weaponName, weaponType);

                if (projectileModifiers != null)
                    rangedWeapon.setProjectileModifiers(projectileModifiers);

                weapon = rangedWeapon;
                break;

            default:
                throw new IllegalStateException("Invalid damageType when building weapon.");
        }

        if (meta != null)
            weapon.setWeaponMeta(meta);

        if (modifiers != null)
            weapon.setWeaponModifiers(modifiers);

        if (effects != null) {
            weapon.setEffects(effects);
            buildEffectLore(weapon);
        }

        return weapon;
    }

    private void buildEffectLore(Weapon weapon) {
        weapon.setEffects(effects);

        ItemMeta meta = weapon.getItemStack().getItemMeta();
        if (meta == null)
            throw new IllegalStateException("ItemStack ItemMeta is null");

        List<String> lore = meta.getLore() != null ?
                meta.getLore() : new ArrayList<>();

        if (weapon.getWeaponEffects() == null)
            return;

        lore.add("");
        for (String effectName : weapon.getWeaponEffects().keySet()) {
            TerraEffect effect = TerraItems.lookupTerraPlugin().getEffectsConfig().getItems().get(effectName);

            if (effect.getMeta() != null && effect.getMeta().getDisplay() != null) {
                String displayLore = effect.getMeta().getDisplay();
                String[] l = displayLore.split("\n");

                for (String ls : l) {
                    lore.add(ChatColor.translateAlternateColorCodes('&', "&a" + ls));
                }
            }
        }

        meta.setLore(lore);
        weapon.getItemStack().setItemMeta(meta);
    }

    public WeaponBuilder setName(String name) {
        this.weaponName = name;

        return this;
    }

    public WeaponBuilder setProjectileModifiers(ConfigurationSection section) {
        if (section == null)
            return this;

        ProjectileModifiers projModifiers = new ProjectileModifiers();
        projModifiers.setProjectileDamage(section.getInt("damage"));
        projModifiers.setProjectileKnockback(section.getInt("knockback"));
        projModifiers.setReloadSpeed(section.getInt("reload-speed"));

        this.projectileModifiers = projModifiers;
        return this;
    }

    public WeaponBuilder setWeaponType(String weaponType) {
        this.weaponType = WeaponType.valueOf(weaponType.toUpperCase());
        return this;
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

    public HashMap<String, EffectTrigger> getEffects() {
        return effects;
    }

    public WeaponBuilder setEffects(@Nullable ConfigurationSection section) {
        effects = new HashMap<>();
        if (section == null)
            return this;

        Set<String> effectKeys = section.getKeys(false);
        for (String effectKey : effectKeys) {
            ConfigurationSection effectSection = section.getConfigurationSection(effectKey);
            if (effectSection == null)
                continue;

            String triggerTypeString =  effectSection.getString("trigger");
            if (triggerTypeString == null)
                continue;

            EffectTriggerType triggerType = EffectTriggerType.valueOf(triggerTypeString.toUpperCase());
            int chance = effectSection.getInt("chance");

            EffectTrigger trigger = new EffectTrigger(triggerType, chance);
            TerraEffect effect = TerraItems.lookupTerraPlugin().getEffectsConfig().getItems().get(effectKey);

            effects.put(effect.getEffectName(), trigger);
        }

        return this;
    }

    public WeaponBuilder setModifiers(@Nullable ConfigurationSection section) {
        if (section == null) {
            modifiers = new WeaponModifiers(weaponType);
            return this;
        }

        modifiers = new WeaponModifiers(weaponType);

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

        modifiers.setAttributeConfigurations(attributeConfigurations);
        modifiers.setEnchantments(section.getStringList("enchantments"));

        return this;
    }
}