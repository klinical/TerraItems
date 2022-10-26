package net.terramc.terraitems.weapons.melee;

import net.terramc.terraitems.weapons.WeaponType;

public class Dagger extends MeleeWeapon {
    public Dagger(String weaponName) {
        super(weaponName, WeaponType.DAGGER);
    }

    @Override
    public double getAttackDamage() {
        return 4;
    }

    @Override
    public double getAttackSpeed() {
        return 1.75;
    }
}
