package tk.shanebee.enchBook;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class EnchBook extends JavaPlugin {

    private FileConfiguration config = this.getConfig();


    @Override
    public void onEnable() {

        Config.loadConfig(config);
        saveConfig();

        String prefix = ChatColor.GRAY + "[" +
                (ChatColor.translateAlternateColorCodes('&', this.getConfig().getString("Options.Prefix")))
                + ChatColor.GRAY + "] ";
        getServer().getConsoleSender().sendMessage(prefix + ChatColor.GREEN + "LOADED SUCCESSFULLY");
        this.getCommand("enchbook").setExecutor(new Commands(this));
        this.getCommand("enchbook").setTabCompleter(new Commands(this));
    }

    @Override
    public void onDisable() {
        String prefix = ChatColor.GRAY + "[" +
                (ChatColor.translateAlternateColorCodes('&', this.getConfig().getString("Options.Prefix")))
                + ChatColor.GRAY + "] ";
        getServer().getConsoleSender().sendMessage(prefix + ChatColor.GREEN + "UNLOADED SUCCESSFULLY");
    }
}
