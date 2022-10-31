package net.terramc.terraitems.item;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import net.terramc.terraitems.TerraItems;
import net.terramc.terraitems.shared.AttributeConfiguration;
import net.terramc.terraitems.shared.ItemType;
import net.terramc.terraitems.shared.Rarity;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public abstract class Item {
    @Nonnull
    protected String itemName;
    protected ItemType itemType;
    protected Rarity rarity;
    @Nonnull
    protected final ItemStack itemStack;
    protected List<String> customLore;
    protected ItemMeta itemMeta;

    public Item(@NotNull String itemName, Material mat, ItemType itemType) {
        this.itemName = itemName;
        this.itemStack = new ItemStack(mat, 1);
        this.itemMeta = itemStack.getItemMeta();
        this.rarity = Rarity.COMMON;
        this.itemType = itemType;
        this.customLore = new ArrayList<>();

        setInitialPdc();
    }

    private void setInitialPdc() {
        PersistentDataContainer pdc = itemMeta.getPersistentDataContainer();
        pdc.set(
                new NamespacedKey(TerraItems.lookupTerraPlugin(), itemType.toString()),
                PersistentDataType.STRING,
                itemName
        );

        updateMeta();
    }

    public void setRarity(Rarity rarity) {
        this.rarity = rarity;
        updateLore();
    }

    public void setCustomModel(int model) {
        itemMeta.setCustomModelData(model);

        updateMeta();
    }

    public void setDisplayName(String displayName) {
        itemMeta.setDisplayName(ChatColor.translateAlternateColorCodes(
                '&',
                "&" + rarity.titleColor().getChar() + displayName));

        updateMeta();
    }

    public void setLore(List<String> lore) {
        itemMeta.setLore(lore);
        updateMeta();
    }

    public void setCustomLore(List<String> lore) {
        customLore = lore.stream()
                .map((l) -> ChatColor.GRAY + l)
                .collect(Collectors.toList());

        updateLore();
    }

    public void setMetaFromConfigurationSection(ConfigurationSection metaSection) {
        List<String> customLoreList = metaSection.getStringList("lore");
        if (!customLoreList.isEmpty())
            setCustomLore(customLoreList);

        String displayName = metaSection.getString("display-name");
        if (displayName != null)
            setDisplayName(displayName);

        if (metaSection.contains("model"))
            setCustomModel(metaSection.getInt("model"));

        updateMeta();
    }

    public void setVanillaAttributeModifiersFromConfigurationSection(ConfigurationSection modifierSection) {
        ConfigurationSection attributeSection = modifierSection.getConfigurationSection("attributes");
        List<String> enchantmentStringList = modifierSection.getStringList("enchantments");

        setEnchantments(enchantmentStringList);

        if (attributeSection != null) {
            List<AttributeConfiguration> attributeConfigurations = new ArrayList<>();
            for (String attributeName : attributeSection.getKeys(false)) {
                ConfigurationSection attributeConfiguration = Objects.requireNonNull(
                        attributeSection.getConfigurationSection(attributeName)
                );

                int amount = attributeConfiguration.getInt("value");
                List<EquipmentSlot> slots = attributeSection
                        .getStringList("slots")
                        .stream()
                        .map((s) -> EquipmentSlot.valueOf(s.toUpperCase()))
                        .collect(Collectors.toList());
                Attribute attributeParsed = Attribute.valueOf(attributeName.toUpperCase());

                attributeConfigurations.add(new AttributeConfiguration(attributeParsed, amount, slots));
            }

            setItemModifiers(attributeConfigurations);
        }
    }

    public void setEnchantments(List<String> enchantmentStringList) {
        String regex = "(\\w+)\\s+(\\d+)";
        Pattern pattern = Pattern.compile(regex);

        for (String enchantment : enchantmentStringList) {
            Matcher matcher = pattern.matcher(enchantment);

            if (matcher.find()) {
                String enchantmentName = matcher.group(1).toLowerCase();
                int enchantmentLevel = Integer.parseInt(matcher.group(2));

                Enchantment itemEnchantment = Enchantment.getByKey(NamespacedKey.minecraft(enchantmentName));
                if (itemEnchantment != null) {
                    itemMeta.addEnchant(itemEnchantment, enchantmentLevel, false);
                } else {
                    throw new IllegalStateException("Invalid enchantment: " + enchantmentName);
                }
            }
        }

        updateMeta();
    }

    public void setItemModifiers(List<AttributeConfiguration> configurations) {
        Multimap<Attribute, AttributeModifier> modifiers = ArrayListMultimap.create();

        for (AttributeConfiguration configuration : configurations) {
            for (EquipmentSlot slot : configuration.getSlots()) {
                AttributeModifier modifier = new AttributeModifier(
                        UUID.randomUUID(),
                        slot + "-" + configuration.getAttribute(),
                        configuration.getValue(),
                        AttributeModifier.Operation.ADD_NUMBER, slot
                );

                modifiers.put(configuration.getAttribute(), modifier);
            }
        }

        itemMeta.setAttributeModifiers(modifiers);
        updateMeta();
    }

    protected void updateMeta() {
        itemStack.setItemMeta(itemMeta);
    }

    protected void updateLore() {
        List<String> newLore = new ArrayList<>(getItemDescriptionLore());

        List<String> preCustomLoreLore = getPreCustomLoreLore();
        if (preCustomLoreLore.size() > 0) {
            newLore.add("");
            newLore.addAll(preCustomLoreLore);
        }

        if (customLore.size() > 0) {
            newLore.add("");
            newLore.addAll(customLore);
        }

        List<String> postCustomLoreLore = getPostCustomLoreLore();
        if (postCustomLoreLore.size() > 0) {
            newLore.add("");
            newLore.addAll(postCustomLoreLore);
        }

        itemMeta.setLore(newLore);
        updateMeta();
    }

    protected List<String> getItemDescriptionLore() {
        String loreLine = ChatColor.translateAlternateColorCodes(
                '&',
                Rarity.getPrefix(rarity) +
                        rarity.getDisplayName() +
                        "&r &7" + getItemDescriptionString());

        List<String> loreList = new ArrayList<>();
        loreList.add(loreLine);

        return loreList;
    }

    protected abstract int getDefaultModel();

    /**
     *
     * @return The lore that appears before the 'custom lore' of the item
     */
    @Nonnull protected abstract List<String> getPreCustomLoreLore();

    /**
     *
     * @return The lore that appears after the 'custom lore' of the item
     */
    @Nonnull protected abstract List<String> getPostCustomLoreLore();
    @Nonnull protected abstract String getItemDescriptionString();

    @Nonnull
    public String getItemName() {
        return itemName;
    }

    @Nonnull
    public ItemStack getItemStack() {
        return itemStack;
    }
}
