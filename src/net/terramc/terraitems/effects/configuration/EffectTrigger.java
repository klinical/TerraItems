package net.terramc.terraitems.effects.configuration;

import net.terramc.terraitems.effects.EffectTriggerType;

public class EffectTrigger {
    private final EffectTriggerType triggerType;
    private final int chance;

    public EffectTrigger(EffectTriggerType triggerType, int chance) {
        this.triggerType = triggerType;
        this.chance = chance;
    }

    public int getChance() {
        return chance;
    }

    public EffectTriggerType getTriggerType() {
        return triggerType;
    }
}
