package org.liuyi.chat.adapter.persistence;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "private_chat_session")
public class PrivateChatSessionDO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id; // 数据库自增主键

    @Column(name = "business_id", unique = true, nullable = false, length = 64)
    private String businessId; // 对应领域模型的 id.value()（如 PS-xxx）

    @Column(name = "friend_ship_id", nullable = false, length = 128)
    private String friendShipId;

    @Column(name = "create_time", nullable = false)
    private Instant createTime;
}
