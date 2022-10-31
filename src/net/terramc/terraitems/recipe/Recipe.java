package net.terramc.terraitems.recipe;

import net.terramc.terraitems.TerraItems;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;

public class Recipe {
    public final static ShapedRecipe LIGHT_SHOT = lightShotRecipe();

    private static ShapedRecipe lightShotRecipe() {
        ItemStack itemStack = TerraItems.lookupTerraPlugin()
                .getAmmoConfig()
                .getItemMap()
                .get("light-shot")
                .getItemStack();

        ItemMeta bulletMeta = itemStack.getItemMeta();
        if (bulletMeta == null)
            throw new IllegalStateException("Could not create bullet recipe, null meta");

        ShapedRecipe bulletRecipe = new ShapedRecipe(
                new NamespacedKey(TerraItems.lookupTerraPlugin(), itemStack.getItemMeta().getDisplayName()),
                itemStack
        );

        bulletRecipe.shape(" C ", "PGP", " C ");
        bulletRecipe.setIngredient('C', Material.COPPER_INGOT);
        bulletRecipe.setIngredient('P', Material.PAPER);
        bulletRecipe.setIngredient('G', Material.GUNPOWDER);

        return bulletRecipe;
    }
}
