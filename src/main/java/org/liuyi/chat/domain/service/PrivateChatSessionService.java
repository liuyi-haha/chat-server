package org.liuyi.chat.domain.service;

import lombok.RequiredArgsConstructor;
import org.liuyi.chat.domain.private_chat_session.PrivateChatSession;
import org.liuyi.chat.port.repository.PrivateChatSessionRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;

@RequiredArgsConstructor
@Service
public class PrivateChatSessionService {
    private final PrivateChatSessionRepository privateChatSessionRepository;

    public String createPrivateChatSession(String friendShipId, Instant operateTime) {
        PrivateChatSession session = PrivateChatSession.create(friendShipId, operateTime);
        privateChatSessionRepository.save(session);
        return session.getId();
    }
}
