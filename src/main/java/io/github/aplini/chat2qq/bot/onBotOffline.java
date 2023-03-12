package io.github.aplini.chat2qq.bot;

import io.github.aplini.chat2qq.Chat2QQ;
import me.dreamvoid.miraimc.bukkit.event.bot.MiraiBotOfflineEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.concurrent.TimeUnit;

import static org.bukkit.Bukkit.getLogger;

public class onBotOffline implements Listener {
    private final Chat2QQ plugin;
    public onBotOffline(Chat2QQ plugin){
        this.plugin = plugin;
    }

    // 掉线重连程序
    public void reconnect(MiraiBotOfflineEvent e, int reconnectNum){
        reconnectNum ++;
        if(reconnectNum > plugin.getConfig().getInt("aplini.bot-offline.max-reconnect-num", 4)){
            return;
        }

        boolean state;
        try {
            TimeUnit.MILLISECONDS.sleep(plugin.getConfig().getLong("aplini.bot-offline.delay", 10) * 1000);
            getLogger().info("[Chat2QQ] 尝试重新登录机器人 "+ e.getBotID() +"...");
            state = e.reconnect();
        } catch (InterruptedException ex) {
            getLogger().info("[Chat2QQ] 尝试重新登录机器人 "+ e.getBotID() +" 时出现异常!");
            throw new RuntimeException(ex);
        }

        if(!state){
            reconnect(e, reconnectNum);
        }
    }

    @EventHandler // 机器人掉线
    public void onMiraiBotOfflineEvent(MiraiBotOfflineEvent e) {
        // 如果是配置中的机器人
        if(plugin.getConfig().getLongList("aplini.bot-offline.bot-ids").contains(e.getBotID())){
            reconnect(e, 0);
        }
    }
}
