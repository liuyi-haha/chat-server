package org.liuyi.chat.application.test_fixture;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.liuyi.chat.domain.friend_application.FriendApplication;
import org.liuyi.chat.domain.friend_ship.FriendShip;
import org.liuyi.chat.domain.participant.Participant;
import org.liuyi.chat.domain.participant.ParticipantRole;
import org.liuyi.chat.domain.private_chat_session.PrivateChatSession;
import org.liuyi.chat.port.repository.FriendApplicationRepository;
import org.liuyi.chat.port.repository.FriendShipRepository;
import org.liuyi.chat.port.repository.ParticipantRepository;
import org.liuyi.chat.port.repository.PrivateChatSessionRepository;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;


@Component
@RequiredArgsConstructor
public class RelationTestFixture {
    private final FriendApplicationRepository friendApplicationRepository;
    private final FriendShipRepository friendShipRepository;
    private final PrivateChatSessionRepository privateChatSessionRepository;
    private final ParticipantRepository participantRepository;
    private final String userId1 = "123456789";
    private final String userId2 = "234567890";

    // 让两个用户建立建立好友关系
    public FriendDTO makeFriends() {
        var crateTime = Instant.now();
        // 1.建立申请
        var application = FriendApplication.create(userId1, userId2, crateTime, "用户1给用户2的备注", "验证消息");
        application.accept(userId2);
        friendApplicationRepository.save(application);

        // 建立聊天好友关系
        var friendShip = FriendShip.create(userId1, userId2, crateTime);
        friendShipRepository.save(friendShip);

        // 建立单聊会话
        var privateChatSession = PrivateChatSession.create("123456789-234567890", crateTime);
        privateChatSessionRepository.save(privateChatSession);

        // 建立单聊会话的成员
        var p1 = Participant.create(privateChatSession.getId(), userId1, ParticipantRole.PrivateMember, "用户2给用户1的备注");
        var p2 = Participant.create(privateChatSession.getId(), userId2, ParticipantRole.PrivateMember, "用户1给用户2的备注");
        participantRepository.addAll(List.of(p1, p2));

        return FriendDTO.builder()
                .userId1(userId1)
                .userId2(userId2)
                .privateChatSessionId(privateChatSession.getId())
                .friendShipId(friendShip.getId())
                .build();

    }

    @Builder
    @Data
    public static class FriendDTO {
        private String userId1;
        private String userId2;
        private String privateChatSessionId;
        private String friendShipId;
    }


}