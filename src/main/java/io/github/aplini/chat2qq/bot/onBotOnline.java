package io.github.aplini.chat2qq.bot;

import io.github.aplini.chat2qq.Chat2QQ;
import me.dreamvoid.miraimc.bukkit.event.bot.MiraiBotOnlineEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerLoadEvent;

import java.util.concurrent.TimeUnit;

import static io.github.aplini.chat2qq.Chat2QQ.group_cache_all;
import static io.github.aplini.chat2qq.utils.Util._setGroupCacheAll;
import static org.bukkit.Bukkit.getLogger;

public class onBotOnline implements Listener {
    private final Chat2QQ plugin;
    public onBotOnline(Chat2QQ plugin){
        this.plugin = plugin;
    }

    @EventHandler // 机器人登录
    public void onMiraiBotOnlineEvent(MiraiBotOnlineEvent e) {
        // 启用群缓存功能
        if(plugin.getConfig().getBoolean("aplini.player-cache.enabled", true)){
            // 如果这是已配置的机器人
            if(plugin.getConfig().getLongList("bot.bot-accounts").contains(e.getBotID())){
                getLogger().info("§f[§7Chat2QQ§f] §f群成员缓存程序已启动...");
                _setGroupCacheAll(plugin);
            }
        }
    }


    @EventHandler // 服务器启动完成
    public void onServerLoad(ServerLoadEvent event) {
        // 无法启动的解决方案
        if(plugin.getConfig().getBoolean("aplini.player-cache.fix-start.enabled", true)){
            try {
                TimeUnit.MILLISECONDS.sleep(plugin.getConfig().getInt("aplini.player-cache.fix-start.await-time", 6400));

                // 防止重复运行
                if(!plugin.getConfig().getBoolean("aplini.player-cache.fix-start.prevent-duplication", true) || group_cache_all == null){
                    getLogger().info("§f[§7Chat2QQ§f] §f群成员缓存程序已通过备用启动方案启动...");
                    _setGroupCacheAll(plugin);
                }

            } catch (InterruptedException ex) {
                throw new RuntimeException(ex);
            }
        }
    }
}
