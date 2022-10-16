package net.terramc.terraitems.shared;

import org.bukkit.ChatColor;

public enum Rarity {
    JUNK,
    COMMON,
    UNCOMMON,
    RARE,
    EPIC,
    LEGENDARY;
    
    public ChatColor titleColor() {
        switch (this) {
            case JUNK:
                return ChatColor.DARK_GRAY;
            case UNCOMMON:
                return ChatColor.GREEN;
            case RARE:
                return ChatColor.BLUE;
            case EPIC:
                return ChatColor.LIGHT_PURPLE;
            case LEGENDARY:
                return ChatColor.GOLD;
            default:
                // Default to 'common'
                return ChatColor.WHITE;
        }
    }

    public String getDisplayName() {
        switch (this) {
            case JUNK:
                return "Junk";

            default:
            case COMMON:
                return  "Common";

            case UNCOMMON:
                return "Uncommon";
            case RARE:
                return "Rare";
            case EPIC:
                return "Epic";
            case LEGENDARY:
                return "Legendary";
        }
    }

    public static ChatColor getPrefix(Rarity rarity) {
        switch (rarity) {
            case JUNK:
                return ChatColor.DARK_GRAY;
            case UNCOMMON:
                return ChatColor.GREEN;
            case RARE:
                return ChatColor.BLUE;
            case EPIC:
                return ChatColor.LIGHT_PURPLE;
            case LEGENDARY:
                return ChatColor.GOLD;
            default:
                // Default to 'common'
                return ChatColor.WHITE;
        }
    }
}
