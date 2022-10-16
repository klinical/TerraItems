package net.terramc.terraitems.eventhandlers;

import net.terramc.terraitems.EffectsConfig;
import net.terramc.terraitems.WeaponsConfig;
import net.terramc.terraitems.effects.EffectManager;
import net.terramc.terraitems.effects.TerraEffect;
import net.terramc.terraitems.weapons.Weapon;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.List;
import java.util.Random;

public class OnHitListener implements Listener {
    private EffectsConfig effectsConfig;
    private WeaponsConfig weaponsConfig;

    public OnHitListener(EffectsConfig effectsConfig, WeaponsConfig weaponsConfig) {
        this.effectsConfig = effectsConfig;
        this.weaponsConfig = weaponsConfig;
    }

    @EventHandler
    public void onEntityHit(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof LivingEntity && event.getEntity() instanceof LivingEntity) {
            LivingEntity target = (LivingEntity) event.getEntity();
            LivingEntity user = (LivingEntity) event.getDamager();
            Player player = (Player) event.getDamager();
            ItemStack playerWeapon = player.getInventory().getItemInMainHand();
            ItemMeta weaponMeta = playerWeapon.getItemMeta();

            PersistentDataContainer container = weaponMeta.getPersistentDataContainer();

            NamespacedKey key = new NamespacedKey(
                    Bukkit.getPluginManager().getPlugin("Terra-Items"),
                    "weapon-name"
            );

            String weaponNameContainerEntry = container.get(key, PersistentDataType.STRING);

            if (weaponNameContainerEntry != null && !weaponNameContainerEntry.isEmpty())  {
                Weapon weapon = weaponsConfig.getItems().get(weaponNameContainerEntry);

                if (weapon.hasCustomEffect()) {
                    List<TerraEffect> effects = weapon.getEffects();

                    for (TerraEffect effect : effects) {
                        int chance = effect.getTrigger().getChance();
                        Random random = new Random();
                        int upperbound = 100;
                        int ran = random.nextInt(upperbound + 1);

                        if (chance >= ran) {
                            if (target instanceof Player)
                                if (effect.getMeta().getTargetNotification() != null && event.getEntity() instanceof Player) {
                                    Player p = (Player) event.getEntity();
                                    p.sendMessage(
                                            ChatColor.translateAlternateColorCodes(
                                                    '&',
                                                    effect.getMeta().getTargetNotification()
                                            )
                                    );
                                }

                            if (user instanceof Player)
                                if (effect.getMeta().getUserNotification() != null) {
                                    player.sendMessage(
                                            ChatColor.translateAlternateColorCodes(
                                                    '&',
                                                    effect.getMeta().getUserNotification()
                                            )
                                    );
                                }

                            EffectManager.applyEffect(target, effect);
                        }
                    }
                }
            }
        }
    }
}
