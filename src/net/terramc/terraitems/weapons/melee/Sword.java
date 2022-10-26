package net.terramc.terraitems.weapons.melee;

import net.terramc.terraitems.weapons.WeaponType;

public class Sword extends MeleeWeapon {
    public Sword(String weaponName) {
        super(weaponName, WeaponType.SWORD);
    }

    @Override
    public double getAttackDamage() {
        return 6;
    }

    @Override
    public double getAttackSpeed() {
        return 1.6;
    }
}
