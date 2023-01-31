package me.dreamvoid.chat2qq.bukkit;

import me.dreamvoid.chat2qq.bukkit.listener.*;
import me.dreamvoid.chat2qq.bukkit.utils.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

import static me.dreamvoid.chat2qq.bukkit.utils.Util.sendToGroup;

public class BukkitPlugin extends JavaPlugin implements Listener, CommandExecutor {

    @Override // 加载插件
    public void onLoad() {
        saveDefaultConfig();
        reloadConfig();
    }

    @Override // 启用插件
    public void onEnable() {
        Bukkit.getPluginManager().registerEvents(new onGroupMessage(this), this);
        Bukkit.getPluginManager().registerEvents(new onGroupCommandMessage(this), this);
        Bukkit.getPluginManager().registerEvents(new onPlayerMessage(this), this);
        Bukkit.getPluginManager().registerEvents(new onPlayerJoin(this), this);
        Bukkit.getPluginManager().registerEvents(new onPlayerQuit(this), this);
        getCommand("qchat").setExecutor(this);
        getCommand("chat2qq").setExecutor(this);
        if(getConfig().getBoolean("allow-bStats",true)){
            int pluginId = 17587;
            new Metrics(this, pluginId);
        }
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if(command.getName().equalsIgnoreCase("qchat")){

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
                    getConfig().getBoolean("use-general-group-ids", true)? "general.group-ids" : "aplini.qchat.group-ids")
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
            if(args.length>=1 && args[0].equalsIgnoreCase("reload")){
                if(sender.hasPermission("miraimc.command.chat2qq")){
                    reloadConfig();
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&',"&a配置文件已经重新载入！"));
                } else sender.sendMessage(ChatColor.translateAlternateColorCodes('&',"&c你没有足够的权限使用此命令！"));
            } else {
                sender.sendMessage("This server is running "+getDescription().getName()+
                        " version "+getDescription().getVersion()+
                        " by "+ getDescription().getAuthors().toString().replace("[","").replace("]","")+
                        " (MiraiMC version "+Bukkit.getPluginManager().getPlugin("MiraiMC").getDescription().getVersion()+")");
            }
        }
        return true;
    }

}
