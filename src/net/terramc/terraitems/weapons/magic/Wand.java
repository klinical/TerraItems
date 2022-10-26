package net.terramc.terraitems.weapons.magic;

import net.terramc.terraitems.spells.Spell;
import net.terramc.terraitems.weapons.WeaponType;

public class Wand extends MagicWeapon {
    public Wand(String name, Spell spell) {
        super(name, WeaponType.WAND, spell);
    }
}
