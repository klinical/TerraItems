package net.terramc.terraitems.weapons.ranged;

import com.google.common.collect.ArrayListMultimap;
import net.terramc.terraitems.shared.Rarity;
import net.terramc.terraitems.weapons.Weapon;
import net.terramc.terraitems.weapons.WeaponType;
import net.terramc.terraitems.weapons.configuration.ProjectileModifiers;
import net.terramc.terraitems.weapons.configuration.WeaponMeta;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class RangedWeapon extends Weapon {
    private ProjectileModifiers projectileModifiers;

    public RangedWeapon(String weaponName, WeaponType weaponType) {
        super(weaponName, weaponType);

        this.projectileModifiers = new ProjectileModifiers();
        setDisplayName();
    }

    public void setProjectileModifiers(ProjectileModifiers modifiers) {
        this.projectileModifiers = modifiers;
        setDisplayName();
    }

    private void setDisplayName() {
        ItemMeta meta = itemStack.getItemMeta();
        String displayName = "&r" + weaponType.getDefaultMaterialType().getPrefix() + ' ' + weaponType.getDisplayName();

        if (meta == null)
            throw new IllegalStateException("Meta null when setting display name");

        meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', displayName));
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
