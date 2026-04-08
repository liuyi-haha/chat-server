package org.liuyi.chat.adapter.persistence;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity
@Table(name = "message")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MessageDO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 数据库主键

    @Column(name = "business_id", nullable = false, length = 128)
    private String businessId; // 如 PS-abc123-5

    @Column(name = "session_id", nullable = false, length = 64)
    private String sessionId; // PS-abc123

    @Column(name = "seq_in_session", nullable = false)
    private Integer seqInSession; // 5

    @Column(name = "content_type", nullable = false, length = 16)
    private String contentType; // "Text"

    // ✅ 修正：字段名为 "text"，允许为空
    @Column(name = "text", nullable = true, columnDefinition = "TEXT")
    private String text; // TextContent 的内容

    @Column(name = "send_time", nullable = false)
    private Instant sendTime;

    @Column(name = "sender_user_id", nullable = false, length = 64)
    private String senderUserId;
}
