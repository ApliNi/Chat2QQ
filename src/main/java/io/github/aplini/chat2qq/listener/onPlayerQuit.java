package io.github.aplini.chat2qq.listener;

import io.github.aplini.chat2qq.Chat2QQ;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import static io.github.aplini.chat2qq.utils.Util.sendToGroup;

public class onPlayerQuit implements Listener {
    private final Chat2QQ plugin;
    public onPlayerQuit(Chat2QQ plugin){
        this.plugin = plugin;
    }
    private static final ArrayList<Player> cache = new ArrayList<>();

    @EventHandler
    public void onPlayerQuitEvent(PlayerQuitEvent e){
        if(plugin.getConfig().getBoolean("bot.send-player-join-quit-message",false) && !e.getPlayer().hasPermission("chat2qq.quit.silent") && !cache.contains(e.getPlayer())){
            CompletableFuture.runAsync(() -> {
                String message = plugin.getConfig().getString("bot.player-quit-message", "player Quit").replace("%player%", e.getPlayer().getName());
                plugin.getConfig().getLongList("bot.bot-accounts").forEach(bot -> plugin.getConfig().getLongList("general.group-ids").forEach(group -> {
                    sendToGroup(plugin, group, message);
                    int interval = plugin.getConfig().getInt("bot.player-join-quit-message-interval");
                    if (interval > 0) {
                        cache.add(e.getPlayer());
                        CompletableFuture.runAsync(() -> {
                            try {
                                TimeUnit.MILLISECONDS.sleep(interval * 1000L);
                            } catch (InterruptedException ex) {
                                throw new RuntimeException(ex);
                            }
                            cache.remove(e.getPlayer());
                        });
                    }
                }));
            });
        }
    }
}
