package net.terramc.terraitems.weapons;

import com.google.common.collect.ArrayListMultimap;
import net.terramc.terraitems.shared.EquipmentMaterialType;
import net.terramc.terraitems.shared.Rarity;
import org.bukkit.ChatColor;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

public class MeleeWeapon extends Weapon {

    public MeleeWeapon(String configKey, EquipmentMaterialType materialType, WeaponType weaponType) {
        super(configKey, weaponType, materialType);
    }

    protected String getDefaultDisplayName() {
        String displayName = "&r" + materialType.getPrefix() + ' ' + weaponType.getDisplayName();

        return ChatColor.translateAlternateColorCodes('&', displayName);
    }

    public List<String> getWeaponInfoLore() {
        Rarity rarity = weaponMeta.getRarity();

        String loreLine = ChatColor.translateAlternateColorCodes(
                '&',
                Rarity.getPrefix(rarity) +
                        rarity.getDisplayName() +
                        "&r &8" +
                        materialType.getPrefix() +
                        " " + weaponType.getDisplayName());

        List<String> list = new ArrayList<>();
        list.add(loreLine);
        list.add("");

        return list;
    }

    protected ArrayListMultimap<Attribute, AttributeModifier> getDefaultModifiers() {
        ArrayListMultimap<Attribute, AttributeModifier> map = ArrayListMultimap.create();
        ItemMeta meta = Objects.requireNonNull(this.itemStack.getItemMeta());

        AttributeModifier damageModifier =  new AttributeModifier(
                UUID.randomUUID(),
                meta.getDisplayName() + "-" + Attribute.GENERIC_ATTACK_DAMAGE,
                weaponType.getAttributeDamage(materialType),
                AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.HAND
        );
        map.put(Attribute.GENERIC_ATTACK_DAMAGE, damageModifier);


        AttributeModifier speedModifier =  new AttributeModifier(
                UUID.randomUUID(),
                meta.getDisplayName() + "-" + Attribute.GENERIC_ATTACK_SPEED,
                weaponType.getAttributeSpeed(materialType),
                AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.HAND
        );
        map.put(Attribute.GENERIC_ATTACK_SPEED, speedModifier);

        return map;
    }

    public ItemStack getItemStack() {
        return itemStack;
    }
}
