package net.terramc.terraitems.effects.configuration;

import org.bukkit.configuration.ConfigurationSection;

import java.util.Objects;

public class EffectMeta {

    private final String userNotification;
    private final String targetNotification;
    private final String display;

    public EffectMeta(ConfigurationSection section) {
        Objects.requireNonNull(section);
        userNotification = section.getString("user-notification");
        targetNotification = section.getString("target-notification");
        display = section.getString("display");
    }

    public String getUserNotification() {
        return userNotification;
    }

    public String getTargetNotification() {
        return targetNotification;
    }

    public String getDisplay() {
        return display;
    }
}
