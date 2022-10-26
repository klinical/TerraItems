package net.terramc.terraitems.ammunition;

import net.terramc.terraitems.TerraItems;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public class CopperBullet {
    public static final ItemStack itemStack = new ItemStack(Material.FIREWORK_STAR);
    public static final ShapedRecipe RECIPE = getRecipe();

    private static ShapedRecipe getRecipe() {
        ItemMeta bulletMeta = itemStack.getItemMeta();

        if (bulletMeta == null)
            throw new IllegalStateException("Could not create bullet recipe, null meta");


        bulletMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&rCopper Bullet"));
        bulletMeta.setCustomModelData(1);
        PersistentDataContainer pdc = bulletMeta.getPersistentDataContainer();

        pdc.set(new NamespacedKey(TerraItems.lookupTerraPlugin(), "AMMUNITION"), PersistentDataType.STRING, "COPPER_BULLET");

        itemStack.setItemMeta(bulletMeta);
        ShapedRecipe bulletRecipe = new ShapedRecipe(
                new NamespacedKey(TerraItems.lookupTerraPlugin(), "Copper-Bullet"),
                itemStack
        );

        bulletRecipe.shape(" C ", "PGP", " C ");
        bulletRecipe.setIngredient('C', Material.COPPER_INGOT);
        bulletRecipe.setIngredient('P', Material.PAPER);
        bulletRecipe.setIngredient('G', Material.GUNPOWDER);

        return bulletRecipe;
    }
}
