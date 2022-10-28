package net.terramc.terraitems.weapons;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import net.terramc.terraitems.TerraItems;
import net.terramc.terraitems.effects.TerraEffect;
import net.terramc.terraitems.effects.configuration.EffectTrigger;
import net.terramc.terraitems.shared.*;
import net.terramc.terraitems.weapons.configuration.*;
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
import org.checkerframework.checker.nullness.qual.NonNull;
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
    @Nullable protected List<StatModifier> statModifiers;

    public Weapon(@NotNull String weaponName, WeaponType weaponType) {
        this.itemStack = new ItemStack(weaponType.getVanillaItem());
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

    private void setInitialDefaultMeta() {
        ItemMeta meta = Objects.requireNonNull(itemStack.getItemMeta());

        String displayName = "&r" + weaponType.getDisplayName();
        meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', displayName));

        meta.setCustomModelData(weaponType.getDefaultModel());
        meta.setAttributeModifiers(getDefaultModifiers());

        PersistentDataContainer data  = meta.getPersistentDataContainer();
        NamespacedKey key = new NamespacedKey(TerraItems.lookupTerraPlugin(), NamespaceKeys.WEAPON_KEY);
        data.set(key, PersistentDataType.STRING, weaponName);

        List<String> baseLore = new ArrayList<>(getRarityAndWeaponTypeLore());
        meta.setLore(baseLore);

        itemStack.setItemMeta(meta);
    }

    @Nullable
    public List<StatModifier> getStatModifiers() {
        return statModifiers;
    }

    public void setStatModifiers(@NonNull List<StatModifier> statModifiers) {
        this.statModifiers = statModifiers;
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
        if (weaponMeta.hasTitle())
            meta.setDisplayName(ChatColor.translateAlternateColorCodes(
                    '&',
                    "&" + rarity.titleColor().getChar() + weaponMeta.getTitle()));

        meta.setCustomModelData(weaponMeta.getCustomModel());
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

    protected List<String> buildLore() {
        List<String> newLore = new ArrayList<>(getWeaponInformationLore());

        if (this.weaponMeta.hasCustomLore()) {
            newLore.add("");
            newLore.addAll(this.weaponMeta.getCustomLore());
        }

        if (this.hasWeaponEffects()) {
            newLore.add("");
            newLore.addAll(getEffectLore());
        }

        return newLore;
    }

    protected List<String> getWeaponInformationLore() {
        List<String> lore = new ArrayList<>(getRarityAndWeaponTypeLore());
        if (this.statModifiers != null)
            lore.addAll(getStatModifierLore());

        return lore;
    }

    protected List<String> getEffectLore() {
        if (this.weaponEffects == null)
            return new ArrayList<>();

        Set<String> effectNames = this.weaponEffects.keySet();
        List<String> effectLore = new ArrayList<>();
        for (String effectName : effectNames) {
            TerraEffect effect = TerraItems.lookupTerraPlugin().getEffectsConfig().getItems().get(effectName);

            if (effect.getMeta() != null && effect.getMeta().getDisplay() != null) {
                String displayLore = effect.getMeta().getDisplay();
                String[] l = displayLore.split("\n");

                for (String ls : l) {
                    effectLore.add(ChatColor.translateAlternateColorCodes('&', "&a" + ls));
                }
            }
        }

        return effectLore;
    }

    protected List<String> getStatModifierLore() {
        if (this.statModifiers != null)
            return this.statModifiers
                    .stream()
                    .map((statModifier -> ChatColor.GREEN + "+" +
                            statModifier.getAmount() + " " +
                            statModifier.getDisplayName()))
                    .collect(Collectors.toList());
        else
            return new ArrayList<>();
    }

    public @NotNull ItemStack getItemStack() {
        return itemStack;
    }

    public @NotNull WeaponType getWeaponType() {
        return weaponType;
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

    public List<String> getRarityAndWeaponTypeLore() {
        Rarity rarity = weaponMeta.getRarity();

        String loreLine = ChatColor.translateAlternateColorCodes(
                '&',
                Rarity.getPrefix(rarity) +
                        rarity.getDisplayName() +
                        "&r &7" + weaponType.getDisplayName());

        List<String> loreList = new ArrayList<>();
        loreList.add(loreLine);

        return loreList;
    }

    protected abstract ArrayListMultimap<Attribute, AttributeModifier> getDefaultModifiers();
}
