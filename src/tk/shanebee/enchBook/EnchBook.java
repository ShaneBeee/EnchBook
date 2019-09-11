package tk.shanebee.enchBook;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;
import tk.shanebee.enchBook.commands.CmdEnchBook;
import tk.shanebee.enchBook.commands.TabEnchBook;
import tk.shanebee.enchBook.events.AnvilPrepare;
import tk.shanebee.enchBook.metrics.Metrics;

@SuppressWarnings("ConstantConditions")
public class EnchBook extends JavaPlugin {

    private Config pluginConfig;
    private String prefix;

    @Override
    @SuppressWarnings("unused")
    public void onEnable() {
        Metrics metrics = new Metrics(this);
        this.pluginConfig = new Config(this);

        prefix = ChatColor.translateAlternateColorCodes('&', "&7[" + this.pluginConfig.PREFIX + "&7] ");
        this.getCommand("enchbook").setPermissionMessage(prefix + getColString(pluginConfig.MSG_NO_PERM));
        this.getCommand("enchbook").setExecutor(new CmdEnchBook(this));
        this.getCommand("enchbook").setTabCompleter(new TabEnchBook());
        this.getServer().getPluginManager().registerEvents(new AnvilPrepare(this), this);

        log("&aLOADED SUCCESSFULLY");
    }

    @Override
    public void onDisable() {
        this.pluginConfig = null;
        getServer().getConsoleSender().sendMessage(prefix + ChatColor.GREEN + "UNLOADED SUCCESSFULLY");
    }

    public Config getPluginConfig() {
        return this.pluginConfig;
    }

    public void reloadPluginConfig() {
        this.pluginConfig = new Config(this);
    }

    private String getColString(String string) {
        return ChatColor.translateAlternateColorCodes('&', string);
    }

    public void log(String message) {
        Bukkit.getConsoleSender().sendMessage(getColString(prefix + message));
    }

}
