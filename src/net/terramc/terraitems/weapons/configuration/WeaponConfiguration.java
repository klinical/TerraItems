package net.terramc.terraitems.weapons.configuration;

import net.terramc.terraitems.shared.EquipmentMaterialType;
import net.terramc.terraitems.weapons.WeaponType;

import java.io.Serializable;

public class WeaponConfiguration implements Serializable {
    private String name;
    private WeaponMeta meta;
    private WeaponModifiers modifiers;

    public WeaponConfiguration(String name, WeaponMeta meta) {
        this.name = name;
        this.meta = meta;
    }

    public WeaponConfiguration(EquipmentMaterialType material, WeaponType type) {
        this.modifiers = new WeaponModifiers(material);
        this.meta = new WeaponMeta(type);
    }

    public void setWeaponModifiers(WeaponModifiers modifiers) {
        this.modifiers = modifiers;
    }

    public WeaponConfiguration(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public WeaponMeta getMeta() {
        return meta;
    }

    public WeaponModifiers getModifiers() {
        return modifiers;
    }

    public boolean hasModifiers() {
        return modifiers != null;
    }

}
