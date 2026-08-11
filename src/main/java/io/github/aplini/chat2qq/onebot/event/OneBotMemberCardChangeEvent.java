package io.github.aplini.chat2qq.onebot.event;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * OneBot 群成员名片变更事件 (notice/group_card)
 * 兼容原 MiraiMC 的 MiraiMemberCardChangeEvent
 */
public class OneBotMemberCardChangeEvent extends Event {
    private static final HandlerList handlers = new HandlerList();

    private final long botID;
    private final long groupID;
    private final long memberID;
    private final String newNick;
    private final String memberNick;

    public OneBotMemberCardChangeEvent(long botID, long groupID, long memberID, String newNick, String memberNick) {
        this.botID = botID;
        this.groupID = groupID;
        this.memberID = memberID;
        this.newNick = newNick;
        this.memberNick = memberNick;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    /** 机器人 QQ 号 */
    public long getBotID() {
        return botID;
    }

    /** 群号 */
    public long getGroupID() {
        return groupID;
    }

    /** 成员 QQ 号 */
    public long getMemberID() {
        return memberID;
    }

    /** 新群名片 */
    public String getNewNick() {
        return newNick;
    }

    /** 成员昵称 (OneBot v11 group_card 不包含, 可能为空) */
    public String getMemberNick() {
        return memberNick;
    }
}
