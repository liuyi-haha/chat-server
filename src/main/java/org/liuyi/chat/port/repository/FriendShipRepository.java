package org.liuyi.chat.port.repository;

import org.liuyi.chat.domain.friend_ship.FriendShip;

import java.util.Optional;

public interface FriendShipRepository {

    /**
     * 根据逻辑ID查找好友关系。
     * ID 格式为 "userId1:userId2" (规范化)
     */
    Optional<FriendShip> findById(String friendShipId);
    Optional<FriendShip> findByUserIds(String userId1, String userId2);

    void save(FriendShip friendShip);
}
