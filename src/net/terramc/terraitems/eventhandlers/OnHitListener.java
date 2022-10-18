package net.terramc.terraitems.eventhandlers;

import net.terramc.terraitems.TerraItems;
import net.terramc.terraitems.effects.manager.EffectManager;
import net.terramc.terraitems.effects.TerraEffect;
import net.terramc.terraitems.shared.TerraWeaponPersistentDataType;
import net.terramc.terraitems.weapons.configuration.WeaponConfiguration;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.persistence.PersistentDataContainer;

import java.util.List;
import java.util.Random;
import java.util.logging.Logger;

public class OnHitListener implements Listener {

    public OnHitListener() { }

    private void handleLivingEntityMeleeHit(LivingEntity attacker, LivingEntity defender) {
        Logger log = Bukkit.getLogger();

        EntityEquipment userEquipment = attacker.getEquipment();
        if (userEquipment == null)
            return;

        ItemStack userMainhand = userEquipment.getItemInMainHand();

        ItemMeta weaponMeta = userMainhand.getItemMeta();
        if (weaponMeta == null) {
            log.warning("null meta!");
            return;
        }

        PersistentDataContainer container = weaponMeta.getPersistentDataContainer();
        NamespacedKey key = new NamespacedKey(TerraItems.lookupTerraPlugin(), "weapon");
        WeaponConfiguration weaponConfiguration = container.get(key, TerraWeaponPersistentDataType.DATA_TYPE);

        if (weaponConfiguration == null) {
            log.warning("No weapon for melee!");
            return;
        }

        List<TerraEffect> effects = weaponConfiguration.getModifiers().getEffects();
        for (TerraEffect effect : effects) {
            if (successfulProcRoll(effect.getTrigger().getChance())) {
                EffectManager.sendMetaNotificationMessages(attacker, defender, effect.getMeta());
                EffectManager.applyEffect(attacker, defender, effect);
            }
        }
    }

    private void handleLivingEntityRangedEvent(Projectile projectile, LivingEntity defender) {
        Logger log = Bukkit.getLogger();
        LivingEntity shooter = (LivingEntity) projectile.getShooter();

        List<MetadataValue> effects = projectile.getMetadata("effects");
        if (effects.isEmpty())
            return;

        for (MetadataValue value : effects) {
            if (!(value instanceof FixedMetadataValue)) {
                log.warning("value not instanceof FixedMetadataValue! For " + value.asString());
                continue;
            }

            FixedMetadataValue fixedMetadataValue = (FixedMetadataValue) value;
            Object metadataValue = fixedMetadataValue.value();

            if (!(metadataValue instanceof List)) {
                log.warning("metadataValue not instanceof List!");
                continue;
            }

            // todo remove needless allocation of new array
            List<?> metadataValueList = (List<?>) metadataValue;
            for (Object something : metadataValueList) {
                if (!(something instanceof TerraEffect)) {
                    log.warning("Object something not instanceof TerraEffect!");
                    continue;
                }

                TerraEffect effect = (TerraEffect) something;
                if (successfulProcRoll(effect.getTrigger().getChance())) {
                    EffectManager.sendMetaNotificationMessages(shooter, defender, effect.getMeta());
                    EffectManager.applyEffect(shooter, defender, effect);
                }
            }

        }

    }

    @EventHandler
    public void onEntityHit(EntityDamageByEntityEvent event) {
        Entity defender = event.getEntity();
        Entity attacker = event.getDamager();

        if (attacker instanceof LivingEntity && defender instanceof LivingEntity) {
            handleLivingEntityMeleeHit((LivingEntity) attacker, (LivingEntity) defender);
        } else if (attacker instanceof Projectile && defender instanceof LivingEntity){
            handleLivingEntityRangedEvent((Projectile) event.getDamager(), (LivingEntity) event.getEntity());
        }
    }

    private boolean successfulProcRoll(int chance) {
        Random random = new Random();
        int upperbound = 100;
        int randomGenerated = random.nextInt(upperbound + 1);

        return chance >= randomGenerated;
    }
}
