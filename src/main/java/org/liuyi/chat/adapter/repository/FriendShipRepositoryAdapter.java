package org.liuyi.chat.adapter.repository;

import lombok.RequiredArgsConstructor;
import org.liuyi.chat.adapter.mapper.FriendShipMapper;
import org.liuyi.chat.adapter.persistence.FriendShipDO;
import org.liuyi.chat.domain.friend_ship.FriendShip;
import org.liuyi.chat.port.repository.FriendShipRepository;
import org.liuyi.common.domain.exception.DomainException;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Repository
public class FriendShipRepositoryAdapter implements FriendShipRepository {
    private final FriendShipJpaRepository friendShipJpaRepository;

    @Override
    public Optional<FriendShip> findById(String friendShipId) {
        return friendShipJpaRepository.findByFriendShipId(friendShipId);
    }

    @Override
    public Optional<FriendShip> findByUserIds(String userId1, String userId2) {
        Optional<FriendShipDO> friendShipDOOpt = friendShipJpaRepository.findBySrcUserIdAndPeerUserId(userId1, userId2);
        return friendShipDOOpt.map(FriendShipMapper::toDomain);
    }

    @Override
    public void save(FriendShip friendShip) {
        // 查找两条记录
        Optional<FriendShipDO> do1 = friendShipJpaRepository
                .findBySrcUserIdAndPeerUserId(friendShip.getSrcUserId(), friendShip.getPeerUserId());
        Optional<FriendShipDO> do2 = friendShipJpaRepository
                .findBySrcUserIdAndPeerUserId(friendShip.getPeerUserId(), friendShip.getSrcUserId());

        // 检查一致性：要么都存在，要么都不存在
        if (do1.isPresent() != do2.isPresent()) {
            throw new DomainException("数据不一致：只有单向好友关系");
        }

        List<FriendShipDO> dos = FriendShipMapper.toDOList(friendShip);

        if (do1.isEmpty() && do2.isEmpty()) {
            // 都不存在：新增
            friendShipJpaRepository.saveAll(dos);
        } else {
            // 都存在：更新，设置主键
            dos.get(0).setId(do1.get().getId());
            dos.get(1).setId(do2.get().getId());
            friendShipJpaRepository.saveAll(dos);
        }
    }

}
