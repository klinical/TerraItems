package net.terramc.terraitems.weapons;

import net.terramc.terraitems.effects.Effect;
import net.terramc.terraitems.effects.TerraEffect;
import net.terramc.terraitems.shared.EquipmentMaterialType;
import net.terramc.terraitems.shared.Rarity;
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
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Weapon {
    private String name;
    private String title;

    private final ItemStack itemStack;
    private final ItemMeta meta;
    private List<TerraEffect> effects;

    private List<String> customLore = new ArrayList<>();
    private List<String> attributeLore = new ArrayList<>();
    private List<String> effectLore = new ArrayList<>();

    private final WeaponType weaponType;
    private final EquipmentMaterialType materialType;
    private Rarity rarity = Rarity.COMMON; // All items default to common unless changed

    public Weapon(EquipmentMaterialType materialType, WeaponType weaponType) {
        this.weaponType = weaponType;
        this.materialType = materialType;
        this.itemStack = new ItemStack(materialType.getWeaponMaterial(weaponType));
        this.meta = itemStack.getItemMeta();

        if (meta != null) {
            meta.setDisplayName(ChatColor.translateAlternateColorCodes(
                    '&',
                    "&r" + materialType.getPrefix() + ' ' + weaponType.getDisplayName()));

            meta.addAttributeModifier(Attribute.GENERIC_ATTACK_SPEED, getAttackSpeedModifier());
            meta.addAttributeModifier(Attribute.GENERIC_ATTACK_DAMAGE, getAttackDamageModifier());

            // Use default WeaponType custom model
            meta.setCustomModelData(1);

            itemStack.setItemMeta(meta);
        }
    }

    public List<TerraEffect> getEffects() {
        return effects;
    }

    public boolean hasCustomEffect() {
        return !effects.isEmpty();
    }

    public void setCustomModel(int model) {
        meta.setCustomModelData(model);
        this.itemStack.setItemMeta(meta);
    }

    public void setEffects(List<TerraEffect> effects) {
        this.effects = effects;

        for (TerraEffect effect : effects) {

            if (effect.getMeta() != null && effect.getMeta().getDisplay() != null) {
                String displayLore = effect.getMeta().getDisplay();
                String[] l = displayLore.split("\n");

                Bukkit.getLogger().warning(displayLore);
                Bukkit.getLogger().warning(l.toString());

                for (String ls : l) {
                    effectLore.add(ChatColor.translateAlternateColorCodes('&', "&a" + ls));
                }
            }
        }
    }

    public void buildLore() {
        List<String> lore = new ArrayList<>();

        lore.addAll(getWeaponInfoLore());
        lore.addAll(effectLore);

        if (!effectLore.isEmpty())
            lore.add("");

        meta.setLore(lore);
        this.itemStack.setItemMeta(meta);
    }

    private AttributeModifier getAttackSpeedModifier() {
        UUID uuid = UUID.randomUUID();
        String name = meta.getDisplayName() + Attribute.GENERIC_ATTACK_DAMAGE;

        return new AttributeModifier(
                uuid, name, weaponType.getAttributeSpeed(materialType),
                AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.HAND);
    }

    private AttributeModifier getAttackDamageModifier() {
        UUID uuid = UUID.randomUUID();
        String name = meta.getDisplayName() + Attribute.GENERIC_ATTACK_DAMAGE;

        return new AttributeModifier(
                uuid, name, weaponType.getAttributeDamage(materialType),
                AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.HAND);
    }

    // Name is the name of the weapon as it were an entry in the weapons.yml
    public void setName(String name) {
        this.name = name;
        PersistentDataContainer data  = meta.getPersistentDataContainer();
        NamespacedKey key = new NamespacedKey(Bukkit.getPluginManager().getPlugin("Terra-Items"), "weapon-name");
        data.set(key,
                PersistentDataType.STRING,
                name);

        this.itemStack.setItemMeta(meta);
    }

    // Title is the name of the weapon as it were set in the weapons.yml in combination with the color code of the
    // appropriate 'rarity' setting on the weapon
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

    // How 'common' the item is, affects the default color of the in-game item display name
    public void setRarity(Rarity rarity) {
        this.rarity = rarity;
    }

    // The lore regarding the weaponType and EquipmentMaterialType
    private List<String> getWeaponInfoLore() {
        List<String> lore = new ArrayList<>();
        lore.add("");
        lore.add(ChatColor.translateAlternateColorCodes(
                '&',
                Rarity.getPrefix(rarity) + rarity.getDisplayName() + "&r &8" + materialType.getPrefix() + " " + weaponType.getDisplayName()));

        return lore;
    }

    // Base stat attributes such as attack damage, knockback
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
            List<String> translatedCustomLore = lore
                    .stream()
                    .map(l -> ChatColor.translateAlternateColorCodes('&', l))
                    .collect(Collectors.toList());

            customLore.addAll(translatedCustomLore);
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
