package tk.shanebee.enchBook.commands;


import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;
import tk.shanebee.enchBook.Config;
import tk.shanebee.enchBook.EnchBook;

import java.util.ArrayList;
import java.util.List;

public class CmdEnchBook implements CommandExecutor {

    private static EnchBook plugin;
    public CmdEnchBook(EnchBook instance) {
        plugin = instance;
    }


    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        String prefix = ChatColor.GRAY + "[" +
                (ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("Options.Prefix")))
                + ChatColor.GRAY + "]";

        if(sender instanceof Player) {
            if (args.length >= 2) {
                Player player = (Player) sender;
                Material hand = player.getInventory().getItemInMainHand().getType();

                if(args[0].equalsIgnoreCase("rename")) {
                    if (hand != Material.ENCHANTED_BOOK) {
                        player.sendMessage(prefix + ChatColor.RED + " You can only rename an enchanted book");
                        return true;
                    }
                    StringBuilder sb = new StringBuilder();
                    for (int i = 1; i < args.length; i++){
                        sb.append(args[i]).append(" ");
                    }
                    String allArgs = sb.toString().trim();
                    String newName = ChatColor.translateAlternateColorCodes('&', allArgs);
                    ItemStack item = player.getInventory().getItemInMainHand();
                    ItemMeta meta = item.getItemMeta();
                    meta.setDisplayName(newName);
                    item.setItemMeta(meta);
                    player.sendMessage(prefix + ChatColor.GREEN + " You have successfully renamed your book to: " + newName);
                    return true;
                }
                if(args[0].equalsIgnoreCase("addLore")) {
                    if (hand != Material.ENCHANTED_BOOK) {
                        player.sendMessage(prefix + ChatColor.RED + " You can only rename an enchanted book");
                        return true;
                    }
                    StringBuilder sb = new StringBuilder();
                    for (int i = 1; i < args.length; i++){
                        sb.append(args[i]).append(" ");
                    }
                    String allArgs = sb.toString().trim();
                    String newLore = ChatColor.translateAlternateColorCodes('&', allArgs);

                    ItemStack item = player.getInventory().getItemInMainHand();
                    ItemMeta meta = item.getItemMeta();
                    List<String> lore = new ArrayList<>();
                    if(meta.hasLore()) lore = meta.getLore();

                    lore.add(newLore);
                    meta.setLore(lore);
                    item.setItemMeta(meta);
                    player.sendMessage(prefix + ChatColor.GREEN + " You have successfully add lore to your book: " + newLore);
                    return true;
                }
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

                if (args[0].equalsIgnoreCase("newbook")) {
                    ItemStack book = new ItemStack(Material.ENCHANTED_BOOK);
                    ItemMeta meta = book.getItemMeta();
                    ((EnchantmentStorageMeta) meta).addStoredEnchant(ench, lvl, true);
                    book.setItemMeta(meta);
                    player.getInventory().addItem(book);


                    String string = plugin.getConfig().getString("Messages.Create New Book");
                    String msg = (ChatColor.translateAlternateColorCodes('&', string.replace("{level}", level).replace("{ench}", enchant)));
                    player.sendMessage(prefix + " " + msg);

                } else if (args[0].equalsIgnoreCase("addEnchant")) {
                    if (hand != Material.ENCHANTED_BOOK) {
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
                } else if(args[0].equalsIgnoreCase("removeEnchant")) {
                    if (hand != Material.ENCHANTED_BOOK) {
                        player.sendMessage(prefix + ChatColor.RED + " You can only remove enchantments from enchanted books");
                        return true;
                    }
                    ItemStack book = player.getInventory().getItemInMainHand();
                    ItemMeta meta = book.getItemMeta();
                    if(!((EnchantmentStorageMeta) meta).hasStoredEnchant(ench)) {
                        player.sendMessage(prefix + ChatColor.RED + " That book is not enchanted with " + ChatColor.AQUA + args[1].toUpperCase().replace("_", " "));
                        return true;
                    }
                    ((EnchantmentStorageMeta) meta).removeStoredEnchant(ench);
                    book.setItemMeta(meta);
                    String string = plugin.getConfig().getString("Messages.Removed Enchant");
                    String msg = (ChatColor.translateAlternateColorCodes('&', string.replace("{ench}", enchant)));
                    player.sendMessage(prefix + " " + msg);
                }
            } else if(args.length == 1) {
                if(args[0].equalsIgnoreCase("removeLore")) {
                    Player player = ((Player) sender);
                    ItemStack hand = player.getInventory().getItemInMainHand();
                    if (hand.getType() != Material.ENCHANTED_BOOK) {
                        player.sendMessage(prefix + ChatColor.RED + " You can only remove lore from an enchanted book");
                        return true;
                    }
                    ItemMeta meta = hand.getItemMeta();
                    if(!meta.hasLore()) {
                        player.sendMessage(prefix + ChatColor.RED + " This book does not have any more");
                        return true;
                    }
                    meta.setLore(null);
                    hand.setItemMeta(meta);
                    player.sendMessage(prefix + ChatColor.GREEN + " You have successfully remove all lore from your book");
                    return true;
                }
            }
        }

        if(args.length == 1) {
            if (args[0].equalsIgnoreCase("reload")) {
                plugin.reloadConfig();
                Config.loadConfig(plugin.getConfig());
                plugin.saveConfig();
                sender.sendMessage(prefix + ChatColor.GREEN + " Config successfully reloaded");
                return true;
            } else if (args[0].equalsIgnoreCase("about")) {
                sender.sendMessage("");
                sender.sendMessage("" + ChatColor.RED + ChatColor.BOLD + ChatColor.UNDERLINE + "ENCHBOOK ABOUT:");
                sender.sendMessage("");
                sender.sendMessage(ChatColor.GOLD + "Version: " + ChatColor.AQUA + plugin.getDescription().getVersion());
                sender.sendMessage(ChatColor.GOLD + "Author: " + ChatColor.AQUA + plugin.getDescription().getAuthors());
                sender.sendMessage(ChatColor.GOLD + "Website: " + ChatColor.AQUA + plugin.getDescription().getWebsite());
                return true;
            } else if (args[0].equalsIgnoreCase("help")) {
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
            } else {
                sender.sendMessage(prefix + ChatColor.GOLD + " Correct Usage: /enchbook <about/add/new/reload>");
                return true;
            }
        } return true;
    }

}
