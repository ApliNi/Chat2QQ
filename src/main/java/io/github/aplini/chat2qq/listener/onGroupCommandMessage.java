package io.github.aplini.chat2qq.listener;

import io.github.aplini.chat2qq.Chat2QQ;
import io.github.aplini.chat2qq.onebot.event.OneBotGroupMessageEvent;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
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
    public void onGroupMessageReceive(OneBotGroupMessageEvent e) {

        // 异步
        CompletableFuture.runAsync(() -> {

            // 是否启用
            if (!plugin.getConfig().getBoolean("aplini.run-command.enabled", false)) {
                return;
            }

            // 是否为设置的QQ群
            if (!plugin.getConfig().getLongList("aplini.run-command.qq-group").contains((Long) e.getGroupID())) {
                return;
            }

            // 消息是否以指令前缀开头
            if (!e.getMessage().startsWith(plugin.getConfig().getString("aplini.run-command.command-prefix", "/"))) {
                return;
            }

            // 获取指令
            String command = e.getMessage().substring(plugin.getConfig().getString("aplini.run-command.command-prefix", "/").length());

            // 长度限制
            if (!(command.length() <= plugin.getConfig().getInt("aplini.run-command.command-max-length", 255))) {
                return;
            }

            // 实际执行的指令保留原始大小写, 仅将副本小写用于指令匹配
            final String cmd = command;
            String matchCommand = command;
            if (plugin.getConfig().getBoolean("aplini.run-command.always-lowercase", false)) {
                matchCommand = matchCommand.toLowerCase();
            }
            final String mc = matchCommand;

            // 初始化运行状态 // 指令是否运行成功
            boolean runOK = false;


            // 遍历所有小于等于自己权限层级的组
            String[] roleNames = {"member", "admin", "owner"};
            for (int permission_int = e.getSenderPermission(); permission_int >= 0; permission_int--) {
                List<String> commands = getPermissionCommands(permission_int, roleNames);
                if (commands == null) continue;
                for (String list1 : commands) {

                    // 匹配指令：白名单条目作为命令前缀，词边界检查确保参数精确匹配
                    if (commandMatches(mc, list1)) {

                        getLogger().info("[Chat2QQ] " + e.getGroupID() + "." + e.getSenderID() + " 运行指令: /" + cmd);

                        // 执行指令
                        // 是否开启获取指令返回消息
                        if (plugin.getConfig().getBoolean("aplini.run-command.return", true)
                                && plugin.getConfig().getStringList("aplini.run-command.special.no-return")
                                    .stream().noneMatch(entry -> commandMatches(mc, entry))) {

                            // 判断指令输出为空的正则
                            Pattern isNull = Pattern.compile(plugin.getConfig().getString("aplini.run-command.return-isNull", "^\\s*$"), Pattern.DOTALL);
                            // 循环判断指令返回是否为空, 总计等待时间
                            int sleepTime = plugin.getConfig().getInt("aplini.run-command.return-sleep-min", 150);

                            // 指令输出
                            String text;

                            List<String> output = new ArrayList<>();
                            CommandSender capturingSender = Bukkit.createCommandSender(component -> {
                                output.add(PlainTextComponentSerializer.plainText().serialize(component));
                            });

                            try {
                                Bukkit.getScheduler().callSyncMethod(plugin, () -> (Boolean) Bukkit.dispatchCommand(capturingSender, cmd));

                                // 等待指令运行
                                TimeUnit.MILLISECONDS.sleep(plugin.getConfig().getInt("aplini.run-command.return-sleep-min", 150));

                                // 循环判断指令返回是否为空
                                while (true) {
                                    // 输出不为空或换行
                                    text = mergeCommandMessage(output);
                                    if (!isNull.matcher(text).matches()) {
                                        break;
                                    }
                                    // 超时
                                    if (sleepTime >= plugin.getConfig().getInt("aplini.run-command.return-sleep-max", 5500)) {
                                        break;
                                    }
                                    // 等待一个采样间隔时间, 并累计总计等待时间
                                    TimeUnit.MILLISECONDS.sleep(plugin.getConfig().getInt("aplini.run-command.return-sleep-sampling-interval", 200));
                                    sleepTime = sleepTime + plugin.getConfig().getInt("aplini.run-command.return-sleep-sampling-interval", 200);
                                }

                            } catch (InterruptedException ex) {
                                getLogger().info("[Chat2QQ] 运行指令 \"/" + cmd + "\" 时出现异常!");
                                throw new RuntimeException(ex);
                            }

                            // 如果指令输出为空
                            if (isNull.matcher(text).matches()) {
                                text = plugin.getConfig().getString("aplini.run-command.message-no-out", "message-no-out");
                            }

                            // 后处理
                            text = pretreatment(plugin, "aplini.pretreatment-command-message-all", text);
                            if (plugin.getConfig().getBoolean("aplini.pretreatment-command-message-all.enabled-placeholder", false)) {
                                text = text
                                        .replace("%command%", cmd)
                                        .replace("%time%", String.valueOf(sleepTime))
                                        .replace("%qq%", String.valueOf(e.getSenderID()))
                                        .replace("%group%", String.valueOf(e.getGroupID()));
                            }

                            // 打印日志
                            if (plugin.getConfig().getBoolean("aplini.run-command.return-log", true)) {
                                getLogger().info("[Chat2QQ] 指令运行完成, 耗时 " + sleepTime + "ms: \n" + text);
                            }

                            // 指令返回消息
                            sendToGroup(plugin, e.getGroupID(), text);

                        } else {

                            Bukkit.getScheduler().callSyncMethod(plugin, () -> (Boolean) Bukkit.dispatchCommand(Bukkit.getConsoleSender(), cmd));

                            // "运行无返回指令"
                            if (!plugin.getConfig().getString("aplini.run-command.message-no-out", "").isEmpty()) {
                                sendToGroup(plugin, e.getGroupID(), plugin.getConfig().getString("aplini.run-command.message-no-out", "message-no-out"));
                            }
                        }

                        // 运行成功
                        runOK = true;
                        break;
                    }
                }
                if (runOK) break;
            }

            // 指令没有运行成功 && 设置了未命中消息
            if (!runOK && !plugin.getConfig().getString("aplini.run-command.message-miss", "").isEmpty()) {
                // 发送消息
                sendToGroup(plugin, e.getGroupID(), plugin.getConfig().getString("aplini.run-command.message-miss"));
            }
        });
    }


    /**
     * 根据权限层级获取指令白名单，优先读取新版原生角色名 key，无效时回退旧数值 key。
     * @param level 0=member, 1=admin, 2=owner
     * @param roleNames [member, admin, owner]
     */
    private List<String> getPermissionCommands(int level, String[] roleNames) {
        if (level < 0 || level >= roleNames.length) return null;
        String newKey = "aplini.run-command.group." + roleNames[level];
        List<String> list = plugin.getConfig().getStringList(newKey);
        if (!list.isEmpty()) {
            return list;
        }
        String oldKey = "aplini.run-command.group.permission_" + level;
        return plugin.getConfig().getStringList(oldKey);
    }

    /**
     * 命令匹配：白名单条目作为命令前缀，词边界检查确保参数精确匹配。
     * 例如 "list grop" 匹配 "/list grop" 和 "/list grop args"，但不匹配 "/list" 或 "/list grop1"。
     * 单词条目如 "list" 匹配 "/list" 和 "/list args"，但不匹配 "/list1"。
     */
    private static boolean commandMatches(String command, String entry) {
        if ("___ALL_COMMAND___".equals(entry)) {
            return true;
        }
        if (!command.startsWith(entry)) {
            return false;
        }
        return command.length() == entry.length() || command.charAt(entry.length()) == ' ';
    }

    // 合并指令输出为多行字符串
    public String mergeCommandMessage(List<String> output){
        StringJoiner joiner = new StringJoiner("\n");
        for (String line : output) {
            joiner.add(pretreatment(plugin, "aplini.pretreatment-command-message", line));
        }
        return joiner.toString();
    }

}
