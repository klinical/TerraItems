package net.terramc.terraitems.weapons;

import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.UUID;

public class Sword extends Weapon {
    private final EquipmentSlot modifierSlot = EquipmentSlot.HAND;
    private final AttributeModifier.Operation modifierOperation = AttributeModifier.Operation.ADD_NUMBER;
    private final Material material;
    private final ItemMeta meta;

    private static final int baseIronDamage = 6;
    private static final int baseDiamondDamage = 7;
    private static final int baseNetheriteDamage = 8;
    private static final double baseSpeed = 1.6;

    public Sword(Material material) {
        super(material);
        this.material = material;

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
        UUID uuid = UUID.randomUUID();
        String name = meta.getDisplayName() + Attribute.GENERIC_ATTACK_DAMAGE;

        switch (material) {
            case DIAMOND_SWORD:
                return new AttributeModifier(
                        uuid, name, baseDiamondDamage,
                        modifierOperation, modifierSlot);

            case NETHERITE_SWORD:
                return new AttributeModifier(
                        uuid, name, baseNetheriteDamage,
                        modifierOperation, modifierSlot);

            case IRON_SWORD:
            default:
                return new AttributeModifier(
                        uuid, name, baseIronDamage, modifierOperation, modifierSlot);
        }
    }

}
