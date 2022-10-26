package net.terramc.terraitems.weapons.magic;

import net.terramc.terraitems.spells.Spell;
import net.terramc.terraitems.weapons.WeaponType;

public class SpellBook extends MagicWeapon {
    public SpellBook(String name, Spell spell) {
        super(name, WeaponType.SPELL_BOOK, spell);
    }
}
