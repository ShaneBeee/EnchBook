package tk.shanebee.enchBook.util;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import tk.shanebee.enchBook.EnchBook;

public class Util {

    public static String getColString(String string) {
        return ChatColor.translateAlternateColorCodes('&', string);
    }

    public static void log(String message) {
        Bukkit.getConsoleSender().sendMessage(getColString(EnchBook.getPlugin().getPluginConfig().PREFIX + " " + message));
    }

}
