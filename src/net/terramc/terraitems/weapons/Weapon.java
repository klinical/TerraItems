package net.terramc.terraitems.weapons;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import net.terramc.terraitems.TerraItems;
import net.terramc.terraitems.effects.EffectTriggerType;
import net.terramc.terraitems.effects.TerraEffect;
import net.terramc.terraitems.effects.configuration.EffectTrigger;
import net.terramc.terraitems.shared.*;
import net.terramc.terraitems.spells.Spell;
import net.terramc.terraitems.weapons.configuration.*;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public abstract class Weapon {

    @Nonnull protected WeaponType weaponType;
    @Nonnull protected String weaponName;
    @Nonnull protected final ItemStack itemStack;
    @Nonnull protected WeaponMeta weaponMeta;
    @Nullable protected HashMap<String, EffectTrigger> weaponEffects;
    @Nullable protected WeaponModifiers weaponModifiers;
    private Spell spell;

    public Weapon(@NotNull String weaponName, WeaponType weaponType) {
        this.itemStack = new ItemStack(weaponType.getDefaultMaterialType().getWeaponMaterial(weaponType));
        this.weaponType = weaponType;
        this.weaponName = weaponName;
        this.weaponMeta = new WeaponMeta(weaponType);

        setInitialDefaultMeta();
    }

    public Weapon(
            @NotNull String weaponName,
            @NotNull WeaponType weaponType,
            @NotNull EquipmentMaterialType materialType
    ) {
        this.itemStack = new ItemStack(materialType.getWeaponMaterial(weaponType));
        this.weaponType = weaponType;
        this.weaponName = weaponName;
        this.weaponMeta = new WeaponMeta(weaponType);

        setInitialDefaultMeta();
    }

    public boolean hasWeaponEffects() {
        return weaponEffects != null && !weaponEffects.isEmpty();
    }

    public boolean hasWeaponModifiers() {
        return weaponModifiers != null;
    }

    public void setSpell(Spell spell) {
        this.spell = spell;
    }

    private void setInitialDefaultMeta() {
        ItemMeta meta = Objects.requireNonNull(itemStack.getItemMeta());

        ArrayList<String> lore = new ArrayList<>();
        lore.add("");
        meta.setLore(lore);

        meta.setCustomModelData(weaponType.getDefaultModel());
        meta.setAttributeModifiers(getDefaultModifiers());

        PersistentDataContainer data  = meta.getPersistentDataContainer();
        NamespacedKey key = new NamespacedKey(TerraItems.lookupTerraPlugin(), NamespaceKeys.WEAPON_KEY);
        data.set(key, PersistentDataType.STRING, weaponName);

        itemStack.setItemMeta(meta);
    }

    public void setEffects(HashMap<String, EffectTrigger> effects) {
        this.weaponEffects = effects;
    }

    public void setWeaponMeta(@Nonnull WeaponMeta weaponMeta) {
        this.weaponMeta = weaponMeta;

        ItemMeta meta = itemStack.getItemMeta();
        if (meta == null)
            throw new IllegalStateException("ItemStack ItemMeta is null");

        Rarity rarity = weaponMeta.getRarity();
        List<String> lore = meta.getLore() != null ? meta.getLore() : new ArrayList<>();

        lore.addAll(getWeaponInfoLore());

        if (weaponMeta.hasTitle())
            meta.setDisplayName(ChatColor.translateAlternateColorCodes(
                    '&',
                    "&" + rarity.titleColor().getChar() + weaponMeta.getTitle()));

        if (weaponMeta.hasCustomLore()) {
            List<String> translatedCustomLore = weaponMeta.getCustomLore()
                    .stream()
                    .map(l -> {
                        String builder = "&f" + l;
                        return ChatColor.translateAlternateColorCodes('&', builder);
                    })
                    .collect(Collectors.toList());

            lore.add("");
            lore.addAll(translatedCustomLore);
            Bukkit.getLogger().warning(lore + "");
        }

        meta.setCustomModelData(weaponMeta.getCustomModel());
        Bukkit.getLogger().warning(lore + "");
        meta.setLore(lore);

        Bukkit.getLogger().warning(lore + "");
        Bukkit.getLogger().warning(meta.getLore() + "");

        itemStack.setItemMeta(meta);
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

    public void setWeaponModifiers(@Nonnull WeaponModifiers weaponModifiers) {
        this.weaponModifiers = weaponModifiers;

        ItemMeta meta = itemStack.getItemMeta();
        if (meta == null)
            throw new IllegalStateException("ItemStack ItemMeta is null");

        if (weaponModifiers.hasEnchantments())
            setEnchantments(weaponModifiers.getEnchantments(), meta);

        if (weaponModifiers.hasAttributeConfigurations())
            meta.setAttributeModifiers(buildModifiers(weaponModifiers.getAttributeConfigurations()));
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

        itemStack.setItemMeta(meta);
    }

    public @NotNull ItemStack getItemStack() {
        return itemStack;
    }

    public @NotNull WeaponType getWeaponType() {
        return weaponType;
    }

    public Spell getSpell() {
        return spell;
    }

    public @NotNull String getWeaponName() {
        return weaponName;
    }

    public @NotNull WeaponMeta getWeaponMeta() {
        return weaponMeta;
    }

    public @Nullable HashMap<String, EffectTrigger> getWeaponEffects() {
        return weaponEffects;
    }

    public @Nullable WeaponModifiers getWeaponModifiers() {
        return weaponModifiers;
    }

    public abstract List<String> getWeaponInfoLore();

    protected abstract ArrayListMultimap<Attribute, AttributeModifier> getDefaultModifiers();
}
