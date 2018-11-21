package tk.shanebee.enchBook.events;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;
import tk.shanebee.enchBook.EnchBook;


public class AnvilPrepare implements Listener {

    private static EnchBook plugin;
    public AnvilPrepare(EnchBook instance) {
        plugin = instance;
    }

    @EventHandler
    public void onAnvilPrepare(PrepareAnvilEvent e) {
        if(!(plugin.getConfig().getBoolean("Options.Safe Enchants"))) {
            ItemStack item = e.getInventory().getItem(0);
            ItemStack book = e.getInventory().getItem(1);

            if((item == null) || (book == null)) return;

            if(book.getType() == Material.ENCHANTED_BOOK) {
                if(item.getType() != Material.ENCHANTED_BOOK) {

                    ItemStack result = item.clone();
                    ItemMeta meta = e.getInventory().getItem(1).getItemMeta();
                    for (Enchantment enchantment : ((EnchantmentStorageMeta) meta).getStoredEnchants().keySet()) {
                        if (enchantment.canEnchantItem(item)) {
                            int lvl = ((EnchantmentStorageMeta) meta).getStoredEnchantLevel(enchantment);
                            if (item.getEnchantmentLevel(enchantment) < lvl) {
                                result.addUnsafeEnchantment(enchantment, lvl);
                            }
                        }
                    }
                    e.setResult(result);
                } else {
                    ItemStack result = new ItemStack(Material.ENCHANTED_BOOK);
                    ItemMeta newMeta = result.getItemMeta();
                    ItemMeta bookMeta = book.getItemMeta();
                    ItemMeta itemMeta = item.getItemMeta();
                    for (Enchantment enchantment : ((EnchantmentStorageMeta) bookMeta).getStoredEnchants().keySet()) {
                        int lvl = ((EnchantmentStorageMeta) bookMeta).getStoredEnchantLevel(enchantment);
                        ((EnchantmentStorageMeta) newMeta).addStoredEnchant(enchantment, lvl, true);
                    }
                    for (Enchantment enchantment : ((EnchantmentStorageMeta) itemMeta).getStoredEnchants().keySet()) {
                        int lvl = ((EnchantmentStorageMeta) itemMeta).getStoredEnchantLevel(enchantment);
                        ((EnchantmentStorageMeta) newMeta).addStoredEnchant(enchantment, lvl, true);
                    }
                    result.setItemMeta(newMeta);
                    e.setResult(result);

                }
            }
        }
    }

}
