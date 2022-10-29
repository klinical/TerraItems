package net.terramc.terraitems.ammunition;

import net.terramc.terraitems.TerraItems;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;

public abstract class Ammo {
    protected ItemStack itemStack;
    private AmmoModifiers modifiers;

    public Ammo(String ammoName, String displayName, Material material) {
        ItemStack itemStack = new ItemStack(material);
        ItemMeta bulletMeta = itemStack.getItemMeta();

        if (bulletMeta == null)
            throw new IllegalStateException("Could not create bullet recipe, null meta");

        bulletMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', ChatColor.RESET + displayName));
        PersistentDataContainer pdc = bulletMeta.getPersistentDataContainer();

        pdc.set(
                new NamespacedKey(TerraItems.lookupTerraPlugin(), "AMMUNITION"),
                PersistentDataType.STRING,
                ammoName
        );

        itemStack.setItemMeta(bulletMeta);
        this.itemStack = itemStack;
    }

    public ItemStack getItemStack() {
        return itemStack;
    }

    public AmmoModifiers getModifiers() {
        return modifiers;
    }

    public void setModifiers(AmmoModifiers modifiers) {
        Bukkit.getLogger().warning(modifiers.getKnockback() + " wtf");
        this.modifiers = modifiers;
        List<String> lore = new ArrayList<>();

        if (modifiers.getDamage() > 0) {
            lore.add(ChatColor.GREEN + "+" + modifiers.getDamage() + " Damage");
        } else if (modifiers.getDamage() < 0) {
            lore.add(ChatColor.RED + "" + modifiers.getDamage() + " Damage");
        }

        if (modifiers.getKnockback() > 0) {
            lore.add(ChatColor.GREEN + "+" + modifiers.getKnockback() + " Knockback");
        } else if (modifiers.getKnockback() < 0) {
            lore.add(ChatColor.RED + "" + modifiers.getKnockback() + " Knockback");
        }

        if (modifiers.getVelocity() > 0) {
            lore.add(ChatColor.GREEN + "+" + modifiers.getVelocity() + " Velocity");
        } else if (modifiers.getVelocity() < 0) {
            lore.add(ChatColor.RED + "" + modifiers.getVelocity() + " Velocity");
        }

        ItemMeta meta = itemStack.getItemMeta();
        if (meta == null)
            return;

        meta.setLore(lore);
        itemStack.setItemMeta(meta);
    }

    public void setCustomModel(int model) {
        ItemMeta meta = itemStack.getItemMeta();
        if (meta == null)
            throw new IllegalStateException("meta null when setting custom model for ammo");

        meta.setCustomModelData(model);

        itemStack.setItemMeta(meta);
    }


}
