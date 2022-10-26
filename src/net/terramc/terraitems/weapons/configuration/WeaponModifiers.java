package net.terramc.terraitems.weapons.configuration;

import net.terramc.terraitems.shared.AttributeConfiguration;

import java.util.ArrayList;
import java.util.List;

public class WeaponModifiers {
    private List<AttributeConfiguration> attributeConfigurations = new ArrayList<>();
    private List<String> enchantments = new ArrayList<>();

    public WeaponModifiers() { }


    public boolean hasAttributeConfigurations() {
        return !attributeConfigurations.isEmpty();
    }

    public boolean hasEnchantments() {
        return !enchantments.isEmpty();
    }

    public List<AttributeConfiguration> getAttributeConfigurations() {
        return attributeConfigurations;
    }

    public List<String> getEnchantments() {
        return enchantments;
    }

    public void setAttributeConfigurations(List<AttributeConfiguration> attributeConfigurations) {
        this.attributeConfigurations = attributeConfigurations;
    }

    public void setEnchantments(List<String> enchantments) {
        this.enchantments = enchantments;
    }
}
