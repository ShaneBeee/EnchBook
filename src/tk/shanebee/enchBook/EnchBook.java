package tk.shanebee.enchBook;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import tk.shanebee.enchBook.commands.CmdEnchBook;
import tk.shanebee.enchBook.events.AnvilPrepare;
import tk.shanebee.enchBook.commands.TabEnchBook;
import tk.shanebee.enchBook.metrics.Metrics;

public class EnchBook extends JavaPlugin {

    //Messages.No Permission

    private FileConfiguration config = this.getConfig();

    @Override
    @SuppressWarnings("unused")
    public void onEnable() {
        Metrics metrics = new Metrics(this);
        Config.loadConfig(config);
        saveConfig();

        String prefix = ChatColor.GRAY + "[" +
                (ChatColor.translateAlternateColorCodes('&', this.getConfig().getString("Options.Prefix")))
                + ChatColor.GRAY + "] ";
        String noPerm = ChatColor.translateAlternateColorCodes('&', this.getConfig().getString("Messages.No Permission"));
        getServer().getConsoleSender().sendMessage(prefix + ChatColor.GREEN + "LOADED SUCCESSFULLY");
        this.getCommand("enchbook").setPermissionMessage(prefix + noPerm);
        this.getCommand("enchbook").setExecutor(new CmdEnchBook(this));
        this.getCommand("enchbook").setTabCompleter(new TabEnchBook());
        this.getServer().getPluginManager().registerEvents(new AnvilPrepare(this), this);
    }

    @Override
    public void onDisable() {
        String prefix = ChatColor.GRAY + "[" +
                (ChatColor.translateAlternateColorCodes('&', this.getConfig().getString("Options.Prefix")))
                + ChatColor.GRAY + "] ";
        getServer().getConsoleSender().sendMessage(prefix + ChatColor.GREEN + "UNLOADED SUCCESSFULLY");
    }
}
