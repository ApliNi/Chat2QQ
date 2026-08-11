package io.github.aplini.chat2qq.listener;

import io.github.aplini.chat2qq.Chat2QQ;
import io.github.aplini.chat2qq.onebot.event.OneBotMemberJoinEvent;
import io.github.aplini.chat2qq.onebot.event.OneBotMemberLeaveEvent;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.Map;

import static io.github.aplini.chat2qq.utils.Util.isGroupInConfig;
import static io.github.aplini.chat2qq.utils.Util.sendToGroup;

public class EventFunc implements Listener {
    private final Chat2QQ plugin;
    public EventFunc(Chat2QQ plugin){
        this.plugin = plugin;
    }

    // 群成员 复用
    public void FuncMember(String eventNameInConfig, Long groupID){
        // 如果不是配置中的群
        if(! isGroupInConfig(plugin, "aplini.event-func", groupID)){
            return;
        }

        for(Map<?, ?> funcConfig : plugin.getConfig().getMapList("aplini.event-func."+ eventNameInConfig)){
            // 执行指令
            if(funcConfig.get("command") != null){
                plugin.getLogger().info("[Chat2QQ] [event-func] 运行指令: /"+ funcConfig.get("command"));
                Bukkit.getScheduler().callSyncMethod(plugin, () ->
                        Bukkit.dispatchCommand(Bukkit.getServer().getConsoleSender(), String.valueOf(funcConfig.get("command"))));
            }
            // 发送消息
            else if(funcConfig.get("message-text") != null){
                Long messageGroupID = funcConfig.get("message-group") != null ? Long.valueOf(String.valueOf(funcConfig.get("message-group"))) : groupID;
                sendToGroup(plugin, messageGroupID, String.valueOf(funcConfig.get("message-text")));
            }
        }
    }
    @EventHandler // 成员加入
    public void onOneBotMemberJoinEvent(OneBotMemberJoinEvent e) {
        FuncMember("MiraiMemberJoinEvent", e.getGroupID());
    }
    @EventHandler // 成员退出
    public void onOneBotMemberLeaveEvent(OneBotMemberLeaveEvent e) {
        FuncMember("MiraiMemberLeaveEvent", e.getGroupID());
    }
}
