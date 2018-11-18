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

    @SuppressWarnings("deprecation")
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(sender instanceof Player) {
            Player player = (Player) sender;
            int lvl = 1;
            if (args.length == 3 && args[2].matches("\\d+")) {
                lvl = new Integer(args[2]);
            }
            Enchantment ench = Enchantment.getByKey(NamespacedKey.minecraft(args[1]));
            if (!(ench instanceof Enchantment)) {
                player.sendMessage(ChatColor.GOLD + args[1].toUpperCase() + ChatColor.RED + " IS NOT AN ENCHANTMENT");
                return false;
            }
            Integer max = ench.getMaxLevel();
            if (args[0].equalsIgnoreCase("new")) {
                ItemStack book = new ItemStack(Material.ENCHANTED_BOOK);
                ItemMeta meta = book.getItemMeta();
                ((EnchantmentStorageMeta) meta).addStoredEnchant(ench, lvl, false);
                book.setItemMeta(meta);
                player.getInventory().addItem(book);
            } else if (args[9].equalsIgnoreCase("add")) {
                ItemStack book = player.getInventory().getItemInMainHand();


            }

        } return true;
    }

    private static final List<String> COMMANDS = Arrays.asList("new", "add");
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
                if(ench instanceof Enchantment) {
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
