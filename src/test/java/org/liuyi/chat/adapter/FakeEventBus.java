package org.liuyi.chat.adapter;

import org.liuyi.chat.adapter.publisher.EventBus;
import org.liuyi.chat_api.event.FriendApplicationHandledEvent;
import org.liuyi.chat_api.event.FriendApplicationSentEvent;
import org.liuyi.chat_api.event.MessageSentEvent;
import org.liuyi.chat_api.event.PrivateSessionCreatedEvent;
import org.liuyi.common.domain.event.Event;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.HashMap;

@Component
@Profile("test")
@Primary
public class FakeEventBus implements EventBus {
    private final HashMap<String, Event> events = new HashMap<>();

    public Event getEvent(String topic) {
        return events.get(topic);
    }

    public void reset() {
        events.clear();
    }

    @Override
    public void publish(FriendApplicationSentEvent event) {
        events.put(FriendApplicationSentEvent.TOPIC, event);
    }

    @Override
    public void publish(FriendApplicationHandledEvent event) {
        events.put(FriendApplicationHandledEvent.TOPIC, event);
    }

    @Override
    public void publish(MessageSentEvent infraEvent) {
        events.put(MessageSentEvent.TOPIC, infraEvent);
    }

    @Override
    public void publish(PrivateSessionCreatedEvent infraEvent) {
        events.put(PrivateSessionCreatedEvent.TOPIC, infraEvent);
    }
}
