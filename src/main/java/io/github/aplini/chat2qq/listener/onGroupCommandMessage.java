package io.github.aplini.chat2qq.listener;

import io.github.aplini.chat2qq.Chat2QQ;
import io.github.aplini.chat2qq.utils.Commander;
import me.dreamvoid.miraimc.bukkit.event.message.passive.MiraiGroupMessageEvent;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static io.github.aplini.chat2qq.utils.Util.sendToGroup;
import static org.bukkit.Bukkit.getLogger;

// 运行指令的功能

public class onGroupCommandMessage implements Listener {
    private final Chat2QQ plugin;
    public onGroupCommandMessage(Chat2QQ plugin){
        this.plugin = plugin;
    }

    @EventHandler
    public void onGroupMessageReceive(MiraiGroupMessageEvent e) {

        // 是否启用
        if(! plugin.getConfig().getBoolean("aplini.run-command.enabled",false)){
            return;
        }

        // 是否为设置的QQ群
        if(! plugin.getConfig().getLongList("aplini.run-command.qq-group").contains(e.getGroupID())){
            return;
        }

        // 消息是否以指令前缀开头
        if(! e.getMessage().startsWith(plugin.getConfig().getString("aplini.run-command.command-prefix", "/"))){
            return;
        }

        // 获取指令
        String command = e.getMessage().substring(plugin.getConfig().getString("aplini.run-command.command-prefix", "/").length());

        // 长度限制
        if(! (command.length() <= plugin.getConfig().getInt("aplini.run-command.command-max-length", 255))){
            return;
        }

        // 正则匹配主命令
        Matcher matcher = Pattern.compile(plugin.getConfig().getString("aplini.run-command.regex-command-main", "^([^ ]+)")).matcher(command);

        // 是否匹配到主命令
        if(! matcher.find()){
            return;
        }

        // 主命令
        String commandMain = matcher.group(1);

        // 转换为小写
        if(plugin.getConfig().getBoolean("aplini.run-command.always-lowercase", false)){
            commandMain = commandMain.toLowerCase();
        }

        // 初始化运行状态 // 指令是否运行成功
        boolean runOK = false;


        // 遍历所有小于等于自己权限数值的组
        for(int permission_int = e.getSenderPermission(); permission_int >= 0; permission_int --){
            for(String list1 : plugin.getConfig().getStringList("aplini.run-command.group.permission_"+ permission_int)){

                // ___ALL_COMMAND___ 表示可以运行任何指令
                if(Objects.equals(commandMain, list1) || Objects.equals(list1, "___ALL_COMMAND___")){

                    getLogger().info("[Chat2QQ] "+ e.getGroupID() +"."+ e.getSenderID() + " 运行指令: /"+ command);

                    // 执行指令
                    // 是否开启获取指令返回消息
                    if(plugin.getConfig().getBoolean("aplini.run-command.return",true)){

                        Commander Sender = new Commander();

                        try {
                            Bukkit.getScheduler().callSyncMethod(plugin, () -> Bukkit.dispatchCommand(Sender, command));
                            // 等待指令运行
                            TimeUnit.MILLISECONDS.sleep(plugin.getConfig().getInt("aplini.run-command.return-sleep", 300));
                        } catch (InterruptedException ex) {
                            getLogger().info("[Chat2QQ] 运行指令 \"/"+ command +"\" 时出现异常!");
                            throw new RuntimeException(ex);
                        }

                        // 消息处理
                        StringBuilder text = new StringBuilder();
                        if(Sender.message.size() == 1){
                            text = Optional.ofNullable(Sender.message.get(0)).map(StringBuilder::new).orElse(null);
                        }else if(Sender.message.size() > 1){
                            for(String m : Sender.message){
                                text.append(m).append("\n");
                            }
                        }else{
                            text = new StringBuilder(plugin.getConfig().getString("aplini.run-command.message-no-out","message-no-out"));
                        }

                        // 打印日志
                        if(plugin.getConfig().getBoolean("aplini.run-command.return-log",true)){
                            getLogger().info("指令输出: \n"+ text);
                        }

                        // 处理格式化字符
                        String finalText = String.valueOf(text).replaceAll("§[a-z0-9]", "");

                        // 指令返回消息
                        sendToGroup(plugin, e.getGroupID(), finalText);

                    } else {

                        Bukkit.dispatchCommand(Bukkit.getServer().getConsoleSender(), command);

                        // "运行无返回指令"
                        if(! plugin.getConfig().getString("aplini.run-command.mmessage-no-out", "").equals("")) {
                            sendToGroup(plugin, e.getGroupID(),
                                    plugin.getConfig().getString("aplini.run-command.message-no-out", "message-no-out"));
                        }
                    }

                    // 运行成功
                    runOK = true;
                    break;
                }
            }
            if(runOK) break;
        }

        // 指令没有运行成功 && 设置了未命中消息
        if(!runOK && !plugin.getConfig().getString("aplini.run-command.message-miss", "").equals("")){
            // 发送消息
            sendToGroup(plugin, e.getGroupID(), plugin.getConfig().getString("aplini.run-command.message-miss"));
        }
    }

}
