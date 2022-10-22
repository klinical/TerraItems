package net.terramc.terraitems.eventhandlers;

import net.terramc.terraitems.TerraItems;
import net.terramc.terraitems.shared.NamespaceKeys;
import net.terramc.terraitems.weapons.Weapon;
import net.terramc.terraitems.weapons.WeaponType;
import net.terramc.terraitems.weapons.ranged.RangedWeapon;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitScheduler;

import java.util.HashMap;

public class InteractEventHandler implements Listener {

    public InteractEventHandler() {
    }

    private static final HashMap<String, Boolean> reloadCounter = new HashMap<>();

    public void handleGunShotEvent(Player player) {
        ItemStack itemStack = player.getInventory().getItemInMainHand();
        ItemMeta itemMeta = itemStack.getItemMeta();
        if (itemMeta == null)
            return;

        PersistentDataContainer pdc = itemMeta.getPersistentDataContainer();
        NamespacedKey key = new NamespacedKey(TerraItems.lookupTerraPlugin(), NamespaceKeys.WEAPON_KEY);
        String weaponName = pdc.get(key, PersistentDataType.STRING);
        Weapon weapon = TerraItems.lookupTerraPlugin().getWeaponsConfig().getItems().get(weaponName);

        if (weapon == null)
            return;

        if (weapon instanceof RangedWeapon && weapon.getWeaponType() == WeaponType.GUN) {
            RangedWeapon rangedWeapon = (RangedWeapon) weapon;
            reloadCounter.putIfAbsent(player.getName(), false);

            Location playerLoc = player.getLocation();
            PlayerInventory playerInventory = player.getInventory();
            ItemStack ammo = getAmmunition(playerInventory);

            boolean isReloading = reloadCounter.get(player.getName());
            if (isReloading || ammo == null) {
                if (ammo == null)
                    player.getWorld().playSound(playerLoc, "gun:terra.sound.empty", 1.0f, 1.0f);

                return;
            }

            BukkitScheduler scheduler = TerraItems.lookupTerraPlugin().getServer().getScheduler();
            scheduler.scheduleSyncDelayedTask(TerraItems.lookupTerraPlugin(), () -> {
                reloadCounter.put(player.getName(), false);
                player.getWorld().playSound(player.getLocation(), "gun:terra.sound.reload", 1.0f, 1.0f);
            }, rangedWeapon.getProjectileModifiers().getReloadSpeed());

            reloadCounter.put(player.getName(), true);
            Entity bullet = player.launchProjectile(
                    Snowball.class,
                    player.getLocation().getDirection().multiply(4)
            );

            if (player.getGameMode() != GameMode.CREATIVE)
                ammo.setAmount(ammo.getAmount() - 1);

            bullet.setPersistent(false);
            bullet.setMetadata(NamespaceKeys.WEAPON_KEY, new FixedMetadataValue(
                    TerraItems.lookupTerraPlugin(),
                    weapon.getWeaponName()
            ));

            player.getWorld().playSound(playerLoc, "gun:terra.sound.gunshot", 1.0f, 1.0f);
        }
    }

    private ItemStack getAmmunition(PlayerInventory inventory) {
        for (ItemStack item : inventory) {
            if (item == null)
                continue;

            ItemMeta meta = item.getItemMeta();
            if (meta == null)
                continue;

            String ammunition = item.getItemMeta().getPersistentDataContainer().get(
                    new NamespacedKey(TerraItems.lookupTerraPlugin(), "AMMUNITION"),
                    PersistentDataType.STRING);

            if (ammunition != null && ammunition.equals("COPPER_BULLET")) {
                return item;
            }
        }

        return null;
    }

    @EventHandler
    public void handleInteractEvent(PlayerInteractEvent event) {
        if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK)
            handleGunShotEvent(event.getPlayer());
    }

    @EventHandler
    public void handleEntityInteractEntityEvent(PlayerInteractEntityEvent event) {
        handleGunShotEvent(event.getPlayer());
    }
}
