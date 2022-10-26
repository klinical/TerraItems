package net.terramc.terraitems.weapons.melee;

import com.google.common.collect.ArrayListMultimap;
import net.terramc.terraitems.shared.Rarity;
import net.terramc.terraitems.weapons.Weapon;
import net.terramc.terraitems.weapons.WeaponType;
import org.bukkit.ChatColor;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public abstract class MeleeWeapon extends Weapon {

    public MeleeWeapon(String weaponName, WeaponType weaponType) {
        super(weaponName, weaponType);
    }

    public List<String> getWeaponInfoLore() {
        Rarity rarity = weaponMeta.getRarity();

        String loreLine = ChatColor.translateAlternateColorCodes(
                '&',
                Rarity.getPrefix(rarity) +
                        rarity.getDisplayName() +
                        "&r &7" + weaponType.getDisplayName());

        List<String> list = new ArrayList<>();
        list.add(loreLine);

        return list;
    }

    protected ArrayListMultimap<Attribute, AttributeModifier> getDefaultModifiers() {
        ArrayListMultimap<Attribute, AttributeModifier> map = ArrayListMultimap.create();
        ItemMeta meta = Objects.requireNonNull(this.itemStack.getItemMeta());

        AttributeModifier damageModifier =  new AttributeModifier(
                UUID.randomUUID(),
                meta.getDisplayName() + "-" + Attribute.GENERIC_ATTACK_DAMAGE,
                getAttackDamage(),
                AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.HAND
        );
        map.put(Attribute.GENERIC_ATTACK_DAMAGE, damageModifier);

        AttributeModifier speedModifier =  new AttributeModifier(
                UUID.randomUUID(),
                meta.getDisplayName() + "-" + Attribute.GENERIC_ATTACK_SPEED,
                getAttackSpeed(),
                AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.HAND
        );
        map.put(Attribute.GENERIC_ATTACK_SPEED, speedModifier);

        return map;
    }

    public @NotNull ItemStack getItemStack() {
        return itemStack;
    }

    public abstract double getAttackDamage();
    public abstract double getAttackSpeed();
}
