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
public class FriendApplicationAcceptedEvent implements ApplicationEvent {
    private Instant operateTime;
    private String applicationId;
    private String fromUserId;
    private String toUserId;
    private boolean isNewFriendShip; // 是否新建了好友，如果没有新建好友，下面这些都不存在
    private String friendshipId;
    private String privateChatSessionId;
    private String applicantParticipantId;
    private String targetUserParticipantId;

    @Override
    public String eventId() {
        return "";
    }
}