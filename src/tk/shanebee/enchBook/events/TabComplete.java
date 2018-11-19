package tk.shanebee.enchBook.events;

import org.bukkit.ChatColor;
import org.bukkit.NamespacedKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.util.StringUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TabComplete implements TabCompleter {

    private static final List<String> COMMANDS = Arrays.asList("new", "add", "reload", "about", "help");
    private static final List<String> BLANK = Arrays.asList("", "");
    private static final List<String> ERROR = Arrays.asList(ChatColor.RED + "NOT AN ENCHANTMENT");

    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if(command.getName().equalsIgnoreCase("enchbook")) {
            if(args.length == 1) {
                return StringUtil.copyPartialMatches(args[0], COMMANDS, new ArrayList<>());
            } else if(args.length == 2) {
                List<String> items = new ArrayList<>();
                for (Enchantment ench : Enchantment.values()) {
                    items.add(ench.getKey().toString().replace("minecraft:", ""));
                }
                return StringUtil.copyPartialMatches(args[1], items, new ArrayList<>());

            } else if(args.length == 3) {
                String string = args[1];
                NamespacedKey key = NamespacedKey.minecraft(string);
                Enchantment ench = Enchantment.getByKey(key);
                if(ench != null) {
                    int max = ench.getMaxLevel();
                    if (max >= 2) {
                        List<String> msg = Arrays.asList("[<max-level=" + max + ">]");
                        return StringUtil.copyPartialMatches(args[2], msg, new ArrayList<>());
                    } else return BLANK;
                } else return ERROR;

            } return BLANK;
        }
        return null;
    }
}
