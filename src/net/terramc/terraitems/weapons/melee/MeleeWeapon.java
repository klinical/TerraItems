package net.terramc.terraitems.weapons.melee;

import net.terramc.terraitems.shared.AttributeConfiguration;
import net.terramc.terraitems.weapons.Weapon;
import net.terramc.terraitems.weapons.WeaponType;
import org.bukkit.attribute.Attribute;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public abstract class MeleeWeapon extends Weapon {

    public MeleeWeapon(String weaponName, WeaponType weaponType) {
        super(weaponName, weaponType);
    }

    protected List<AttributeConfiguration> getDefaultModifiers() {
        List<AttributeConfiguration> attributeConfigurationList = new ArrayList<>();
        List<EquipmentSlot> slots = new ArrayList<>(Collections.singleton(EquipmentSlot.HAND));

        AttributeConfiguration attackConfiguration = new AttributeConfiguration(
                Attribute.GENERIC_ATTACK_DAMAGE,
                getAttackDamage(),
                slots
        );

        AttributeConfiguration speedConfiguration = new AttributeConfiguration(
                Attribute.GENERIC_ATTACK_SPEED,
                getAttackSpeed(),
                slots
        );

        attributeConfigurationList.add(attackConfiguration);
        attributeConfigurationList.add(speedConfiguration);

        return attributeConfigurationList;
    }

    public @NotNull ItemStack getItemStack() {
        return itemStack;
    }

    public abstract double getAttackDamage();
    public abstract double getAttackSpeed();
}
