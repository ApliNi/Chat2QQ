package io.github.aplini.chat2qq.bot;

import io.github.aplini.chat2qq.Chat2QQ;
import me.dreamvoid.miraimc.bukkit.event.group.member.MiraiMemberCardChangeEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static io.github.aplini.chat2qq.utils.Util.isGroupInConfig;
import static org.bukkit.Bukkit.getLogger;

public class onCardChange implements Listener {
    private final Chat2QQ plugin;
    public onCardChange(Chat2QQ plugin){
        this.plugin = plugin;
    }

    @EventHandler // 成员群名片修改
    public void onMiraiMemberCardChangeEvent(MiraiMemberCardChangeEvent e) {
        // 异步
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.submit(() -> {
            // 启用群缓存功能 && 自动更新缓存
            if (plugin.getConfig().getBoolean("aplini.player-cache.enabled", true) && plugin.getConfig().getBoolean("aplini.player-cache.auto-update", true)) {
                // 如果这是已配置的机器人
                if (plugin.getConfig().getLongList("bot.bot-accounts").contains(e.getBotID())) {
                    long groupID = e.getGroupID();
                    // 如果这个群在配置中
                    if (isGroupInConfig(plugin, "aplini.player-cache", groupID)) {
                        // 获取散列表
                        Map<Long, String> group_cache = Chat2QQ.group_cache_all.get(groupID);
                        if (group_cache != null) {
                            String name = e.getNewNick();
                            if (Objects.equals(name, "")) {
                                if (plugin.getConfig().getBoolean("general.use-nick-if-namecard-null", true)) {
                                    name = e.getMemberNick();
                                } else {
                                    name = String.valueOf(e.getMemberID());
                                }
                            }

                            if(plugin.getConfig().getBoolean("aplini.player-cache.auto-update-log", true)){
                                getLogger().info("[Chat2QQ] 群名片修改 "+ groupID +"."+ e.getMemberID() +": "+ e.getOldNick() +" -> "+ e.getNewNick());
                            }

                            group_cache.put(e.getMemberID(), name);
                        }
                    }
                }
            }
        });
        executor.shutdown();
    }
}
