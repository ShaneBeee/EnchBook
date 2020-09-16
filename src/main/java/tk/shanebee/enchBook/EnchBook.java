package tk.shanebee.enchBook;

import org.bstats.bukkit.Metrics;
import org.bukkit.plugin.java.JavaPlugin;
import tk.shanebee.enchBook.commands.CmdEnchBook;
import tk.shanebee.enchBook.commands.TabEnchBook;
import tk.shanebee.enchBook.events.AnvilPrepare;
import tk.shanebee.enchBook.util.Util;

@SuppressWarnings("ConstantConditions")
public class EnchBook extends JavaPlugin {

    private static EnchBook INSTANCE;
    private Config pluginConfig;

    @Override
    @SuppressWarnings("unused")
    public void onEnable() {
        INSTANCE = this;
        Metrics metrics = new Metrics(this);
        this.pluginConfig = new Config(this);

        String permMsg = Util.getColString(this.pluginConfig.PREFIX + " " + pluginConfig.MSG_NO_PERM);
        this.getCommand("enchbook").setPermissionMessage(permMsg);
        this.getCommand("enchbook").setExecutor(new CmdEnchBook(this));
        this.getCommand("enchbook").setTabCompleter(new TabEnchBook());
        this.getServer().getPluginManager().registerEvents(new AnvilPrepare(this), this);

        Util.log("&aLOADED SUCCESSFULLY");
    }

    @Override
    public void onDisable() {
        Util.log("&aUNLOADED SUCCESSFULLY");
        this.pluginConfig = null;
    }

    public static EnchBook getPlugin() {
        return INSTANCE;
    }

    public Config getPluginConfig() {
        return this.pluginConfig;
    }

    public void reloadPluginConfig() {
        this.pluginConfig = new Config(this);
    }

}
