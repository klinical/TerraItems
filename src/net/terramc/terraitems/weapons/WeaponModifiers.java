package net.terramc.terraitems.weapons;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import net.terramc.terraitems.TerraItems;
import net.terramc.terraitems.effects.TerraEffect;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.EquipmentSlot;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class WeaponModifiers {
    private final List<TerraEffect> effects = new ArrayList<>();
    private final List<String> effectLore = new ArrayList<>();
    private final Multimap<Attribute, AttributeModifier> attributeModifiers = ArrayListMultimap.create();
    private final List<String> enchantments;

    public WeaponModifiers(ConfigurationSection section) {
        enchantments = section.getStringList("enchantments");
        List<String> effectsStringList = section.getStringList("effects");
        List<String> attributesStringList = section.getStringList("attributes");

        if (!(attributesStringList.isEmpty())) {
            for (String attribute : attributesStringList) {
                int amount = section.getInt("value");
                List<String> slots = section.getStringList("slots");

                for (String slot : slots) {
                    AttributeModifier modifier = new AttributeModifier(UUID.randomUUID(),
                            slot + "-" + attribute, amount,
                            AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.valueOf(slot.toUpperCase()));

                    attributeModifiers.put(Attribute.valueOf(attribute.toUpperCase()), modifier);
                }
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

    public boolean hasEffects() {
        return !effects.isEmpty();
    }

    public boolean hasAttributeModifiers() {
        return !attributeModifiers.isEmpty();
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

    public Multimap<Attribute, AttributeModifier> getAttributeModifiers() {
        return attributeModifiers;
    }

    public List<String> getEnchantments() {
        return enchantments;
    }
}
