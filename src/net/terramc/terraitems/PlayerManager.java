package net.terramc.terraitems;

import org.bukkit.Bukkit;

import java.util.ArrayList;
import java.util.HashMap;

public class PlayerManager {
    private static final int updateManaBarTask;
    private static final HashMap<String, TerraPlayer> terraPlayerMap = new HashMap<>();
    static {
        updateManaBarTask = Bukkit.getScheduler().scheduleSyncRepeatingTask(
                TerraItems.lookupTerraPlugin(),
                () -> {
                    ArrayList<TerraPlayer> players = new ArrayList<>(terraPlayerMap.values());

                    for (TerraPlayer player : players)
                        player.setMana(player.getMana() + player.getManaRegenRate());
                },
                0,
                20
        );
    }

    public int getUpdateManaBarTask() {
        return updateManaBarTask;
    }

    public static HashMap<String, TerraPlayer> getPlayerMap() {
        return terraPlayerMap;
    }
}
