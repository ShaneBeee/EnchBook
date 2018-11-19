package tk.shanebee.enchBook;


import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.StringUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Commands implements CommandExecutor, TabCompleter {

    private static EnchBook plugin;
    Commands(EnchBook instance) {
        plugin = instance;
    }

    @SuppressWarnings("deprecation")
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        String prefix = ChatColor.GRAY + "[" +
                (ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("Options.Prefix")))
                + ChatColor.GRAY + "]";

        if(sender instanceof Player) {
            if (args.length >= 2) {
                Player player = (Player) sender;
                String level = "";
                int lvl = 1;
                String enchant = args[1].replace("_", " ").toUpperCase();
                Enchantment ench = Enchantment.getByKey(NamespacedKey.minecraft(args[1]));

                if (args.length == 3 && args[2].matches("\\d+")) {
                    lvl = new Integer(args[2]);
                }
                if (lvl > 1) {
                    level = String.valueOf(lvl);
                }

                if (ench == null) {
                    player.sendMessage(ChatColor.GOLD + args[1].toUpperCase() + ChatColor.RED + " IS NOT AN ENCHANTMENT");
                    return true;
                }
                int max = ench.getMaxLevel();
                if (lvl > max && plugin.getConfig().getBoolean("Options.Safe Enchants")) {
                    player.sendMessage(prefix + ChatColor.RED + " That level is too high");
                    return true;
                }

                if (args[0].equalsIgnoreCase("new")) {
                    ItemStack book = new ItemStack(Material.ENCHANTED_BOOK);
                    ItemMeta meta = book.getItemMeta();
                    ((EnchantmentStorageMeta) meta).addStoredEnchant(ench, lvl, true);
                    book.setItemMeta(meta);
                    player.getInventory().addItem(book);


                    String string = plugin.getConfig().getString("Messages.Create New Book");
                    String msg = (ChatColor.translateAlternateColorCodes('&', string.replace("{level}", level).replace("{ench}", enchant)));
                    player.sendMessage(prefix + " " + msg);

                } else if (args[0].equalsIgnoreCase("add")) {
                    if (player.getInventory().getItemInMainHand().getType() != Material.ENCHANTED_BOOK) {
                        player.sendMessage(prefix + ChatColor.RED + " You can only add enchantments to enchanted books");
                        return true;
                    }
                    ItemStack book = player.getInventory().getItemInMainHand();
                    ItemMeta meta = book.getItemMeta();
                    ((EnchantmentStorageMeta) meta).addStoredEnchant(ench, lvl, true);
                    book.setItemMeta(meta);

                    String string = plugin.getConfig().getString("Messages.Added Enchant");
                    String msg = (ChatColor.translateAlternateColorCodes('&', string.replace("{level}", level).replace("{ench}", enchant)));
                    player.sendMessage(prefix + " " + msg);

                }
            }
        }
        if(args[0].equalsIgnoreCase("reload")) {
            plugin.reloadConfig();
            Config.loadConfig(plugin.getConfig());
            plugin.saveConfig();
            sender.sendMessage(prefix + ChatColor.GREEN + " Config successfully reloaded");
            return true;
        } else if(args[0].equalsIgnoreCase("about")) {
            sender.sendMessage("");
            sender.sendMessage("" + ChatColor.RED + ChatColor.BOLD + ChatColor.UNDERLINE + "ENCHBOOK ABOUT:");
            sender.sendMessage("");
            sender.sendMessage(ChatColor.GOLD + "Version: " + ChatColor.AQUA + plugin.getDescription().getVersion());
            sender.sendMessage(ChatColor.GOLD + "Author: " + ChatColor.AQUA + plugin.getDescription().getAuthors());
            sender.sendMessage(ChatColor.GOLD + "Website: " + ChatColor.AQUA + plugin.getDescription().getWebsite());
            return true;
        } else if(args[0].equalsIgnoreCase("help")) {
            sender.sendMessage("");
            sender.sendMessage("" + ChatColor.RED + ChatColor.BOLD + ChatColor.UNDERLINE + "ENCHBOOK HELP:");
            sender.sendMessage("");
            sender.sendMessage(ChatColor.GOLD + "Commands:");
            sender.sendMessage(ChatColor.AQUA + "/enchbook new <enchantment> <level> " + ChatColor.GRAY + "- Gives yourself a new enchanted book");
            sender.sendMessage(ChatColor.AQUA + "/enchbook add <enchantment> <level> " + ChatColor.GRAY + "- Adds an enchantment to the enchanted book in your hand");
            sender.sendMessage(ChatColor.AQUA + "/enchbook reload " + ChatColor.GRAY + "- Reloads the config");
            sender.sendMessage(ChatColor.AQUA + "/enchbook about " + ChatColor.GRAY + "- Some info about this plugin");
            sender.sendMessage("");
            sender.sendMessage(ChatColor.GOLD + "Command Aliases:");
            sender.sendMessage(ChatColor.AQUA + "/bookench, /eb, /be");

            return true;
        }
        else {
            sender.sendMessage(prefix + ChatColor.GOLD + " Correct Usage: /enchbook <about/add/new/reload>");
            return true;
        }
    }

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
