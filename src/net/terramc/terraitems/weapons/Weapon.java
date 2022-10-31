package net.terramc.terraitems.weapons;

import net.terramc.terraitems.item.Item;
import net.terramc.terraitems.TerraItems;
import net.terramc.terraitems.effects.TerraEffect;
import net.terramc.terraitems.effects.configuration.EffectTrigger;
import net.terramc.terraitems.shared.*;
import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;
import java.util.stream.Collectors;

public abstract class Weapon extends Item {

    @Nonnull protected WeaponType weaponType;
    @Nullable protected HashMap<String, EffectTrigger> weaponEffects;
    @Nullable protected List<StatModifier> statModifiers;

    public Weapon(@NotNull String weaponName, WeaponType weaponType) {
        super(weaponName, weaponType.getVanillaItem(), ItemType.WEAPON);

        this.weaponType = weaponType;

        setItemModifiers(getDefaultModifiers());
        setDisplayName(weaponType.getDisplayName());
        setLore(getItemDescriptionLore());
        setCustomModel(getDefaultModel());
    }

    @Nullable
    public List<StatModifier> getStatModifiers() {
        return statModifiers;
    }

    protected @NotNull List<String> getPreCustomLoreLore() {
        return getStatLore();
    }

    @NotNull
    @Override
    protected List<String> getPostCustomLoreLore() {
        return getEffectLore();
    }

    protected List<String> getStatLore() {
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

    protected List<String> getEffectLore() {
        Set<String> effectNames = weaponEffects != null ?
                weaponEffects.keySet() : new HashSet<>();

        List<String> effectLore = new ArrayList<>();
        for (String effectName : effectNames) {
            TerraEffect effect = TerraItems.lookupTerraPlugin().getEffectsConfig().getItems().get(effectName);

            if (effect.getMeta() != null && effect.getMeta().getDisplay() != null) {
                String displayLore = effect.getMeta().getDisplay();
                String[] l = displayLore.split("\n");

                for (String ls : l) {
                    effectLore.add(ChatColor.translateAlternateColorCodes(
                            '&',
                            "&a" + ls)
                    );
                }
            }
        }

        return effectLore;
    }

    @NotNull
    protected String getItemDescriptionString() {
        return weaponType.getDisplayName();
    }

    public @NotNull ItemStack getItemStack() {
        return itemStack;
    }

    public @NotNull WeaponType getWeaponType() {
        return weaponType;
    }

    public @Nullable HashMap<String, EffectTrigger> getWeaponEffects() {
        return weaponEffects;
    }

    @Override
    protected int getDefaultModel() {
        return weaponType.getDefaultModel();
    }

    public boolean hasWeaponEffects() {
        return weaponEffects != null && !weaponEffects.isEmpty();
    }

    public void setStatModifiers(@NonNull List<StatModifier> statModifiers) {
        this.statModifiers = statModifiers;

        this.updateLore();
    }

    public void setEffects(HashMap<String, EffectTrigger> effects) {
        this.weaponEffects = effects;

        this.updateLore();
    }

    protected abstract List<AttributeConfiguration> getDefaultModifiers();
}
