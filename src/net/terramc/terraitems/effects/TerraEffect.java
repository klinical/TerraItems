package net.terramc.terraitems.effects;

import net.terramc.terraitems.effects.application.EffectApplication;
import net.terramc.terraitems.effects.trigger.EffectTrigger;

public class TerraEffect {
    private final EffectApplication application;
    private final Effect effect;
    private final EffectTrigger trigger;
    private EffectMeta meta;
    private final String effectName;

    public TerraEffect(EffectApplication application, Effect effect, EffectTrigger trigger, String effectName) {
        this.application = application;
        this.effect = effect;
        this.trigger = trigger;
        this.effectName = effectName;
    }

    public void setTerraMeta(EffectMeta meta) {
        this.meta = meta;
    }

    public EffectApplication getApplication() {
        return application;
    }

    public Effect getEffect() {
        return effect;
    }

    public EffectTrigger getTrigger() {
        return trigger;
    }

    public EffectMeta getMeta() {
        return meta;
    }

    public String getEffectName() {
        return effectName;
    }
}
