package tk.shanebee.enchBook;

import org.bstats.bukkit.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;
import tk.shanebee.enchBook.commands.CmdEnchBook;
import tk.shanebee.enchBook.commands.TabEnchBook;
import tk.shanebee.enchBook.events.AnvilPrepare;

@SuppressWarnings("ConstantConditions")
public class EnchBook extends JavaPlugin {

    private Config pluginConfig;
    private String prefix;

    @Override
    @SuppressWarnings("unused")
    public void onEnable() {
        Metrics metrics = new Metrics(this);
        this.pluginConfig = new Config(this);

        prefix = getColString(this.pluginConfig.PREFIX + " ");
        this.getCommand("enchbook").setPermissionMessage(prefix + getColString(pluginConfig.MSG_NO_PERM));
        this.getCommand("enchbook").setExecutor(new CmdEnchBook(this));
        this.getCommand("enchbook").setTabCompleter(new TabEnchBook());
        this.getServer().getPluginManager().registerEvents(new AnvilPrepare(this), this);

        log("&aLOADED SUCCESSFULLY");
    }

    @Override
    public void onDisable() {
        this.pluginConfig = null;
        log("&aUNLOADED SUCCESSFULLY");
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
