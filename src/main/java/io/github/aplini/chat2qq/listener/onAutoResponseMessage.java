package io.github.aplini.chat2qq.listener;

import io.github.aplini.chat2qq.Chat2QQ;
import me.dreamvoid.miraimc.bukkit.event.message.passive.MiraiGroupMessageEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.regex.Pattern;

import static io.github.aplini.chat2qq.utils.Util.*;

public class onAutoResponseMessage implements Listener {
    private final Chat2QQ plugin;
    public onAutoResponseMessage(Chat2QQ plugin){
        this.plugin = plugin;
    }

    @EventHandler
    public void onGroupMessageReceive(MiraiGroupMessageEvent e) {

        // 异步
        CompletableFuture.runAsync(() -> {

            // 是否启用
            if (!plugin.getConfig().getBoolean("aplini.auto-response.enabled", true)) {
                return;
            }

            // 是否为设置的QQ群
            if (!isGroupInConfig(plugin, "aplini.auto-response", e.getGroupID())) {
                return;
            }

            // 获取消息内容
            String message = e.getMessage();
            // 要发送的消息
            String sendMessage = null;

            // 遍历所有配置
            for (Map<?, ?> config : plugin.getConfig().getMapList("aplini.auto-response.list")) {
                // 前缀匹配
                if (config.get("prefix") != null && message.startsWith((String) config.get("prefix"))) {
                    sendMessage = (String) config.get("send");
                }

                // 包含
                else if (config.get("contain") != null && message.contains((String) config.get("contain"))) {
                    sendMessage = (String) config.get("send");
                }

                // 相等
                else if (config.get("equal") != null && Objects.equals(message, config.get("equal"))) {
                    sendMessage = (String) config.get("send");
                }

                // 正则匹配
                else if (config.get("regular") != null && Pattern.compile((String) config.get("regular")).matcher(message).find()) {
                    sendMessage = message.replaceAll((String) config.get("regular"), (String) config.get("send"));
                }
            }

            // 是否存在消息
            if (sendMessage == null) {
                return;
            }

            // PAPI
            if (plugin.getConfig().getBoolean("aplini.auto-response.enable-papi", false)) {
                sendMessage = PAPIString(sendMessage);
            }

            // 处理格式化字符
            sendMessage = sendMessage.replaceAll("§[a-z0-9]", "");

            // 发送消息
            sendToGroup(plugin, e.getGroupID(), sendMessage);

        });
    }
}
