package net.terramc.terraitems.effects;

import net.terramc.terraitems.TerraItems;
import net.terramc.terraitems.effects.application.OnceApplication;
import net.terramc.terraitems.effects.application.TimerApplication;
import org.bukkit.Bukkit;
import org.bukkit.entity.LivingEntity;

import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitScheduler;

import java.util.HashMap;

public class EffectManager {
    private static final HashMap<LivingEntity, HashMap<String, EffectTaskPair>> effectMap = new HashMap<>();

    public static HashMap<LivingEntity, HashMap<String, EffectTaskPair>> getEffectMap() {
        return effectMap;
    }

    public static void cancelEffectTaskPair(LivingEntity target, String effectName) {
        BukkitScheduler scheduler = Bukkit.getPluginManager().getPlugin("terra-items").getServer().getScheduler();
        HashMap<String, EffectTaskPair> effectPairMap = effectMap.get(target);

        if (effectPairMap != null) {
            EffectTaskPair taskPair = effectMap.get(target).get(effectName);

            scheduler.cancelTask(taskPair.getEffectTaskId());
            scheduler.cancelTask(taskPair.getCancelEffectTaskId());

            effectMap.get(target).remove(effectName);
        }
    }

    public static void applyEffect(LivingEntity target, TerraEffect terraEffect) {
        Plugin plugin = Bukkit.getPluginManager().getPlugin("terra-items");
        BukkitScheduler scheduler = plugin.getServer().getScheduler();

        switch (terraEffect.getApplication().getApplicationType()) {
            case TIMER:
                effectMap.putIfAbsent(target, new HashMap<>());
                HashMap<String, EffectTaskPair> effectApplications = effectMap.get(target);

                if (effectApplications.get(terraEffect.getEffectName()) != null) {
                    cancelEffectTaskPair(target, terraEffect.getEffectName());
                }

                Runnable runnable = new TimerApplication(target, terraEffect);

                int t = scheduler.scheduleSyncRepeatingTask(plugin, runnable,
                        0, 20L * terraEffect.getApplication().getProcRate()
                );
                int c = scheduler.scheduleSyncDelayedTask(plugin, () -> {
                    scheduler.cancelTask(t);
                }, 20L * terraEffect.getEffect().getDuration());

                effectApplications.put(terraEffect.getEffectName(), new EffectTaskPair(t, c));
                break;

            case ONCE:
                OnceApplication application = new OnceApplication(target, terraEffect);
                application.apply();
                break;
        }
    }
}
