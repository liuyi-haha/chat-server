package org.liuyi.chat.adapter.persistence;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "friend_ship", indexes = {
        @Index(name = "uk_src_peer_user", columnList = "srcUserId, peerUserId", unique = true),
        @Index(name = "idx_friend_ship_id", columnList = "friendShipId")
})
public class FriendShipDO {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 数据库主键
    @Column(nullable = false, length = 64) // 注意，这里不应该是unique
    private String friendShipId;
    @Column(nullable = false)
    private Instant createTime;
    @Column(nullable = false, length = 64)
    private String srcUserId;
    @Column(nullable = false, length = 64)
    private String peerUserId;
}