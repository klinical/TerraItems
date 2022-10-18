package net.terramc.terraitems.weapons;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import net.terramc.terraitems.TerraItems;
import net.terramc.terraitems.shared.AttributeConfiguration;
import net.terramc.terraitems.shared.EquipmentMaterialType;
import net.terramc.terraitems.shared.Rarity;
import net.terramc.terraitems.shared.TerraWeaponPersistentDataType;
import net.terramc.terraitems.weapons.configuration.WeaponConfiguration;
import net.terramc.terraitems.weapons.configuration.WeaponMeta;
import net.terramc.terraitems.weapons.configuration.WeaponModifiers;
import org.bukkit.ChatColor;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public abstract class Weapon {

    protected final WeaponConfiguration configuration;
    protected final ItemStack itemStack;

    public Weapon(WeaponConfiguration configuration) {
        this.configuration = configuration;

        WeaponType weaponType = configuration.getMeta().getWeaponType();
        EquipmentMaterialType materialType = configuration.getModifiers().getMaterialType();

        if (materialType != null) {
            this.itemStack = new ItemStack(configuration.getModifiers().getMaterialType().getWeaponMaterial(weaponType));
        } else {
            this.itemStack = new ItemStack(weaponType.getDefaultMaterialType().getWeaponMaterial(weaponType));
        }

        ItemMeta meta = Objects.requireNonNull(itemStack.getItemMeta());

        ArrayList<String> lore = new ArrayList<>();
        lore.add("");
        meta.setLore(lore);

        meta.setCustomModelData(configuration.getMeta().getCustomModel());
        meta.setAttributeModifiers(getDefaultModifiers());
        meta.setDisplayName(getDefaultDisplayName());

        setWeaponMeta(configuration.getMeta(), meta);
        setWeaponModifiers(configuration.getModifiers(), meta);
        setEnchantments(configuration.getModifiers().getEnchantments(), meta);

        PersistentDataContainer data  = meta.getPersistentDataContainer();
        NamespacedKey key = new NamespacedKey(TerraItems.lookupTerraPlugin(), "weapon");
        data.set(key, TerraWeaponPersistentDataType.DATA_TYPE, configuration);

        this.itemStack.setItemMeta(meta);
    }

    public abstract List<String> getWeaponInfoLore();

    protected abstract String getDefaultDisplayName();

    protected abstract ArrayListMultimap<Attribute, AttributeModifier> getDefaultModifiers();

    private void setWeaponMeta(@Nonnull WeaponMeta weaponMeta, ItemMeta meta) {
        Rarity rarity = weaponMeta.getRarity();

        if (meta == null)
            throw new IllegalStateException("ItemStack Meta object is null while setting WeaponMeta " + weaponMeta);

        List<String> lore = meta.getLore() != null ? meta.getLore() : new ArrayList<>();

        lore.addAll(getWeaponInfoLore());

        if (weaponMeta.hasTitle())
            meta.setDisplayName(ChatColor.translateAlternateColorCodes(
                    '&',
                    "&" + rarity.titleColor().getChar() + weaponMeta.getTitle()));

        if (weaponMeta.hasCustomLore()) {
            List<String> translatedCustomLore = weaponMeta.getCustomLore()
                    .stream()
                    .map(l -> ChatColor.translateAlternateColorCodes('&', l))
                    .collect(Collectors.toList());

            lore.add("");
            lore.addAll(translatedCustomLore);
        }

        meta.setCustomModelData(weaponMeta.getCustomModel());

        meta.setLore(lore);
    }

    private Multimap<Attribute, AttributeModifier> buildModifiers(List<AttributeConfiguration> configurations) {
        Multimap<Attribute, AttributeModifier> modifiers = ArrayListMultimap.create();

        for (AttributeConfiguration configuration : configurations) {
            for (String slot : configuration.getSlots()) {
                AttributeModifier modifier = new AttributeModifier(UUID.randomUUID(),
                        slot + "-" + configuration.getAttribute(), configuration.getValue(),
                        AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.valueOf(slot.toUpperCase()));

                modifiers.put(configuration.getAttribute(), modifier);
            }
        }

        return modifiers;
    }

    private void setWeaponModifiers(@Nonnull WeaponModifiers weaponModifiers, ItemMeta meta) {
        if (weaponModifiers.hasEnchantments())
            setEnchantments(weaponModifiers.getEnchantments(), meta);

        if (weaponModifiers.hasAttributeConfigurations())
            meta.setAttributeModifiers(buildModifiers(weaponModifiers.getAttributeConfigurations()));

        if (weaponModifiers.hasEffects() && weaponModifiers.hasEffectLore()) {
            setEffectLore(weaponModifiers.getEffectLore(), meta);
        }
    }

    private void setEffectLore(List<String> effectLore, @Nonnull ItemMeta itemMeta) {
        List<String> lore = Objects.requireNonNull(itemMeta.getLore());

        lore.add("");
        lore.addAll(effectLore);

        itemMeta.setLore(lore);
    }

    private void setEnchantments(List<String> enchantmentStringList, ItemMeta meta) {
        String regex = "(\\w+)\\s+(\\d+)";
        Pattern pattern = Pattern.compile(regex);

        for (String enchantment : enchantmentStringList) {
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
    }

    public WeaponConfiguration getConfiguration() {
        return configuration;
    }

    public ItemStack getItemStack() {
        return itemStack;
    }
}
