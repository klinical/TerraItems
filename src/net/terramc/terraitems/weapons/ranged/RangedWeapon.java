package net.terramc.terraitems.weapons.ranged;

import com.google.common.collect.ArrayListMultimap;
import net.terramc.terraitems.weapons.Weapon;
import net.terramc.terraitems.weapons.WeaponType;
import org.bukkit.ChatColor;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;

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

    @Override
    public List<String> buildLore() {
        List<String> newLore = new ArrayList<>(getWeaponInformationLore());

        newLore.add("");

        if (this.weaponMeta.hasCustomLore()) {
            newLore.addAll(this.weaponMeta.getCustomLore());
            newLore.add("");
        }

        if (this.hasWeaponEffects()) {
            newLore.addAll(getEffectLore());
        }

        if (this.projectileModifiers != null) {
            newLore.add("");
            newLore.add(ChatColor.GRAY + "When Fired:");
            newLore.add(ChatColor.translateAlternateColorCodes(
                    '&', "&9+" +
                            (double) projectileModifiers.getReloadSpeed() / 20L +
                            "s Reload Speed"
            ));
            newLore.add(ChatColor.translateAlternateColorCodes(
                    '&', "&9+" +
                            projectileModifiers.getProjectileDamage() +
                            " Projectile Damage"
            ));
        }

        return newLore;
    }

    public ProjectileModifiers getProjectileModifiers() {
        return projectileModifiers;
    }


    @Override
    protected ArrayListMultimap<Attribute, AttributeModifier> getDefaultModifiers() {
        return ArrayListMultimap.create();
    }
}
