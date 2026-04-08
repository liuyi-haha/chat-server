package org.liuyi.chat.domain.friend_ship;

import lombok.Getter;
import org.liuyi.common.domain.object.RandomIdGenerator;
import org.liuyi.common.domain.exception.DomainException;

import java.time.Instant;

/**
 * 好友关系聚合根。
 * 规则：
 * - 双向关系：A 和 B 是好友，则 A 的好友列表中有 B，B 的好友列表中有 A。
 * - 关系一旦建立不可删除（软删除可选），仅可解除（即双向移除）。
 * - id 为业务主键（如 "srcUserId:peerUserId" 的规范顺序组合）。
 */
@Getter
public class FriendShip {

    private String id;           // 业务唯一ID，格式：min(src, peer) + ":" + max(src, peer)
    private String srcUserId;    // 关系发起方（逻辑上无先后，但用于构建唯一ID）
    private String peerUserId;   // 关系对方
    private Instant createTime;  // 创建时间

    // 私有构造，强制通过工厂创建
    private FriendShip(String id, String srcUserId, String peerUserId, Instant createTime) {
        if (id == null || id.isBlank()) {
            throw new DomainException("id cannot be null or blank");
        }
        if (srcUserId == null || srcUserId.isBlank()) {
            throw new DomainException("srcUserId cannot be null or blank");
        }
        if (peerUserId == null || peerUserId.isBlank()) {
            throw new DomainException("peerUserId cannot be null or blank");
        }
        if (createTime == null) {
            throw new DomainException("createTime cannot be null");
        }
        this.id = id;
        this.srcUserId = srcUserId;
        this.peerUserId = peerUserId;
        this.createTime = createTime;
    }

    /**
     * 静态工厂方法：创建好友关系。
     * 自动规范 ID 顺序（确保 (A,B) 和 (B,A) 生成同一个 ID）。
     *
     * @param srcUserId   用户A ID
     * @param peerUserId  用户B ID
     * @param createTime  创建时间
     * @return 新的 FriendShip 实例
     */
    public static FriendShip create(String srcUserId, String peerUserId, Instant createTime) {
        // 校验
        if (srcUserId.equals(peerUserId)) {
            throw new DomainException("Cannot be friend with oneself");
        }

        // 规范化 ID：按字典序排序，保证 (A,B) 和 (B,A) 生成相同 id
        String id;
        if (srcUserId.compareTo(peerUserId) < 0) {
            id = srcUserId + ":" + peerUserId;
        } else {
            id = peerUserId + ":" + srcUserId;
        }

        return new FriendShip(id, srcUserId, peerUserId, createTime);
    }

    /**
     * 静态工厂方法：从已有数据重建（用于从数据库加载）。
     * 注意：此方法不校验 ID 规范性（假设数据库已存储规范 ID）。
     *
     * @param id          业务ID（已规范）
     * @param srcUserId
     * @param peerUserId
     * @param createTime
     * @return FriendShip 实例
     */
    public static FriendShip of(String id, String srcUserId, String peerUserId, Instant createTime) {
        return new FriendShip(id, srcUserId, peerUserId, createTime);
    }

    // 业务行为：无（关系本身是静态的，增删由 Repository/Service 协调）
    // 例如：解除好友关系应由外部调用 Repository.delete(FriendShip) 完成
}