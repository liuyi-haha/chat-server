package org.liuyi.chat.application.event;

import org.liuyi.common.application.ApplicationEvent;

import java.time.Instant;

public class FriendApplicationRejected implements ApplicationEvent {
    private String friendApplicationId;
    private String fromUserId;
    private String toUserId;
    private Instant operateTime;

    @Override
    public String eventId() {
        return "";
    }
}
