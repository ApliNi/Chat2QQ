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
    public void reconnect(Long botID, int reconnectNum, MiraiBotOfflineEvent e){
        while (true){
            // 在次数限制外退出循环
            reconnectNum ++;
            if(reconnectNum > plugin.getConfig().getInt("aplini.bot-offline.max-reconnect-num", 7)){
                getLogger().warning("[Chat2QQ] [bot-offline] 达到次数限制, 已取消重新连接: "+ botID +". ");
                return;
            }

            // 重新连接
            boolean state = false;
            try {
                TimeUnit.MILLISECONDS.sleep(plugin.getConfig().getLong("aplini.bot-offline.delay", 14) * 1000);
                state = e.reconnect();
            } catch (InterruptedException ex) {
                getLogger().warning("[Chat2QQ] [bot-offline] 尝试重新登录账号 "+ botID +" 时出现异常!");
                throw new RuntimeException(ex);
            }

            // 重连成功时退出循环
            if(state){
                getLogger().info("[Chat2QQ] [bot-offline] 重新连接成功, 或已完成请求: "+ botID);
                return;
            }else{
                getLogger().info("[Chat2QQ] [bot-offline] 重新连接失败: "+ botID +", 次数: "+ reconnectNum);
            }
        }
    }

    @EventHandler // 机器人掉线
    public void onMiraiBotOfflineEvent(MiraiBotOfflineEvent e) {
        // 如果是配置中的账号
        if(plugin.getConfig().getLongList("aplini.bot-offline.bot-ids").contains(e.getBotID())){
            getLogger().info("[Chat2QQ] [bot-offline] 账号离线: "+ e.getBotID() +". 等待重新连接延迟...");
            reconnect(e.getBotID(), 0, e);
        }
    }
}
