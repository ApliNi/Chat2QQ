package io.github.aplini.chat2qq.listener;

import io.github.aplini.chat2qq.Chat2QQ;
import me.dreamvoid.miraimc.bukkit.event.group.member.MiraiMemberJoinEvent;
import me.dreamvoid.miraimc.bukkit.event.group.member.MiraiMemberLeaveEvent;
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

    // 复用
    public void FuncMember(String eventNameInConfig, Long groupID){
        // 如果不是配置中的群
        if(! isGroupInConfig(plugin, "aplini.event-func", groupID)){
            return;
        }

        for(Map<?, ?> func : plugin.getConfig().getMapList("aplini.event-func."+ eventNameInConfig)){
            // 执行指令
            if(func.get("command") != null){
                sendToGroup(plugin, groupID, (String) func.get("command"));
            }
            // 发送消息
            if(func.get("message") != null){
                sendToGroup(plugin, groupID, (String) func.get("message"));
            }
        }
    }

    @EventHandler // 成员加入
    public void onMiraiMemberJoinEvent(MiraiMemberJoinEvent e) {
        FuncMember("group-join", e.getGroupID());
    }

    @EventHandler // 成员退出
    public void onMiraiMemberLeaveEvent(MiraiMemberLeaveEvent e) {
        FuncMember("group-quit", e.getGroupID());
    }

}
