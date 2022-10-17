package net.terramc.terraitems.weapons;

import com.google.common.collect.ArrayListMultimap;
import net.terramc.terraitems.shared.Rarity;
import org.bukkit.ChatColor;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;

import java.util.ArrayList;
import java.util.List;

public class RangedWeapon extends Weapon {
    public RangedWeapon(String configKey, WeaponType weaponType) {
        super(configKey, weaponType);
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
        loreList.add("");

        return loreList;
    }

    @Override
    protected String getDefaultDisplayName() {
        String displayName = "&r" + materialType.getPrefix() + ' ' + weaponType.getDisplayName();

        return ChatColor.translateAlternateColorCodes('&', displayName);
    }

    @Override
    protected ArrayListMultimap<Attribute, AttributeModifier> getDefaultModifiers() {
        return ArrayListMultimap.create();
    }
}
