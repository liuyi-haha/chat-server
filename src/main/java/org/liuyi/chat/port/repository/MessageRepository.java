package org.liuyi.chat.port.repository;

import org.liuyi.chat.domain.message.Message;

import java.util.Optional;

public interface MessageRepository {
    Integer findMaxSeqInSession(String sessionId);

    // 通过id查询
    Optional<Message> findById(String messageId);

    void save(Message message);
}
