package org.liuyi.chat.domain.message;

import lombok.Getter;
import org.liuyi.common.domain.exception.DomainException;

import java.time.Instant;

/**
 * 消息聚合根。
 * 业务ID格式：{sessionId}-{seq}，例如：PS-abc123-5
 */
@Getter
public class Message {

    private String id;                     // 业务ID，如 PS-abc123-5
    private String sessionId;              // 聊天会话ID（如 PS-abc123）
    private Integer seqInChatSession;      // 会话内序号（从1开始）
    private Content content;
    private Instant sendTime;
    private String senderUserId;

    // 默认构造：包外无法访问
    Message(String id, String sessionId, Integer seqInChatSession,
                      Content content, Instant sendTime, String senderUserId) {
        this.id = id;
        this.sessionId = sessionId;
        this.seqInChatSession = seqInChatSession;
        this.content = content;
        this.sendTime = sendTime;
        this.senderUserId = senderUserId;
    }
}