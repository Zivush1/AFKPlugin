package me.zivush.afkplugin;

import org.bukkit.plugin.java.JavaPlugin;

public class AFKPlugin extends JavaPlugin {
    private AFKManager afkManager;
    private ConfigManager configManager;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        configManager = new ConfigManager(this);
        afkManager = new AFKManager(this);
        getCommand("afk").setExecutor(afkManager);
        getLogger().info("AFKPlugin has been enabled!");
    }

    @Override
    public void onDisable() {
        getLogger().info("AFKPlugin has been disabled!");
    }

    public ConfigManager getConfigManager() {
        return configManager;
    }
}
