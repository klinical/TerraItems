package net.terramc.terraitems.weapons;

import net.terramc.terraitems.shared.EquipmentMaterialType;

import java.util.HashMap;

public enum WeaponType {
    SWORD,
    DAGGER,
    AXE,
    MACE,
    STAFF,
    GLAIVE,
    BOW,
    CROSSBOW,
    GUN;

    /**
     * TODO: these default values being stored in a flat file such as JSON would probably be cleaner and easier
     * to maintain
     */
    private static final HashMap<EquipmentMaterialType, HashMap<WeaponType, Integer>> damageMap;
    static {
        damageMap = new HashMap<>();
        HashMap<WeaponType, Integer> ironValues = new HashMap<>();
        damageMap.put(EquipmentMaterialType.IRON, ironValues);
        ironValues.put(WeaponType.DAGGER, 5);
        ironValues.put(WeaponType.SWORD, 6);
        ironValues.put(WeaponType.AXE, 9);
        ironValues.put(WeaponType.MACE, 9);
        ironValues.put(WeaponType.STAFF, 6);
        ironValues.put(WeaponType.GLAIVE, 9);
        HashMap<WeaponType, Integer> diamondValues = new HashMap<>();
        damageMap.put(EquipmentMaterialType.DIAMOND, diamondValues);
        diamondValues.put(WeaponType.DAGGER, 6);
        diamondValues.put(WeaponType.SWORD, 7);
        diamondValues.put(WeaponType.AXE, 9);
        diamondValues.put(WeaponType.MACE, 9);
        diamondValues.put(WeaponType.STAFF, 7);
        diamondValues.put(WeaponType.GLAIVE, 9);
        HashMap<WeaponType, Integer> netheriteValues = new HashMap<>();
        damageMap.put(EquipmentMaterialType.NETHERITE, netheriteValues);
        netheriteValues.put(WeaponType.DAGGER, 7);
        netheriteValues.put(WeaponType.SWORD, 8);
        netheriteValues.put(WeaponType.AXE, 10);
        netheriteValues.put(WeaponType.MACE, 10);
        netheriteValues.put(WeaponType.STAFF, 8);
        netheriteValues.put(WeaponType.GLAIVE, 10);
    }

    private static final HashMap<EquipmentMaterialType, HashMap<WeaponType, Double>> speedMap;
    static {
        speedMap = new HashMap<>();
        HashMap<WeaponType, Double> ironValues = new HashMap<>();
        speedMap.put(EquipmentMaterialType.IRON, ironValues);
        ironValues.put(WeaponType.DAGGER, 1.9);
        ironValues.put(WeaponType.SWORD, 1.6);
        ironValues.put(WeaponType.AXE, 0.9);
        ironValues.put(WeaponType.MACE, 0.9);
        ironValues.put(WeaponType.STAFF, 0.9);
        ironValues.put(WeaponType.GLAIVE, 0.9);
        HashMap<WeaponType, Double> diamondValues = new HashMap<>();
        speedMap.put(EquipmentMaterialType.DIAMOND, diamondValues);
        diamondValues.put(WeaponType.DAGGER, 1.9);
        diamondValues.put(WeaponType.SWORD, 1.6);
        diamondValues.put(WeaponType.AXE, 0.9);
        diamondValues.put(WeaponType.MACE, 0.9);
        diamondValues.put(WeaponType.STAFF, 0.9);
        diamondValues.put(WeaponType.GLAIVE, 0.9);
        HashMap<WeaponType, Double> netheriteValues = new HashMap<>();
        speedMap.put(EquipmentMaterialType.NETHERITE, netheriteValues);
        netheriteValues.put(WeaponType.DAGGER, 1.9);
        netheriteValues.put(WeaponType.SWORD, 1.6);
        netheriteValues.put(WeaponType.AXE, 1.0);
        netheriteValues.put(WeaponType.MACE, 1.0);
        netheriteValues.put(WeaponType.STAFF, 1.0);
        netheriteValues.put(WeaponType.GLAIVE, 1.0);
    }

    public Integer getAttributeDamage(EquipmentMaterialType mat) {
        return damageMap.get(mat).get(this);
    }

    public Double getAttributeSpeed(EquipmentMaterialType mat) {
        return speedMap.get(mat).get(this);
    }

    public String getDisplayName() {
        switch (this) {
            default:
            case SWORD: return "Sword";
            case DAGGER: return "Dagger";
            case AXE: return "Axe";
            case MACE: return "Mace";
            case STAFF: return "Staff";
            case GLAIVE: return "Glaive";
        }
    }

    public String getVanillaItemName() {
        switch (this) {
            default:
            case SWORD:
            case DAGGER: return "SWORD";
            case AXE:
            case MACE: return "AXE";
            case STAFF: return "SHOVEL";
            case GLAIVE: return "HOE";
        }
    }

    public WeaponDamageType damageType() {
        switch (this) {
            case SWORD:
            case DAGGER:
            case AXE:
            case MACE:
            case STAFF:
            case GLAIVE:
            default:
                return WeaponDamageType.MELEE;
            case BOW:
            case GUN:
            case CROSSBOW:
                return WeaponDamageType.RANGED;
        }
    }

    public EquipmentMaterialType getDefaultMaterialType() {
        switch (this) {
            case SWORD:
            case DAGGER:
            case AXE:
            case MACE:
            case STAFF:
            case GLAIVE:
            default:
                return EquipmentMaterialType.IRON;

            case BOW:
                return EquipmentMaterialType.BOW;
            case CROSSBOW:
                return EquipmentMaterialType.CROSSBOW;
            case GUN:
                return EquipmentMaterialType.GUN;
        }
    }

    public int getDefaultModel() {
        switch (this) {
            case BOW:
            case CROSSBOW:
            case SWORD:
            case AXE:
            default:
                return 0;

            case DAGGER:
            case MACE:
            case STAFF:
            case GLAIVE:
            case GUN:
                return 1;
        }
    }
}
