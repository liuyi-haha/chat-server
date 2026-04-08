package org.liuyi.chat.adapter.mapper;

import org.liuyi.chat.domain.friend_ship.FriendShip;
import org.liuyi.chat.adapter.persistence.FriendShipDO;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class FriendShipMapper {

    /**
     * 将领域模型 (Entity) 转换为数据模型 (DO) 列表。
     * 一个逻辑 FriendShip 会生成两条物理 FriendShipDO 记录。
     *
     * @param domainObj 领域模型实例
     * @return 数据模型实例列表 (size=2)
     */
    public static List<FriendShipDO> toDOList(FriendShip domainObj) {
        if (domainObj == null) {
            return null;
        }

        List<FriendShipDO> dos = new ArrayList<>(2);

        // 记录 1: src -> peer
        dos.add(FriendShipDO.builder()
                        .friendShipId(domainObj.getId())
                        .srcUserId(domainObj.getSrcUserId())
                        .peerUserId(domainObj.getPeerUserId())
                        .createTime(domainObj.getCreateTime())
                        .build());

        // 记录 2: peer -> src
        dos.add(FriendShipDO.builder()
                .friendShipId(domainObj.getId())
                .srcUserId(domainObj.getPeerUserId())
                .peerUserId(domainObj.getSrcUserId())
                .createTime(domainObj.getCreateTime())
                .build());

        return dos;
    }

    /**
     * 将单条数据模型 (DO) 记录转换为领域模型 (Entity)。
     * 注意：一个 DO 记录映射回一个逻辑 FriendShip 实体。
     * 由于领域模型中的 id 是规范化的，需要根据 DO 中的 src/peer 来重新生成。
     *
     * @param doObj 单条数据模型实例 (e.g., A -> B 或 B -> A)
     * @return 领域模型实例
     */
    public static FriendShip toDomain(FriendShipDO doObj) {
        if (doObj == null) return null;

        // 从 DO 中取出 src 和 peer
        String srcUserId = doObj.getSrcUserId();
        String peerUserId = doObj.getPeerUserId();
        Instant createTime = doObj.getCreateTime();

        // 重新规范化 id 来创建领域模型 (无论 DO 是 A->B 还是 B->A，领域模型的 id 应该一样)
        String friendShipId = doObj.getFriendShipId();
        return FriendShip.of(friendShipId, srcUserId, peerUserId, createTime); // srcUserId和peerUserId的顺序无所谓
    }
}