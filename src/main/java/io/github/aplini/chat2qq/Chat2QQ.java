package io.github.aplini.chat2qq;

import io.github.aplini.chat2qq.listener.*;
import io.github.aplini.chat2qq.utils.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static io.github.aplini.chat2qq.utils.Util.sendToGroup;

public class Chat2QQ extends JavaPlugin implements Listener, CommandExecutor, TabExecutor {

    @Override // 加载插件
    public void onLoad() {
        saveDefaultConfig();
        reloadConfig();
    }

    @Override // 启用插件
    public void onEnable() {
        // 注册事件
        Bukkit.getPluginManager().registerEvents(new onGroupMessage(this), this);
        Bukkit.getPluginManager().registerEvents(new onGroupCommandMessage(this), this);
        Bukkit.getPluginManager().registerEvents(new onPlayerMessage(this), this);
        Bukkit.getPluginManager().registerEvents(new onPlayerJoin(this), this);
        Bukkit.getPluginManager().registerEvents(new onPlayerQuit(this), this);
        // 注册指令
        Objects.requireNonNull(getCommand("qchat")).setExecutor(this);
        Objects.requireNonNull(getCommand("chat2qq")).setExecutor(this);
        // bStats
        if(getConfig().getBoolean("allow-bStats",true)){new Metrics(this, 17587);}
    }

    // 指令
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if(command.getName().equalsIgnoreCase("qchat")){
            if(! sender.hasPermission("chat2qq.command.qchat")){
                sender.sendMessage("§f[§7Chat2QQ§f] §7没有权限");
                return false;
            }

            if(args.length == 0){
                sender.sendMessage("/qchat [name] <message>");
                return false;
            }

            String name;
            StringBuilder message = new StringBuilder();

            // 是否为玩家
            if(sender instanceof Player){
                name = ((Player) sender).getDisplayName();
                // 黑名单. 玩家名
                if(getConfig().getStringList("blacklist.player").contains(name)){
                    return false;
                }
                // 获取消息内容
                Arrays.stream(args).forEach(arg -> message.append(arg).append(" "));
            }else{
                // 使用填充名称
                if(getConfig().getBoolean("aplini.qchat.use-fill-name", false)){
                    name = getConfig().getString("aplini.qchat.fill-name", "console");
                    Arrays.stream(args).forEach(arg -> message.append(arg).append(" "));
                }else{
                    if(args.length == 1){ // 未设定自定义名称
                        name = getConfig().getString("aplini.qchat.fill-name", "console");
                        Arrays.stream(args).forEach(arg -> message.append(arg).append(" "));
                    }else{
                        name = args[0];
                        for(int i = 1; i < args.length; i++){
                            message.append(args[i]).append(" ");
                        }
                    }
                }
            }

            // 黑名单. 关键词
            if(getConfig().getStringList("blacklist.word").stream().anyMatch(t -> message.toString().contains(t))){
                return false;
            }

            // 消息格式
            String formatText = getConfig().getString("aplini.qchat.qq-format", "qq-format")
                    .replace("%name%", name)
                    .replace("%message%", message);

            // 发送到群

            getConfig().getLongList(
                    getConfig().getBoolean("aplini.qchat.use-general-group-ids", true)? "general.group-ids" : "aplini.qchat.group-ids")
                    .forEach(gid -> sendToGroup(this, gid, formatText));

            // 广播到服务器
            if(getConfig().getBoolean("aplini.qchat.mc-broadcast", true)){
                Bukkit.broadcastMessage(
                        getConfig().getString("aplini.qchat.mc-format", "mc-format")
                                .replace("%name%", name)
                                .replace("%message%", message)
                );
            }
        }

        else if(command.getName().equalsIgnoreCase("chat2qq")){
            if(args.length == 0){
                sender.sendMessage("§f[§7Chat2QQ§f] §7https://github.com/ApliNi/Chat2QQ");
                sender.sendMessage("§7   |                 ");
                sender.sendMessage("§7   //| |\\  | _| |\\ _|");
                sender.sendMessage("§7     |               ");
                return true;
            }
            else if(args[0].equalsIgnoreCase("reload")){
                if(! sender.hasPermission("chat2qq.command.chat2qq")){
                    sender.sendMessage("§f[§7Chat2QQ§f] §7没有权限");
                    return false;
                }
                reloadConfig();
                sender.sendMessage("§f[§7Chat2QQ§f] §7已重载配置");
                return true;
            }
        }
        return false;
    }

    // 指令补全
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if(args.length == 1){
            List<String> list = new ArrayList<>();
            list.add("reload"); // 重载配置
            return list;
        }
        return null;
    }
}
