package net.terramc.terraitems.shared;

import net.terramc.terraitems.TerraItems;
import org.bukkit.configuration.ConfigurationSection;

import java.io.File;

public class ConfigUtility {
    public static String readString(ConfigurationSection section, String attribute) {
        String val = section.getString(attribute);

        if (val == null)
            throw new IllegalArgumentException(section + " missing config attribute " + attribute);
        else
            return val.toUpperCase();
    }

    public static File createConfigFile(String configFileName, TerraItems plugin) {
        File configFile = new File(plugin.getDataFolder(), configFileName);
        if (!configFile.exists()) {
            configFile.getParentFile().mkdirs();
            plugin.saveResource(configFileName, false);
        }

        return configFile;
    }
}
