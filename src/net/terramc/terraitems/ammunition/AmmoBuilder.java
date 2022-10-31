package net.terramc.terraitems.ammunition;

import org.bukkit.configuration.ConfigurationSection;

public class AmmoBuilder {
    private String itemName;
    private AmmoType ammoType;
    private AmmoModifiers ammoModifiers;

    public AmmoBuilder setName(String itemName) {
        this.itemName = itemName;

        return this;
    }

    public AmmoBuilder setAmmoType(String ammoTypeString) {
        ammoType = AmmoType.valueOf(ammoTypeString.toUpperCase());

        return this;
    }

    public AmmoBuilder setAmmoModifiers(ConfigurationSection section) {
        if (section != null) {
            ammoModifiers = new AmmoModifiers();
            ammoModifiers.setDamage(section.getDouble("damage"));
            ammoModifiers.setKnockback(section.getDouble("knockback"));
            ammoModifiers.setVelocity(section.getDouble("velocity"));
        }

        return this;
    }

    public Ammo build() {
        Ammo ammo;
        switch (ammoType) {
            case BULLET:
                ammo = new Bullet(itemName);
                break;

            default:
            case ARROW:
                ammo = new Arrow(itemName);
                break;
        }

        if (ammoModifiers != null)
            ammo.setAmmoModifiers(ammoModifiers);

        return ammo;
    }
}
