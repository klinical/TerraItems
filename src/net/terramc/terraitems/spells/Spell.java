package net.terramc.terraitems.spells;

import net.terramc.terraitems.TerraPlayer;

public abstract class Spell {
    public abstract void cast(TerraPlayer player);
    public abstract double getManaCost();
}
