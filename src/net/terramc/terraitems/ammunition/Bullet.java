package net.terramc.terraitems.ammunition;

import org.bukkit.Material;

public class Bullet extends Ammo {
    public Bullet(String ammoName, String displayName) {
        super(ammoName, displayName, Material.FIREWORK_STAR);
    }
}
