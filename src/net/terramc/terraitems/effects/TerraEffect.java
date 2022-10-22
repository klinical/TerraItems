package net.terramc.terraitems.effects;

import net.terramc.terraitems.effects.configuration.EffectApplication;
import net.terramc.terraitems.effects.configuration.Effect;
import net.terramc.terraitems.effects.configuration.EffectMeta;
import net.terramc.terraitems.effects.configuration.EffectTrigger;

import java.io.Serializable;

public class TerraEffect implements Serializable {
    private final EffectApplication application;
    private final Effect effect;
    private EffectMeta meta;
    private final String effectName;

    public TerraEffect(EffectApplication application, Effect effect, String effectName) {
        this.application = application;
        this.effect = effect;
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

    public EffectMeta getMeta() {
        return meta;
    }

    public String getEffectName() {
        return effectName;
    }
}
