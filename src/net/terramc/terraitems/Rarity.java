package net.terramc.terraitems;

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
}
