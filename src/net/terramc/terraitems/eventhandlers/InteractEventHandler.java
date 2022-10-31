package net.terramc.terraitems.eventhandlers;

import net.terramc.terraitems.PlayerManager;
import net.terramc.terraitems.TerraItems;
import net.terramc.terraitems.TerraPlayer;
import net.terramc.terraitems.shared.ItemType;
import net.terramc.terraitems.spells.Spell;
import net.terramc.terraitems.weapons.Weapon;
import net.terramc.terraitems.weapons.magic.MagicWeapon;
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

    public void handleGunShotEvent(Player player, RangedWeapon weapon) {
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
        }, weapon.getProjectileModifiers().getReloadSpeed());

        reloadCounter.put(player.getName(), true);
        Entity bullet = player.launchProjectile(
                Snowball.class,
                player.getLocation().getDirection().multiply(4)
        );

        if (player.getGameMode() != GameMode.CREATIVE)
            ammo.setAmount(ammo.getAmount() - 1);

        bullet.setPersistent(false);
        bullet.setMetadata(ItemType.WEAPON.name(), new FixedMetadataValue(
                TerraItems.lookupTerraPlugin(),
                weapon.getItemName()
        ));

        player.getWorld().playSound(playerLoc, "gun:terra.sound.gunshot", 1.0f, 1.0f);
    }


    private ItemStack getAmmunition(PlayerInventory inventory) {
        for (ItemStack item : inventory) {
            if (item == null)
                continue;

            ItemMeta meta = item.getItemMeta();
            if (meta == null)
                continue;

            String ammunition = item.getItemMeta().getPersistentDataContainer().get(
                    new NamespacedKey(TerraItems.lookupTerraPlugin(), ItemType.AMMO.name()),
                    PersistentDataType.STRING);

            if (ammunition != null) {
                return item;
            }
        }

        return null;
    }

    private Weapon getPlayerWeapon(PlayerInventory playerInventory) {
        ItemStack itemStack = playerInventory.getItemInMainHand();
        ItemMeta itemMeta = itemStack.getItemMeta();
        if (itemMeta == null)
            return null;

        PersistentDataContainer pdc = itemMeta.getPersistentDataContainer();
        NamespacedKey key = new NamespacedKey(TerraItems.lookupTerraPlugin(), ItemType.WEAPON.name());
        String weaponName = pdc.get(key, PersistentDataType.STRING);

        return (Weapon) TerraItems.lookupTerraPlugin()
                .getWeaponsConfig()
                .getItemMap()
                .get(weaponName);
    }

    private void handleSpellCast(Player player, MagicWeapon weapon) {
        TerraPlayer terraPlayer = PlayerManager
                .getPlayerMap()
                .putIfAbsent(player.getName(), new TerraPlayer(player));

        if (terraPlayer == null)
            return;

        Spell spell = weapon.getSpell();
        if (spell == null)
            return;

        if (!terraPlayer.isShowingManaBar()) {
            terraPlayer.showManaBar();
        } else {
            terraPlayer.refreshManaBarDisplayDuration();
        }

        if (spell.getManaCost() <= terraPlayer.getMana()) {
            player.getWorld().playSound(player.getLocation(), "gun:terra.sound.spell-cast", 1, 1);
            spell.cast(terraPlayer);
            terraPlayer.setMana(terraPlayer.getMana() - spell.getManaCost());
        } else {
            player.sendMessage("Not enough mana to cast that.");
        }
    }

    private void handleRangedAction(Player player) {
        Weapon weapon = getPlayerWeapon(player.getInventory());
        if (weapon == null)
            return;

        switch (weapon.getWeaponType()) {
            case SWORD:
            case DAGGER:
            case AXE:
            case MACE:
            case GLAIVE:
            case BOW:
            case CROSSBOW:
                return;

            case STAFF:
            case WAND:
            case SPELL_BOOK:
                handleSpellCast(player, (MagicWeapon) weapon);
                break;

            case GUN:
                handleGunShotEvent(player, (RangedWeapon) weapon);
                break;
        }
    }

    @EventHandler
    public void handleInteractEvent(PlayerInteractEvent event) {
        if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            handleRangedAction(event.getPlayer());
        }
    }

    @EventHandler
    public void handleEntityInteractEntityEvent(PlayerInteractEntityEvent event) {
        handleRangedAction(event.getPlayer());
    }
}
