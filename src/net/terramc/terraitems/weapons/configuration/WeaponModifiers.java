package net.terramc.terraitems.weapons.configuration;

import net.terramc.terraitems.TerraItems;
import net.terramc.terraitems.effects.TerraEffect;
import net.terramc.terraitems.shared.AttributeConfiguration;
import net.terramc.terraitems.shared.EquipmentMaterialType;
import org.bukkit.ChatColor;
import org.bukkit.attribute.Attribute;
import org.bukkit.configuration.ConfigurationSection;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class WeaponModifiers {
    @Nullable private final EquipmentMaterialType materialType;
    private final List<TerraEffect> effects = new ArrayList<>();
    private final List<String> effectLore = new ArrayList<>();
    private final List<AttributeConfiguration> attributeConfigurations;
    private final List<String> enchantments;

    public WeaponModifiers(ConfigurationSection section) {
        enchantments = section.getStringList("enchantments");
        attributeConfigurations = new ArrayList<>();

        String materialTypeString = section.getString("material");
        List<String> effectsStringList = section.getStringList("effects");
        List<String> attributesStringList = section.getStringList("attributes");

        materialType = (materialTypeString != null) ?
                EquipmentMaterialType.valueOf(materialTypeString.toUpperCase()) :
                null;

        if (!(attributesStringList.isEmpty())) {
            for (String attribute : attributesStringList) {
                int amount = section.getInt("value");
                List<String> slots = section.getStringList("slots");
                Attribute attributeParsed = Attribute.valueOf(attribute.toUpperCase());

                this.attributeConfigurations.add(new AttributeConfiguration(attributeParsed, amount, slots));
            }
        }

        if (!(effectsStringList.isEmpty())) {
            List<TerraEffect> parsedEffects = effectsStringList
                    .stream()
                    .map(effect -> TerraItems.lookupTerraPlugin().getEffectsConfig().getItems().get(effect))
                    .collect(Collectors.toList());

            for (TerraEffect effect : parsedEffects) {
                if (effect.getMeta() != null && effect.getMeta().getDisplay() != null) {
                    String displayLore = effect.getMeta().getDisplay();
                    String[] l = displayLore.split("\n");

                    for (String ls : l) {
                        effectLore.add(ChatColor.translateAlternateColorCodes('&', "&a" + ls));
                    }
                }

                effects.add(effect);
            }
        }
    }

    public WeaponModifiers(EquipmentMaterialType type) {
        Objects.requireNonNull(type);
        this.materialType = type;

        this.attributeConfigurations = new ArrayList<>();
        this.enchantments = new ArrayList<>();
    }

    public boolean hasMaterialType() {
        return materialType != null;
    }

    public boolean hasEffects() {
        return !effects.isEmpty();
    }

    public boolean hasAttributeConfigurations() {
        return !attributeConfigurations.isEmpty();
    }

    public boolean hasEnchantments() {
        return !enchantments.isEmpty();
    }

    public boolean hasEffectLore() {
        return !effectLore.isEmpty();
    }

    public List<String> getEffectLore() {
        return effectLore;
    }

    public List<TerraEffect> getEffects() {
        return effects;
    }

    public List<AttributeConfiguration> getAttributeConfigurations() {
        return attributeConfigurations;
    }

    public @Nullable EquipmentMaterialType getMaterialType() {
        return materialType;
    }

    public List<String> getEnchantments() {
        return enchantments;
    }
}
