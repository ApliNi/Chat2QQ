package io.github.aplini.chat2qq.listener;

import io.github.aplini.chat2qq.Chat2QQ;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;

import static io.github.aplini.chat2qq.utils.Util.sendToGroup;

public class onPlayerJoin implements Listener {
    private final Chat2QQ plugin;
    public onPlayerJoin(Chat2QQ plugin){
        this.plugin = plugin;
    }
    private static final ArrayList<Player> cache = new ArrayList<>();

    @EventHandler
    public void onPlayerJoinEvent(PlayerJoinEvent e){
        if(plugin.getConfig().getBoolean("bot.send-player-join-quit-message",false) && !e.getPlayer().hasPermission("chat2qq.join.silent") && !cache.contains(e.getPlayer())){
            new BukkitRunnable() {
                @Override
                public void run() {
                    String message = plugin.getConfig().getString("bot.player-join-message", "player Join").replace("%player%", e.getPlayer().getName());
                    plugin.getConfig().getLongList("bot.bot-accounts").forEach(bot -> plugin.getConfig().getLongList("general.group-ids").forEach(group -> {
                        try {
                            sendToGroup(plugin, group, message);
                        } finally {
                            int interval = plugin.getConfig().getInt("bot.player-join-quit-message-interval");
                            if(interval > 0) {
                                cache.add(e.getPlayer());
                                new BukkitRunnable() {
                                    @Override
                                    public void run() {
                                        cache.remove(e.getPlayer());
                                    }
                                }.runTaskLaterAsynchronously(plugin,interval * 20L);
                            }
                        }
                    }));
                }
            }.runTaskAsynchronously(plugin);
        }
    }
}
