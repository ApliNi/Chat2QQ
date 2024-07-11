package io.github.aplini.chat2qq.bot;

import io.github.aplini.chat2qq.Chat2QQ;
import me.dreamvoid.miraimc.bukkit.event.bot.MiraiBotOnlineEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerLoadEvent;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import static io.github.aplini.chat2qq.utils.Util._setGroupCacheAll;
import static org.bukkit.Bukkit.getLogger;

public class onBotOnline implements Listener {
    private final Chat2QQ plugin;
    public onBotOnline(Chat2QQ plugin){
        this.plugin = plugin;
    }

    static boolean alreadyRunning = false;

    @EventHandler // 机器人登录
    public void onMiraiBotOnlineEvent(MiraiBotOnlineEvent e) {
        // 启用群缓存功能
        if(plugin.getConfig().getBoolean("aplini.player-cache.enabled", true)){
            // 如果这是已配置的机器人
            if(plugin.getConfig().getLongList("bot.bot-accounts").contains(e.getBotID())){
                alreadyRunning = true;
                getLogger().info("[Chat2QQ] 群成员缓存程序已启动...");
                _setGroupCacheAll(plugin);
            }
        }
    }


    @EventHandler // 服务器启动完成
    public void onServerLoad(ServerLoadEvent event) {
        // 异步
        CompletableFuture.runAsync(() -> {
            // 无法启动的解决方案
            try {
                TimeUnit.MILLISECONDS.sleep(1000);

                if(!alreadyRunning){
                    getLogger().info("[Chat2QQ] 群成员缓存程序已启动...");
                    _setGroupCacheAll(plugin);
                }

            } catch (InterruptedException ex) {
                throw new RuntimeException(ex);
            }
        });
    }
}
