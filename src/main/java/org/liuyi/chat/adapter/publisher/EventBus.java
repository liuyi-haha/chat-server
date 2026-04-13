package org.liuyi.chat.adapter.publisher;


import org.liuyi.chat_api.event.FriendApplicationHandledEvent;
import org.liuyi.chat_api.event.FriendApplicationSentEvent;
import org.liuyi.chat_api.event.MessageSentEvent;
import org.liuyi.chat_api.event.PrivateSessionCreatedEvent;

public interface EventBus {
    void publish(FriendApplicationSentEvent event);

    void publish(FriendApplicationHandledEvent event);

    void publish(MessageSentEvent infraEvent);

    void publish(PrivateSessionCreatedEvent infraEvent);
}
