package net.terramc.terraitems.weapons.melee;

import com.google.common.collect.ArrayListMultimap;
import net.terramc.terraitems.shared.EquipmentMaterialType;
import net.terramc.terraitems.shared.Rarity;
import net.terramc.terraitems.weapons.Weapon;
import net.terramc.terraitems.weapons.WeaponType;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class MeleeWeapon extends Weapon {
    @NotNull private final EquipmentMaterialType materialType;

    public MeleeWeapon(String weaponName, WeaponType weaponType) {
        super(weaponName, weaponType);

        this.materialType = weaponType.getDefaultMaterialType();
        setDisplayName();
    }

    public MeleeWeapon(String weaponName, WeaponType weaponType, @NotNull EquipmentMaterialType materialType) {
        super(weaponName, weaponType, materialType);

        this.materialType = materialType;
        setDisplayName();
    }

    private void setDisplayName() {
        ItemMeta meta = itemStack.getItemMeta();
        String displayName = "&r" + materialType.getPrefix() + ' ' + weaponType.getDisplayName();

        if (meta == null)
            throw new IllegalStateException("Meta null when setting display name");

        meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', displayName));
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

        return list;
    }

    protected ArrayListMultimap<Attribute, AttributeModifier> getDefaultModifiers() {
        ArrayListMultimap<Attribute, AttributeModifier> map = ArrayListMultimap.create();
        ItemMeta meta = Objects.requireNonNull(this.itemStack.getItemMeta());

        AttributeModifier damageModifier =  new AttributeModifier(
                UUID.randomUUID(),
                meta.getDisplayName() + "-" + Attribute.GENERIC_ATTACK_DAMAGE,
                weaponType.getAttributeDamage(weaponType.getDefaultMaterialType()),
                AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.HAND
        );
        map.put(Attribute.GENERIC_ATTACK_DAMAGE, damageModifier);

        AttributeModifier speedModifier =  new AttributeModifier(
                UUID.randomUUID(),
                meta.getDisplayName() + "-" + Attribute.GENERIC_ATTACK_SPEED,
                weaponType.getAttributeSpeed(weaponType.getDefaultMaterialType()),
                AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.HAND
        );
        map.put(Attribute.GENERIC_ATTACK_SPEED, speedModifier);

        return map;
    }

    public @NotNull ItemStack getItemStack() {
        return itemStack;
    }
}
