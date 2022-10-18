package net.terramc.terraitems.weapons;

import com.google.common.collect.ArrayListMultimap;
import net.terramc.terraitems.shared.Rarity;
import net.terramc.terraitems.weapons.configuration.WeaponConfiguration;
import org.bukkit.ChatColor;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;

import java.util.ArrayList;
import java.util.List;

public class RangedWeapon extends Weapon {
    public RangedWeapon(WeaponConfiguration configuration) {
        super(configuration);
    }

    @Override
    public List<String> getWeaponInfoLore() {
        WeaponType weaponType = configuration.getMeta().getWeaponType();
        Rarity rarity = configuration.getMeta().getRarity();

        String loreLine = ChatColor.translateAlternateColorCodes(
                '&',
                Rarity.getPrefix(rarity) +
                        rarity.getDisplayName() +
                        "&r &8" + weaponType.getDisplayName());

        List<String> loreList = new ArrayList<>();
        loreList.add(loreLine);

        return loreList;
    }

    @Override
    protected String getDefaultDisplayName() {
        WeaponType weaponType = configuration.getMeta().getWeaponType();

        String displayName = "&r" + weaponType.getDefaultMaterialType().getPrefix() + ' ' + weaponType.getDisplayName();

        return ChatColor.translateAlternateColorCodes('&', displayName);
    }

    @Override
    protected ArrayListMultimap<Attribute, AttributeModifier> getDefaultModifiers() {
        return ArrayListMultimap.create();
    }
}
