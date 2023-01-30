package me.dreamvoid.chat2qq.bukkit.listener;

import me.dreamvoid.chat2qq.bukkit.BukkitPlugin;
import me.dreamvoid.miraimc.bukkit.event.message.passive.MiraiGroupMessageEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import static me.dreamvoid.chat2qq.bukkit.utils.renderGroupMessage.*;
import static org.bukkit.Bukkit.getServer;

import static org.bukkit.Bukkit.getLogger;

public class onGroupMessage implements Listener {
    private final BukkitPlugin plugin;
    public onGroupMessage(BukkitPlugin plugin){
        this.plugin = plugin;
    }

    @EventHandler
    public void onGroupMessageReceive(MiraiGroupMessageEvent e) {
        if(plugin.getConfig().getStringList("blacklist.word").stream().anyMatch(s -> e.getMessage().contains(s)) || plugin.getConfig().getLongList("blacklist.qq").contains(e.getSenderID())) return;




//        // 预设的格式调整功能. 是否删除 %message% 消息 中的格式化字符
//        if(plugin.getConfig().getBoolean("aplini.other-format-presets.render-message_format-code",false)){
//            message = message.replace("§", "");
//        }
//
//        // 预设的格式调整功能. 是否删除 %nick% 群名片 中的格式化字符
//        if(plugin.getConfig().getBoolean("aplini.other-format-presets.render-nick_format-code",true)){
//            name = name.replace("§", "");
//        }
//
//        // 预设的格式调整功能. 更好的多行消息
//        if(plugin.getConfig().getBoolean("aplini.other-format-presets.multiline-message.enabled",true) && message.contains("\n")){
//            String _l0 = plugin.getConfig().getString("aplini.other-format-presets.multiline-message.line-0","line-0");
//            String _l1 = plugin.getConfig().getString("aplini.other-format-presets.multiline-message.line-prefix","line-prefix");
//            message = _l0 +"\n"+ _l1 + message.replace("\n", "\n"+ _l1);
//        }
//
//
//        // cleanup-name
//        String $regex_nick = "%regex_nick%";
//        if(plugin.getConfig().getBoolean("aplini.cleanup-name.enabled",false)){
//            Matcher matcher = Pattern.compile(plugin.getConfig().getString("aplini.cleanup-name.regex")).matcher(name);
//            if(matcher.find()){
//                $regex_nick = matcher.group(1);
//            } else {
//                $regex_nick = plugin.getConfig().getString("aplini.cleanup-name.not-captured")
//                        .replace("%groupname%", e.getGroupName())
//                        .replace("%groupid%", String.valueOf(e.getGroupID()))
//                        .replace("%nick%", name)
////                        .replace("%regex_nick%", "%regex_nick%")
//                        .replace("%qq%", String.valueOf(e.getSenderID()))
//                        .replace("%message%", message);
//            }
//        }
//
//        // 预处理
//        if(plugin.getConfig().getBoolean("aplini.pretreatment.enabled",false)){
//            for(Map<?, ?> config : plugin.getConfig().getMapList("aplini.pretreatment.list")){
//                // 前缀匹配
//                if(config.get("prefix") != null && message.startsWith((String) config.get("prefix"))){
//                    if(config.get("send") != null){
//                        return;
//                    }
//                    else if(config.get("to_all") != null){
//                        message = (String) config.get("to_all");
//                    }
//                    else if(config.get("to_replace") != null){
//                        message = message.replace((String) config.get("prefix"), (String) config.get("to_replace"));
//                    }
//                }
//
//                // 包含
//                else if(config.get("contain") != null && message.contains((String) config.get("contain"))){
//                    if(config.get("send") != null){
//                        return;
//                    }
//                    else if(config.get("to_replace") != null){
//                        message = message.replace((String) config.get("contain"), (String) config.get("to_replace"));
//                    }
//                    else if(config.get("to_all") != null){
//                        message = (String) config.get("to_all");
//                    }
//                }
//
//                // 相等
//                else if(config.get("equal") != null && Objects.equals(message, config.get("equal"))){
//                    if(config.get("send") != null){
//                        return;
//                    }
//                    else if(config.get("to_all") != null){
//                        message = (String) config.get("to_all");
//                    }
//                }
//
//                // 正则匹配
//                else if(config.get("regular") != null && Pattern.compile((String) config.get("regular")).matcher(message).find()){
//                    if(config.get("send") != null){
//                        return;
//                    }
//                    else if(config.get("to_regular") != null){
//                        message = message.replaceAll((String) config.get("regular"), (String) config.get("to_regular"));
//                    }
//                    else if(config.get("to_all") != null){
//                        message = (String) config.get("to_all");
//                    }
//                }
//            }
//        }



//        String formatText;
//        if(plugin.getConfig().getBoolean("general.use-miraimc-bind",false) && MiraiMC.getBind(e.getSenderID()) != null){
//            formatText = plugin.getConfig().getString("general.bind-chat-format")
//                    .replace("%groupname%",e.getGroupName())
//                    .replace("%groupid%",String.valueOf(e.getGroupID()))
//                    .replace("%nick%",name)
//                    .replace("%regex_nick%", $regex_nick)
//                    .replace("%qq%",String.valueOf(e.getSenderID()))
//                    .replace("%message%", message);
////            if(Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null){
////                formatText = PlaceholderAPI.setPlaceholders(Bukkit.getOfflinePlayer(MiraiMC.getBind(e.getSenderID())), formatText);
////            }
//        } else {
//            formatText = plugin.getConfig().getString("general.in-game-chat-format")
//                    .replace("%groupname%",e.getGroupName())
//                    .replace("%groupid%",String.valueOf(e.getGroupID()))
//                    .replace("%nick%",name)
//                    .replace("%regex_nick%", $regex_nick)
//                    .replace("%qq%",String.valueOf(e.getSenderID()))
//                    .replace("%message%", message);
//        }


        String message = renderMessage(plugin, e);


        if(! message.equals("") &&
                plugin.getConfig().getLongList("bot.bot-accounts").contains(e.getBotID()) &&
                plugin.getConfig().getLongList("general.group-ids").contains(e.getGroupID())){

            if(plugin.getConfig().getBoolean("aplini.other-format-presets.message-to-log", true)){
                getLogger().info(message);
            }

            TextComponent formatText = new TextComponent(message);

            // 回复消息
            if(e.getQuoteReplyMessage() != null){
                String replyMessage = plugin.getConfig().getString("aplini.reply-message.message")
                        .replace("%qq%", ""+ e.getQuoteReplySenderID())
                        .replace("%_/n_%", "\n")
                        .replace("%message%", ""+ _renderMessage(plugin, e.getQuoteReplyMessage()));

                formatText.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                        new ComponentBuilder(replyMessage).create()));
            }

            //
            getServer().spigot().broadcast(formatText);
        }

//        if(plugin.getConfig().getLongList("bot.bot-accounts").contains(e.getBotID()) && plugin.getConfig().getLongList("general.group-ids").contains(e.getGroupID())){
//
//            String message1 = renderGroupMessage(plugin, e);
//
//            TextComponent formatText2 = new TextComponent(formatText);
//            // 回复消息
//            if(e.getQuoteReplyMessage() != null){
//                formatText2.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(e.getQuoteReplyMessage()).create()));
//            }
//
//            getServer().spigot().broadcast(formatText2);
//        }
    }

}
