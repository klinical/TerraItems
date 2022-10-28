package net.terramc.terraitems.weapons;

import net.terramc.terraitems.TerraItems;
import net.terramc.terraitems.effects.EffectTriggerType;
import net.terramc.terraitems.effects.TerraEffect;
import net.terramc.terraitems.effects.configuration.EffectTrigger;
import net.terramc.terraitems.shared.AttributeConfiguration;
import net.terramc.terraitems.shared.Rarity;
import net.terramc.terraitems.shared.StatModifier;
import net.terramc.terraitems.shared.StatModifierType;
import net.terramc.terraitems.spells.DragonBreathe;
import net.terramc.terraitems.spells.Spell;
import net.terramc.terraitems.weapons.configuration.*;
import net.terramc.terraitems.weapons.magic.SpellBook;
import net.terramc.terraitems.weapons.magic.Staff;
import net.terramc.terraitems.weapons.magic.Wand;
import net.terramc.terraitems.weapons.melee.*;
import net.terramc.terraitems.weapons.ranged.*;
import org.bukkit.ChatColor;
import org.bukkit.attribute.Attribute;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.meta.ItemMeta;

import javax.annotation.Nullable;
import java.util.*;
import java.util.stream.Collectors;

public class WeaponBuilder {
    private WeaponType weaponType;
    private WeaponMeta meta;
    private WeaponModifiers modifiers;
    private ProjectileModifiers projectileModifiers;
    private List<StatModifier> statModifiers;
    private HashMap<String, EffectTrigger> effects;
    private Spell spell;

    private String weaponName;

    public Weapon build() {
        Objects.requireNonNull(weaponName);
        Objects.requireNonNull(weaponType);

        Weapon weapon;
        switch (weaponType) {
            default:
            case SWORD:
                weapon = new Sword(weaponName);
                break;
            case DAGGER:
                weapon = new Dagger(weaponName);
                break;
            case AXE:
                weapon = new Axe(weaponName);
                break;
            case MACE:
                weapon = new Mace(weaponName);
                break;
            case GLAIVE:
                weapon = new Glaive(weaponName);
                break;

            case GUN:
                weapon = new Gun(weaponName);
                break;
            case BOW:
                weapon = new Bow(weaponName);
                break;
            case CROSSBOW:
                weapon = new Crossbow(weaponName);
                break;

            case STAFF:
                weapon = new Staff(weaponName, Objects.requireNonNull(spell));
                break;
            case SPELL_BOOK:
                weapon = new SpellBook(weaponName, Objects.requireNonNull(spell));
                break;
            case WAND:
                weapon = new Wand(weaponName, Objects.requireNonNull(spell));
                break;
        }

        if (weapon instanceof RangedWeapon && projectileModifiers != null)
            ((RangedWeapon) weapon).setProjectileModifiers(projectileModifiers);

        if (meta != null)
            weapon.setWeaponMeta(meta);

        if (modifiers != null)
            weapon.setWeaponModifiers(modifiers);

        if (statModifiers != null)
            weapon.setStatModifiers(statModifiers);

        if (effects != null) {
            weapon.setEffects(effects);
        }

        ItemMeta itemMeta = weapon.itemStack.getItemMeta();
        if (itemMeta == null)
            throw new RuntimeException();

        itemMeta.setLore(weapon.buildLore());
        weapon.getItemStack().setItemMeta(itemMeta);

        return weapon;
    }

    public WeaponBuilder setSpell(String spell) {
        if (spell == null)
            return this;

        if ("DRAGON_BREATHE".equalsIgnoreCase(spell)) {
            this.spell = new DragonBreathe();
        }

        return this;
    }

    public WeaponBuilder setName(String name) {
        this.weaponName = name;

        return this;
    }

    public WeaponBuilder setStatModifiers(ConfigurationSection section) {
        if (section == null)
            return this;

        statModifiers = new ArrayList<>();

        Set<String> statKeys = section.getKeys(false);
        for (String statKey : statKeys) {
            StatModifierType modifierType = StatModifierType.valueOf(statKey.toUpperCase());
            int statAmount = section.getInt(statKey);

            statModifiers.add(new StatModifier(modifierType, statAmount));
        }

        return this;
    }

    public WeaponBuilder setProjectileModifiers(ConfigurationSection section) {
        if (section == null)
            return this;

        ProjectileModifiers projModifiers = new ProjectileModifiers();
        projModifiers.setProjectileDamage(section.getInt("damage"));
        projModifiers.setProjectileKnockback(section.getInt("knockback"));
        projModifiers.setReloadSpeed(section.getInt("reload-speed"));

        this.projectileModifiers = projModifiers;
        return this;
    }

    public WeaponBuilder setWeaponType(String weaponType) {
        this.weaponType = WeaponType.valueOf(weaponType.toUpperCase());
        return this;
    }

    public WeaponMeta getMeta() {
        return meta;
    }

    public WeaponBuilder setMeta(@Nullable ConfigurationSection section) {
        if (section == null) {
            meta = new WeaponMeta(weaponType);
            return this;
        }

        meta = new WeaponMeta(weaponType);
        meta.setTitle(section.getString("title"));

        String rarityString = section.getString("rarity");
        if (rarityString != null)
            meta.setRarity(Rarity.valueOf(rarityString.toUpperCase()));

        meta.setCustomLore(section.getStringList("lore")
                .stream()
                .map(l -> ChatColor.translateAlternateColorCodes('&', l))
                .collect(Collectors.toList())
        );

        meta.setCustomModel(section.getInt("model"));
        return this;
    }

    public HashMap<String, EffectTrigger> getEffects() {
        return effects;
    }

    public WeaponBuilder setEffects(@Nullable ConfigurationSection section) {
        effects = new HashMap<>();
        if (section == null)
            return this;

        Set<String> effectKeys = section.getKeys(false);
        for (String effectKey : effectKeys) {
            ConfigurationSection effectSection = section.getConfigurationSection(effectKey);
            if (effectSection == null)
                continue;

            String triggerTypeString =  effectSection.getString("trigger");
            if (triggerTypeString == null)
                continue;

            EffectTriggerType triggerType = EffectTriggerType.valueOf(triggerTypeString.toUpperCase());
            int chance = effectSection.getInt("chance");

            EffectTrigger trigger = new EffectTrigger(triggerType, chance);
            TerraEffect effect = TerraItems.lookupTerraPlugin().getEffectsConfig().getItems().get(effectKey);

            effects.put(effect.getEffectName(), trigger);
        }

        return this;
    }

    public WeaponBuilder setModifiers(@Nullable ConfigurationSection section) {
        if (section == null) {
            modifiers = new WeaponModifiers();
            return this;
        }

        modifiers = new WeaponModifiers();

        List<AttributeConfiguration> attributeConfigurations = new ArrayList<>();
        List<String> attributesStringList = section.getStringList("attributes");
        if (!(attributesStringList.isEmpty())) {
            for (String attribute : attributesStringList) {
                int amount = section.getInt("value");
                List<String> slots = section.getStringList("slots");
                Attribute attributeParsed = Attribute.valueOf(attribute.toUpperCase());

                attributeConfigurations.add(new AttributeConfiguration(attributeParsed, amount, slots));
            }
        }

        modifiers.setAttributeConfigurations(attributeConfigurations);
        modifiers.setEnchantments(section.getStringList("enchantments"));

        return this;
    }
}
