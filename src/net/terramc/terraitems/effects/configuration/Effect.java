package net.terramc.terraitems.effects.configuration;

import net.terramc.terraitems.effects.EffectType;
import net.terramc.terraitems.shared.ConfigUtility;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.potion.PotionEffectType;

import java.util.Objects;

public class Effect {

    private final EffectType effectType;
    private PotionEffectType potionEffectType;
    private final int level;
    private final int duration;

    public Effect(ConfigurationSection section) throws IllegalArgumentException {
        Objects.requireNonNull(section);
        effectType = EffectType.valueOf(ConfigUtility.readString(section, "type"));
        level = section.getInt("level");
        duration = section.getInt("duration");

        if (level <= 0)
            throw new IllegalArgumentException("invalid <=0 value for " + section + " level");

        String potionEffect = section.getString("potion-type");
        if (potionEffect != null) {
            potionEffectType = PotionEffectType.getByName(potionEffect.toUpperCase());
        }
    }


    public PotionEffectType getPotionEffectType() {
        return potionEffectType;
    }

    public boolean hasPotionEffectType() {
        return potionEffectType != null;
    }


    public EffectType getEffectType() {
        return effectType;
    }

    public int getLevel() {
        return level;
    }

    public int getDuration() {
        return duration;
    }
}
