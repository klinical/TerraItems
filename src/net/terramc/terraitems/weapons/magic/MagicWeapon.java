package net.terramc.terraitems.weapons.magic;

import net.terramc.terraitems.shared.AttributeConfiguration;
import net.terramc.terraitems.spells.Spell;
import net.terramc.terraitems.weapons.Weapon;
import net.terramc.terraitems.weapons.WeaponType;
import org.bukkit.ChatColor;

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
    public boolean hasWeaponEffects() {
        return true;
    }

    @Override
    protected List<String> getEffectLore() {
        List<String> lore = spell.getItemDescription()
                .stream()
                .map((s -> ChatColor.DARK_PURPLE + s))
                .collect(Collectors.toList());

        lore.add(ChatColor.DARK_AQUA + "Mana cost: " + ChatColor.AQUA + spell.getManaCost());
        return lore;
    }

    @Override
    protected List<AttributeConfiguration> getDefaultModifiers() {
        return new ArrayList<>();
    }
}
