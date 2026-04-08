package org.liuyi.chat.domain.private_chat_session;

/**
 * 聊天会话抽象接口。
 * 所有会话类型（单聊、群聊）都实现此接口。
 */
public interface ChatSession {
    String getId();          // 业务ID（如 PS-xxx 或 G-xxx）
    ChatSessionType getType();
}