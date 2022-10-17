package net.terramc.terraitems;

import net.terramc.terraitems.commands.ItemSpawn;
import net.terramc.terraitems.commands.TerraConfig;
import net.terramc.terraitems.eventhandlers.OnHitListener;
import net.terramc.terraitems.shared.EquipmentMaterialType;
import net.terramc.terraitems.weapons.MeleeWeapon;
import net.terramc.terraitems.weapons.WeaponType;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public class TerraItems extends JavaPlugin {

    private WeaponsConfig weaponsConfig;
    private EffectsConfig effectsConfig;

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
        saveDefaultConfig();
    }

    private void initCommandExecutors() {
        Objects.requireNonNull(this.getCommand("terraitem"))
                .setExecutor(new ItemSpawn(this));
        Objects.requireNonNull(this.getCommand("terraconfig"))
                .setExecutor(new TerraConfig(this));
    }

    private void initEventHandlers() {
        getServer().getPluginManager().registerEvents(new OnHitListener(
                        weaponsConfig, this),
                this
        );
    }

    // Add base item crafting recipes for custom items - were gonna need a better way to do this
    private void initCraftingRecipes() {
        EquipmentMaterialType[] materials = {
                EquipmentMaterialType.IRON, EquipmentMaterialType.DIAMOND, EquipmentMaterialType.NETHERITE
        };

        for (EquipmentMaterialType material : materials) {
            MeleeWeapon dagger = new MeleeWeapon("default-dagger", material, WeaponType.DAGGER);
            MeleeWeapon mace = new MeleeWeapon("default-mace", material, WeaponType.MACE);
            MeleeWeapon staff = new MeleeWeapon("default-staff", material, WeaponType.STAFF);
            MeleeWeapon glaive = new MeleeWeapon("default-glaive", material, WeaponType.GLAIVE);

            ShapedRecipe[] recipes = {
                    new ShapedRecipe(
                            new NamespacedKey(this, material + "dagger"), dagger.getItemStack()),
                    new ShapedRecipe(
                            new NamespacedKey(this, material + "mace"), mace.getItemStack()),
                    new ShapedRecipe(
                            new NamespacedKey(this, material + "staff"), staff.getItemStack()),
                    new ShapedRecipe(
                            new NamespacedKey(this, material + "glaive"), glaive.getItemStack()),
            };

            recipes[0].shape("   ", " X ", " I ");
            recipes[1].shape("XIX", " I ", " I ");
            recipes[2].shape("IXI", " I ", " I ");
            recipes[3].shape("  X", " I ", "I  ");

            for (ShapedRecipe recipe : recipes) {
                recipe.setIngredient('X', material.getIngot());
                recipe.setIngredient('I', Material.STICK);
                Bukkit.addRecipe(recipe);
            }
        }
    }

    public WeaponsConfig getWeaponsConfig() {
        return weaponsConfig;
    }

    public EffectsConfig getEffectsConfig() {
        return effectsConfig;
    }

    public static TerraItems lookupTerraPlugin() {
        return (TerraItems) Objects.requireNonNull(Bukkit.getPluginManager().getPlugin("Terra-Items"));
    }
}