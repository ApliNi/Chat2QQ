package io.github.aplini.chat2qq.utils;

import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.UUID;

public class __spigot extends CommandSender.Spigot {
    List<String> message;
    public __spigot(List<String> message) {
        this.message = message;
    }

    @Deprecated
    public void sendMessage(@NotNull net.md_5.bungee.api.chat.BaseComponent component) {
        this.message.add(component.toLegacyText());
    }

    @Deprecated
    public void sendMessage(@NotNull net.md_5.bungee.api.chat.BaseComponent... components) {
        for(net.md_5.bungee.api.chat.BaseComponent component : components){
            this.message.add(component.toLegacyText());
        }
    }

    @Deprecated
    public void sendMessage(@Nullable UUID sender, @NotNull net.md_5.bungee.api.chat.BaseComponent component) {
        this.message.add(component.toLegacyText());
    }

    @Deprecated
    public void sendMessage(@Nullable UUID sender, @NotNull net.md_5.bungee.api.chat.BaseComponent... components) {
        for(net.md_5.bungee.api.chat.BaseComponent component : components){
            this.message.add(component.toLegacyText());
        }
    }
}
