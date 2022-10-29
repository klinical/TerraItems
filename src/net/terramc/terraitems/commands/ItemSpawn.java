package net.terramc.terraitems.commands;

import net.terramc.terraitems.TerraItems;
import net.terramc.terraitems.ammunition.Ammo;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.Objects;
import java.util.Set;

public class ItemSpawn implements CommandExecutor {

    private final TerraItems plugin;

    @Override
    public boolean onCommand(@Nonnull CommandSender sender,
                             @Nonnull Command command,
                             @Nonnull String s,
                             @Nonnull String[] args
    ) {
        if (sender instanceof Player) {
            Player player = (Player) sender;

            if (args.length > 0) {
                switch (args[0]) {
                    case "give":
                        if (args.length == 2) {
                            giveItem(player, args[1]);
                        } else if (args.length == 3) {
                            Player targetPlayer = Bukkit.getPlayer(args[2]);

                            if (targetPlayer != null) {
                                giveItem(targetPlayer, args[1]);
                            }
                        } else {
                            player.sendMessage("Invalid number of arguments (2 - 3).");
                        }
                        break;
                    case "ammo":
                        if (args.length == 2) {
                            Ammo ammo = plugin.getAmmoConfig().getItems().get(args[1]);

                            player.getInventory().addItem(ammo.getItemStack());
                        }
                        break;
                    case "list":
                        if (Objects.equals(args[1], "weapons")) {
                            Set<String> itemKeys = plugin.getWeaponsConfig().getItems().keySet();

                            player.sendMessage(Arrays.toString(itemKeys.toArray()));
                        } else if (Objects.equals(args[1], "effects")) {
                            Set<String> itemKeys = plugin.getEffectsConfig().getItems().keySet();

                            player.sendMessage(Arrays.toString(itemKeys.toArray()));
                        } else if (Objects.equals(args[1], "ammo")) {
                            Set<String> ammoKeys = plugin.getAmmoConfig().getItems().keySet();

                            player.sendMessage(Arrays.toString(ammoKeys.toArray()));
                        }
                        break;

                    case "add":
                    case "modify":
                    case "remove":
                    default:
                        break;
                }
            }

            return true;
        } else {
            plugin.getLogger().info("Only players can use this command.");
            return false;
        }
    }

    private void giveItem(Player giveTarget, String itemName) {
        ItemStack newItem = plugin.getWeaponsConfig()
                .getItems()
                .get(itemName)
                .getItemStack();

        giveTarget.getInventory().addItem(newItem);
    }

    public ItemSpawn(TerraItems plugin) {
        this.plugin = plugin;
    }
}
