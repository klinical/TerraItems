package net.terramc.terraitems.weapons;

import net.terramc.terraitems.weapons.melee.Sword;
import org.bukkit.Material;

public enum WeaponType {
    SWORD,
    DAGGER,
    AXE,
    MACE,
    STAFF,
    GLAIVE,
    BOW,
    CROSSBOW,
    SPELL_BOOK,
    WAND,
    GUN;

    public String getDisplayName() {
        switch (this) {
            default:
            case SWORD: return "Sword";
            case DAGGER: return "Dagger";
            case AXE: return "Axe";
            case MACE: return "Mace";
            case STAFF: return "Staff";
            case GLAIVE: return "Glaive";
            case BOW: return "Bow";
            case CROSSBOW: return "Crossbow";
            case GUN: return "Gun";
            case SPELL_BOOK: return "Spell Book";
        }
    }

    public Material getVanillaItem() {
        switch (this) {
            default:
            case SWORD: return Material.GOLDEN_SWORD;
            case DAGGER: return Material.BLAZE_ROD;
            case AXE: return Material.GOLDEN_AXE;
            case MACE: return Material.BONE;
            case GLAIVE: return Material.STICK;

            case BOW: return Material.BOW;
            case GUN: return Material.CARROT_ON_A_STICK;
            case CROSSBOW: return Material.CROSSBOW;

            case SPELL_BOOK: return Material.BOOK;
            case WAND: return Material.LIGHTNING_ROD;
            case STAFF: return Material.WARPED_FUNGUS_ON_A_STICK;
        }
    }

    public WeaponDamageType damageType() {
        switch (this) {
            case SWORD:
            case DAGGER:
            case AXE:
            case MACE:
            case GLAIVE:
            default:
                return WeaponDamageType.MELEE;

            case BOW:
            case GUN:
            case CROSSBOW:
                return WeaponDamageType.RANGED;

            case SPELL_BOOK:
            case STAFF:
            case WAND:
                return WeaponDamageType.MAGIC;
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
            case SPELL_BOOK:
            case WAND:
            case MACE:
            case STAFF:
            case GLAIVE:
            case GUN:
                return 1;
        }
    }
}
