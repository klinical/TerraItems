package net.terramc.terraitems.weapons.ranged;

import net.terramc.terraitems.shared.AttributeConfiguration;
import net.terramc.terraitems.weapons.Weapon;
import net.terramc.terraitems.weapons.WeaponType;
import org.bukkit.ChatColor;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public abstract class RangedWeapon extends Weapon {
    private ProjectileModifiers projectileModifiers;

    public RangedWeapon(String weaponName, WeaponType weaponType) {
        super(weaponName, weaponType);

        this.projectileModifiers = new ProjectileModifiers();
    }

    public void setProjectileModifiers(ProjectileModifiers modifiers) {
        this.projectileModifiers = modifiers;
    }

    public ProjectileModifiers getProjectileModifiers() {
        return projectileModifiers;
    }


    @Override
    protected List<AttributeConfiguration> getDefaultModifiers() {
        return new ArrayList<>();
    }

    @Override
    protected @NotNull List<String> getPostCustomLoreLore() {
        List<String> postLore = super.getPostCustomLoreLore();

        if (this.projectileModifiers != null) {
            postLore.add("");
            postLore.add(ChatColor.GRAY + "When Fired:");
            postLore.add(ChatColor.translateAlternateColorCodes(
                    '&', "&9+" +
                            (double) projectileModifiers.getReloadSpeed() / 20L +
                            "s Reload Speed"
            ));
            postLore.add(ChatColor.translateAlternateColorCodes(
                    '&', "&9+" +
                            projectileModifiers.getProjectileDamage() +
                            " Projectile Damage"
            ));
        }

        return postLore;
    }
}
