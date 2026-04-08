package org.liuyi.chat.adapter.publisher;

import lombok.RequiredArgsConstructor;
import org.liuyi.chat.application.event.FriendApplicationAcceptedEvent;
import org.liuyi.chat.port.publisher.FriendApplicationAcceptedEventPublisher;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FriendApplicationAcceptedEventPublisherAdapter implements FriendApplicationAcceptedEventPublisher {
    private final EventBus eventBus;

    @Override
    public void publish(FriendApplicationAcceptedEvent event) {

    }
}
