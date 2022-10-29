package net.terramc.terraitems;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.terramc.terraitems.commands.ItemSpawn;
import net.terramc.terraitems.commands.TerraConfig;
import net.terramc.terraitems.eventhandlers.EntityShootBowHandler;
import net.terramc.terraitems.eventhandlers.InteractEventHandler;
import net.terramc.terraitems.eventhandlers.OnHitListener;
import net.terramc.terraitems.recipe.Recipe;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public class TerraItems extends JavaPlugin {

    private WeaponsConfig weaponsConfig;
    private EffectsConfig effectsConfig;
    private AmmoConfig ammoConfig;
    private static final Gson gson = buildGson();

    @Override
    public void onDisable() {
        this.getLogger().info("[Terra Items] Disabled.");
    }

    @Override
    public void onEnable() {
        initConfigs();
        initCraftingRecipes();
        initCommandExecutors();
        initEventHandlers();

        Bukkit.getLogger().info("[Terra Items] Enabled.");
    }

    public void reloadConfigs() {
        effectsConfig = new EffectsConfig(this);
        weaponsConfig = new WeaponsConfig(this);
        this.getLogger().info("Reloaded configuration files.");
    }

    private void initConfigs() {
        effectsConfig = new EffectsConfig(this);
        weaponsConfig = new WeaponsConfig(this);
        ammoConfig = new AmmoConfig(this);

        saveDefaultConfig();
    }

    private void initCommandExecutors() {
        Objects.requireNonNull(this.getCommand("terraitem"))
                .setExecutor(new ItemSpawn(this));
        Objects.requireNonNull(this.getCommand("terraconfig"))
                .setExecutor(new TerraConfig(this));
    }

    private void initEventHandlers() {
        PluginManager manager = getServer().getPluginManager();

        manager.registerEvents(new OnHitListener(), this);
        manager.registerEvents(new EntityShootBowHandler(), this);
        manager.registerEvents(new InteractEventHandler(), this);
    }

    // Add base item crafting recipes for custom items - we're going to need a better way to do this
    private void initCraftingRecipes() {
//        EquipmentMaterialType[] materials = {
//                EquipmentMaterialType.IRON, EquipmentMaterialType.DIAMOND, EquipmentMaterialType.NETHERITE
//        };
//
//        for (EquipmentMaterialType material : materials) {
//            MeleeWeapon dagger = new MeleeWeapon("DefaultDagger_" + material, WeaponType.DAGGER);
//            MeleeWeapon mace = new MeleeWeapon("DefaultDagger_" + material, WeaponType.MACE);
//            MeleeWeapon staff = new MeleeWeapon("DefaultDagger_" + material, WeaponType.STAFF);
//            MeleeWeapon glaive = new MeleeWeapon("DefaultDagger_" + material, WeaponType.GLAIVE);
//
//            ShapedRecipe[] recipes = {
//                    new ShapedRecipe(
//                            new NamespacedKey(this, material + "dagger"), dagger.getItemStack()),
//                    new ShapedRecipe(
//                            new NamespacedKey(this, material + "mace"), mace.getItemStack()),
//                    new ShapedRecipe(
//                            new NamespacedKey(this, material + "staff"), staff.getItemStack()),
//                    new ShapedRecipe(
//                            new NamespacedKey(this, material + "glaive"), glaive.getItemStack()),
//            };
//
//            recipes[0].shape("   ", " X ", " I ");
//            recipes[1].shape("XIX", " I ", " I ");
//            recipes[2].shape("IXI", " I ", " I ");
//            recipes[3].shape("  X", " I ", "I  ");
//
//            for (ShapedRecipe recipe : recipes) {
//                recipe.setIngredient('X', material.getIngot());
//                recipe.setIngredient('I', Material.STICK);
//                Bukkit.addRecipe(recipe);
//            }
//        }

        //Bukkit.addRecipe(Recipe.LIGHT_SHOT);
    }

    public WeaponsConfig getWeaponsConfig() {
        return weaponsConfig;
    }

    public EffectsConfig getEffectsConfig() {
        return effectsConfig;
    }

    public AmmoConfig getAmmoConfig() { return ammoConfig; }

    public static TerraItems lookupTerraPlugin() {
        return (TerraItems) Objects.requireNonNull(Bukkit.getPluginManager().getPlugin("Terra-Items"));
    }

    private static Gson buildGson() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        return gsonBuilder.create();
    }

    public static Gson getGson() {
        return gson;
    }
}