package tk.shanebee.enchBook.events;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;
import tk.shanebee.enchBook.Config;
import tk.shanebee.enchBook.EnchBook;


@SuppressWarnings("FieldCanBeLocal")
public class AnvilPrepare implements Listener {

    private final EnchBook plugin;
    private final boolean SAFE_BOOKS;
    private final int MAX_LEVEL;
    private final boolean REQ_PERM;
    private final String PERM_BYPASS_SAFE = "enchbook.bypass.safe";
    private final String PERM_BYPASS_VANILLA = "enchbook.bypass.vanilla_max_level";
    private final String PERM_BYPASS_MAX = "enchbook.bypass.max_level";

    public AnvilPrepare(EnchBook instance) {
        this.plugin = instance;
        Config config = plugin.getPluginConfig();
        SAFE_BOOKS = config.SAFE_BOOKS;
        MAX_LEVEL = config.MAX_LEVEL;
        REQ_PERM = config.ABOVE_VAN_REQUIRES_PERM;
    }

    @EventHandler
    public void onAnvilPrepare(PrepareAnvilEvent e) {
        if (!(plugin.getPluginConfig().SAFE_ENCHANTS)) {
            Player player = ((Player) e.getViewers().get(0));
            ItemStack item = e.getInventory().getItem(0);
            ItemStack book = e.getInventory().getItem(1);

            if ((item == null) || (book == null)) return;

            if (book.getType() == Material.ENCHANTED_BOOK) {
                if (item.getType() != Material.ENCHANTED_BOOK) {

                    ItemStack result = item.clone();
                    ItemMeta bookMeta = book.getItemMeta();
                    assert bookMeta != null;
                    for (Enchantment enchantment : ((EnchantmentStorageMeta) bookMeta).getStoredEnchants().keySet()) {
                        if (enchantment.canEnchantItem(item)) {
                            int bookLevel = ((EnchantmentStorageMeta) bookMeta).getStoredEnchantLevel(enchantment);
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
                        assert bookMeta != null;
                        assert itemMeta != null;
                        int lvl1 = ((EnchantmentStorageMeta) bookMeta).getStoredEnchantLevel(enchantment);
                        int lvl2 = ((EnchantmentStorageMeta) itemMeta).getStoredEnchantLevel(enchantment);
                        if (lvl1 == lvl2 && lvl1 > 0) {
                            int newLvl = canEnchantAboveMax(lvl1 + 1, enchantment, player) ? lvl1 + 1 : lvl1;
                            assert newMeta != null;
                            ((EnchantmentStorageMeta) newMeta).addStoredEnchant(enchantment, newLvl, true);
                        } else {
                            int newLvl = Math.max(lvl1, lvl2);
                            if (newLvl > 0) {
                                assert newMeta != null;
                                ((EnchantmentStorageMeta) newMeta).addStoredEnchant(enchantment, newLvl, true);
                            }
                        }
                    }
                    result.setItemMeta(newMeta);
                    e.setResult(result);
                }
            }
        }
    }

    private boolean canEnchantAboveMax(int level, Enchantment enchantment, Player player) {
        if (SAFE_BOOKS && level > enchantment.getMaxLevel() && !player.hasPermission(PERM_BYPASS_SAFE)) {
            return false;
        }
        if (!SAFE_BOOKS) {
            if (level > enchantment.getMaxLevel() && REQ_PERM) {
                return player.hasPermission(PERM_BYPASS_VANILLA);
            }
            if (level > MAX_LEVEL) {
                return player.hasPermission(PERM_BYPASS_MAX);
            }
        }
        return true;
    }

}
