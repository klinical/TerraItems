package net.terramc.terraitems.ammunition;

import net.terramc.terraitems.item.Item;
import net.terramc.terraitems.shared.ItemType;
import org.bukkit.ChatColor;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public abstract class Ammo extends Item {
    private AmmoModifiers ammoModifiers;
    private final AmmoType ammoType;

    public Ammo(String ammoName, AmmoType ammoType) {
        super(ammoName, ammoType.getVanillaItem(), ItemType.AMMO);
        this.ammoType = ammoType;

        setDisplayName(ammoType.getDisplayName());
        setLore(getItemDescriptionLore());
    }

    public AmmoModifiers getAmmoModifiers() {
        return ammoModifiers;
    }

    @Override
    protected @NotNull List<String> getPreCustomLoreLore() {
        List<String> lore = new ArrayList<>();

        if (ammoModifiers.getDamage() > 0) {
            lore.add(ChatColor.GREEN + "+" + ammoModifiers.getDamage() + " Damage");
        } else if (ammoModifiers.getDamage() < 0) {
            lore.add(ChatColor.RED + "" + ammoModifiers.getDamage() + " Damage");
        }

        if (ammoModifiers.getKnockback() > 0) {
            lore.add(ChatColor.GREEN + "+" + ammoModifiers.getKnockback() + " Knockback");
        } else if (ammoModifiers.getKnockback() < 0) {
            lore.add(ChatColor.RED + "" + ammoModifiers.getKnockback() + " Knockback");
        }

        if (ammoModifiers.getVelocity() > 0) {
            lore.add(ChatColor.GREEN + "+" + ammoModifiers.getVelocity() + " Velocity");
        } else if (ammoModifiers.getVelocity() < 0) {
            lore.add(ChatColor.RED + "" + ammoModifiers.getVelocity() + " Velocity");
        }

        return lore;
    }

    @Override
    protected @NotNull List<String> getPostCustomLoreLore() {
        return new ArrayList<>();
    }

    public void setAmmoModifiers(AmmoModifiers modifiers) {
        this.ammoModifiers = modifiers;

        updateLore();
    }

    public @NotNull String getItemDescriptionString() {
        return ammoType.getDisplayName();
    }

    @Override
    protected int getDefaultModel() {
        return ammoType.getDefaultModel();
    }
}
