package net.terramc.terraitems.effects;

import net.terramc.terraitems.shared.ConfigUtility;
import net.terramc.terraitems.shared.EffectType;
import org.bukkit.configuration.ConfigurationSection;

import java.util.Objects;

public class Effect {

    private EffectType effectType;
    private int level;
    private int duration;

    public Effect(EffectType effectType, int level, int duration) {
        this.effectType = effectType;
        this.level = level;
        this.duration = duration;
    }

    public Effect(ConfigurationSection section) throws IllegalArgumentException {
        Objects.requireNonNull(section);
        effectType = EffectType.valueOf(ConfigUtility.readString(section, "type"));
        level = section.getInt("level");
        duration = section.getInt("duration");

        if (level <= 0)
            throw new IllegalArgumentException("invalid <=0 value for " + section + " level");
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
