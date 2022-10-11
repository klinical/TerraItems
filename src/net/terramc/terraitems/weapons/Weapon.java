package net.terramc.terraitems.weapons;

import net.terramc.terraitems.EquipmentMaterialType;
import net.terramc.terraitems.Rarity;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Weapon {
    private String name;
    private Rarity rarity = Rarity.COMMON;
    private String title;
    private ItemMeta meta;
    private WeaponType weaponType;

    private ItemStack itemStack;
    private EquipmentMaterialType weaponMaterialType;

    public Weapon(EquipmentMaterialType materialType, WeaponType weaponType) {
        itemStack = new ItemStack(materialType.getWeaponMaterial(weaponType));
        meta = itemStack.getItemMeta();
        this.weaponType = weaponType;
        weaponMaterialType = materialType;

        if (meta != null) {
            meta.setDisplayName(ChatColor.translateAlternateColorCodes(
                    '&',
                    "&r" + weaponMaterialType.getPrefix() + ' ' + weaponType.getDisplayName()));

            meta.addAttributeModifier(Attribute.GENERIC_ATTACK_SPEED, getAttackSpeedModifier());
            meta.addAttributeModifier(Attribute.GENERIC_ATTACK_DAMAGE, getAttackDamageModifier());
            meta.setLore(getWeaponInfoLore());

            // Use default WeaponType custom model
            meta.setCustomModelData(1);

            itemStack.setItemMeta(meta);
        }
    }

    public void setCustomModel(int model) {
        meta.setCustomModelData(model);
        itemStack.setItemMeta(meta);
    }

    private AttributeModifier getAttackSpeedModifier() {
        UUID uuid = UUID.randomUUID();
        String name = meta.getDisplayName() + Attribute.GENERIC_ATTACK_DAMAGE;

        return new AttributeModifier(
                uuid, name, weaponType.getAttributeSpeed(weaponMaterialType),
                AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.HAND);
    }

    private AttributeModifier getAttackDamageModifier() {
        UUID uuid = UUID.randomUUID();
        String name = meta.getDisplayName() + Attribute.GENERIC_ATTACK_DAMAGE;

        return new AttributeModifier(
                uuid, name, weaponType.getAttributeDamage(weaponMaterialType),
                AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.HAND);
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setTitle(String title) {
        this.title = title;

        if (meta != null) {
            if (rarity != Rarity.COMMON) {
                meta.setDisplayName(ChatColor.translateAlternateColorCodes(
                        '&',
                        "&" + rarity.titleColor().getChar() + title));
            } else {
                meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', title));
            }

            this.itemStack.setItemMeta(meta);
        } else {
            throw new IllegalStateException("Weapon Meta Object is null while setting title");
        }
    }

    // Set rarity and overwrite current item meta display name by adding the rarity title color code in front
    public void setRarity(Rarity rarity) {
        this.rarity = rarity;
    }

    private List<String> getWeaponInfoLore() {
        List<String> lore = new ArrayList<>();
        lore.add("");
        lore.add(ChatColor.translateAlternateColorCodes(
                '&', "&8" + weaponMaterialType.getPrefix()));
        lore.add(ChatColor.translateAlternateColorCodes(
                '&', "&8" + weaponType.getDisplayName() + "\n"));

        return lore;
    }

    public void setAttributes(ConfigurationSection attributeSection, FileConfiguration config) {
        Set<String> attributesKeys = attributeSection.getKeys(false);
        String sectionKey = attributeSection.getCurrentPath();

        if (meta != null) {
            for (String attribute : attributesKeys) {
                String attributeKey = sectionKey + "." + attribute;
                int amount = config.getInt(attributeKey + ".value");
                List<String> slots = config.getStringList(attributeKey + ".slots");

                for (String slot : slots) {
                    AttributeModifier modifier = new AttributeModifier(UUID.randomUUID(),
                            slot + "-" + attribute, amount,
                            AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.valueOf(slot.toUpperCase()));

                    meta.addAttributeModifier(Attribute.valueOf(attribute.toUpperCase()), modifier);
                }
            }

            this.itemStack.setItemMeta(meta);
        } else {
            throw new IllegalStateException("Weapon Meta Object is null while setting attributes");
        }
    }

    public void setLore(List<String> lore) {
        if (meta != null) {
            List<String> newLore = getWeaponInfoLore();
            List<String> translatedCustomLore = lore
                    .stream()
                    .map(l -> ChatColor.translateAlternateColorCodes('&', l))
                    .collect(Collectors.toList());

            newLore.addAll(translatedCustomLore);
            meta.setLore(newLore);

            this.itemStack.setItemMeta(meta);
        } else {
            throw new IllegalStateException("Weapon Meta Object is null while setting lore");
        }
    }

    public void setEnchantments(List<String> enchantments) {
        if (meta != null) {
            String regex = "(\\w+)\\s+(\\d+)";
            Pattern pattern = Pattern.compile(regex);

            for (String enchantment : enchantments) {
                Matcher matcher = pattern.matcher(enchantment);

                if (matcher.find()) {
                    String enchantmentName = matcher.group(1).toLowerCase();
                    int enchantmentLevel = Integer.parseInt(matcher.group(2));

                    Enchantment itemEnchantment = Enchantment.getByKey(NamespacedKey.minecraft(enchantmentName));
                    if (itemEnchantment != null) {
                        meta.addEnchant(itemEnchantment, enchantmentLevel, false);
                    } else {
                        throw new IllegalStateException("Invalid enchantment: " + enchantmentName);
                    }
                }
            }

            this.itemStack.setItemMeta(meta);
        } else {
            throw new IllegalStateException("Weapon Meta Object is null while setting enchantments");
        }
    }

    public String getTitle() {
        return title;
    }

    public Rarity getRarity() {
        return rarity;
    }

    public String getName() {
        return name;
    }

    public WeaponType getWeaponType() { return weaponType; }

    public ItemStack getItemStack() {
        return itemStack;
    }
}
