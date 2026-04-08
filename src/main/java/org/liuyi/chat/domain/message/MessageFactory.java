package org.liuyi.chat.domain.message;

import lombok.RequiredArgsConstructor;
import org.liuyi.chat.port.repository.MessageRepository;
import org.liuyi.common.domain.exception.DomainException;

import java.time.Instant;

@RequiredArgsConstructor
public class MessageFactory {

    private final MessageRepository messageRepository;

    /**
     * 创建文本消息。
     *
     * @param sessionId     聊天会话ID（如 PS-abc123）
     * @param senderUserId  发送者用户ID
     * @param text          文本内容（长度 [1,1000]）
     * @param sendTime      发送时间
     * @return 新的 Message 实例
     */
    public Message createTextMessage(
            String sessionId,
            String senderUserId,
            String text,
            Instant sendTime) {

        // 1. 校验参数（基础）
        if (sessionId == null || sessionId.isBlank()) {
            throw new DomainException("sessionId cannot be null or blank");
        }
        if (senderUserId == null || senderUserId.isBlank()) {
            throw new DomainException("senderUserId cannot be null or blank");
        }
        if (sendTime == null) {
            throw new DomainException("sendTime cannot be null");
        }

        // 2. 获取当前会话最大消息序号（由 Repository 提供）
        Integer maxSeq = messageRepository.findMaxSeqInSession(sessionId);
        Integer nextSeq = (maxSeq == null) ? 0 : maxSeq + 1;

        // 3. 构建业务ID：{sessionId}-{nextSeq}
        String messageId = sessionId + "-" + nextSeq;

        // 4. 创建内容对象（触发 TextContent 内部校验）
        TextContent content = new TextContent(text);

        // 5. 创建 Message（触发其构造校验）
        return new Message(messageId, sessionId, nextSeq, content, sendTime, senderUserId);
    }

    public static Message ofTextMessage(String id, String sessionId, Integer seqInChatSession,
                    String text, Instant sendTime, String senderUserId) {

        if (id == null || id.isBlank()) {
            throw new DomainException("message id cannot be null or blank");
        }
        if (sessionId == null || sessionId.isBlank()) {
            throw new DomainException("sessionId cannot be null or blank");
        }
        if (seqInChatSession == null || seqInChatSession < 1) {
            throw new DomainException("seqInChatSession must be >= 1");
        }
        if (text == null) {
            throw new DomainException("content cannot be null");
        }
        if (sendTime == null) {
            throw new DomainException("sendTime cannot be null");
        }
        if (senderUserId == null || senderUserId.isBlank()) {
            throw new DomainException("senderUserId cannot be null or blank");
        }

        return new Message(id, sessionId, seqInChatSession, new TextContent(text), sendTime, senderUserId);
    }
}