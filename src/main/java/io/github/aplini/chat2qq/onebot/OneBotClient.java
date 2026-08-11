package io.github.aplini.chat2qq.onebot;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import io.github.aplini.chat2qq.Chat2QQ;
import io.github.aplini.chat2qq.onebot.event.OneBotGroupMessageEvent;
import io.github.aplini.chat2qq.onebot.event.OneBotMemberCardChangeEvent;
import io.github.aplini.chat2qq.onebot.event.OneBotMemberJoinEvent;
import io.github.aplini.chat2qq.onebot.event.OneBotMemberLeaveEvent;
import org.bukkit.Bukkit;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class OneBotClient extends WebSocketClient {

    public static volatile OneBotClient INSTANCE;
    private static volatile boolean reconnectEnabled = true;

    private final Gson gson = new Gson();
    private final ConcurrentHashMap<String, CompletableFuture<JsonObject>> pendingRequests = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<Long, String> groupNameCache = new ConcurrentHashMap<>();
    private static final Pattern CQ_REPLY_ID_PATTERN = Pattern.compile("\\[CQ:reply,[^]]*id=([0-9]+)[^]]*]");

    private Chat2QQ plugin;
    private String token;
    private int retrySeconds;
    private volatile boolean shouldReconnect = true;

    // 无参构造，配合后续 connect() 调用
    public OneBotClient() {
        // WebSocketClient 要求构造时传入 URI，此处使用占位 URI，connect() 中会替换
        super(getDummyURI());
    }

    private OneBotClient(Chat2QQ plugin, URI uri, String token, int retrySeconds) {
        super(uri, createHeaders(token));
        this.plugin = plugin;
        this.token = token;
        this.retrySeconds = Math.max(1, retrySeconds);
        this.shouldReconnect = true;
    }

    private static URI getDummyURI() {
        try {
            return new URI("ws://localhost");
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    private static Map<String, String> createHeaders(String token) {
        Map<String, String> headers = new HashMap<>();
        if (token != null && !token.isEmpty()) {
            headers.put("Authorization", "Bearer " + token);
        }
        return headers;
    }

    /**
     * 连接 OneBot WebSocket 服务器
     * @param plugin       插件实例
     * @param host         WebSocket 地址，如 ws://127.0.0.1:3003
     * @param token        Authorization token (可为空)
     * @param retrySeconds 断线重连等待秒数
     */
    public void connect(Chat2QQ plugin, String host, String token, int retrySeconds) {
        reconnectEnabled = true;

        URI uri;
        try {
            uri = new URI(host);
        } catch (URISyntaxException e) {
            plugin.getLogger().severe("[Chat2QQ] OneBot WebSocket 地址格式错误: " + host);
            return;
        }

        plugin.getLogger().info("[Chat2QQ] OneBot 客户端正在连接 " + host + " ...");
        connectNewClient(plugin, uri, token, retrySeconds);
    }

    private static void connectNewClient(Chat2QQ plugin, URI uri, String token, int retrySeconds) {
        if (!reconnectEnabled || !plugin.isEnabled()) {
            return;
        }
        OneBotClient client = new OneBotClient(plugin, uri, token, retrySeconds);
        INSTANCE = client;
        client.connect();
    }

    @Override
    public void onOpen(ServerHandshake handshake) {
        plugin.getLogger().info("[Chat2QQ] OneBot 客户端已连接 (" + getURI() + ")");
        preloadGroupNames();
    }

    @Override
    public void onMessage(String message) {
        JsonObject json;
        try {
            json = gson.fromJson(message, JsonObject.class);
        } catch (Exception e) {
            plugin.getLogger().warning("[Chat2QQ] OneBot 消息解析失败: " + e.getMessage());
            return;
        }
        if (json == null) return;

        // ---- 1) API 调用响应 (echo) ----
        if (json.has("echo") && !json.get("echo").isJsonNull()) {
            String echo = json.get("echo").getAsString();
            CompletableFuture<JsonObject> future = pendingRequests.remove(echo);
            if (future != null) {
                String status = json.has("status") && !json.get("status").isJsonNull()
                        ? json.get("status").getAsString() : "";
                if ("ok".equals(status)) {
                    future.complete(json);
                } else {
                    String errMsg = json.has("msg") && !json.get("msg").isJsonNull()
                            ? json.get("msg").getAsString() : "未知错误";
                    future.completeExceptionally(new RuntimeException("OneBot API 错误: " + errMsg));
                }
            }
            return;
        }

        // ---- 2) 事件推送 ----
        String postType = json.has("post_type") ? json.get("post_type").getAsString() : null;
        if (postType == null) return;

        switch (postType) {
            case "message":
                handleMessage(json);
                break;
            case "notice":
                handleNotice(json);
                break;
            case "meta_event":
                handleMetaEvent(json);
                break;
            default:
                // 忽略未知类型
        }
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        // 失效旧实例的回调
        if (this != INSTANCE) return;

        plugin.getLogger().warning("[Chat2QQ] OneBot 连接已关闭 (code=" + code + ", reason=" + reason + ", remote=" + remote + ")");

        // 使所有待处理请求异常完成
        for (CompletableFuture<JsonObject> future : pendingRequests.values()) {
            future.completeExceptionally(new java.io.IOException("OneBot 连接已关闭"));
        }
        pendingRequests.clear();

        // 自动重连（未调用 closeClient() 时）。连接失败时 remote 可能为 false，也需要重试。
        if (reconnectEnabled && shouldReconnect) {
            plugin.getLogger().info("[Chat2QQ] " + retrySeconds + " 秒后尝试重连...");
            URI reconnectUri = getURI();
            Bukkit.getScheduler().runTaskLater(plugin, () -> {
                try {
                    connectNewClient(plugin, reconnectUri, token, retrySeconds);
                } catch (Exception e) {
                    plugin.getLogger().severe("[Chat2QQ] 重连失败: " + e.getMessage());
                }
            }, retrySeconds * 20L);
        }
    }

    @Override
    public void onError(Exception ex) {
        if (this != INSTANCE) return;
        plugin.getLogger().warning("[Chat2QQ] OneBot WebSocket 错误: " + ex.getMessage());
    }

    /**
     * 主动关闭连接（禁止重连）
     */
    public void closeClient() {
        reconnectEnabled = false;
        shouldReconnect = false;
        if (isOpen()) {
            close(1000, "Plugin disable");
        }
    }

    /**
     * 检查是否已连接
     */
    public boolean isConnected() {
        return isOpen();
    }

    // ==================== 消息发送 ====================

    /**
     * 发送群消息 (send_group_msg)
     * @param groupId 群号
     * @param message 文本消息
     * @return CompletableFuture，完成时为服务器返回的完整 JSON
     */
    public CompletableFuture<String> sendGroupMessage(long groupId, String message) {
        JsonObject params = new JsonObject();
        params.addProperty("group_id", groupId);

        JsonArray messageArray = new JsonArray();
        JsonObject textSegment = new JsonObject();
        textSegment.addProperty("type", "text");
        JsonObject textData = new JsonObject();
        textData.addProperty("text", message);
        textSegment.add("data", textData);
        messageArray.add(textSegment);
        params.add("message", messageArray);

        return sendAction("send_group_msg", params).thenApply(JsonObject::toString);
    }

    private CompletableFuture<JsonObject> sendAction(String action, JsonObject params) {
        CompletableFuture<JsonObject> future = new CompletableFuture<>();

        if (this != INSTANCE || !isOpen()) {
            plugin.getLogger().warning("[Chat2QQ] OneBot 客户端未连接，无法调用 " + action);
            future.completeExceptionally(new IllegalStateException("OneBot 客户端未连接"));
            return future;
        }

        String echo = UUID.randomUUID().toString();
        JsonObject request = new JsonObject();
        request.addProperty("action", action);
        request.add("params", params != null ? params : new JsonObject());
        request.addProperty("echo", echo);

        pendingRequests.put(echo, future);
        send(request.toString());

        return future;
    }

    private void preloadGroupNames() {
        sendAction("get_group_list", new JsonObject()).thenAccept(response -> {
            JsonArray data = getDataArray(response);
            if (data == null) {
                return;
            }
            for (JsonElement element : data) {
                if (!element.isJsonObject()) {
                    continue;
                }
                cacheGroupName(element.getAsJsonObject());
            }
            plugin.getLogger().info("[Chat2QQ] OneBot 群名称缓存完成, 已缓存 " + groupNameCache.size() + " 个群");
        }).exceptionally(ex -> {
            plugin.getLogger().warning("[Chat2QQ] OneBot 获取群列表失败: " + ex.getMessage());
            return null;
        });
    }

    private CompletableFuture<String> getGroupName(long groupId, JsonObject eventJson) {
        String eventGroupName = getString(eventJson, "group_name", null);
        if (eventGroupName != null && !eventGroupName.isEmpty()) {
            groupNameCache.put(groupId, eventGroupName);
            return CompletableFuture.completedFuture(eventGroupName);
        }

        String cached = groupNameCache.get(groupId);
        if (cached != null) {
            return CompletableFuture.completedFuture(cached);
        }

        JsonObject params = new JsonObject();
        params.addProperty("group_id", groupId);
        params.addProperty("no_cache", false);
        return sendAction("get_group_info", params).thenApply(response -> {
            JsonObject data = getDataObject(response);
            if (data == null) {
                throw new IllegalStateException("get_group_info 响应缺少 data");
            }
            String groupName = cacheGroupName(data);
            if (groupName == null || groupName.isEmpty()) {
                throw new IllegalStateException("get_group_info 响应缺少 group_name");
            }
            return groupName;
        });
    }

    // ==================== 事件分发 ====================

    /** message/group */
    private void handleMessage(JsonObject json) {
        if (!json.has("message_type") || !"group".equals(json.get("message_type").getAsString())) {
            return; // 只处理群消息
        }

        long botID = json.get("self_id").getAsLong();
        long groupID = json.get("group_id").getAsLong();
        long userID = json.get("user_id").getAsLong();

        // sender
        JsonObject sender = json.getAsJsonObject("sender");
        String nickname = sender != null && sender.has("nickname") ? sender.get("nickname").getAsString() : "";
        String card = sender != null && sender.has("card") ? sender.get("card").getAsString() : "";
        // role -> permission: member=0, admin=1, owner=2
        int permission = 0;
        if (sender != null && sender.has("role")) {
            String role = sender.get("role").getAsString();
            if ("admin".equals(role)) permission = 1;
            else if ("owner".equals(role)) permission = 2;
        }
        final int senderPermission = permission;

        // 消息内容: 优先从 message 段数组拼接(文本更干净, at/图片/表情转为可读文本而非 CQ 码),
        // 无段数组时回落 raw_message
        String rawMessage;
        if (json.has("message") && json.get("message").isJsonArray()) {
            rawMessage = messageSegmentsToText(json.getAsJsonArray("message"));
        } else if (json.has("raw_message") && !json.get("raw_message").isJsonNull()) {
            rawMessage = json.get("raw_message").getAsString();
        } else {
            rawMessage = "";
        }

        long quoteMessageId = extractReplyMessageId(json);

        CompletableFuture<String> groupNameFuture = getGroupName(groupID, json).exceptionally(ex -> {
            plugin.getLogger().warning("[Chat2QQ] 获取群 " + groupID + " 名称失败, 已使用空群名继续处理该条群消息: " + ex.getMessage());
            return "";
        });

        CompletableFuture<QuoteReply> quoteReplyFuture = getQuoteReply(quoteMessageId).exceptionally(ex -> {
            plugin.getLogger().warning("[Chat2QQ] 获取引用回复消息 " + quoteMessageId + " 失败, 已不带引用内容继续处理该条群消息: " + ex.getMessage());
            return QuoteReply.empty();
        });

        groupNameFuture.thenCombine(quoteReplyFuture, (groupName, quoteReply) -> {
            callGroupMessageEvent(
                    botID, groupID, userID,
                    nickname, card, senderPermission,
                    rawMessage, groupName,
                    quoteReply.message, quoteReply.senderId
            );
            return null;
        });
    }

    private CompletableFuture<QuoteReply> getQuoteReply(long messageId) {
        if (messageId <= 0) {
            return CompletableFuture.completedFuture(QuoteReply.empty());
        }

        JsonObject params = new JsonObject();
        params.addProperty("message_id", messageId);
        return sendAction("get_msg", params).thenApply(response -> {
            JsonObject data = getDataObject(response);
            if (data == null) {
                throw new IllegalStateException("get_msg 响应缺少 data");
            }

            String message = extractMessageText(data);
            long senderId = extractSenderId(data);
            return new QuoteReply(message, senderId);
        });
    }

    private long extractReplyMessageId(JsonObject eventJson) {
        JsonElement message = eventJson.get("message");
        if (message == null || message.isJsonNull()) {
            return 0L;
        }

        if (message.isJsonArray()) {
            for (JsonElement segment : message.getAsJsonArray()) {
                if (!segment.isJsonObject()) {
                    continue;
                }
                JsonObject segmentObject = segment.getAsJsonObject();
                if (!"reply".equals(getString(segmentObject, "type", ""))) {
                    continue;
                }
                JsonObject data = segmentObject.getAsJsonObject("data");
                long id = getLong(data, "id");
                if (id > 0) {
                    return id;
                }
            }
            return 0L;
        }

        if (message.isJsonPrimitive()) {
            Matcher matcher = CQ_REPLY_ID_PATTERN.matcher(message.getAsString());
            if (matcher.find()) {
                try {
                    return Long.parseLong(matcher.group(1));
                } catch (NumberFormatException ignored) {
                    return 0L;
                }
            }
        }

        return 0L;
    }

    private void callGroupMessageEvent(long botID, long groupID, long userID, String nickname, String card,
                                       int permission, String rawMessage, String groupName,
                                       String quoteMessage, long quoteSenderID) {
        callEvent(new OneBotGroupMessageEvent(
                botID, groupID, userID,
                nickname, card, permission,
                rawMessage, groupName,
                quoteMessage, quoteSenderID
        ));
    }

    /** notice/* */
    private void handleNotice(JsonObject json) {
        String noticeType = json.has("notice_type") ? json.get("notice_type").getAsString() : "";
        long botID = getLong(json, "self_id");

        switch (noticeType) {
            case "group_increase": {
                long groupID = json.get("group_id").getAsLong();
                long userID = json.get("user_id").getAsLong();
                callEvent(new OneBotMemberJoinEvent(botID, groupID, userID));
                break;
            }
            case "group_decrease": {
                long groupID = json.get("group_id").getAsLong();
                long userID = json.get("user_id").getAsLong();
                callEvent(new OneBotMemberLeaveEvent(botID, groupID, userID));
                break;
            }
            case "group_card": {
                long groupID = json.get("group_id").getAsLong();
                long userID = json.get("user_id").getAsLong();
                String cardNew = json.has("card_new") && !json.get("card_new").isJsonNull()
                        ? json.get("card_new").getAsString() : "";
                // OneBot v11 group_card 不提供旧的昵称字段
                callEvent(new OneBotMemberCardChangeEvent(botID, groupID, userID, cardNew, ""));
                break;
            }
            default:
                // 忽略其他 notice
        }
    }

    /** meta_event/* (lifecycle/heartbeat 等, 当前无需要处理的事件) */
    private void handleMetaEvent(JsonObject json) {
        // 忽略所有元事件
    }

    /** 在 Bukkit 主线程调用事件 */
    private void callEvent(org.bukkit.event.Event event) {
        if (plugin == null || !plugin.isEnabled()) return;
        Bukkit.getScheduler().runTask(plugin, () -> Bukkit.getPluginManager().callEvent(event));
    }

    // ==================== 工具方法 ====================

    /** 将 OneBot v11 message 段数组转换为纯文本 */
    private static String messageSegmentsToText(JsonArray segments) {
        StringBuilder sb = new StringBuilder();
        for (JsonElement seg : segments) {
            JsonObject obj = seg.getAsJsonObject();
            String type = obj.get("type").getAsString();
            JsonObject data = obj.getAsJsonObject("data");

            switch (type) {
                case "text":
                    if (data != null && data.has("text")) {
                        sb.append(data.get("text").getAsString());
                    }
                    break;
                case "at":
                    if (data != null && data.has("qq")) {
                        sb.append("@").append(data.get("qq").getAsString());
                    }
                    break;
                case "face":
                    sb.append("[表情]");
                    break;
                case "image":
                    sb.append("[图片]");
                    break;
                case "reply":
                    // 引用标记，不添加到文本
                    break;
                default:
                    sb.append("[").append(type).append("]");
                    break;
            }
        }
        return sb.toString();
    }

    private String cacheGroupName(JsonObject groupInfo) {
        if (groupInfo == null || !groupInfo.has("group_id") || !groupInfo.has("group_name")) {
            return null;
        }
        long groupId = groupInfo.get("group_id").getAsLong();
        String groupName = getString(groupInfo, "group_name", null);
        if (groupName != null) {
            groupNameCache.put(groupId, groupName);
        }
        return groupName;
    }

    private static JsonObject getDataObject(JsonObject response) {
        if (response == null || !response.has("data") || response.get("data").isJsonNull() || !response.get("data").isJsonObject()) {
            return null;
        }
        return response.getAsJsonObject("data");
    }

    private static JsonArray getDataArray(JsonObject response) {
        if (response == null || !response.has("data") || response.get("data").isJsonNull() || !response.get("data").isJsonArray()) {
            return null;
        }
        return response.getAsJsonArray("data");
    }

    private static String getString(JsonObject json, String key, String defaultValue) {
        if (json == null || !json.has(key) || json.get(key).isJsonNull()) {
            return defaultValue;
        }
        return json.get(key).getAsString();
    }

    private static long getLong(JsonObject json, String key) {
        if (json == null || !json.has(key) || json.get(key).isJsonNull()) {
            return 0L;
        }
        try {
            return json.get(key).getAsLong();
        } catch (NumberFormatException | UnsupportedOperationException e) {
            return 0L;
        }
    }

    private static String extractMessageText(JsonObject messageObject) {
        JsonElement message = messageObject.get("message");
        if (message != null && !message.isJsonNull()) {
            if (message.isJsonArray()) {
                return messageSegmentsToText(message.getAsJsonArray());
            }
            if (message.isJsonPrimitive()) {
                return stripCQCode(message.getAsString()).trim();
            }
        }

        String rawMessage = getString(messageObject, "raw_message", null);
        if (rawMessage != null) {
            return stripCQCode(rawMessage).trim();
        }
        return null;
    }

    private static long extractSenderId(JsonObject messageObject) {
        JsonObject sender = messageObject.getAsJsonObject("sender");
        long senderId = getLong(sender, "user_id");
        if (senderId > 0) {
            return senderId;
        }
        return getLong(messageObject, "user_id");
    }

    private static String stripCQCode(String message) {
        if (message == null) {
            return "";
        }
        return message.replaceAll("\\[CQ:[^]]+]", "");
    }

    private static final class QuoteReply {
        private final String message;
        private final long senderId;

        private QuoteReply(String message, long senderId) {
            this.message = message;
            this.senderId = senderId;
        }

        private static QuoteReply empty() {
            return new QuoteReply(null, 0L);
        }
    }
}
