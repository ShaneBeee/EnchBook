package tk.shanebee.enchBook.util;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import tk.shanebee.enchBook.EnchBook;

public class Util {

    private static final String prefix = "&7[&bEnchBook&7] ";

    public static String getColString(String string) {
        return ChatColor.translateAlternateColorCodes('&', string);
    }

    public static void log(String format, Object... objects) {
        String message = String.format(format, objects);
        message = getColString(prefix + message);
        Bukkit.getConsoleSender().sendMessage(message);
    }

}
