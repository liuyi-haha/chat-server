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
            return MessageFactory.ofTextMessage(
                    doObj.getBusinessId(),
                    doObj.getSessionId(),
                    doObj.getSeqInSession(),
                    doObj.getText(),
                    doObj.getSendTime(),
                    doObj.getSenderUserId()
            );

        } else if (ContentType.Image.name().equals(doObj.getContentType())) {
            return MessageFactory.ofImageMessage(
                    doObj.getBusinessId(),
                    doObj.getSessionId(),
                    doObj.getSeqInSession(),
                    doObj.getSendTime(),
                    doObj.getSenderUserId(),
                    doObj.getFileId(),
                    doObj.getImageWidth(),
                    doObj.getImageHeight()
            );
        } else if (ContentType.Speech.name().equals(doObj.getContentType())) {
            return MessageFactory.ofSpeechMessage(
                    doObj.getBusinessId(),
                    doObj.getSessionId(),
                    doObj.getSeqInSession(),
                    doObj.getSendTime(),
                    doObj.getSenderUserId(),
                    doObj.getFileId(),
                    doObj.getDurationSeconds()
            );
        } else if (ContentType.Document.name().equals(doObj.getContentType())) {
            return MessageFactory.ofDocumentMessage(
                    doObj.getBusinessId(),
                    doObj.getSessionId(),
                    doObj.getSeqInSession(),
                    doObj.getSendTime(),
                    doObj.getSenderUserId(),
                    doObj.getFileId(),
                    doObj.getDocumentName(),
                    doObj.getDocumentBytes(),
                    doObj.getDocumentType()  // 直接传入枚举
            );
        } else {
            throw new UnsupportedOperationException("Unsupported content type: " + doObj.getContentType());
        }


    }

    public static MessageDO toDO(Message domainObj) {
        if (domainObj == null) return null;


        if (domainObj.getContent() instanceof TextContent textContent) {
            return MessageDO.builder()
                    .businessId(domainObj.getId())
                    .sessionId(domainObj.getSessionId())
                    .seqInSession(domainObj.getSeqInChatSession())
                    .contentType(textContent.getType().name())
                    .sendTime(domainObj.getSendTime())
                    .senderUserId(domainObj.getSenderUserId())
                    .text(textContent.text())
                    .build();
        } else if (domainObj.getContent() instanceof ImageContent imageContent) {
            return MessageDO.builder()
                    .businessId(domainObj.getId())
                    .sessionId(domainObj.getSessionId())
                    .seqInSession(domainObj.getSeqInChatSession())
                    .contentType(imageContent.getType().name())
                    .sendTime(domainObj.getSendTime())
                    .senderUserId(domainObj.getSenderUserId())
                    .fileId(imageContent.fileId())
                    .imageWidth(imageContent.size().width())
                    .imageHeight(imageContent.size().height())
                    .build();
        } else if (domainObj.getContent() instanceof SpeechContent speechContent) {
            return MessageDO.builder()
                    .businessId(domainObj.getId())
                    .sessionId(domainObj.getSessionId())
                    .seqInSession(domainObj.getSeqInChatSession())
                    .contentType(speechContent.getType().name())
                    .sendTime(domainObj.getSendTime())
                    .senderUserId(domainObj.getSenderUserId())
                    .fileId(speechContent.fileId())
                    .durationSeconds(speechContent.durationSeconds())
                    .build();
        } else if (domainObj.getContent() instanceof DocumentContent documentContent) {
            return MessageDO.builder()
                    .businessId(domainObj.getId())
                    .sessionId(domainObj.getSessionId())
                    .seqInSession(domainObj.getSeqInChatSession())
                    .contentType(documentContent.getType().name())
                    .sendTime(domainObj.getSendTime())
                    .senderUserId(domainObj.getSenderUserId())
                    .fileId(documentContent.fileId())
                    .documentName(documentContent.documentName())
                    .documentBytes(documentContent.documentSize().toBytes())
                    .documentType(documentContent.type())
                    .build();
        } else {
            throw new UnsupportedOperationException("Unsupported content type: " + domainObj.getContent().getType());
        }


    }
}