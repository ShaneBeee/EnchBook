package tk.shanebee.enchBook;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginDescriptionFile;

public class Config implements Listener {

    public final String PREFIX;
    public final boolean SAFE_ENCHANTS;
    public final boolean SAFE_BOOKS;
    public final int MAX_LEVEL;
    public final boolean ABOVE_VAN_REQUIRES_PERM;

    final String MSG_NO_PERM;

    Config(EnchBook plugin) {
        FileConfiguration config = plugin.getConfig();
        PluginDescriptionFile pdfFile = plugin.getDescription();

        config.addDefault("Options.Prefix", "&7[&bEnchBook&7]");
        config.addDefault("Options.Safe Enchants", true);
        config.addDefault("Options.Safe Books", true);
        config.addDefault("Options.Max Level", 10);
        config.addDefault("Options.Above Vanilla Requires Permission", false);



        config.addDefault("Messages.Create New Book", "&aYou created a new enchanted book with &b{ench} {level}");
        config.addDefault("Messages.Added Enchant", "&aYou have successfully added &b{ench} {level} &ato your book");
        config.addDefault("Messages.Removed Enchant", "&aYou have successfully removed &b{ench} &afrom your book");
        config.addDefault("Messages.No Permission", "&cYou do not have permission to use this command");

        config.options().copyDefaults(true);

        config.options().header("EnchBook\n" + "Version: " + pdfFile.getVersion() + "\n\n" +
                "SAFE ENCHANTS:\n" + "If safe enchants is true, you will only be able to enchant books with the max level of said enchant\n" +
                "If it is false, you can enchant to your hearts desire, and also enchant tools/weapons/armor in the anvil with them\n\n" +
                "SAFE BOOKS:\n" + "If safe books is true, players will not be able to join 2 of the same books (which would increase the level by 1)\n" +
                "to increase the level over the max possible level for that enchant\n\n" +
                "VARIABLES FOR MESSAGES:\n" + "{ench} = Enchantment\n" + "{level} = Level of Enchantment" + "\n\n");
        plugin.saveConfig();

        this.PREFIX = config.getString("Options.Prefix");
        this.SAFE_ENCHANTS = config.getBoolean("Options.Safe Enchants");
        this.SAFE_BOOKS = config.getBoolean("Options.Safe Books");
        this.MAX_LEVEL = config.getInt("Options.Max Level");
        this.ABOVE_VAN_REQUIRES_PERM = config.getBoolean("Options.Above Vanilla Requires Permission");
        this.MSG_NO_PERM = config.getString("Messages.No Permission");
    }

}
