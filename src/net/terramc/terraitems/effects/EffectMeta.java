package net.terramc.terraitems.effects;

import org.bukkit.configuration.ConfigurationSection;

import java.util.Objects;

public class EffectMeta {

    private String userNotification;
    private String targetNotification;
    private String display;

    public EffectMeta(String userNotification, String targetNotification, String display) {
        this.userNotification = userNotification;
        this.targetNotification = targetNotification;
        this.display = display;
    }

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
