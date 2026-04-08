package org.liuyi.chat.port.repository;

import org.liuyi.chat.domain.private_chat_session.PrivateChatSession;

public interface PrivateChatSessionRepository {
    void save(PrivateChatSession session);
}
