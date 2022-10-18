package net.terramc.terraitems.shared;

import org.bukkit.attribute.Attribute;

import java.util.List;

public class AttributeConfiguration {
    private Attribute attribute;
    private int value;
    private List<String> slots;

    public AttributeConfiguration(Attribute attribute, int value, List<String> slots) {
        this.attribute = attribute;
        this.value = value;
        this.slots = slots;
    }

    public AttributeConfiguration() { }

    public Attribute getAttribute() {
        return attribute;
    }

    public int getValue() {
        return value;
    }

    public List<String> getSlots() {
        return slots;
    }
}
