package net.terramc.terraitems.effects.configuration;

import net.terramc.terraitems.effects.TerraEffect;
import org.bukkit.Bukkit;
import org.bukkit.entity.LivingEntity;
import org.bukkit.potion.PotionEffect;

public class EffectApplicationInstance {
    private final LivingEntity target;
    private final LivingEntity self;
    private final TerraEffect terraEffect;

    public EffectApplicationInstance(LivingEntity self, LivingEntity target, TerraEffect terraEffect) {
        this.target = target;
        this.self = self;
        this.terraEffect = terraEffect;
    }

    public void apply() {
        Effect effect = terraEffect.getEffect();

        switch (effect.getEffectType()) {
            case POTION:
                if (effect.hasPotionEffectType()) {
                    PotionEffect potionEffect = new PotionEffect(
                            effect.getPotionEffectType(),
                            20 * effect.getDuration(),
                            effect.getLevel()
                    );

                    target.addPotionEffect(potionEffect);
                } else {
                    Bukkit.getLogger().warning("Effect " + terraEffect.getEffectName() + " has no potion type!");
                }
                break;

            case FIRE:
                target.setFireTicks(20);
                break;

            case DRAIN:
                if (target.getHealth() - effect.getLevel() < 0)
                    target.setHealth(0);
                else {
                    target.damage(target.getHealth() - effect.getLevel());

                    // Restore half the level amount back to the wielder
                    if (self.getHealth() + effect.getLevel() > 20)
                        self.setHealth(20);
                    else
                        self.setHealth(self.getHealth() + (double) effect.getLevel() / 2L);
                }

                break;

            case DAMAGE:
                if (target.getHealth() - effect.getLevel() < 0)
                    target.setHealth(0);
                else
                    target.damage(effect.getLevel());

                break;
        }
    }
}
