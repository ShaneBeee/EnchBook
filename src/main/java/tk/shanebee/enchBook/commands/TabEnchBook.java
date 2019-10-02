package tk.shanebee.enchBook.commands;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.util.StringUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class TabEnchBook implements TabCompleter {

    private final List<String> COMMANDS = Arrays.asList("newBook", "addEnchant", "reload", "about", "help", "removeEnchant", "rename", "addLore", "removeLore");
    private final List<String> CONSOLE = Arrays.asList("reload", "about", "help");
    private final List<String> BLANK = Collections.singletonList("");
    private final List<String> ERROR_ENCH = Collections.singletonList(ChatColor.RED + "NOT AN ENCHANTMENT");
    private final List<String> ERROR_TOOL = Collections.singletonList(ChatColor.RED + "BOOK REQUIRED IN HAND");

    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if(!(sender instanceof Player)) return CONSOLE;
        if(command.getName().equalsIgnoreCase("enchbook")) {
            if(args.length == 1) {
                return StringUtil.copyPartialMatches(args[0], COMMANDS, new ArrayList<>());
            } else if(args.length == 2) {
                if(args[0].equalsIgnoreCase("addEnchant")) {
                    List<String> items = new ArrayList<>();
                    for (Enchantment ench : Enchantment.values()) {
                        items.add(ench.getKey().toString().replace("minecraft:", ""));
                    }
                    Player p = (Player) sender;
                    if(p.getInventory().getItemInMainHand().getType() != Material.ENCHANTED_BOOK) return ERROR_TOOL;
                    for (Enchantment ench : ((EnchantmentStorageMeta) p.getInventory().getItemInMainHand().getItemMeta()).getStoredEnchants().keySet()) {
                        items.remove(ench.getKey().toString().replace("minecraft:", ""));
                    }
                    return StringUtil.copyPartialMatches(args[1], items, new ArrayList<>());
                } else if(args[0].equalsIgnoreCase("newbook")) {
                    List<String> items = new ArrayList<>();
                    for (Enchantment ench : Enchantment.values()) {
                        items.add(ench.getKey().toString().replace("minecraft:", ""));
                    }
                    return StringUtil.copyPartialMatches(args[1], items, new ArrayList<>());
                } else if(args[0].equalsIgnoreCase("removeEnchant")) {
                    List<String> items = new ArrayList<>();
                    Player p = (Player)sender;
                    for (Enchantment ench : ((EnchantmentStorageMeta) p.getInventory().getItemInMainHand().getItemMeta()).getStoredEnchants().keySet()) {
                        items.add(ench.getKey().toString().replace("minecraft:", ""));
                    }
                    return StringUtil.copyPartialMatches(args[1], items, new ArrayList<>());
                } return BLANK;
            } else if(args.length == 3) {
                if(args[0].equalsIgnoreCase("rename") ||
                        args[0].equalsIgnoreCase("addLore") ||
                        args[0].equalsIgnoreCase("removeLore")) return BLANK;
                String string = args[1];
                NamespacedKey key = NamespacedKey.minecraft(string);
                Enchantment ench = Enchantment.getByKey(key);
                if(ench != null) {
                    int max = ench.getMaxLevel();
                    if (max >= 2) {
                        List<String> msg = Collections.singletonList("[<max-level=" + max + ">]");
                        return StringUtil.copyPartialMatches(args[2], msg, new ArrayList<>());
                    } else return BLANK;
                } else return ERROR_ENCH;

            } else if(args.length > 3) return BLANK;
        }
        return null;
    }
}
