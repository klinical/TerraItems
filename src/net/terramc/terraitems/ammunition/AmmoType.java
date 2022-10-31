package net.terramc.terraitems.ammunition;

import org.bukkit.Material;

public enum AmmoType {
    BULLET,
    ARROW;

    public Material getVanillaItem() {
        switch (this) {
            case BULLET:
                return Material.FIREWORK_STAR;

            default:
            case ARROW:
                return Material.ARROW;
        }
    }

    public String getDisplayName() {
        switch (this) {
            case BULLET:
                return "Bullet";

            default:
            case ARROW:
                return "Arrow";
        }
    }

    public int getDefaultModel() {
        switch (this) {
            case BULLET:
                return 1;

            default:
            case ARROW:
                return 0;
        }
    }
}
