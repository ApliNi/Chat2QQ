package io.github.aplini.chat2qq.utils;

import org.bukkit.plugin.Plugin;

public class versionCheck {

    // 配置文件版本不同时返回 true
    public static boolean versionCheck_configVersion(Plugin plugin) {
        if(plugin.getConfig().getLong("config-version", 0) != 100){
            return true;
        }
        return false;
    }
}
