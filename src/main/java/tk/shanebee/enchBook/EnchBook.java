package tk.shanebee.enchBook;

import org.bstats.bukkit.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.java.JavaPlugin;
import tk.shanebee.enchBook.commands.CmdEnchBook;
import tk.shanebee.enchBook.events.AnvilPrepare;
import tk.shanebee.enchBook.util.Util;

@SuppressWarnings("ConstantConditions")
public class EnchBook extends JavaPlugin {

    private static EnchBook INSTANCE;
    private Config pluginConfig;

    @Override
    public void onEnable() {
        INSTANCE = this;
        new Metrics(this, 3606);
        this.pluginConfig = new Config(this);

        initiateCommand();
        Util.log("&aLOADED SUCCESSFULLY");
    }

    @Override
    public void onDisable() {
        Util.log("&aUNLOADED SUCCESSFULLY");
        this.pluginConfig = null;
    }

    private void initiateCommand() {
        PluginCommand command = this.getCommand("enchbook");
        String permMsg = Util.getColString(pluginConfig.PREFIX + " " + pluginConfig.MSG_NO_PERM);
        command.setPermissionMessage(permMsg);
        command.setExecutor(new CmdEnchBook(this));
        Bukkit.getPluginManager().registerEvents(new AnvilPrepare(this), this);
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
