package net.terramc.terraitems.weapons.melee;

import net.terramc.terraitems.weapons.WeaponType;

public class Mace extends MeleeWeapon {
    public Mace(String weaponName) {
        super(weaponName, WeaponType.MACE);
    }

    @Override
    public double getAttackDamage() {
        return 10;
    }

    @Override
    public double getAttackSpeed() {
        return 0.8;
    }
}
