package org.liuyi.chat.adapter.mapper;
import lombok.RequiredArgsConstructor;
import org.liuyi.chat.adapter.persistence.MessageDO;
import org.liuyi.chat.domain.message.*;

@RequiredArgsConstructor
public class MessageMapper {
    private final MessageFactory messageFactory;
    public static Message toDomain(MessageDO doObj) {
        if (doObj == null) return null;

        Content content;
        if (ContentType.Text.name().equals(doObj.getContentType())) {
            if (doObj.getText() == null || doObj.getText().isBlank()) {
                throw new IllegalStateException("Text content is required for TEXT type but was null/blank in DB.");
            }
            content = new TextContent(doObj.getText());
        } else {
            // TODO: 处理其他 Content 类型，如 FileContent, SpeechContent 等
            // 暂抛异常或返回 null，取决于具体策略
            throw new UnsupportedOperationException("Unsupported content type: " + doObj.getContentType());
        }

        return MessageFactory.ofTextMessage(
                doObj.getBusinessId(),
                doObj.getSessionId(),
                doObj.getSeqInSession(),
                doObj.getText(),
                doObj.getSendTime(),
                doObj.getSenderUserId()
        );
    }

    public static MessageDO toDO(Message domainObj) {
        if (domainObj == null) return null;

        String text = null;
        // ✅ 修正：根据 domainObj.getContent() 的类型决定如何填充 DO
        if (domainObj.getContent() instanceof TextContent) {
            text = ((TextContent) domainObj.getContent()).getText();
        } else {
            // TODO: 处理其他 Content 类型，填充相应字段
            // 当前仅处理 TextContent，其他类型 text 为 null
        }

        return MessageDO.builder()
                .businessId(domainObj.getId())
                .sessionId(domainObj.getSessionId())
                .seqInSession(domainObj.getSeqInChatSession())
                .contentType(domainObj.getContent().getType().name())
                .text(text)
                .sendTime(domainObj.getSendTime())
                .senderUserId(domainObj.getSenderUserId())
                .build();
    }
}