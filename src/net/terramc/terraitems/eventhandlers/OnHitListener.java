package net.terramc.terraitems.eventhandlers;

import net.terramc.terraitems.TerraItems;
import net.terramc.terraitems.WeaponsConfig;
import net.terramc.terraitems.effects.EffectManager;
import net.terramc.terraitems.effects.TerraEffect;
import net.terramc.terraitems.weapons.MeleeWeapon;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.List;
import java.util.Random;

public class OnHitListener implements Listener {
    private final WeaponsConfig weaponsConfig;
    private final TerraItems plugin;

    public OnHitListener(WeaponsConfig weaponsConfig, TerraItems plugin) {
        this.weaponsConfig = weaponsConfig;
        this.plugin = plugin;
    }

    @EventHandler
    public void onEntityHit(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof LivingEntity && event.getEntity() instanceof LivingEntity) {
            LivingEntity target = (LivingEntity) event.getEntity();
            LivingEntity user = (LivingEntity) event.getDamager();

            EntityEquipment userEquipment = user.getEquipment();
            if (userEquipment == null)
                return;

            ItemStack userMainhand = userEquipment.getItemInMainHand();

            ItemMeta weaponMeta = userMainhand.getItemMeta();
            if (weaponMeta == null)
                return;

            PersistentDataContainer container = weaponMeta.getPersistentDataContainer();
            NamespacedKey key = new NamespacedKey(plugin, "weapon-name");
            String weaponNameContainerEntry = container.get(key, PersistentDataType.STRING);
            if (weaponNameContainerEntry == null)
                return;

            MeleeWeapon meleeWeapon = (MeleeWeapon) weaponsConfig.getItems().get(weaponNameContainerEntry);
            if (meleeWeapon == null) {
                plugin.getLogger().warning(
                        "Failed to look up weapon-name " +
                                weaponNameContainerEntry +
                                " for " + ((Player) user).getDisplayName() +
                                " item " + weaponMeta.getDisplayName()
                );

                return;
            }

            if (!meleeWeapon.getWeaponModifiers().hasEffects())
                return;

            List<TerraEffect> effects = meleeWeapon.getWeaponModifiers().getEffects();
            for (TerraEffect effect : effects) {
                if (successfulProcRoll(effect.getTrigger().getChance())) {
                    EffectManager.sendMetaNotificationMessages(user, target, effect.getMeta());
                    EffectManager.applyEffect(user, target, effect);
                }
            }
        }
    }

    private boolean successfulProcRoll(int chance) {
        Random random = new Random();
        int upperbound = 100;
        int randomGenerated = random.nextInt(upperbound + 1);

        return chance >= randomGenerated;
    }
}
