package org.liuyi.chat.adapter.persistence;
import lombok.*;
import jakarta.persistence.*;
import org.liuyi.chat.domain.participant.ParticipantRole;

import java.time.Instant;
@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "participant")
public class ParticipantDO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 自增 Long 主键
    @Column(name = "id")
    private Long id; // 数据库自增主键

    @Column(name = "business_id", nullable = false, length = 64) // 业务ID
    private String businessId;

    @Column(name = "session_id", nullable = false, length = 128)
    private String sessionId;

    @Column(name = "user_id", nullable = false, length = 64)
    private String userId;

    @Column(name = "role", nullable = false, length = 20)
    @Enumerated(EnumType.STRING)
    private ParticipantRole role;

    @Column(name = "remark", length = 20)
    private String remark;

    @Column(name = "create_time", nullable = false)
    private Instant createTime;
}