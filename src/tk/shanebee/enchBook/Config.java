package tk.shanebee.enchBook;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginDescriptionFile;

public class Config implements Listener {

    public static void loadConfig(FileConfiguration config) {
        PluginDescriptionFile pdfFile = EnchBook.getPlugin(EnchBook.class).getDescription();

        config.addDefault("Options.Prefix", "&bEnchBook");
        config.addDefault("Options.Safe Enchants", true);



        config.addDefault("Messages.Create New Book", "&aYou created a new enchanted book with &b{ench} {level}");
        config.addDefault("Messages.Added Enchant", "&aYou have successfully added &b{ench} {level} &ato your book");
        config.addDefault("Messages.Removed Enchant", "&aYou have successfully removed &b{ench} &afrom your book");
        config.addDefault("Messages.No Permission", "&cYou do not have permission to use this command");

        config.options().copyDefaults(true);

        config.options().header("EnchBook\n" + "Version: " + pdfFile.getVersion() + "\n\n" +
                "SAFE ENCHANTS:\n" + "If safe enchants is true, you will only be able to enchant books with the max level of said enchant\n" +
                "If it is false, you can enchant to your hearts desire, and also enchant tools/weapons/armor in the anvil with them\n\n" +
                "VARIABLES FOR MESSAGES:\n" + "{ench} = Enchantment\n" + "{level} = Level of Enchantment" + "\n\n");
    }
}
