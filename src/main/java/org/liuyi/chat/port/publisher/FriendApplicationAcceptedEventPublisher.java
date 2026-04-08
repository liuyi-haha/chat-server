package org.liuyi.chat.port.publisher;

import org.liuyi.chat.application.event.FriendApplicationAcceptedEvent;

public interface FriendApplicationAcceptedEventPublisher {
    void publish(FriendApplicationAcceptedEvent event);
}
