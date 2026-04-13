package org.liuyi.chat.port.repository;

import org.liuyi.chat.domain.private_chat_session.PrivateChatSession;

import java.util.Optional;

public interface PrivateChatSessionRepository {
    void save(PrivateChatSession session);

    Optional<PrivateChatSession> findByFriendShipId(String friendshipId);

    Optional<PrivateChatSession> findById(String privateSessionId);
}
