package tk.shanebee.enchBook.events;

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

    private EnchBook plugin;

    public AnvilPrepare(EnchBook instance) {
        this.plugin = instance;
    }

    @EventHandler
    public void onAnvilPrepare(PrepareAnvilEvent e) {
        if(!(plugin.getPluginConfig().SAFE_ENCHANTS)) {
            ItemStack item = e.getInventory().getItem(0);
            ItemStack book = e.getInventory().getItem(1);

            if ((item == null) || (book == null)) return;

            if (book.getType() == Material.ENCHANTED_BOOK) {
                if (item.getType() != Material.ENCHANTED_BOOK) {

                    ItemStack result = item.clone();
                    ItemMeta meta = e.getInventory().getItem(1).getItemMeta();
                    for (Enchantment enchantment : ((EnchantmentStorageMeta) meta).getStoredEnchants().keySet()) {
                        if (enchantment.canEnchantItem(item)) {
                            int bookLevel = ((EnchantmentStorageMeta) meta).getStoredEnchantLevel(enchantment);
                            int itemLevel = item.getEnchantmentLevel(enchantment);
                            if (itemLevel < bookLevel) {
                                result.addUnsafeEnchantment(enchantment, bookLevel);
                            } else if (itemLevel == bookLevel) {
                                result.addUnsafeEnchantment(enchantment, bookLevel + 1);
                            }
                        }
                    }
                    e.setResult(result);
                } else {
                    ItemStack result = new ItemStack(Material.ENCHANTED_BOOK);
                    ItemMeta newMeta = result.getItemMeta();
                    ItemMeta bookMeta = book.getItemMeta();
                    ItemMeta itemMeta = item.getItemMeta();

                    for (Enchantment enchantment : Enchantment.values()) {
                        int lvl1 = ((EnchantmentStorageMeta) bookMeta).getStoredEnchantLevel(enchantment);
                        int lvl2 = ((EnchantmentStorageMeta) itemMeta).getStoredEnchantLevel(enchantment);
                        if (lvl1 == lvl2 && lvl1 > 0) {
                            int newLvl = (lvl1 + 1 > enchantment.getMaxLevel() && plugin.getPluginConfig().SAFE_BOOKS) ? lvl1 : lvl1 + 1;
                            ((EnchantmentStorageMeta) newMeta).addStoredEnchant(enchantment, newLvl, true);
                        } else {
                            int newLvl = Math.max(lvl1, lvl2);
                            if (newLvl > 0)
                                ((EnchantmentStorageMeta) newMeta).addStoredEnchant(enchantment, newLvl, true);
                        }
                    }
                    result.setItemMeta(newMeta);
                    e.setResult(result);
                }
            }
        }
    }

}
