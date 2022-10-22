package net.terramc.terraitems.weapons.configuration;

import net.terramc.terraitems.effects.TerraEffect;
import net.terramc.terraitems.effects.configuration.EffectTrigger;

public class WeaponEffectConfiguration {
    private TerraEffect effect;
    private EffectTrigger trigger;

    public WeaponEffectConfiguration(EffectTrigger trigger, TerraEffect effect) {
        this.effect = effect;
        this.trigger = trigger;
    }

    public TerraEffect getEffect() {
        return effect;
    }

    public void setEffect(TerraEffect effect) {
        this.effect = effect;
    }

    public EffectTrigger getTrigger() {
        return trigger;
    }

    public void setTrigger(EffectTrigger trigger) {
        this.trigger = trigger;
    }
}
