package io.github.aplini.chat2qq.bot;

import io.github.aplini.chat2qq.Chat2QQ;
import me.dreamvoid.miraimc.bukkit.event.group.member.MiraiMemberCardChangeEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

import static io.github.aplini.chat2qq.Chat2QQ.plugin;
import static io.github.aplini.chat2qq.utils.Util.isGroupInConfig;
import static org.bukkit.Bukkit.getLogger;

public class onCardChange implements Listener {

    @EventHandler // 成员群名片修改
    public void onMiraiMemberCardChangeEvent(MiraiMemberCardChangeEvent e) {
        if(plugin.getConfig().getBoolean("aplini.player-cache.auto-update", true)){
            updateMemberCardChange(e.getBotID(), e.getGroupID(), e.getMemberID(), e.getNewNick(), e.getMemberNick());
        }
    }

    // 更新群成员缓存
    public static void updateMemberCardChange(long botID, long groupID, long qq, String name1, String name2){
        CompletableFuture.runAsync(() -> {
            // 启用群缓存功能
            if (!plugin.getConfig().getBoolean("aplini.player-cache.enabled", true)) {
                return;
            }
            // 如果这是已配置的机器人
            if (!plugin.getConfig().getLongList("bot.bot-accounts").contains(botID)) {
                return;
            }
            // 如果这个群在配置中
            if (!isGroupInConfig(plugin, "aplini.player-cache", groupID)) {
                return;
            }

            // 获取散列表
            Map<Long, String> group_cache = Chat2QQ.group_cache_all.get(groupID);
            if(group_cache == null){
                Chat2QQ.group_cache_all.put(groupID, new HashMap<>());
                group_cache = Chat2QQ.group_cache_all.get(groupID);
            }

            String name;
            if (Objects.equals(name1, "")) {
                if (plugin.getConfig().getBoolean("general.use-nick-if-namecard-null", true)) {
                    name = name2;
                } else {
                    name = String.valueOf(qq);
                }
            }else{
                name = name1;
            }

            String oldName = group_cache.get(qq);
            if(Objects.equals(oldName, name)){
                return;
            }

            if(plugin.getConfig().getBoolean("aplini.player-cache.auto-update-log", true)){
                getLogger().info("[Chat2QQ] 群名片修改 "+ groupID +"."+ qq +": "+ oldName +" -> "+ name);
            }

            group_cache.put(qq, name);

            // 保存缓存文件
            Chat2QQ.temp.set("group_cache_all", Chat2QQ.group_cache_all);
            try {
                Chat2QQ.temp.save(Chat2QQ.tempFile);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
