package net.terramc.terraitems.eventhandlers;

import net.terramc.terraitems.PlayerManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerQuitHandler implements Listener {
    @EventHandler
    public void handlePlayerQuitEvent(PlayerQuitEvent event) {
        String playerName = event.getPlayer().getName();
        PlayerManager.getPlayerMap().remove(playerName);
    }
}
