package net.terramc.terraitems.weapons;

import com.google.common.collect.ArrayListMultimap;
import net.terramc.terraitems.TerraItems;
import net.terramc.terraitems.shared.EquipmentMaterialType;
import net.terramc.terraitems.shared.Rarity;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public abstract class Weapon {
    protected final WeaponType weaponType;
    protected final EquipmentMaterialType materialType;

    protected WeaponMeta weaponMeta;
    protected WeaponModifiers weaponModifiers;

    protected final ItemStack itemStack;

    public Weapon(String configKey, WeaponType weaponType, EquipmentMaterialType materialType) {
        this.weaponType = weaponType;
        this.materialType = materialType;
        this.itemStack = new ItemStack(materialType.getWeaponMaterial(weaponType));

        ItemMeta meta = Objects.requireNonNull(itemStack.getItemMeta());

        ArrayList<String> lore = new ArrayList<>();
        lore.add("");
        meta.setLore(lore);

        meta.setCustomModelData(weaponType.getDefaultModel());
        meta.setAttributeModifiers(getDefaultModifiers());
        meta.setDisplayName(getDefaultDisplayName());

        PersistentDataContainer data  = meta.getPersistentDataContainer();
        NamespacedKey key = new NamespacedKey(TerraItems.lookupTerraPlugin(), "weapon-name");
        data.set(key, PersistentDataType.STRING,configKey);

        this.itemStack.setItemMeta(meta);
    }

    public Weapon(String configKey, WeaponType weaponType) {
        this.weaponType = weaponType;
        this.materialType = weaponType.getDefaultMaterialType();
        this.itemStack = new ItemStack(materialType.getWeaponMaterial(weaponType));
    }

    public abstract List<String> getWeaponInfoLore();

    protected abstract String getDefaultDisplayName();

    protected abstract ArrayListMultimap<Attribute, AttributeModifier> getDefaultModifiers();

    public void setWeaponMeta(@Nonnull WeaponMeta weaponMeta) {
        this.weaponMeta = weaponMeta;

        Rarity rarity = weaponMeta.getRarity();
        ItemMeta meta = itemStack.getItemMeta();
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

            lore.addAll(translatedCustomLore);
        }

        meta.setCustomModelData(weaponMeta.getCustomModel());

        meta.setLore(lore);
        this.itemStack.setItemMeta(meta);
    }

    public void setWeaponModifiers(@Nonnull WeaponModifiers weaponModifiers) {
        this.weaponModifiers = weaponModifiers;
        ItemMeta meta = Objects.requireNonNull(itemStack.getItemMeta());

        if (weaponModifiers.hasEnchantments())
            setEnchantments(weaponModifiers.getEnchantments(), meta);

        if (weaponModifiers.hasAttributeModifiers())
            meta.setAttributeModifiers(weaponModifiers.getAttributeModifiers());

        if (weaponModifiers.hasEffects() && weaponModifiers.hasEffectLore()) {
            setEffectLore(weaponModifiers.getEffectLore(), meta);
        }

        this.itemStack.setItemMeta(meta);
    }

    private void setEffectLore(List<String> effectLore, @Nonnull ItemMeta itemMeta) {
        List<String> lore = Objects.requireNonNull(itemMeta.getLore());
        List<String> weaponInfoLore = lore.subList(0, 3);
        List<String> theRest = lore.subList(3, lore.size());

        List<String> newLore = new ArrayList<>();

        newLore.addAll(weaponInfoLore);
        newLore.addAll(effectLore);
        if (!theRest.isEmpty()) {
            newLore.add("");
            newLore.addAll(theRest);
        }

        itemMeta.setLore(newLore);
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

    public boolean hasWeaponMeta() {
        return weaponMeta != null;
    }

    public boolean hasWeaponModifiers() {
        return weaponModifiers != null;
    }

    public ItemStack getItemStack() {
        return itemStack;
    }

    public WeaponType getWeaponType() {
        return weaponType;
    }

    public EquipmentMaterialType getMaterialType() {
        return materialType;
    }

    public WeaponMeta getWeaponMeta() {
        return weaponMeta;
    }

    public WeaponModifiers getWeaponModifiers() {
        return weaponModifiers;
    }
}
