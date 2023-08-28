package io.github.aplini.chat2qq.listener;

import io.github.aplini.chat2qq.Chat2QQ;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.concurrent.CompletableFuture;

import static io.github.aplini.chat2qq.utils.Util.sendToGroup;

public class onPlayerMessage implements Listener {
    private final Chat2QQ plugin;
    public onPlayerMessage(Chat2QQ plugin){
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerChat(AsyncPlayerChatEvent e){

        // 异步
        CompletableFuture.runAsync(() -> {

            if (e.isCancelled()) {
                return;
            }

            // 检查权限
            if(!e.getPlayer().hasPermission("chat2qq.chat.requite")){
                return;
            }

            if (!(plugin.getConfig().getBoolean("bot.require-command-to-chat", false))) {
                boolean allowWorld = false;
                boolean allowPrefix = false;
                String message = ChatColor.stripColor(e.getMessage());

                // 判断玩家所处世界
                for (String world : plugin.getConfig().getStringList("bot.available-worlds")) {
                    if (e.getPlayer().getWorld().getName().equalsIgnoreCase(world)) {
                        allowWorld = true;
                        break;
                    }
                }
                if (plugin.getConfig().getBoolean("bot.available-worlds-use-as-blacklist", true)){
                    allowWorld = !allowWorld;
                }

                // 判断消息是否带前缀
                if (plugin.getConfig().getBoolean("bot.requite-special-word-prefix.enabled", true)) {
                    for (String prefix : plugin.getConfig().getStringList("bot.requite-special-word-prefix.prefix")) {
                        if (message.startsWith(prefix)) {
                            allowPrefix = true;
                            message = message.substring(prefix.length());
                            break;
                        }
                    }
                } else allowPrefix = true;

                // 服务器消息发送到QQ群的格式
                String formatText = plugin.getConfig().getString("bot.group-chat-format", "message")
                        .replace("%player%", e.getPlayer().getName())
                        .replace("%message%", message);

                if (allowWorld && allowPrefix) {
                    plugin.getConfig().getLongList("bot.group-ids").forEach(group -> sendToGroup(plugin, group, formatText));
                }

            }
        });
    }
}
