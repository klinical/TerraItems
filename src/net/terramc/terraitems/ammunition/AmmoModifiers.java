package net.terramc.terraitems.ammunition;

import org.bukkit.configuration.ConfigurationSection;

public class AmmoModifiers {
    private double velocity;
    private double damage;
    private double knockback;

    public AmmoModifiers() {
        velocity = 0.0;
        damage = 0.0;
        knockback = 0.0;
    }

    public AmmoModifiers(ConfigurationSection section) {
        velocity = section.getDouble("velocity");
        damage = section.getDouble("damage");
        knockback = section.getDouble("knockback");
    }

    public double getVelocity() {
        return velocity;
    }

    public void setVelocity(double velocity) {
        this.velocity = velocity;
    }

    public double getDamage() {
        return damage;
    }

    public void setDamage(double damage) {
        this.damage = damage;
    }

    public double getKnockback() {
        return knockback;
    }

    public void setKnockback(double knockback) {
        this.knockback = knockback;
    }
}
