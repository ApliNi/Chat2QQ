package me.dreamvoid.chat2qq.bukkit.utils;

import me.dreamvoid.miraimc.api.MiraiMC;
import me.dreamvoid.miraimc.bukkit.event.message.passive.MiraiGroupMessageEvent;
import org.bukkit.plugin.Plugin;

import java.util.Map;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class renderGroupMessage {

    // 渲染 message
    public static String _renderMessage(Plugin plugin, String message) {
        if(plugin.getConfig().getBoolean("aplini.pretreatment.enabled",false)){
            for(Map<?, ?> config : plugin.getConfig().getMapList("aplini.pretreatment.list")){
                // 前缀匹配
                if(config.get("prefix") != null && message.startsWith((String) config.get("prefix"))){
                    if(config.get("send") != null){
                        return "";
                    }
                    else if(config.get("to_all") != null){
                        message = (String) config.get("to_all");
                    }
                    else if(config.get("to_replace") != null){
                        message = message.replace((String) config.get("prefix"), (String) config.get("to_replace"));
                    }
                }

                // 包含
                else if(config.get("contain") != null && message.contains((String) config.get("contain"))){
                    if(config.get("send") != null){
                        return "";
                    }
                    else if(config.get("to_replace") != null){
                        message = message.replace((String) config.get("contain"), (String) config.get("to_replace"));
                    }
                    else if(config.get("to_all") != null){
                        message = (String) config.get("to_all");
                    }
                }

                // 相等
                else if(config.get("equal") != null && Objects.equals(message, config.get("equal"))){
                    if(config.get("send") != null){
                        return "";
                    }
                    else if(config.get("to_all") != null){
                        message = (String) config.get("to_all");
                    }
                }

                // 正则匹配
                else if(config.get("regular") != null && Pattern.compile((String) config.get("regular")).matcher(message).find()){
                    if(config.get("send") != null){
                        return "";
                    }
                    else if(config.get("to_regular") != null){
                        message = message.replaceAll((String) config.get("regular"), (String) config.get("to_regular"));
                    }
                    else if(config.get("to_all") != null){
                        message = (String) config.get("to_all");
                    }
                }
            }
        }

        // 预设的格式调整功能. 是否删除 %message% 消息 中的格式化字符
        if(plugin.getConfig().getBoolean("aplini.other-format-presets.render-message_format-code",false)){
            message = message.replace("§", "");
        }

        // 预设的格式调整功能. 更好的多行消息
        if(plugin.getConfig().getBoolean("aplini.other-format-presets.multiline-message.enabled",true) && message.contains("\n")) {
            String _l0 = plugin.getConfig().getString("aplini.other-format-presets.multiline-message.line-0", "line-0");
            String _l1 = plugin.getConfig().getString("aplini.other-format-presets.multiline-message.line-prefix", "line-prefix");
            message = _l0 + "\n" + _l1 + message.replace("\n", "\n" + _l1);
        }

        return message;
    }



    // 回复消息
    public static String getReplyVar(Plugin plugin, MiraiGroupMessageEvent e) {
        if(e.getQuoteReplyMessage() != null){
            return plugin.getConfig().getString("aplini.reply-message.var")
                    .replace("%qq%", ""+ e.getQuoteReplySenderID());
        }
        return "";
    }



    // 渲染主消息
    public static String renderMessage(Plugin plugin, MiraiGroupMessageEvent e) {

        // 判断消息是否带前缀
        String message = e.getMessage();
        if(plugin.getConfig().getBoolean("general.requite-special-word-prefix.enabled",false)){
            boolean allowPrefix = false;
            for(String prefix : plugin.getConfig().getStringList("general.requite-special-word-prefix.prefix")){
                if(e.getMessage().startsWith(prefix)){
                    allowPrefix = true;
                    message = message.substring(prefix.length());
                    break;
                }
            }
            if(! allowPrefix) return "";
        }

        String name = e.getSenderNameCard();
        if(name.equalsIgnoreCase("") && plugin.getConfig().getBoolean("general.use-nick-if-namecard-null",true)){
            name = e.getSenderName();
        }

        // 预设的格式调整功能. 是否删除 %nick% 群名片 中的格式化字符
        if(plugin.getConfig().getBoolean("aplini.other-format-presets.render-nick_format-code",true)){
            name = name.replace("§", "");
        }

        // cleanup-name
        String $regex_nick = "%regex_nick%";
        if(plugin.getConfig().getBoolean("aplini.cleanup-name.enabled",false)){
            Matcher matcher = Pattern.compile(plugin.getConfig().getString("aplini.cleanup-name.regex")).matcher(name);
            if(matcher.find()){
                $regex_nick = matcher.group(1);
            } else {
                $regex_nick = plugin.getConfig().getString("aplini.cleanup-name.not-captured")
                        .replace("%groupname%", e.getGroupName())
                        .replace("%groupid%", String.valueOf(e.getGroupID()))
                        .replace("%nick%", name)
                        .replace("%qq%", String.valueOf(e.getSenderID()))
                        .replace("%message%", message);
            }
        }

        // 预处理
        message = _renderMessage(plugin, message);
        if(message.equals("")) return "";

        String message2_config_path = "general.in-game-chat-format";
        if(plugin.getConfig().getBoolean("general.use-miraimc-bind",false) && MiraiMC.getBind(e.getSenderID()) != null){
            message2_config_path = "general.bind-chat-format";
        }

        return plugin.getConfig().getString(message2_config_path)
                .replace("%groupname%",e.getGroupName())
                .replace("%groupid%",String.valueOf(e.getGroupID()))
                .replace("%qq%",String.valueOf(e.getSenderID()))
                .replace("%nick%",name)
                .replace("%regex_nick%", $regex_nick)
                .replace("%_reply_%", getReplyVar(plugin, e))
                .replace("%message%", message);

    }

}
