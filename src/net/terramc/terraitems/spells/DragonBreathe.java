package net.terramc.terraitems.spells;

import net.terramc.terraitems.TerraItems;
import net.terramc.terraitems.TerraPlayer;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.block.Block;
import org.bukkit.entity.*;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class DragonBreathe extends Spell {
    private final double manaCost = 2;
    private final double spellDamage = 7;

    @Override
    public void cast(TerraPlayer terraPlayer) {
        Player player = terraPlayer.getPlayer();

        Location startLoc = player.getLocation().clone();
        Vector direction = startLoc.clone().getDirection().normalize().multiply(2);
        Location newLoc = startLoc.clone();
        newLoc.setZ((newLoc.getZ() + direction.getZ()));
        newLoc.setX((newLoc.getX() + direction.getX()));
        newLoc.setY(0.75 + newLoc.getY() + direction.getY());

        player.getWorld().spawnParticle(
                Particle.SPELL_WITCH,
                newLoc,
                128, 2, 0.5, 2, 0
        );

        BukkitRunnable runnable = new BukkitRunnable() {
            int num = 1;

            @Override
            public void run() {
                if (num > 15)
                    cancel();

                Vector direction = startLoc.clone().getDirection().normalize().multiply(num);
                Location newLoc = startLoc.clone();
                newLoc.setZ((newLoc.getZ() + direction.getZ()));
                newLoc.setX((newLoc.getX() + direction.getX()));
                newLoc.setY(1.75 + newLoc.getY() + direction.getY());

                Block block = player.getWorld().getBlockAt(newLoc);
                Material mat = block.getType();

                if (mat.isSolid())
                    cancel();

                player.getWorld().spawnParticle(
                        Particle.SPELL_WITCH,
                        newLoc,
                        64, (float) num/6, 1.5, (float) num/6, 0
                );

                List<Entity> entityList = new ArrayList<>(Objects.requireNonNull(newLoc.getWorld())
                        .getNearbyEntities(newLoc, (float) num / 4, 2, (float) num / 4));

                for (Entity entity : entityList) {
                    if (entity instanceof LivingEntity && player != entity)
                        ((LivingEntity) entity).damage(spellDamage);
                }

                num += 1;
            }
        };

        runnable.runTaskTimer(TerraItems.lookupTerraPlugin(), 0, 2L);
    }

    @Override
    public double getManaCost() {
        return manaCost;
    }
}
