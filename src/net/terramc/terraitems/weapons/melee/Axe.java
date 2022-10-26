package net.terramc.terraitems.weapons.melee;

import net.terramc.terraitems.weapons.WeaponType;

public class Axe extends MeleeWeapon {
    public Axe(String weaponName) {
        super(weaponName, WeaponType.AXE);
    }

    @Override
    public double getAttackDamage() {
        return 9;
    }

    @Override
    public double getAttackSpeed() {
        return 0.9;
    }
}
