package io.github.aplini.chat2qq.onebot.event;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * OneBot 群成员离开事件 (notice/group_decrease)
 * 兼容原 MiraiMC 的 MiraiMemberLeaveEvent
 */
public class OneBotMemberLeaveEvent extends Event {
    private static final HandlerList handlers = new HandlerList();

    private final long botID;
    private final long groupID;
    private final long memberID;

    public OneBotMemberLeaveEvent(long botID, long groupID, long memberID) {
        this.botID = botID;
        this.groupID = groupID;
        this.memberID = memberID;
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

    /** 离开成员 QQ 号 */
    public long getMemberID() {
        return memberID;
    }
}
