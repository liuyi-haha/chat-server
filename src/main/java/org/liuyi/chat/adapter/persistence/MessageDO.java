package org.liuyi.chat.adapter.persistence;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.liuyi.chat.domain.message.DocumentType;

import java.time.Instant;

@Entity
@Table(name = "message",
        indexes = {
                @Index(name = "idx_session_seq", columnList = "session_id, seq_in_session", unique = true)
        })
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MessageDO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 数据库主键

    @Column(name = "business_id", nullable = false, unique = true, length = 128)
    private String businessId; // 如 PS-abc123-5

    @Column(name = "session_id", nullable = false, length = 64)
    private String sessionId; // PS-abc123

    @Column(name = "seq_in_session", nullable = false)
    private Integer seqInSession;

    @Column(name = "content_type", nullable = false, length = 16)
    private String contentType; // "Text"

    @Column(name = "send_time", nullable = false)
    private Instant sendTime;

    @Column(name = "sender_user_id", nullable = false, length = 64)
    private String senderUserId;

    @Column(name = "text", columnDefinition = "TEXT")
    private String text; // TextContent 的内容


    // 文件类消息的文件id
    @Column(name = "file_id", length = 128)
    private String fileId;

    // 图片消息字段
    @Column(name = "image_width")
    private Integer imageWidth; // 图片宽度

    @Column(name = "image_height")
    private Integer imageHeight; // 图片高度

    // 语音消息字段
    @Column(name = "duration_seconds")
    private Integer durationSeconds; // 语音时长，单位秒

    // 文档消息字段
    @Column(name = "document_name", columnDefinition = "TEXT")
    private String documentName; // 文档原始名称

    @Column(name = "document_bytes")
    private Long documentBytes; // 文档体积大小，单位B

    @Enumerated(EnumType.STRING)
    @Column(name = "document_type", length = 16)
    private DocumentType documentType; // 文档类型: PDF, WORD, TXT

}
