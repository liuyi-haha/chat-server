package org.liuyi.chat.adapter.repository;

import org.liuyi.chat.adapter.persistence.FriendShipDO;
import org.liuyi.chat.domain.friend_ship.FriendShip;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FriendShipJpaRepository extends JpaRepository<FriendShipDO, Long> {

    Optional<FriendShip> findByFriendShipId(String friendApplicationId);

    Optional<FriendShipDO> findBySrcUserIdAndPeerUserId(String srcUserId, String peerUserId);
}
