package net.terramc.terraitems.weapons;

import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.UUID;

public class Staff extends Weapon {
    private final EquipmentSlot modifierSlot = EquipmentSlot.HAND;
    private final AttributeModifier.Operation modifierOperation = AttributeModifier.Operation.ADD_NUMBER;

    private final ItemMeta meta;

    private static final int baseDamage = 5;
    private static final double baseSpeed = 1.9;

    public Staff(Material material) {
        super(material);

        meta = this.getItemMeta();
        if (meta != null) {
            meta.addAttributeModifier(Attribute.GENERIC_ATTACK_SPEED, getAttackSpeedModifier());
            meta.addAttributeModifier(Attribute.GENERIC_ATTACK_DAMAGE, getAttackDamageModifier());
        } else {
            // TODO: Needs logged, default modifiers failed to apply due to null meta object
        }
    }

    private AttributeModifier getAttackSpeedModifier() {
        return new AttributeModifier(
                UUID.randomUUID(),
                meta.getDisplayName() + Attribute.GENERIC_ATTACK_SPEED,
                baseSpeed,
                modifierOperation,
                modifierSlot);
    }

    private AttributeModifier getAttackDamageModifier() {
        return new AttributeModifier(
                UUID.randomUUID(),
                meta.getDisplayName() + Attribute.GENERIC_ATTACK_DAMAGE,
                baseDamage,
                modifierOperation,
                modifierSlot);
    }
}
