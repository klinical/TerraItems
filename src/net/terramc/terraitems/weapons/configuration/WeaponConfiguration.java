package net.terramc.terraitems.weapons.configuration;

import net.terramc.terraitems.shared.EquipmentMaterialType;
import net.terramc.terraitems.weapons.WeaponType;

import java.io.Serializable;

public class WeaponConfiguration implements Serializable {
    private String name;
    private WeaponMeta meta;
    private WeaponModifiers modifiers;
    private WeaponType weaponType;
    private EquipmentMaterialType materialType;

    public WeaponConfiguration(WeaponType weaponType) {
        this.weaponType = weaponType;
        this.meta = new WeaponMeta(weaponType);
        this.modifiers = new WeaponModifiers(weaponType);
        this.materialType = weaponType.getDefaultMaterialType();
    }

    public void setMeta(WeaponMeta meta) {
        this.meta = meta;
    }

    public void setModifiers(WeaponModifiers modifiers) {
        this.modifiers = modifiers;
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

    public WeaponType getWeaponType() {
        return weaponType;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setWeaponType(WeaponType weaponType) {
        this.weaponType = weaponType;
    }

    public EquipmentMaterialType getMaterialType() {
        if (materialType == null)
            return weaponType.getDefaultMaterialType();

        return materialType;
    }

    public void setMaterialType(EquipmentMaterialType materialType) {
        this.materialType = materialType;
    }
}
