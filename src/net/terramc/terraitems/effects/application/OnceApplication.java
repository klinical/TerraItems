package net.terramc.terraitems.effects.application;

import net.terramc.terraitems.effects.TerraEffect;
import org.bukkit.entity.LivingEntity;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class OnceApplication {
    private LivingEntity target;
    private TerraEffect effect;

    public OnceApplication(LivingEntity target, TerraEffect effect) {
        this.target = target;
        this.effect = effect;
    }

    public void apply() {
        switch (effect.getEffect().getEffectType()) {
            case POISON:
                PotionEffect potionEffect = new PotionEffect(
                        PotionEffectType.POISON,
                        20 * effect.getEffect().getDuration(),
                        effect.getEffect().getLevel()
                );

                target.addPotionEffect(potionEffect);
                break;

            case FIRE:
                target.setFireTicks(effect.getEffect().getDuration() * 20);
                break;

            case WITHER:
                PotionEffect witherEffect = new PotionEffect(
                        PotionEffectType.WITHER,
                        20 * effect.getEffect().getDuration(),
                        effect.getEffect().getLevel()
                );

                target.addPotionEffect(witherEffect);
                break;

            case DAMAGE:
                target.damage(target.getHealth() - effect.getEffect().getLevel());
                break;
        }
    }
}
