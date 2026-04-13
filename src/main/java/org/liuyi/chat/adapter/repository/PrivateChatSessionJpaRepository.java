package org.liuyi.chat.adapter.repository;

import org.liuyi.chat.adapter.persistence.PrivateChatSessionDO;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PrivateChatSessionJpaRepository extends JpaRepository<PrivateChatSessionDO, Long> {
    Optional<PrivateChatSessionDO> findByBusinessId(String businessId);

    Optional<PrivateChatSessionDO> findByFriendShipId(String friendshipId);
}
