package net.terramc.terraitems.weapons.ranged;

import com.google.common.collect.ArrayListMultimap;
import net.terramc.terraitems.shared.Rarity;
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
    public List<String> getWeaponInfoLore() {
        Rarity rarity = weaponMeta.getRarity();

        String loreLine = ChatColor.translateAlternateColorCodes(
                '&',
                Rarity.getPrefix(rarity) +
                        rarity.getDisplayName() +
                        "&r &8" + weaponType.getDisplayName());

        List<String> loreList = new ArrayList<>();
        loreList.add(loreLine);
        loreList.add(ChatColor.translateAlternateColorCodes(
                '&', "&9" + (double) projectileModifiers.getReloadSpeed() / 20L + "s Reload Speed"
        ));
        loreList.add(ChatColor.translateAlternateColorCodes(
                '&', "&9" + projectileModifiers.getProjectileDamage() + " Projectile Damage"
        ));

        return loreList;
    }

    public ProjectileModifiers getProjectileModifiers() {
        return projectileModifiers;
    }

    @Override
    protected ArrayListMultimap<Attribute, AttributeModifier> getDefaultModifiers() {
        return ArrayListMultimap.create();
    }
}
