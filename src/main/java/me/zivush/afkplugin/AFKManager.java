package me.zivush.afkplugin;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class AFKManager implements Listener, CommandExecutor {
    private final AFKPlugin plugin;
    private final Map<UUID, Long> lastActivity;
    private final Map<UUID, BukkitTask> afkTasks;

    public AFKManager(AFKPlugin plugin) {
        this.plugin = plugin;
        this.lastActivity = new HashMap<>();
        this.afkTasks = new HashMap<>();

        Bukkit.getPluginManager().registerEvents(this, plugin);
        startAFKChecker();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("This command can only be used by players.");
            return true;
        }

        Player player = (Player) sender;
        toggleAFK(player);
        return true;
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        updateLastActivity(event.getPlayer());
    }

    private void startAFKChecker() {
        Bukkit.getScheduler().runTaskTimer(plugin, () -> {
            long now = System.currentTimeMillis();
            for (Player player : Bukkit.getOnlinePlayers()) {
                UUID uuid = player.getUniqueId();
                if (lastActivity.containsKey(uuid)) {
                    long lastActive = lastActivity.get(uuid);
                    if (now - lastActive > plugin.getConfigManager().getAfkTime() * 1000L) {
                        setAFK(player, true);
                    }
                } else {
                    updateLastActivity(player);
                }
            }
        }, 20L, 20L);
    }

    private void updateLastActivity(Player player) {
        lastActivity.put(player.getUniqueId(), System.currentTimeMillis());
        if (isAFK(player)) {
            setAFK(player, false);
        }
    }

    private void toggleAFK(Player player) {
        setAFK(player, !isAFK(player));
    }

    private void setAFK(Player player, boolean afk) {
        UUID uuid = player.getUniqueId();
        if (afk && !isAFK(player)) {
            player.sendTitle(
                    plugin.getConfigManager().getAfkTitle(),
                    plugin.getConfigManager().getAfkSubtitle(),
                    10, 70, 20
            );
            player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, Integer.MAX_VALUE, 0));
            afkTasks.put(uuid, Bukkit.getScheduler().runTaskLater(plugin, () -> player.removePotionEffect(PotionEffectType.BLINDNESS), 100L));
        } else if (!afk && isAFK(player)) {
            player.sendTitle(
                    plugin.getConfigManager().getNotAfkTitle(),
                    plugin.getConfigManager().getNotAfkSubtitle(),
                    10, 70, 20
            );
            player.removePotionEffect(PotionEffectType.BLINDNESS);
            if (afkTasks.containsKey(uuid)) {
                afkTasks.get(uuid).cancel();
                afkTasks.remove(uuid);
            }
        }
    }

    private boolean isAFK(Player player) {
        return afkTasks.containsKey(player.getUniqueId());
    }
}
