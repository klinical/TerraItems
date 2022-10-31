package net.terramc.terraitems.shared;

import org.bukkit.attribute.Attribute;
import org.bukkit.inventory.EquipmentSlot;

import java.util.List;

public class AttributeConfiguration {
    private final Attribute attribute;
    private final double value;
    private final List<EquipmentSlot> slots;

    public AttributeConfiguration(Attribute attribute, double value, List<EquipmentSlot> slots) {
        this.attribute = attribute;
        this.value = value;
        this.slots = slots;
    }

    public Attribute getAttribute() {
        return attribute;
    }

    public double getValue() {
        return value;
    }

    public List<EquipmentSlot> getSlots() {
        return slots;
    }
}
