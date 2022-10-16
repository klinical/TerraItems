package net.terramc.terraitems.effects;

import net.terramc.terraitems.effects.application.EffectApplication;
import net.terramc.terraitems.effects.application.TimerApplication;
import net.terramc.terraitems.effects.trigger.EffectTrigger;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitScheduler;

public class TerraEffect {
    private EffectApplication application;
    private Effect effect;
    private EffectTrigger trigger;
    private EffectMeta meta;
    private String effectName;

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
