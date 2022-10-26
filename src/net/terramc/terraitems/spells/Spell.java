package net.terramc.terraitems.spells;

import net.terramc.terraitems.TerraPlayer;

import java.util.List;

public abstract class Spell {
    public abstract void cast(TerraPlayer player);
    public abstract double getManaCost();
    public abstract List<String> getItemDescription();
}
