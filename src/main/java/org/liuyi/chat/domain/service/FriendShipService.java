package org.liuyi.chat.domain.service;

import lombok.RequiredArgsConstructor;
import org.liuyi.chat.domain.exception.AlreadyFriendException;
import org.liuyi.chat.domain.friend_ship.FriendShip;
import org.liuyi.chat.port.repository.FriendShipRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;

@RequiredArgsConstructor
@Service
public class FriendShipService {
    private final FriendShipRepository friendShipRepository;

    public String createFriendShip(String applicantUserId, String targetUserId, Instant createTime) {
        // 检查是否已经是好友关系
        friendShipRepository.findByUserIds(applicantUserId, targetUserId).ifPresent(friendShip -> {
            throw AlreadyFriendException.builder()
                    .applicantUserId(applicantUserId)
                    .targetUserId(targetUserId)
                    .build();
        });

        // 不存在则创建
        FriendShip friendShip = FriendShip.create(applicantUserId, targetUserId, createTime);
        // 保存
        friendShipRepository.save(friendShip);
        return friendShip.getId();
    }
}
