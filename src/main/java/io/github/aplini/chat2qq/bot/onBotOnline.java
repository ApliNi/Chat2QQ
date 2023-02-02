package io.github.aplini.chat2qq.bot;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import io.github.aplini.chat2qq.Chat2QQ;
import me.dreamvoid.miraimc.api.MiraiBot;
import me.dreamvoid.miraimc.bukkit.event.bot.MiraiBotOnlineEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static org.bukkit.Bukkit.getLogger;

public class onBotOnline implements Listener {
    private final Chat2QQ plugin;
    public onBotOnline(Chat2QQ plugin){
        this.plugin = plugin;
    }

    @EventHandler // 机器人登录
    public void onMiraiBotOnlineEvent(MiraiBotOnlineEvent e_MiraiBotOnlineEvent) {
        // 启用群缓存功能
        if(plugin.getConfig().getBoolean("aplini.player-cache.enabled", false)){
            MiraiBot bot = e_MiraiBotOnlineEvent.getBot();
            // 散列表
            Map<Long, Map<Long, String>> group_cache_all = new HashMap<>();

            // 如果这是已配置的机器人
            if(plugin.getConfig().getLongList("bot.bot-accounts").contains(bot.getID())){
                List<Long> groupList = bot.getGroupList();

                // 遍历需要缓存的群
                plugin.getConfig().getLongList("aplini.player-cache.group-ids").forEach(gid -> {
                    // 如果机器人加入了这个群
                    if(groupList.contains(gid)){
                        getLogger().info("§f[§7Chat2QQ§f] §f正在缓存群: "+ gid);
                        // 散列表
                        Map<Long, String> group_cache = new HashMap<>();

                        // 获取群数据
                        JsonArray groupArray;
                        try {
                            String jsonString = Files.readString(Paths.get(
                                    "plugins/MiraiMC/MiraiBot/bots/2469678133/cache/contacts/groups/435876332.json"
                                            .replace("/", File.separator)
                            ));
                            JsonObject groupJson = new Gson().fromJson(jsonString, JsonObject.class);
                            groupArray = groupJson.getAsJsonArray("list");
                        } catch (IOException e) {
                            getLogger().info("§f[§7Chat2QQ§f] §f读取MiraiMC群数据缓存时出错: "+ gid);
                            throw new RuntimeException(e);
                        }

                        groupArray.forEach(JsonElement -> {
                            JsonObject aGroup = (JsonObject) JsonElement;
                            Long id = aGroup.get("uin").getAsLong();
                            String name = aGroup.get("nameCard").getAsString();
                            if(Objects.equals(name, "")){
                                if(plugin.getConfig().getBoolean("general.use-nick-if-namecard-null", true)){
                                    name = aGroup.get("nick").getAsString();
                                }else{
                                    name = ""+ id;
                                }
                            }
                            group_cache.put(id, name);
                        });

                        // 添加到all
                        group_cache_all.put(gid, group_cache);

                    }else{
                        getLogger().info("§f[§7Chat2QQ§f] §f机器人未加入此群, 将跳过缓存: "+ gid);
                    }
                });
            }

            // 存放在 plugin
            Chat2QQ.group_cache_all = group_cache_all;

            getLogger().info("§f[§7Chat2QQ§f] §f缓存完成!");

//            System.out.println(plugin.group_cache_all);
        }

    }
}
