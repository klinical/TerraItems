package net.terramc.terraitems.effects.configuration;

import net.terramc.terraitems.effects.EffectApplicationType;
import net.terramc.terraitems.shared.ConfigUtility;
import net.terramc.terraitems.effects.EffectTargetType;
import org.bukkit.configuration.ConfigurationSection;

import java.util.Objects;

public class EffectApplication {
    private final EffectApplicationType applicationType;
    private final EffectTargetType targetType;
    private int procRate;

    public EffectApplication(ConfigurationSection section) throws IllegalArgumentException {
        Objects.requireNonNull(section);
        applicationType = EffectApplicationType.valueOf(ConfigUtility.readString(section, "type"));
        targetType = EffectTargetType.valueOf(ConfigUtility.readString(section, "target"));

        if (applicationType == EffectApplicationType.TIMER) {
            procRate = section.getInt("every");

            if (procRate == 0)
                throw new IllegalArgumentException("Invalid value (missing or <=0) for " + section + " 'every'");
        }
    }

    public EffectApplicationType getApplicationType() {
        return applicationType;
    }


    public int getProcRate() {
        return procRate;
    }
}
