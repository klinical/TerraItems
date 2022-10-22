package net.terramc.terraitems;

import net.terramc.terraitems.effects.configuration.EffectApplication;
import net.terramc.terraitems.effects.configuration.Effect;
import net.terramc.terraitems.effects.configuration.EffectMeta;
import net.terramc.terraitems.effects.configuration.EffectTrigger;
import net.terramc.terraitems.shared.ConfigUtility;
import net.terramc.terraitems.effects.TerraEffect;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.*;
import java.util.logging.Logger;

public class EffectsConfig {
    private final FileConfiguration config;
    private final TerraItems plugin;
    private HashMap<String, TerraEffect> effects;

    EffectsConfig(TerraItems plugin) {
        this.plugin = plugin;

        File configFile = ConfigUtility.createConfigFile("effects.yml", plugin);
        this.config = YamlConfiguration.loadConfiguration(configFile);

        readEffects();

        plugin.getLogger().info("Loaded effects.yml.");
    }

    private void readEffects() {
        // Initialize hashmap of item key -> item as an ItemStack
        effects = new HashMap<>();
        Logger logger = plugin.getLogger();
        Set<String> sectionEntries = config.getKeys(false);

        for (String effectTitle : sectionEntries) {
            // Required
            try {
                EffectApplication effectApplication = new EffectApplication(
                        config.getConfigurationSection(effectTitle + ".application"));

                Effect effect = new Effect(
                        config.getConfigurationSection(effectTitle + ".effect"));


                TerraEffect terraEffect = new TerraEffect(effectApplication, effect, effectTitle);

                ConfigurationSection metaSection = config.getConfigurationSection(effectTitle + ".meta");
                if (metaSection != null) {
                    EffectMeta effectMeta = new EffectMeta(metaSection);
                    terraEffect.setTerraMeta(effectMeta);
                }

                logger.info("Loaded " + effectApplication.getApplicationType() + " effect " + effectTitle);

                effects.put(effectTitle, terraEffect);
            } catch (IllegalArgumentException ex) {
                logger.warning(ex.toString());
            }
        }
    }

    public HashMap<String, TerraEffect> getItems() {
        return effects;
    }
}
