package io.github.aplini.chat2qq.listener;

import io.github.aplini.chat2qq.Chat2QQ;
import io.github.aplini.chat2qq.utils._Commander;
import me.dreamvoid.miraimc.bukkit.event.message.passive.MiraiGroupMessageEvent;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static io.github.aplini.chat2qq.utils.Util.pretreatment;
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
                    if(plugin.getConfig().getBoolean("aplini.run-command.return",true)
                            && !plugin.getConfig().getStringList("aplini.run-command.special.no-return").contains(command)){

                        // 判断指令输出为空的正则
                        Pattern isNull = Pattern.compile(plugin.getConfig().getString("aplini.run-command.return-isNull", "^\\s*$"), Pattern.DOTALL);
                        // 循环判断指令返回是否为空, 总计等待时间
                        int sleepTime = plugin.getConfig().getInt("aplini.run-command.return-sleep-min", 14);
                        // 指令输出
                        String text;

                        _Commander Sender = new _Commander();

                        try {
                            Bukkit.getScheduler().callSyncMethod(plugin, () -> Bukkit.dispatchCommand(Sender, command));

                            // 等待指令运行
                            TimeUnit.MILLISECONDS.sleep(plugin.getConfig().getInt("aplini.run-command.return-sleep-min", 14));

                            // 循环判断指令返回是否为空
                            while(true){
                                // 输出不为空或换行
                                text = mergeCommandMessage(Sender);
                                if(!isNull.matcher(text).matches()){
                                    break;
                                }
                                // 超时
                                if(sleepTime >= plugin.getConfig().getInt("aplini.run-command.return-sleep-max", 5346)){
                                    break;
                                }
                                // 等待一个采样间隔时间, 并累计总计等待时间
                                TimeUnit.MILLISECONDS.sleep(plugin.getConfig().getInt("aplini.run-command.return-sleep-sampling-interval", 172));
                                sleepTime = sleepTime + plugin.getConfig().getInt("aplini.run-command.return-sleep-sampling-interval", 172);
                            }

                        } catch (InterruptedException ex) {
                            getLogger().info("[Chat2QQ] 运行指令 \"/"+ command +"\" 时出现异常!");
                            throw new RuntimeException(ex);
                        }

                        // 如果指令输出为空
                        if(isNull.matcher(text).matches()){
                            text = plugin.getConfig().getString("aplini.run-command.message-no-out","message-no-out");
                        }

                        // 后处理
                        text = pretreatment(plugin, "aplini.pretreatment-command-message-all", text);
                        if(plugin.getConfig().getBoolean("aplini.pretreatment-command-message-all.enabled-placeholder",false)){
                            text = text
                                    .replace("%command%", command)
                                    .replace("%time%", String.valueOf(sleepTime))
                                    .replace("%qq%", String.valueOf(e.getSenderID()))
                                    .replace("%group%", String.valueOf(e.getGroupID()));
                        }

                        // 打印日志
                        if(plugin.getConfig().getBoolean("aplini.run-command.return-log",true)){
                            getLogger().info("[Chat2QQ] 指令运行完成, 耗时 "+ sleepTime +"ms: \n"+ text);
                        }

                        // 指令返回消息
                        sendToGroup(plugin, e.getGroupID(), text);

                    } else {

//                        Bukkit.dispatchCommand(Bukkit.getServer().getConsoleSender(), command);
                        Bukkit.getScheduler().callSyncMethod(plugin, () -> Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command));


                        // "运行无返回指令"
                        if(! plugin.getConfig().getString("aplini.run-command.message-no-out", "").equals("")) {
                            sendToGroup(plugin, e.getGroupID(), plugin.getConfig().getString("aplini.run-command.message-no-out", "message-no-out"));
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


    // 合并指令输出为多行字符串
    public String mergeCommandMessage(_Commander Sender){
        StringBuilder text = new StringBuilder();

        if(Sender.message.size() != 0){
            for(String line : Sender.message){
                text.append(pretreatment(plugin, "aplini.pretreatment-command-message", line +"\n"));
            }
        }

        return text.toString();
    }

}
