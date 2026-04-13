package org.liuyi.chat.adapter.repository;

import lombok.RequiredArgsConstructor;
import org.liuyi.chat.adapter.mapper.MessageMapper;
import org.liuyi.chat.adapter.persistence.MessageDO;
import org.liuyi.chat.domain.message.Message;
import org.liuyi.chat.port.repository.MessageRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class MessageRepositoryAdapter implements MessageRepository {
    private final MessageJpaRepository jpaRepository;

    // 如果返回-1，表示当前session没有消息
    @Override
    public Integer findMaxSeqInSession(String sessionId) {
        return jpaRepository.findTopBySessionIdOrderBySeqInSessionDesc(sessionId)
                .map(MessageDO::getSeqInSession)
                .orElse(-1);
    }

    @Override
    public Optional<Message> findById(String messageId) {
        return jpaRepository.findByBusinessId(messageId).map(MessageMapper::toDomain);
    }

    @Override
    public void save(Message message) {
        MessageDO newDO = MessageMapper.toDO(message);
        jpaRepository.findByBusinessId(message.getId()).ifPresent(existedDO -> {
            newDO.setId(existedDO.getId());
        });
        jpaRepository.save(newDO);
        jpaRepository.flush(); // 强制落盘
    }
}
