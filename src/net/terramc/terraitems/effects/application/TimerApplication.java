package net.terramc.terraitems.effects.application;

import net.terramc.terraitems.effects.Effect;
import net.terramc.terraitems.effects.EffectManager;
import net.terramc.terraitems.effects.TerraEffect;
import org.bukkit.Bukkit;
import org.bukkit.entity.LivingEntity;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitScheduler;

import static net.terramc.terraitems.effects.EffectManager.cancelEffectTaskPair;

public class TimerApplication extends BukkitRunnable {

    LivingEntity target;
    TerraEffect terraEffect;

    public TimerApplication(LivingEntity target, TerraEffect terraEffect) {
        this.target = target;
        this.terraEffect = terraEffect;
    }

    @Override
    public void run() {
        Effect effect = terraEffect.getEffect();

        switch (effect.getEffectType()) {
            case POISON:
                PotionEffect potionEffect = new PotionEffect(
                        PotionEffectType.POISON,
                        20 * effect.getDuration(),
                        effect.getLevel()
                );

                target.addPotionEffect(potionEffect);
                break;

            case FIRE:
                target.setFireTicks(terraEffect.getEffect().getDuration() * 20);
                break;

            case WITHER:
                PotionEffect witherEffect = new PotionEffect(
                        PotionEffectType.WITHER,
                        20 * terraEffect.getEffect().getDuration(),
                        terraEffect.getEffect().getLevel()
                );

                target.addPotionEffect(witherEffect);

            case DAMAGE:
                if (target.getHealth() - effect.getLevel() < 0)
                    target.setHealth(0);

                if (target.getHealth() == 0)
                    cancelEffectTaskPair(target, terraEffect.getEffectName());
                else
                    target.damage(target.getHealth() - effect.getLevel());

                break;
        }
    }
}
