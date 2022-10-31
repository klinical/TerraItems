package net.terramc.terraitems.eventhandlers;

import net.terramc.terraitems.effects.manager.EffectManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;

public class EntityDeathEventHandler implements Listener {
    @EventHandler
    public void handleEntityDeathEvent(EntityDeathEvent event) {
        EffectManager.cancelAllEffectTaskpairs(event.getEntity());
    }
}
