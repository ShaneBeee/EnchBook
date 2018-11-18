package tk.shanebee.enchBook;

import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

public class EnchBook extends JavaPlugin {

    @Override
    public void onEnable() {

        getServer().getConsoleSender().sendMessage(ChatColor.AQUA + "LOADED SUCCESSFULLY");
        this.getCommand("enchbook").setExecutor(new Commands());
        this.getCommand("enchbook").setTabCompleter(new Commands());

    }

    @Override
    public void onDisable() {
        getServer().getConsoleSender().sendMessage("UNLOADED");
    }
}
