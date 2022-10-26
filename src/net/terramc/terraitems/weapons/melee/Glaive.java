package net.terramc.terraitems.weapons.melee;

import net.terramc.terraitems.weapons.WeaponType;

public class Glaive extends MeleeWeapon {
    public Glaive(String weaponName) {
        super(weaponName, WeaponType.GLAIVE);
    }

    @Override
    public double getAttackDamage() {
        return 11;
    }

    @Override
    public double getAttackSpeed() {
        return 0.7;
    }
}
