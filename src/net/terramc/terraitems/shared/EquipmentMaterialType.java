package net.terramc.terraitems.shared;

import net.terramc.terraitems.weapons.WeaponType;
import org.bukkit.Material;

public enum EquipmentMaterialType {
    IRON,
    DIAMOND,
    NETHERITE;

    public String getPrefix() {
        switch (this) {
            default:
            case IRON: return "Iron";
            case DIAMOND: return "Diamond";
            case NETHERITE: return "Netherite";
        }
    }

    public Material getWeaponMaterial(WeaponType type) {
        return Material.valueOf(this.toString() + '_' + type.getVanillaItemName());
    }

    public Material getIngot() {
        switch (this) {
            default:
            case IRON: return Material.IRON_INGOT;
            case DIAMOND: return Material.DIAMOND;
            case NETHERITE: return Material.NETHERITE_INGOT;
        }
    }
}