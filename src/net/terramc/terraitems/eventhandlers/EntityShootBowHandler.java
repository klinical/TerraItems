package net.terramc.terraitems.eventhandlers;

import net.terramc.terraitems.TerraItems;
import net.terramc.terraitems.shared.ItemType;
import net.terramc.terraitems.weapons.Weapon;
import net.terramc.terraitems.weapons.ranged.RangedWeapon;
import org.bukkit.NamespacedKey;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public class EntityShootBowHandler implements Listener {

    public EntityShootBowHandler() {
    }

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
        NamespacedKey key = new NamespacedKey(TerraItems.lookupTerraPlugin(), ItemType.WEAPON.name());
        String weaponName = bowDataContainer.get(key, PersistentDataType.STRING);

        Weapon weapon = (Weapon) TerraItems.lookupTerraPlugin().getWeaponsConfig().getItemMap().get(weaponName);
        if (!(weapon instanceof RangedWeapon))
            return;

        RangedWeapon rangedWeapon = (RangedWeapon) weapon;
        if (!rangedWeapon.hasWeaponEffects())
            return;

        event.getProjectile().setMetadata(ItemType.WEAPON.name(), new FixedMetadataValue(
                TerraItems.lookupTerraPlugin(),
                weapon
        ));
    }
}
