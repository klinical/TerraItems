package net.terramc.terraitems.eventhandlers;

import net.terramc.terraitems.TerraItems;
import net.terramc.terraitems.effects.TerraEffect;
import net.terramc.terraitems.shared.NamespaceKeys;
import net.terramc.terraitems.shared.TerraWeaponPersistentDataType;
import net.terramc.terraitems.weapons.configuration.WeaponConfiguration;
import org.bukkit.NamespacedKey;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.persistence.PersistentDataContainer;

import java.util.List;

public class EntityShootBowHandler implements Listener {

    /**
     * Capture the bow shot event and attach Terra meta-data to arrow for ProjectileHitEvent to get effect data
     * Also will be used later to add support for new arrow types etc
     * @param event EntityShootBowEvent
     */
    @EventHandler
    public void LivingEntityShootBowHandler(EntityShootBowEvent event) {
        ItemStack bowItemStack = event.getBow();
        if (bowItemStack == null)
            return;

        ItemMeta bowMeta = bowItemStack.getItemMeta();
        if (bowMeta == null)
            return;

        PersistentDataContainer bowDataContainer = bowMeta.getPersistentDataContainer();
        WeaponConfiguration weapon = bowDataContainer.get(
                new NamespacedKey(TerraItems.lookupTerraPlugin(), NamespaceKeys.WEAPON_KEY),
                TerraWeaponPersistentDataType.DATA_TYPE);

        if (weapon == null)
            return;

        if (!weapon.getModifiers().hasEffects())
            return;

        event.getProjectile().setMetadata(NamespaceKeys.WEAPON_KEY, new FixedMetadataValue(
                TerraItems.lookupTerraPlugin(),
                weapon
        ));
    }
}
