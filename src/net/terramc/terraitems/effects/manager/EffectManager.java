package net.terramc.terraitems.effects.manager;

import net.terramc.terraitems.effects.configuration.EffectMeta;
import net.terramc.terraitems.effects.TerraEffect;
import net.terramc.terraitems.effects.configuration.EffectApplicationInstance;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.LivingEntity;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitScheduler;

import java.util.HashMap;
import java.util.Objects;

public class EffectManager {
    /**
     * Maps an entity to a hashmap, where the key is the name of a terra effect, and the value is a pair of integers
     * representing two tasks: 1 for applying an effect periodically, and one that is used to automatically end
     * the effect task when the duration is up
     */
    private static final HashMap<LivingEntity, HashMap<String, EffectTaskPair>> effectMap = new HashMap<>();

    public static void cancelEffectTaskPair(LivingEntity target, String effectName) {
        Plugin plugin = Objects.requireNonNull(Bukkit.getPluginManager().getPlugin("terra-items"));
        BukkitScheduler scheduler = plugin.getServer().getScheduler();

        HashMap<String, EffectTaskPair> effectPairMap = effectMap.get(target);

        if (effectPairMap != null) {
            EffectTaskPair taskPair = effectPairMap.get(effectName);

            scheduler.cancelTask(taskPair.getEffectTaskId());
            scheduler.cancelTask(taskPair.getCancelEffectTaskId());

            effectPairMap.remove(effectName);
        }
    }

    public static void applyEffect(LivingEntity self, LivingEntity target, TerraEffect terraEffect) {
        Plugin plugin = Objects.requireNonNull(Bukkit.getPluginManager().getPlugin("terra-items"));
        BukkitScheduler scheduler = plugin.getServer().getScheduler();

        switch (terraEffect.getApplication().getApplicationType()) {
            case TIMER:
                // Initialize a new hashmap for effect tasks for the target
                effectMap.putIfAbsent(target, new HashMap<>());

                applyTimerEffect(self, target, terraEffect, plugin, scheduler);
                break;

            case ONCE:
                EffectApplicationInstance application = new EffectApplicationInstance(self, target, terraEffect);
                application.apply();
                break;
        }
    }

    public static void sendMetaNotificationMessages(LivingEntity user, LivingEntity target, EffectMeta effectMeta) {
        if (target instanceof Player && effectMeta.getTargetNotification() != null)
            target.sendMessage(ChatColor.translateAlternateColorCodes(
                    '&',
                    effectMeta.getTargetNotification()
            ));

        if (user instanceof Player && effectMeta.getUserNotification() != null)
            user.sendMessage(ChatColor.translateAlternateColorCodes(
                    '&',
                    effectMeta.getUserNotification()
            ));
    }

    private static void applyTimerEffect(
            LivingEntity user,
            LivingEntity target,
            TerraEffect effect,
            Plugin plugin,
            BukkitScheduler scheduler
    ) {
        HashMap<String, EffectTaskPair> effectApplications = effectMap.get(target);
        EffectApplicationInstance applicationInstance = new EffectApplicationInstance(user, target, effect);
        String effectName = effect.getEffectName();

        // Is there already an application of this effect on the target? Cancel if so
        if (effectApplications.containsKey(effectName)) {
            cancelEffectTaskPair(target, effectName);
        }

        int effectTask = scheduler.scheduleSyncRepeatingTask(plugin, () -> {
                    if (target.getHealth() == 0)
                        cancelEffectTaskPair(target, effectName);
                    else
                        applicationInstance.apply();
                },
                0,
                20L * effect.getApplication().getProcRate()
        );

        int effectCancelTask = scheduler.scheduleSyncDelayedTask(plugin, () ->
            scheduler.cancelTask(effectTask)
        , 20L * effect.getEffect().getDuration());

        effectApplications.put(effectName, new EffectTaskPair(effectTask, effectCancelTask));
    }
}
