package io.github.aplini.chat2qq.utils;

import me.dreamvoid.miraimc.api.MiraiMC;
import me.dreamvoid.miraimc.bukkit.event.message.passive.MiraiGroupMessageEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.bukkit.plugin.Plugin;

import static io.github.aplini.chat2qq.utils.Util.*;

public class renderGroupMessage {

    // 渲染 message
    public static String _renderMessage(Plugin plugin, String message) {
        // 预处理模块
        message = pretreatment(plugin, "aplini.pretreatment", message);

        // 预设的格式调整功能. 是否删除 %message% 消息 中的格式化字符
        if(plugin.getConfig().getBoolean("aplini.other-format-presets.render-message_format-code",false)){
            message = message.replaceAll("§[a-z0-9]", "");
        }

        // 预设的格式调整功能. 删除 %message% 前后的空格和换行
        if(plugin.getConfig().getBoolean("aplini.other-format-presets.message-trim",true)){
            message = message.trim();
        }

        return message;
    }



    // 回复消息
    public static String getReplyVar(Plugin plugin, MiraiGroupMessageEvent e) {
        if(e.getQuoteReplyMessage() != null){
            return plugin.getConfig().getString("aplini.reply-message.var", "[reply] ")
                    .replace("%c_name%", cleanupName(
                            plugin,
                            getNameFromCache(plugin, e.getGroupID(), e.getQuoteReplySenderID(), String.valueOf(e.getQuoteReplySenderID())),
                            e.getQuoteReplySenderID()))
                    .replace("%qq%", String.valueOf(e.getQuoteReplySenderID()))
                    .replace("%_/n_%", "\n");
        }
        return "";
    }


    // 可读的消息
    public static String [] renderMessage1(Plugin plugin, MiraiGroupMessageEvent e) {

        String [] message = new String [4];
        message[0] = ""; // 消息类型
        message[1] = e.getMessage(); // 源消息
        message[2] = ""; // 经过格式化后, 内容可见的消息
        message[3] = ""; // 可能不包含可见消息的显示消息, 用于控制台以及配合JSON消息

        // no = 不发送

        // 群聊天前缀 (聊天需要带有指定前缀才能发送到服务器)
        if(plugin.getConfig().getBoolean("general.requite-special-word-prefix.enabled",false)){
            boolean allowPrefix = false;
            // - "#"
            for(String prefix : plugin.getConfig().getStringList("general.requite-special-word-prefix.prefix")){
                if(message[1].startsWith(prefix)){
                    allowPrefix = true;
                    // 移除消息中的前缀
                    message[1] = message[1].substring(prefix.length());
                    break;
                }
            }
            if(! allowPrefix){
                message[0] = "no";
                return message;
            }
        }

        // 当群名片不存在时是否尝试获取昵称
        String name = e.getSenderNameCard();
        if(name.equalsIgnoreCase("") && plugin.getConfig().getBoolean("general.use-nick-if-namecard-null",true)){
            name = e.getSenderName();
        }
        // 预设的格式调整功能. 是否删除 %nick% 群名片 中的格式化字符
        if(plugin.getConfig().getBoolean("aplini.other-format-presets.render-nick_format-code",true)){
            name = name.replaceAll("§[a-z0-9]", "");
        }


        // 经过格式化后, 内容可见的消息
        message[2] = message[1];


        // 引用回复
        // 如果是回复消息, 则删除消息开头重复的 @qqID
        if(e.getQuoteReplyMessage() != null && plugin.getConfig().getBoolean("aplini.reply-message.del-duplicates-at",true)){
            String atField = "@"+ e.getQuoteReplySenderID();
            if(message[2].startsWith(atField)){
                message[2] = message[2].substring(atField.length());
            }
        }

        // 预处理
        message[2] = _renderMessage(plugin, message[2]);
        if(message[2].equals("")){
            message[0] = "no";
            return message;
        }


        // 可能不包含可见消息的显示消息
        message[3] = message[2];


        // 预设的格式调整功能. 更好的多行消息
        if(plugin.getConfig().getBoolean("aplini.other-format-presets.multiline-message.enabled",true) && message[3].contains("\n")){
            String _l0 = plugin.getConfig().getString("aplini.other-format-presets.multiline-message.line-0", "line-0");
            String _l1 = plugin.getConfig().getString("aplini.other-format-presets.multiline-message.line-prefix", "line-prefix");
            message[3] = _l0 + "\n" + _l1 + message[3].replace("\n", "\n" + _l1);
        }

        // 预设的格式调整功能. 聊天消息过长时转换为悬浮文本
        if(message[2].length() > plugin.getConfig().getInt("aplini.other-format-presets.long-message.condition-length", 210) ||
                message[2].contains("\n") &&
                        message[2].length() - message[2].replace("\n","").length() >
                                plugin.getConfig().getInt("aplini.other-format-presets.long-message.condition-line_num", 6)){
            message[0] = "lm";
            message[3] = plugin.getConfig().getString("aplini.other-format-presets.long-message.message");

            // 删除消息中多余的换行, 用于解决 "预设的格式调整功能. 更好的多行消息" 造成的消息开头多一个空行
            message[2] = message[2].trim();
        }


        // Mirai 内置QQ绑定
        String message2_config_path = "general.in-game-chat-format";
        if(plugin.getConfig().getBoolean("general.use-miraimc-bind",false) && MiraiMC.getBind(e.getSenderID()) != null){
            message2_config_path = "general.bind-chat-format";
        }

        String message3 = message[3];
        message[3] = plugin.getConfig().getString(message2_config_path, "message")
                .replace("%groupname%",e.getGroupName())
                .replace("%groupid%",String.valueOf(e.getGroupID()))
                .replace("%qq%",String.valueOf(e.getSenderID()))
                .replace("%nick%",name)
                .replace("%regex_nick%", cleanupName(plugin, name, e.getSenderID())) // aplini.cleanup-name
                .replace("%_reply_%", getReplyVar(plugin, e))
                .replace("%message%", formatQQID(plugin, message3, e.getGroupID()));


        return message;
    }

    // JSON消息
    public static TextComponent renderMessage2(Plugin plugin, String [] message, MiraiGroupMessageEvent e) {

        // 转换为JSON文本
        TextComponent formatText = new TextComponent(message[3]);

        // 预设的格式调整功能. 聊天消息过长时转换为悬浮文本
        if(message[0].equals("lm")){
            // 设置悬浮文本
            formatText.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(message[2])));
        }

        // 如果是回复消息
        if(e.getQuoteReplyMessage() != null){
            // 创建回复消息的悬浮文本
            String replyMessage = plugin.getConfig().getString("aplini.reply-message.message", "[引用回复]")
                    .replace("%c_name%", cleanupName(plugin,
                            getNameFromCache(plugin, e.getGroupID(), e.getQuoteReplySenderID(), String.valueOf(e.getQuoteReplySenderID())),
                            e.getQuoteReplySenderID()))
                    .replace("%qq%", String.valueOf(e.getQuoteReplySenderID()))
                    .replace("%_/n_%", "\n")
                    .replace("%message%", formatQQID(plugin, _renderMessage(plugin, e.getQuoteReplyMessage()), e.getGroupID()))
                    .replace("%main_message%", message[2]); // 跳过消息过长部分
            // 设置悬浮文本
            formatText.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(replyMessage)));
        }

        return formatText;
    }
}
