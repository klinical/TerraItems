package net.terramc.terraitems.weapons.configuration;

import net.terramc.terraitems.effects.TerraEffect;
import net.terramc.terraitems.shared.AttributeConfiguration;
import net.terramc.terraitems.shared.EquipmentMaterialType;
import net.terramc.terraitems.weapons.WeaponType;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class WeaponModifiers {
    private List<TerraEffect> effects = new ArrayList<>();
    private List<String> effectLore = new ArrayList<>();
    private List<AttributeConfiguration> attributeConfigurations = new ArrayList<>();
    private List<String> enchantments = new ArrayList<>();
    private Integer projectileDamage;
    private Integer reloadSpeed;
    private WeaponType weaponType;

    public void setProjectileDamage(Integer projectileDamage) {
        this.projectileDamage = projectileDamage;
    }

    public void setReloadSpeed(Integer reloadSpeed) {
        this.reloadSpeed = reloadSpeed;
    }

    public Integer getProjectileDamage() {
        return projectileDamage;
    }

    public Integer getReloadSpeed() {
        return reloadSpeed;
    }

    public WeaponModifiers(WeaponType weaponType) {
        this.weaponType = weaponType;
    }

    public WeaponType getWeaponType() {
        return weaponType;
    }

    public WeaponModifiers(EquipmentMaterialType type) {
        Objects.requireNonNull(type);

        this.attributeConfigurations = new ArrayList<>();
        this.enchantments = new ArrayList<>();
    }

    public boolean hasEffects() {
        return !effects.isEmpty();
    }

    public boolean hasAttributeConfigurations() {
        return !attributeConfigurations.isEmpty();
    }

    public boolean hasEnchantments() {
        return !enchantments.isEmpty();
    }

    public boolean hasEffectLore() {
        return !effectLore.isEmpty();
    }

    public List<String> getEffectLore() {
        return effectLore;
    }

    public List<TerraEffect> getEffects() {
        return effects;
    }

    public List<AttributeConfiguration> getAttributeConfigurations() {
        return attributeConfigurations;
    }

    public List<String> getEnchantments() {
        return enchantments;
    }

    public void setEffects(List<TerraEffect> effects) {
        this.effects = effects;
    }

    public void setEffectLore(List<String> effectLore) {
        this.effectLore = effectLore;
    }

    public void setAttributeConfigurations(List<AttributeConfiguration> attributeConfigurations) {
        this.attributeConfigurations = attributeConfigurations;
    }

    public void setEnchantments(List<String> enchantments) {
        this.enchantments = enchantments;
    }
}
