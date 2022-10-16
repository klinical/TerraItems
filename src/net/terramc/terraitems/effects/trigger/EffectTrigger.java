package net.terramc.terraitems.effects.trigger;

import net.terramc.terraitems.shared.ItemType;
import net.terramc.terraitems.shared.ConfigUtility;
import org.bukkit.configuration.ConfigurationSection;

import java.util.Objects;

public class EffectTrigger {
    private final ItemType itemType;
    private final EffectTriggerType triggerType;
    private final int chance;

    public EffectTrigger(ItemType itemType, EffectTriggerType triggerType, int chance) {
        this.itemType = itemType;
        this.triggerType = triggerType;
        this.chance = chance;
    }

    public EffectTrigger(ConfigurationSection section) throws IllegalArgumentException {
        Objects.requireNonNull(section);
        itemType = ItemType
                .valueOf(ConfigUtility.readString(section, "type"));
        triggerType = EffectTriggerType
                .valueOf(ConfigUtility.readString(section, "trigger"));
        chance = section.getInt("chance");
    }

    public int getChance() {
        return chance;
    }
}
