package net.terramc.terraitems.weapons.magic;

import net.terramc.terraitems.spells.Spell;
import net.terramc.terraitems.weapons.WeaponType;

public class Staff extends MagicWeapon {
    public Staff(String name, Spell spell) {
        super(name, WeaponType.STAFF, spell);
    }
}
