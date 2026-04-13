package org.liuyi.chat.application.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.liuyi.chat.domain.message.ContentType;
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
    private String textContent;

    @Override
    public String eventId() {
        return "";
    }


}
