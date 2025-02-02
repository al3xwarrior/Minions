package com.al3x.minions.Utils;

import org.bukkit.ChatColor;

public class Color {

    public static String colorize(String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }
    public static String decolorize(String message) {
        return ChatColor.stripColor(message);
    }

}
