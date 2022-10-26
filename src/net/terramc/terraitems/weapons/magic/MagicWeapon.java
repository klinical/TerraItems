package net.terramc.terraitems.weapons.magic;

import com.google.common.collect.ArrayListMultimap;
import net.terramc.terraitems.shared.Rarity;
import net.terramc.terraitems.spells.Spell;
import net.terramc.terraitems.weapons.Weapon;
import net.terramc.terraitems.weapons.WeaponType;
import org.bukkit.ChatColor;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class MagicWeapon extends Weapon {
    private final Spell spell;

    public MagicWeapon(String name, WeaponType weaponType, Spell spell) {
        super(name, weaponType);

        this.spell = spell;
    }

    public Spell getSpell() {
        return spell;
    }

    @Override
    public List<String> getWeaponInfoLore() {
        Rarity rarity = weaponMeta.getRarity();
        List<String> loreList = new ArrayList<>();

        String loreLine = ChatColor.translateAlternateColorCodes(
                '&',
                Rarity.getPrefix(rarity) +
                        rarity.getDisplayName() +
                        "&r &8" + weaponType.getDisplayName());

        loreList.add(loreLine);
        loreList.addAll(spell.getItemDescription()
                .stream()
                .map((s -> ChatColor.LIGHT_PURPLE + s))
                .collect(Collectors.toList()));
        loreList.add(ChatColor.DARK_AQUA + "Mana cost: " + ChatColor.AQUA + spell.getManaCost());

        return loreList;
    }

    @Override
    protected ArrayListMultimap<Attribute, AttributeModifier> getDefaultModifiers() {
        return ArrayListMultimap.create();
    }
}
