package net.terramc.terraitems.eventhandlers;

import net.terramc.terraitems.TerraItems;
import net.terramc.terraitems.effects.TerraEffect;
import net.terramc.terraitems.effects.configuration.EffectTrigger;
import net.terramc.terraitems.effects.manager.EffectManager;
import net.terramc.terraitems.shared.NamespaceKeys;
import net.terramc.terraitems.weapons.Weapon;
import net.terramc.terraitems.weapons.WeaponType;
import net.terramc.terraitems.weapons.ranged.ProjectileModifiers;
import net.terramc.terraitems.weapons.ranged.RangedWeapon;
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
import org.bukkit.persistence.PersistentDataType;

import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class OnHitListener implements Listener {

    public OnHitListener() {
    }

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
        String weaponName = container.get(key, PersistentDataType.STRING);
        Weapon weapon = TerraItems.lookupTerraPlugin().getWeaponsConfig().getItems().get(weaponName);

        if (weapon == null)
            return;

        if (weapon.hasWeaponEffects())
            rollForEffects(weapon.getWeaponEffects(), attacker, defender);
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
            if (!(fixedMetadataValue.value() instanceof String))
                return;

            String weaponName = (String) fixedMetadataValue.value();
            if (weaponName == null)
                return;

            if (!(TerraItems.lookupTerraPlugin().getWeaponsConfig().getItems().get(weaponName) instanceof RangedWeapon))
                return;

            RangedWeapon weapon = (RangedWeapon) TerraItems.lookupTerraPlugin().getWeaponsConfig().getItems().get(weaponName);
            if (weapon == null)
                return;

            if (weapon.hasWeaponEffects())
                rollForEffects(weapon.getWeaponEffects(), shooter, defender);

            if (weapon.getWeaponType() == WeaponType.GUN && projectile instanceof Snowball) {
                ProjectileModifiers modifiers = weapon.getProjectileModifiers();
                defender.damage(modifiers.getProjectileDamage());
                defender.setVelocity(
                        projectile.getVelocity()
                                .normalize()
                                .multiply(modifiers.getProjectileKnockback())
                );
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

    private void rollForEffects(HashMap<String, EffectTrigger> effects, LivingEntity attacker, LivingEntity defender) {
        for (String effectString : effects.keySet()) {
            if (successfulProcRoll(effects.get(effectString).getChance())) {
                TerraEffect effect = TerraItems.lookupTerraPlugin().getEffectsConfig().getItems().get(effectString);
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
