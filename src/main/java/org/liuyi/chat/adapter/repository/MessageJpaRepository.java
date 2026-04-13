package org.liuyi.chat.adapter.repository;

import org.liuyi.chat.adapter.persistence.MessageDO;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MessageJpaRepository extends JpaRepository<MessageDO, Long> {
    /**
     * 查找某个聊天会话中 seq 最大的消息
     *
     * @param sessionId 会话ID
     * @return seq最大的消息（如果不存在则返回空）
     */
    Optional<MessageDO> findTopBySessionIdOrderBySeqInSessionDesc(String sessionId);

    /**
     * 根据 businessId (messageId) 查找消息
     *
     * @param businessId 消息业务ID
     * @return 消息对象
     */
    Optional<MessageDO> findByBusinessId(String businessId);

}
