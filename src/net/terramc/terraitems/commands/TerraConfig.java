package net.terramc.terraitems.commands;

import net.terramc.terraitems.TerraItems;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import java.util.Objects;

public class TerraConfig implements CommandExecutor {

    private final TerraItems plugin;

    @Override
    public boolean onCommand(@Nonnull CommandSender sender,
                             @Nonnull Command command,
                             @Nonnull String s,
                             @Nonnull String[] args
    ) {
        if (sender instanceof Player) {
            Player player = (Player) sender;

            if (Objects.equals(args[0], "reload")) {
                // reload config files
                plugin.reloadConfigs();
                player.sendMessage("Reloaded config files.");
            }

            return true;
        } else {
            plugin.getLogger().info("Only players can use this command.");
            return false;
        }
    }

    public TerraConfig(TerraItems plugin) {
        this.plugin = plugin;
    }
}
