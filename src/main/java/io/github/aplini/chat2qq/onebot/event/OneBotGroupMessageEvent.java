package io.github.aplini.chat2qq.onebot.event;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.Nullable;

/**
 * OneBot v11 群消息事件 (message/group)
 * 兼容原 MiraiMC 的 MiraiGroupMessageEvent 字段
 */
public class OneBotGroupMessageEvent extends Event {
    private static final HandlerList handlers = new HandlerList();

    private final long botID;
    private final long groupID;
    private final long senderID;
    private final String senderName;
    private final String senderNameCard;
    private final int senderPermission;   // member=0, admin=1, owner=2
    private final String message;
    private final String groupName;
    @Nullable
    private final String quoteReplyMessage;
    private final long quoteReplySenderID;

    public OneBotGroupMessageEvent(
            long botID, long groupID, long senderID,
            String senderName, String senderNameCard, int senderPermission,
            String message, String groupName,
            @Nullable String quoteReplyMessage, long quoteReplySenderID
    ) {
        this.botID = botID;
        this.groupID = groupID;
        this.senderID = senderID;
        this.senderName = senderName;
        this.senderNameCard = senderNameCard;
        this.senderPermission = senderPermission;
        this.message = message;
        this.groupName = groupName;
        this.quoteReplyMessage = quoteReplyMessage;
        this.quoteReplySenderID = quoteReplySenderID;
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

    /** 发送者 QQ 号 */
    public long getSenderID() {
        return senderID;
    }

    /** 发送者昵称 */
    public String getSenderName() {
        return senderName;
    }

    /** 发送者群名片 */
    public String getSenderNameCard() {
        return senderNameCard;
    }

    /**
     * 发送者群权限
     * @return 0=成员, 1=管理员, 2=群主
     */
    public int getSenderPermission() {
        return senderPermission;
    }

    /** 消息内容 (由 message 段数组拼接, 无段数组时回落 raw_message) */
    public String getMessage() {
        return message;
    }

    /** 群名称 (OneBot v11 标准事件不包含此字段, 可能为空) */
    public String getGroupName() {
        return groupName;
    }

    /** 被回复消息的内容 (仅当消息为回复时存在, 否则为空字符串) */
    @Nullable
    public String getQuoteReplyMessage() {
        return quoteReplyMessage;
    }

    /** 被回复消息的发送者 QQ (仅当消息为回复时存在, 否则为 0) */
    public long getQuoteReplySenderID() {
        return quoteReplySenderID;
    }
}
