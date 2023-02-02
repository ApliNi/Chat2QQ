package io.github.aplini.chat2qq.bot;

import io.github.aplini.chat2qq.Chat2QQ;
import me.dreamvoid.miraimc.bukkit.event.group.member.MiraiMemberCardChangeEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.List;
import java.util.Map;
import java.util.Objects;

public class onCardChange implements Listener {
    private final Chat2QQ plugin;
    public onCardChange(Chat2QQ plugin){
        this.plugin = plugin;
    }

    @EventHandler // 成员群名片修改
    public void onMiraiMemberCardChangeEvent(MiraiMemberCardChangeEvent e) {
        // 启用群缓存功能 && 自动更新缓存
        if(plugin.getConfig().getBoolean("aplini.player-cache.enabled", true) && plugin.getConfig().getBoolean("aplini.player-cache.auto-update", true)){
            // 如果这是已配置的机器人
            if(plugin.getConfig().getLongList("bot.bot-accounts").contains(e.getBotID())){
                long groupID = e.getGroupID();

                // 获取开启此功能的群
                List<Long> configGroupList;
                if(plugin.getConfig().getBoolean("aplini.player-cache.use-general-group-ids", true)){
                    configGroupList = plugin.getConfig().getLongList("general.group-ids");
                }else{
                    configGroupList = plugin.getConfig().getLongList("aplini.player-cache.group-ids");
                }

                // 如果这个群在配置中
                if(configGroupList.contains(groupID)){
                    // 获取散列表
                    Map<Long, String> group_cache = Chat2QQ.group_cache_all.get(groupID);
                    if(group_cache != null){
                        String name = e.getNewNick();
                        if(Objects.equals(name, "")){
                            if(plugin.getConfig().getBoolean("general.use-nick-if-namecard-null", true)){
                                name = e.getMemberNick();
                            }else{
                                name = ""+ e.getMemberID();
                            }
                        }
                        group_cache.put(groupID, name);
                    }
                }
            }
        }
    }
}
