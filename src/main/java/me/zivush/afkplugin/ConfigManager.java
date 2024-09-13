package me.zivush.afkplugin;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;

public class ConfigManager {
    private final AFKPlugin plugin;
    private final FileConfiguration config;

    public ConfigManager(AFKPlugin plugin) {
        this.plugin = plugin;
        this.config = plugin.getConfig();
        loadDefaultConfig();
    }

    private void loadDefaultConfig() {
        config.addDefault("afk_time", 300);
        config.addDefault("afk_title", "&6You are now AFK");
        config.addDefault("afk_subtitle", "&7Use /afk to toggle");
        config.addDefault("not_afk_title", "&aWelcome back!");
        config.addDefault("not_afk_subtitle", "&7You are no longer AFK");
        config.options().copyDefaults(true);
        plugin.saveConfig();
    }

    public int getAfkTime() {
        return config.getInt("afk_time", 300);
    }

    public String getAfkTitle() {
        return ChatColor.translateAlternateColorCodes('&', config.getString("afk_title", "&6You are now AFK"));
    }

    public String getAfkSubtitle() {
        return ChatColor.translateAlternateColorCodes('&', config.getString("afk_subtitle", "&7Use /afk to toggle"));
    }

    public String getNotAfkTitle() {
        return ChatColor.translateAlternateColorCodes('&', config.getString("not_afk_title", "&aWelcome back!"));
    }

    public String getNotAfkSubtitle() {
        return ChatColor.translateAlternateColorCodes('&', config.getString("not_afk_subtitle", "&7You are no longer AFK"));
    }
}
