package org.liuyi.chat.port.publisher;

import org.liuyi.chat.application.event.FriendApplicationAcceptedEvent;
import org.liuyi.chat.application.event.FriendApplicationRejectedEvent;
import org.liuyi.chat.application.event.FriendApplicationSentEvent;
import org.liuyi.chat.application.event.MessageSentEvent;

public interface Publisher {
    void publish(FriendApplicationRejectedEvent event);

    void publish(FriendApplicationSentEvent event);

    void publish(FriendApplicationAcceptedEvent event);

    void publish(MessageSentEvent event);
}



