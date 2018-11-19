package tk.shanebee.enchBook;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import tk.shanebee.enchBook.events.AnvilPrepare;
import tk.shanebee.enchBook.events.TabComplete;
import tk.shanebee.enchBook.metrics.Metrics;

public class EnchBook extends JavaPlugin {

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
        getServer().getConsoleSender().sendMessage(prefix + ChatColor.GREEN + "LOADED SUCCESSFULLY");
        this.getCommand("enchbook").setExecutor(new Commands(this));
        this.getCommand("enchbook").setTabCompleter(new TabComplete());
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
