package io.github.aplini.chat2qq.utils;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import io.github.aplini.chat2qq.Chat2QQ;
import me.dreamvoid.miraimc.api.MiraiBot;
import me.dreamvoid.miraimc.httpapi.MiraiHttpAPI;
import me.dreamvoid.miraimc.httpapi.exception.AbnormalStatusException;
import org.bukkit.plugin.Plugin;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.bukkit.Bukkit.getLogger;

public class Util {
    // 发送消息到群
    public static void sendToGroup(long botID, long groupID, String message) {
        try {
            MiraiBot.getBot(botID).getGroup(groupID).sendMessageMirai(message);
        } catch (NoSuchElementException e) {
            if (MiraiHttpAPI.Bots.containsKey(botID)) {
                try {
                    MiraiHttpAPI.INSTANCE.sendGroupMessage(MiraiHttpAPI.Bots.get(botID), groupID, message);
                } catch (IOException | AbnormalStatusException ex) {
                    getLogger().warning("[Chat2QQ] 发送消息出现异常: "+ botID +": "+ ex);
                }
            } else getLogger().warning("[Chat2QQ] 指定QQ账号不存在: "+ botID);
        }
    }
    // 简化调用
    public static void sendToGroup(Plugin plugin, long groupID, String message) {
        sendToGroup(plugin.getConfig().getLongList("bot.bot-accounts").get(0), groupID, message);
    }


    // 从群缓存中获取一个群中的玩家的名称
    public static String getNameFromCache(Plugin plugin, long groupID, long playerID, String defaultName) {
        if(plugin.getConfig().getBoolean("aplini.player-cache.enabled", false)){
            Map<Long, String> group_cache = Chat2QQ.group_cache_all.get(groupID);
            if(group_cache != null){
                String name = group_cache.get(playerID);
                if(name != null){
                    return name;
                }
            }
            return ""+ playerID;
        }
        return defaultName;
    }


    // 从群名片匹配出MC标准名称
    public static String cleanupName(Plugin plugin, String name, Long qqID) {
        if(plugin.getConfig().getBoolean("aplini.cleanup-name.enabled",false)){
            Matcher matcher = Pattern.compile(plugin.getConfig().getString("aplini.cleanup-name.regex", "([a-zA-Z0-9_]{3,16})")).matcher(name);
            if(matcher.find()){
                return matcher.group(1);
            } else {
                return plugin.getConfig().getString("aplini.cleanup-name.not-captured", "%nick%")
                        .replace("%nick%", name)
                        .replace("%qq%", String.valueOf(qqID));
            }
        }
        return "[Chat2QQ] [未启用此功能: aplini.cleanup-name.enabled]";
    }


    // 从文本中匹配 @qqID, 并替换为 @名称
    public static String formatQQID(Plugin plugin, String message, long groupID) {
        if(plugin.getConfig().getBoolean("aplini.format-qq-id.enabled", true)){

            Matcher matcher = Pattern.compile(plugin.getConfig().getString("aplini.format-qq-id.regular", "(@[0-9]{5,11})")).matcher(message);

            int count = 0;
            while (matcher.find()) {
                count ++;

                // 获取qqid
                long qqID = Long.parseLong(matcher.group().substring(1));
                // 获取群名片
                String name = getNameFromCache(plugin, groupID, qqID, matcher.group());
                // 从群名片匹配出MC标准名称
                if(plugin.getConfig().getBoolean("aplini.cleanup-name.enabled",false)){
                    name = cleanupName(plugin, name, qqID);
                }
                // 替换
                message = message.replace(matcher.group(),
                        plugin.getConfig().getString("aplini.format-qq-id.format", "format")
                                .replace("%qq%", ""+ qqID)
                                .replace("%name%", name)
                );

                if(count == plugin.getConfig().getInt("aplini.format-qq-id.max-cycles-num", 7)){
                    break;
                }
            }

        }
        return message;
    }


    // 初始化群成员缓存
    public static void _setGroupCacheAll(Plugin plugin) {
        Map<Long, Map<Long, String>> _group_cache_all = new HashMap<>();

        // 获取开启此功能的群
        List<Long> configGroupList;
        if(plugin.getConfig().getBoolean("aplini.player-cache.use-general-group-ids", true)){
            configGroupList = plugin.getConfig().getLongList("general.group-ids");
        }else{
            configGroupList = plugin.getConfig().getLongList("aplini.player-cache.group-ids");
        }

        // 获取机器人账号
        Long botID = plugin.getConfig().getLongList("bot.bot-accounts").get(0);

        // 遍历需要缓存的群
        configGroupList.forEach(gid -> {
            getLogger().info("§f[§7Chat2QQ§f] §f正在缓存群: "+ gid);
            // 散列表
            Map<Long, String> group_cache = new HashMap<>();

            // 获取群数据
            JsonArray groupArray;
            try {
                String jsonString = Files.readString(Paths.get(
                        plugin.getConfig().getString("aplini.player-cache.mirai-cache-path", "plugins/MiraiMC/MiraiBot/bots/%qq%/cache/contacts/groups/%group%.json")
                                .replace("%qq%", ""+ botID)
                                .replace("%group%", ""+ gid)
//                                .replace("/", File.separator) // IDEA说它没用
                ));
                JsonObject groupJson = new Gson().fromJson(jsonString, JsonObject.class);
                groupArray = groupJson.getAsJsonArray("list");
            } catch (IOException e) {
                getLogger().warning("§f[§7Chat2QQ§f] §f读取MiraiMC群数据缓存时出错. botID: "+ botID +", groupID: "+ gid +". \n§r"+ e);
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
            _group_cache_all.put(gid, group_cache);
        });

        Chat2QQ.group_cache_all = _group_cache_all;

        getLogger().info("§f[§7Chat2QQ§f] §f群成员缓存完成!");
    }
}
