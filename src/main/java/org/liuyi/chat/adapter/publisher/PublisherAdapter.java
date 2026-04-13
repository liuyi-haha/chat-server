package org.liuyi.chat.adapter.publisher;

import lombok.RequiredArgsConstructor;
import org.liuyi.chat.application.event.FriendApplicationAcceptedEvent;
import org.liuyi.chat.application.event.FriendApplicationRejectedEvent;
import org.liuyi.chat.domain.message.ContentType;
import org.liuyi.chat.port.publisher.Publisher;
import org.liuyi.chat_api.event.*;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PublisherAdapter implements Publisher {
    private final EventBus eventBus;


    @Override
    public void publish(org.liuyi.chat.application.event.FriendApplicationSentEvent event) {
        FriendApplicationSentEvent kafkaEvent = FriendApplicationSentEvent.builder()
                .fromUserId(event.getFromUserId())
                .toUserId(event.getToUserId())
                .applicationId(event.getApplicationId())
                .verificationMessage(event.getVerificationMessage())
                .sendTime(event.getCreateTime())
                .build();
        eventBus.publish(kafkaEvent);
    }

    @Override
    public void publish(FriendApplicationRejectedEvent event) {
        var infraEvent = FriendApplicationHandledEvent.builder()
                .operateTime(event.getOperateTime())
                .applicationId(event.getFriendApplicationId())
                .fromUserId(event.getFromUserId())
                .toUserId(event.getToUserId())
                .resultType(HandleFriendApplicationResultType.REJECTED)
                .build();
        eventBus.publish(infraEvent);
    }

    @Override
    public void publish(FriendApplicationAcceptedEvent event) {
        var infraApplicationAcceptedEvent = FriendApplicationHandledEvent.builder()
                .operateTime(event.getOperateTime())
                .applicationId(event.getApplicationId())
                .fromUserId(event.getFromUserId())
                .toUserId(event.getToUserId())
                .resultType(HandleFriendApplicationResultType.ACCEPTED)
                .isNewFriendShip(event.isNewFriendShip())
                .friendshipId(event.getFriendshipId())
                .privateChatSessionId(event.getPrivateChatSessionId())
                .applicantParticipantId(event.getApplicantParticipantId())
                .targetUserParticipantId(event.getTargetUserParticipantId())
                .build();
        eventBus.publish(infraApplicationAcceptedEvent);


        if (event.isNewFriendShip() && event.getPrivateChatSessionId() != null) {
            var privateChatSessionCreatedEvent = PrivateSessionCreatedEvent.builder()
                    .createTime(event.getOperateTime())
                    .sessionId(event.getPrivateChatSessionId())
                    .userId1(event.getFromUserId())
                    .userId2(event.getToUserId())
                    .build();
            eventBus.publish(privateChatSessionCreatedEvent);
        }


    }

    @Override
    public void publish(org.liuyi.chat.application.event.MessageSentEvent event) {
        var infraEvent = MessageSentEvent.builder()
                .messageType(domainContentTypeToInfraMessageType(event.getContentType()))
                .sendTime(event.getSendTime())
                .sessionId(event.getSessionId())
                .messageId(event.getMessageId())
                // todo @liuyi 刷新maven
                .seqInSession(event.getSeqInSession())
                .textContent(event.getTextContent())
                .senderUserId(event.getSenderId())
                .build();
        eventBus.publish(infraEvent);
    }

    private MessageType domainContentTypeToInfraMessageType(ContentType type) {
        return switch (type) {
            case Text -> MessageType.TEXT;
            default -> throw new IllegalArgumentException("unsupported content type: " + type);
        };
    }
}
