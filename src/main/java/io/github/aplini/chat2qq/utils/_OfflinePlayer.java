package io.github.aplini.chat2qq.utils;

import org.bukkit.*;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.profile.PlayerProfile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

public class _OfflinePlayer implements OfflinePlayer {
    @Override
    public boolean isOnline() {
        return false;
    }

    @Nullable
    @Override
    public String getName() {
        return "_Chat2QQ_";
    }

    @NotNull
    @Override
    public UUID getUniqueId() {
        return null;
    }

    /**
     * Gets a copy of the player's profile.
     * <p>
     * If the player is online, the returned profile will be complete.
     * Otherwise, only the unique id is guaranteed to be present. You can use
     * {@link PlayerProfile#update()} to complete the returned profile.
     *
     * @return the player's profile
     */
    @NotNull
    @Override
    public PlayerProfile getPlayerProfile() {
        return null;
    }

    @Override
    public boolean isBanned() {
        return false;
    }

    /**
     * Adds this user to the {@link ProfileBanList}. If a previous ban exists, this will
     * update the entry.
     *
     * @param reason  reason for the ban, null indicates implementation default
     * @param expires date for the ban's expiration (unban), or null to imply
     *                forever
     * @param source  source of the ban, null indicates implementation default
     * @return the entry for the newly created ban, or the entry for the
     * (updated) previous ban
     */
    @Nullable
    @Override
    public BanEntry<PlayerProfile> ban(@Nullable String reason, @Nullable Date expires, @Nullable String source) {
        return null;
    }

    /**
     * Adds this user to the {@link ProfileBanList}. If a previous ban exists, this will
     * update the entry.
     *
     * @param reason  reason for the ban, null indicates implementation default
     * @param expires instant for the ban's expiration (unban), or null to imply
     *                forever
     * @param source  source of the ban, null indicates implementation default
     * @return the entry for the newly created ban, or the entry for the
     * (updated) previous ban
     */
    @Nullable
    @Override
    public BanEntry<PlayerProfile> ban(@Nullable String reason, @Nullable Instant expires, @Nullable String source) {
        return null;
    }

    /**
     * Adds this user to the {@link ProfileBanList}. If a previous ban exists, this will
     * update the entry.
     *
     * @param reason   reason for the ban, null indicates implementation default
     * @param duration how long the ban last, or null to imply
     *                 forever
     * @param source   source of the ban, null indicates implementation default
     * @return the entry for the newly created ban, or the entry for the
     * (updated) previous ban
     */
    @Nullable
    @Override
    public BanEntry<PlayerProfile> ban(@Nullable String reason, @Nullable Duration duration, @Nullable String source) {
        return null;
    }

    @Override
    public boolean isWhitelisted() {
        return false;
    }

    @Override
    public void setWhitelisted(boolean value) {

    }

    @Nullable
    @Override
    public Player getPlayer() {
        return null;
    }

    @Override
    public long getFirstPlayed() {
        return 0;
    }

    @Override
    public long getLastPlayed() {
        return 0;
    }

    @Override
    public boolean hasPlayedBefore() {
        return false;
    }

    @Nullable
    @Override
    public Location getBedSpawnLocation() {
        return null;
    }

    @Override
    public void incrementStatistic(@NotNull Statistic statistic) throws IllegalArgumentException {

    }

    @Override
    public void decrementStatistic(@NotNull Statistic statistic) throws IllegalArgumentException {

    }

    @Override
    public void incrementStatistic(@NotNull Statistic statistic, int amount) throws IllegalArgumentException {

    }

    @Override
    public void decrementStatistic(@NotNull Statistic statistic, int amount) throws IllegalArgumentException {

    }

    @Override
    public void setStatistic(@NotNull Statistic statistic, int newValue) throws IllegalArgumentException {

    }

    @Override
    public int getStatistic(@NotNull Statistic statistic) throws IllegalArgumentException {
        return 0;
    }

    @Override
    public void incrementStatistic(@NotNull Statistic statistic, @NotNull Material material) throws IllegalArgumentException {

    }

    @Override
    public void decrementStatistic(@NotNull Statistic statistic, @NotNull Material material) throws IllegalArgumentException {

    }

    @Override
    public int getStatistic(@NotNull Statistic statistic, @NotNull Material material) throws IllegalArgumentException {
        return 0;
    }

    @Override
    public void incrementStatistic(@NotNull Statistic statistic, @NotNull Material material, int amount) throws IllegalArgumentException {

    }

    @Override
    public void decrementStatistic(@NotNull Statistic statistic, @NotNull Material material, int amount) throws IllegalArgumentException {

    }

    @Override
    public void setStatistic(@NotNull Statistic statistic, @NotNull Material material, int newValue) throws IllegalArgumentException {

    }

    @Override
    public void incrementStatistic(@NotNull Statistic statistic, @NotNull EntityType entityType) throws IllegalArgumentException {

    }

    @Override
    public void decrementStatistic(@NotNull Statistic statistic, @NotNull EntityType entityType) throws IllegalArgumentException {

    }

    @Override
    public int getStatistic(@NotNull Statistic statistic, @NotNull EntityType entityType) throws IllegalArgumentException {
        return 0;
    }

    @Override
    public void incrementStatistic(@NotNull Statistic statistic, @NotNull EntityType entityType, int amount) throws IllegalArgumentException {

    }

    @Override
    public void decrementStatistic(@NotNull Statistic statistic, @NotNull EntityType entityType, int amount) {

    }

    @Override
    public void setStatistic(@NotNull Statistic statistic, @NotNull EntityType entityType, int newValue) {

    }

    /**
     * Gets the player's last death location.
     *
     * @return the last death location if it exists, otherwise null.
     */
    @Nullable
    @Override
    public Location getLastDeathLocation() {
        return null;
    }

    @NotNull
    @Override
    public Map<String, Object> serialize() {
        return null;
    }

    @Override
    public boolean isOp() {
        return true;
    }

    @Override
    public void setOp(boolean value) {

    }
}
