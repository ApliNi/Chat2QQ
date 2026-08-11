package io.github.aplini.chat2qq.onebot;

import org.bukkit.Bukkit;

import java.util.concurrent.CompletableFuture;

/**
 * OneBot API 静态封装，替代 miramimc.api.MiraiBot / MiraiMC
 */
public class OneBotAPI {

    /**
     * 发送消息到指定群
     * @param botID   机器人 QQ (保留参数, 当前未使用)
     * @param groupID 群号
     * @param message 消息文本
     */
    public static void sendToGroup(long botID, long groupID, String message) {
        OneBotClient client = OneBotClient.INSTANCE;
        if (client == null || !client.isConnected()) {
            Bukkit.getLogger().warning("[Chat2QQ] OneBot 客户端未连接, 无法发送消息到群 " + groupID);
            return;
        }
        CompletableFuture<String> future = client.sendGroupMessage(groupID, message);
        future.exceptionally(ex -> {
            Bukkit.getLogger().warning("[Chat2QQ] 发送消息到群 " + groupID + " 失败: " + ex.getMessage());
            return null;
        });
    }
}
