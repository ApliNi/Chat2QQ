package io.github.aplini.chat2qq;

import io.github.aplini.chat2qq.bot.onBotOnline;
import io.github.aplini.chat2qq.bot.onCardChange;
import io.github.aplini.chat2qq.listener.*;
import io.github.aplini.chat2qq.utils.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static io.github.aplini.chat2qq.utils.Util.sendToGroup;

public class Chat2QQ extends JavaPlugin implements Listener, CommandExecutor, TabExecutor {
    public static Chat2QQ plugin;
    // Map<群号, Map<QQ号, 名称>>
    public static Map<Long, Map<Long, String>> group_cache_all = new HashMap<>();

    public static File tempFile = new File("./plugins/Chat2QQ/temp.yml");
    public static FileConfiguration temp;

    @Override // 加载插件
    public void onLoad() {
        plugin = this;
        plugin.saveDefaultConfig();
        plugin.reloadConfig();

        readTempData();
    }

    public static void readTempData() {
        temp = YamlConfiguration.loadConfiguration(tempFile);
        if(plugin.getConfig().getBoolean("aplini.player-cache.enabled", true) && temp.get("group_cache_all") != null){
            ConfigurationSection outerSection = temp.getConfigurationSection("group_cache_all");
            for (String groupIdStr : outerSection.getKeys(false)) {
                ConfigurationSection playerSection = outerSection.getConfigurationSection(groupIdStr);
                Map<Long, String> playerMap = playerSection.getKeys(false).stream().collect(Collectors.toMap(Long::parseLong, playerSection::getString));
                group_cache_all.put(Long.parseLong(groupIdStr), playerMap);
            }
        }
    }

    public static boolean saveTempLock = false;
    public static void saveTempData() {
        if(saveTempLock) return;
        saveTempLock = true;
        CompletableFuture.runAsync(() -> {
            try {
                TimeUnit.MILLISECONDS.sleep(3000);
                temp.set("group_cache_all", group_cache_all);
                temp.save(tempFile);
            } catch (InterruptedException | IOException ex) {
                throw new RuntimeException(ex);
            } finally {
                saveTempLock = false;
            }
        });
    }

    @Override // 启用插件
    public void onEnable() {

        // 注册事件

        // 机器人上线
        Bukkit.getPluginManager().registerEvents(new onBotOnline(this), this);
        // 群成员修改名片 :: 群成员缓存
        Bukkit.getPluginManager().registerEvents(new onCardChange(), this);

        // 群成员发送消息
        Bukkit.getPluginManager().registerEvents(new onGroupMessage(this), this);
        // 群成员发送消息 :: 指令
        Bukkit.getPluginManager().registerEvents(new onGroupCommandMessage(this), this);
        // 群成员发送消息 :: 自动回复
        Bukkit.getPluginManager().registerEvents(new onAutoResponseMessage(this), this);
        // 玩家发送消息
        Bukkit.getPluginManager().registerEvents(new onPlayerMessage(this), this);
        // 玩家加入退出
        Bukkit.getPluginManager().registerEvents(new onPlayerJoin(this), this);
        Bukkit.getPluginManager().registerEvents(new onPlayerQuit(this), this);

        // 事件任务程序
        if(getConfig().getBoolean("aplini.event-func.enable", false)){
            Bukkit.getPluginManager().registerEvents(new EventFunc(this), this);
        }

        // 注册指令
        Objects.requireNonNull(getCommand("qchat")).setExecutor(this);
        Objects.requireNonNull(getCommand("chat2qq")).setExecutor(this);

        // PAPI
        if(Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null){
            Bukkit.getPluginManager().registerEvents(this, this);
        }

        // bStats
        new Metrics(this, 17587);
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
                sender.sendMessage("/qchat <message>");
                return false;
            }

            // 获取消息内容
            String message = String.join(" ", args);

            String formatText;

            // 是否为玩家
            if(sender instanceof Player){
                String name = ((Player) sender).getDisplayName();
                // 黑名单. 玩家名
                if(getConfig().getStringList("blacklist.player").contains(name)){
                    return false;
                }
                // 黑名单. 关键词
                if(getConfig().getStringList("blacklist.word").stream().anyMatch(message :: contains)){
                    return false;
                }
                // 广播到服务器
                if(getConfig().getBoolean("aplini.qchat.player.mc-broadcast", true)){
                    Bukkit.broadcastMessage(
                            getConfig().getString("aplini.qchat.player.mc-format", "mc-format")
                                    .replace("%name%", name)
                                    .replace("%message%", message)
                    );
                }
                // 发送到群的消息格式
                formatText = getConfig().getString("aplini.qchat.player.qq-format", "qq-format")
                        .replace("%name%", name)
                        .replace("%message%", message);
            }else{
                // 发送到群的消息格式
                formatText = getConfig().getString("aplini.qchat.console.qq-format", "qq-format")
                        .replace("%message%", message);
            }

            // 发送到群
            getConfig().getLongList(
                    getConfig().getBoolean("aplini.qchat.use-general-group-ids", true)? "general.group-ids" : "aplini.qchat.group-ids")
                    .forEach(gid -> sendToGroup(this, gid, formatText));
        }

        else if(command.getName().equalsIgnoreCase("chat2qq")){
            if(args.length == 0){
                sender.sendMessage("§7   |                 ");
                sender.sendMessage("§7   //| |\\  | _| |\\ _|");
                sender.sendMessage("§7     |               ");
                sender.sendMessage("§f[§7Chat2QQ§f] §7https://github.com/ApliNi/Chat2QQ");
                sender.sendMessage("指令列表: ");
                sender.sendMessage("  - /qchat <消息> §7- 发送一条消息到QQ群中");
                sender.sendMessage("  - /chat2qq reload §7- 重载配置");
                sender.sendMessage("  - /chat2qq outgroupcacheall §7- 打印群成员缓存数据");
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
            else if(args[0].equalsIgnoreCase("outgroupcacheall")){
                if(! sender.hasPermission("chat2qq.command.chat2qq")){
                    sender.sendMessage("§f[§7Chat2QQ§f] §7没有权限");
                    return false;
                }
                sender.sendMessage("§f[§7Chat2QQ§f] §7群成员缓存: \n" + group_cache_all);
                return true;
            }
        }
        return false;
    }

    // 指令补全
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if (command.getName().equalsIgnoreCase("chat2qq")) {
            if (args.length == 1) {
                List<String> list = new ArrayList<>();
                list.add("reload"); // 重载配置
                list.add("outgroupcacheall"); // 打印群成员缓存数据
                return list;
            }
        }
        return null;
    }
}
