package org.liuyi.chat.application.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.liuyi.chat.domain.message.ContentType;
import org.liuyi.chat.domain.message.DocumentType;
import org.liuyi.common.application.ApplicationEvent;

import java.time.Instant;

@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class MessageSentEvent implements ApplicationEvent {
    private Instant sendTime;
    private String messageId;
    private String sessionId;
    private Integer seqInSession;
    private String senderId;
    private ContentType contentType;
    // 文本消息的字段
    private String textContent;
    // 文件类型的消息的文件id
    private String fileId;
    // 图片消息的字段
    private Integer imageWidth;
    private Integer imageHeight;
    // 语音消息字段
    private Integer speechDurationSeconds;
    // 文档消息字段
    private String documentName;
    private Long documentBytes;
    private DocumentType documentType;

    @Override
    public String eventId() {
        return "";
    }


}
