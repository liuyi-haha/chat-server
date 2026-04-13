package org.liuyi.chat.application.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.liuyi.common.application.ApplicationEvent;

import java.time.Instant;

@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class FriendApplicationRejectedEvent implements ApplicationEvent {
    private String friendApplicationId;
    private String fromUserId;
    private String toUserId;
    private Instant operateTime;

    @Override
    public String eventId() {
        return "";
    }
}
