package io.github.aplini.chat2qq.listener;

import io.github.aplini.chat2qq.Chat2QQ;
import me.dreamvoid.miraimc.bukkit.event.message.passive.MiraiGroupMessageEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.concurrent.CompletableFuture;

import static io.github.aplini.chat2qq.bot.onCardChange.updateMemberCardChange;
import static io.github.aplini.chat2qq.utils.renderGroupMessage.renderMessage1;
import static io.github.aplini.chat2qq.utils.renderGroupMessage.renderMessage2;

public class onGroupMessage implements Listener {
    private final Chat2QQ plugin;
    public onGroupMessage(Chat2QQ plugin){
        this.plugin = plugin;
    }

    @EventHandler
    public void onGroupMessageReceive(MiraiGroupMessageEvent e) {

        // 异步
        CompletableFuture.runAsync(() -> {
            // 自动更新玩家信息缓存
            if(plugin.getConfig().getBoolean("aplini.player-cache.auto-update", true)){
                if(plugin.getConfig().getBoolean("aplini.player-cache.auto-update-form-msg", true)){
                    updateMemberCardChange(e.getBotID(), e.getGroupID(), e.getSenderID(), e.getSenderName(), null);
                }
            }

            // QQID黑名单
            if (plugin.getConfig().getLongList("blacklist.qq").contains(e.getSenderID())) return;

            // 渲染为可见消息
            String[] message = renderMessage1(plugin, e);


            if (!message[2].isEmpty() &&
                    plugin.getConfig().getLongList("bot.bot-accounts").contains(e.getBotID()) &&
                    plugin.getConfig().getLongList("general.group-ids").contains(e.getGroupID())) {

                // 输出到控制台
                if (plugin.getConfig().getBoolean("aplini.other-format-presets.message-to-log", true)) {
                    Bukkit.getConsoleSender().sendMessage("[QQ] "+ message[3]);
                }

                // 渲染为JSON消息
                TextComponent formatText = renderMessage2(plugin, message, e);

                // 广播给具有 chat2qq.qq.receive 权限的玩家
                for(Player player : Bukkit.getOnlinePlayers()){
                    if(player.hasPermission("chat2qq.qq.receive")){
                        player.spigot().sendMessage(formatText);
                    }
                }
//                getServer().spigot().broadcast(formatText);
            }

        });

    }

}
