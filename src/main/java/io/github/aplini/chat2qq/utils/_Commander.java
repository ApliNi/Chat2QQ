package io.github.aplini.chat2qq.utils;

import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.command.CommandSender;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.permissions.PermissionAttachmentInfo;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class _Commander implements CommandSender {
    public List<String> message = new ArrayList<>();

    @Override
    public void sendMessage(@NotNull String message) {
        this.message.add(message);
    }

    @Override
    public void sendMessage(@NotNull String[] messages) {
        message.addAll(Arrays.asList(messages));
    }

    @Override
    public void sendMessage(@Nullable UUID sender, @NotNull String message) {
        this.message.add(message);
    }

    @Override
    public void sendMessage(@Nullable UUID sender, @NotNull String... messages) {
        message.addAll(Arrays.asList(messages));
    }

    @NotNull
    @Override
    public Server getServer() {
        return Bukkit.getConsoleSender().getServer();
    }

    @NotNull
    @Override
    public String getName() {
        return "_Chat2QQ_";
    }

    @NotNull
    @Override
    public Spigot spigot() {
        return new __spigot(message);
//        return Bukkit.getConsoleSender().spigot();
    }

    @Override
    public boolean isPermissionSet(@NotNull String s) {
        return Bukkit.getConsoleSender().isPermissionSet(s);
    }

    @Override
    public boolean isPermissionSet(@NotNull Permission permission) {
        return Bukkit.getConsoleSender().isPermissionSet(permission);
    }

    @Override
    public boolean hasPermission(@NotNull String s) {
        return Bukkit.getConsoleSender().hasPermission(s);
    }

    @Override
    public boolean hasPermission(@NotNull Permission permission) {
        return Bukkit.getConsoleSender().hasPermission(permission);
    }

    @NotNull
    @Override
    public PermissionAttachment addAttachment(@NotNull Plugin plugin, @NotNull String s, boolean b) {
        return Bukkit.getConsoleSender().addAttachment(plugin, s, b);
    }

    @NotNull
    @Override
    public PermissionAttachment addAttachment(@NotNull Plugin plugin) {
        return Bukkit.getConsoleSender().addAttachment(plugin);
    }

    @Override
    public PermissionAttachment addAttachment(@NotNull Plugin plugin, @NotNull String s, boolean b, int i) {
        return Bukkit.getConsoleSender().addAttachment(plugin, s, b, i);
    }

    @Override
    public PermissionAttachment addAttachment(@NotNull Plugin plugin, int i) {
        return Bukkit.getConsoleSender().addAttachment(plugin, i);
    }

    @Override
    public void removeAttachment(@NotNull PermissionAttachment permissionAttachment) {

    }

    @Override
    public void recalculatePermissions() {

    }

    @NotNull
    @Override
    public Set<PermissionAttachmentInfo> getEffectivePermissions() {
        return Bukkit.getConsoleSender().getEffectivePermissions();
    }

    @Override
    public boolean isOp() {
        return true;
    }

    @Override
    public void setOp(boolean b) {

    }
}
