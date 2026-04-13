package org.liuyi.chat.adapter.publisher;

import lombok.RequiredArgsConstructor;
import org.liuyi.chat_api.event.FriendApplicationHandledEvent;
import org.liuyi.chat_api.event.FriendApplicationSentEvent;
import org.liuyi.chat_api.event.MessageSentEvent;
import org.liuyi.chat_api.event.PrivateSessionCreatedEvent;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class EventBusImpl implements EventBus {
    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Override
    public void publish(FriendApplicationSentEvent event) {
        kafkaTemplate.send(FriendApplicationSentEvent.TOPIC, event);
    }

    @Override
    public void publish(FriendApplicationHandledEvent event) {
        kafkaTemplate.send(FriendApplicationHandledEvent.TOPIC, event);
    }

    @Override
    public void publish(MessageSentEvent infraEvent) {
        kafkaTemplate.send(MessageSentEvent.TOPIC, infraEvent);

    }

    @Override
    public void publish(PrivateSessionCreatedEvent infraEvent) {
        kafkaTemplate.send(PrivateSessionCreatedEvent.TOPIC, infraEvent);
    }
}
