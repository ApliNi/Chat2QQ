package io.github.aplini.chat2qq.utils;

import org.bukkit.plugin.Plugin;

public class versionCheck {
    public static boolean versionCheck_configVersion(Plugin plugin) {
        if(plugin.getConfig().getLong("config-version", 0) != 100){
            return false;
        }
        return true;
    }
}
