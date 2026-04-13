package org.liuyi.chat.adapter.repository;

import lombok.RequiredArgsConstructor;
import org.liuyi.chat.adapter.mapper.PrivateChatSessionMapper;
import org.liuyi.chat.adapter.persistence.PrivateChatSessionDO;
import org.liuyi.chat.domain.private_chat_session.PrivateChatSession;
import org.liuyi.chat.port.repository.PrivateChatSessionRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@RequiredArgsConstructor
@Repository
public class PrivateChatSessionRepositoryAdapter implements PrivateChatSessionRepository {
    private final PrivateChatSessionJpaRepository jpaRepository;

    @Override
    public void save(PrivateChatSession session) {
        PrivateChatSessionDO newDO = PrivateChatSessionMapper.toDO(session);
        jpaRepository.findByBusinessId(session.getId()).ifPresent(existedDO -> {
            newDO.setId(existedDO.getId());
        });
        jpaRepository.save(newDO);
    }

    @Override
    public Optional<PrivateChatSession> findByFriendShipId(String friendshipId) {
        return jpaRepository.findByFriendShipId(friendshipId).map(PrivateChatSessionMapper::toDomain);
    }

    @Override
    public Optional<PrivateChatSession> findById(String privateSessionId) {
        return jpaRepository.findByBusinessId(privateSessionId).map(PrivateChatSessionMapper::toDomain);
    }
}
