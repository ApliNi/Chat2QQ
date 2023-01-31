package me.dreamvoid.chat2qq.bukkit.utils;

import me.dreamvoid.miraimc.api.MiraiBot;
import me.dreamvoid.miraimc.httpapi.MiraiHttpAPI;
import me.dreamvoid.miraimc.httpapi.exception.AbnormalStatusException;
import org.bukkit.plugin.Plugin;

import java.io.IOException;
import java.util.NoSuchElementException;

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

    // 省略指定机器人账号
    public static void sendToGroup(Plugin plugin, long groupID, String message) {
        sendToGroup(plugin.getConfig().getLongList("bot.bot-accounts").get(0), groupID, message);
    }
}
