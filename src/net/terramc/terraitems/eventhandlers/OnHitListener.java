package net.terramc.terraitems.eventhandlers;

import net.terramc.terraitems.TerraItems;
import net.terramc.terraitems.effects.manager.EffectManager;
import net.terramc.terraitems.effects.TerraEffect;
import net.terramc.terraitems.shared.NamespaceKeys;
import net.terramc.terraitems.shared.TerraWeaponPersistentDataType;
import net.terramc.terraitems.weapons.WeaponType;
import net.terramc.terraitems.weapons.configuration.WeaponConfiguration;
import net.terramc.terraitems.weapons.configuration.WeaponModifiers;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.*;
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

public class OnHitListener implements Listener {

    public OnHitListener() { }

    private void handleLivingEntityMeleeHit(LivingEntity attacker, LivingEntity defender) {
        EntityEquipment userEquipment = attacker.getEquipment();
        if (userEquipment == null)
            return;

        ItemStack userMainhand = userEquipment.getItemInMainHand();
        ItemMeta weaponMeta = userMainhand.getItemMeta();
        if (weaponMeta == null)
            return;

        PersistentDataContainer container = weaponMeta.getPersistentDataContainer();
        NamespacedKey key = new NamespacedKey(TerraItems.lookupTerraPlugin(), NamespaceKeys.WEAPON_KEY);
        WeaponConfiguration weaponConfiguration = container.get(key, TerraWeaponPersistentDataType.DATA_TYPE);

        if (weaponConfiguration == null)
            return;

        List<TerraEffect> effects = weaponConfiguration.getModifiers().getEffects();
        rollForEffects(effects, attacker, defender);
    }

    private void handleLivingEntityRangedEvent(Projectile projectile, LivingEntity defender) {
        if (!(projectile.getShooter() instanceof LivingEntity))
            return;

        LivingEntity shooter = (LivingEntity) projectile.getShooter();
        List<MetadataValue> metadata = projectile.getMetadata(NamespaceKeys.WEAPON_KEY);
        if (metadata.isEmpty())
            return;

        for (MetadataValue value : metadata) {
            if (!(value instanceof FixedMetadataValue))
                return;

            FixedMetadataValue fixedMetadataValue = (FixedMetadataValue) value;
            if (!(fixedMetadataValue.value() instanceof WeaponConfiguration))
                return;

            WeaponConfiguration cfg = (WeaponConfiguration) fixedMetadataValue.value();
            if (cfg == null)
                return;

            WeaponModifiers modifiers = cfg.getModifiers();
            if (modifiers == null)
                return;

            rollForEffects(cfg.getModifiers().getEffects(), shooter, defender);
            if (cfg.getWeaponType() == WeaponType.GUN && projectile instanceof Snowball)
                defender.damage(modifiers.getProjectileDamage());
        }
    }

    @EventHandler
    public void onEntityHit(EntityDamageByEntityEvent event) {
        Entity defender = event.getEntity();
        Entity attacker = event.getDamager();
        Bukkit.getLogger().warning("what the...");

        if (attacker instanceof LivingEntity && defender instanceof LivingEntity) {
            handleLivingEntityMeleeHit((LivingEntity) attacker, (LivingEntity) defender);
        } else if (attacker instanceof Projectile && defender instanceof LivingEntity){
            handleLivingEntityRangedEvent((Projectile) event.getDamager(), (LivingEntity) event.getEntity());
        }
    }

    private void rollForEffects(List<TerraEffect> effects, LivingEntity attacker, LivingEntity defender) {
        for (TerraEffect effect : effects) {
            if (successfulProcRoll(effect.getTrigger().getChance())) {
                EffectManager.sendMetaNotificationMessages(attacker, defender, effect.getMeta());
                EffectManager.applyEffect(attacker, defender, effect);
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
