package io.github.aplini.chat2qq.listener;

import io.github.aplini.chat2qq.Chat2QQ;
import me.dreamvoid.miraimc.bukkit.event.message.passive.MiraiGroupMessageEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import static io.github.aplini.chat2qq.utils.renderGroupMessage._renderMessage;
import static io.github.aplini.chat2qq.utils.renderGroupMessage.renderMessage;
import static org.bukkit.Bukkit.getLogger;
import static org.bukkit.Bukkit.getServer;

public class onGroupMessage implements Listener {
    private final Chat2QQ plugin;
    public onGroupMessage(Chat2QQ plugin){
        this.plugin = plugin;
    }

    @EventHandler
    public void onGroupMessageReceive(MiraiGroupMessageEvent e) {
        // QQID黑名单
        if(plugin.getConfig().getLongList("blacklist.qq").contains(e.getSenderID())) return;

        // 渲染消息
        String [] message = renderMessage(plugin, e);

        if(! message[0].equals("") &&
                plugin.getConfig().getLongList("bot.bot-accounts").contains(e.getBotID()) &&
                plugin.getConfig().getLongList("general.group-ids").contains(e.getGroupID())){

            // 输出到控制台
            if(plugin.getConfig().getBoolean("aplini.other-format-presets.message-to-log", true)){
                getLogger().info(message[1]);
            }

            // 转换格式
            TextComponent formatText = new TextComponent(message[1]);

            // 如果是长消息
            if(message[0].equals("lm")){
                message[1] = _renderMessage(plugin, e.getMessage());
                // 设置悬浮文本
                formatText.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(message[1])));
            }

            // 如果是回复消息
            if(e.getQuoteReplyMessage() != null){
                // 创建回复消息的悬浮文本
                String replyMessage = plugin.getConfig().getString("aplini.reply-message.message", "[引用回复]")
                        .replace("%qq%", ""+ e.getQuoteReplySenderID())
                        .replace("%_/n_%", "\n")
                        .replace("%message%", ""+ _renderMessage(plugin, e.getQuoteReplyMessage()))
                        .replace("%main_message%", message[1]);
                // 设置悬浮文本
                formatText.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(replyMessage)));
            }

            // 广播
            getServer().spigot().broadcast(formatText);
        }

    }

}
