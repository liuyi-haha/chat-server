package org.liuyi.chat.domain.private_chat_session;

import lombok.Getter;
import org.liuyi.common.domain.exception.DomainException;

import java.time.Instant;

/**
 * 单聊会话聚合根。
 */

/**
 * 单聊会话聚合根。
 */
@Getter
public class PrivateChatSession implements ChatSession {

    private PrivateChatSessionId id;        // 类型为 PrivateChatSessionId
    private String friendShipId;
    private Instant createTime;

    // 私有构造：强制通过工厂创建
    // ✅ 参数类型为成员类型本身（PrivateChatSessionId）
    private PrivateChatSession(PrivateChatSessionId id, String friendShipId, Instant createTime) {

        this.id = id;
        this.friendShipId = friendShipId;
        this.createTime = createTime;
    }

    /**
     * 静态工厂：创建新单聊会话。
     * 使用 PrivateChatSessionId() 无参构造自动生成合规 ID。
     *
     * @param friendShipId 好友关系ID（如 "u1-u2"）
     * @param createTime
     * @return 新的 PrivateChatSession 实例
     */
    public static PrivateChatSession create(String friendShipId, Instant createTime) {

        if (createTime == null) {
            throw new DomainException("createTime cannot be null");
        }
        PrivateChatSessionId sessionId = new PrivateChatSessionId(); //
        return new PrivateChatSession(sessionId, friendShipId, createTime);
    }

    /**
     * 静态工厂：从持久化数据重建（如数据库查询结果）。
     * 使用有参构造验证 ID 合规性。
     *
     * @param sessionId    业务ID（必须以 PS- 开头）
     * @param friendShipId 好友关系ID
     * @param createTime   创建时间
     * @return             新的 PrivateChatSession 实例
     */
    public static PrivateChatSession of(String sessionId, String friendShipId, Instant createTime) {
        if (sessionId == null) {
            throw new DomainException("id cannot be null");
        }
        if (friendShipId == null || friendShipId.isBlank()) {
            throw new DomainException("friendShipId cannot be null or blank");
        }
        if (createTime == null) {
            throw new DomainException("createTime cannot be null");
        }
        PrivateChatSessionId id = new PrivateChatSessionId(sessionId);
        return new PrivateChatSession(id, friendShipId, createTime); // ✅ 传入 PrivateChatSessionId 类型
    }

    // 实现 ChatSession 接口
    @Override
    public String getId() {
        return this.id.value(); // 通过 value() 获取 String 类型的 ID
    }

    @Override
    public ChatSessionType getType() {
        return ChatSessionType.PrivateChatSession;
    }
}