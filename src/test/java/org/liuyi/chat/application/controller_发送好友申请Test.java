package org.liuyi.chat.application;

import com.liuyi.auth.openapi.SendFriendApplication200Response;
import com.liuyi.auth.openapi.SendFriendApplicationRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.liuyi.chat.adapter.FakeEventBus;
import org.liuyi.chat.port.repository.FriendApplicationRepository;
import org.liuyi.chat.port.repository.FriendShipRepository;
import org.liuyi.chat_api.event.FriendApplicationSentEvent;
import org.liuyi.common.domain.event.Event;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;


@ActiveProfiles("test")
@SpringBootTest
@Transactional
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
public class controller_发送好友申请Test {
    @Autowired
    FriendApplicationRepository friendApplicationRepository;
    @Autowired
    private FriendShipRepository friendShipRepository;
    @Autowired
    private Application application;
    @Autowired
    private FakeEventBus eventBus;
    @Autowired
    private FakeEventBus fakeEventBus;

    @BeforeEach
    void setUp() {
        eventBus.reset();
    }

    @Test
    void 发送好友申请时_如果参数正常_响应应该符合预期_事件应该正确发布() {
        SendFriendApplicationRequest request = new SendFriendApplicationRequest();
        String fromUserId = "234567890";
        request.targetUserId("123456789").verificationMessage("申请添加你为好友").recipientRemark("小刘");
        Instant sendTime = Instant.now();
        SendFriendApplication200Response resp = application.sendFriendApplication(request, fromUserId, sendTime);

        assertTrue(resp.getSuccess());
        assertNotNull(resp.getData());
        assertNotNull(resp.getData().getFriendApplicationId());

        // 判断事件
        Event event = fakeEventBus.getEvent(FriendApplicationSentEvent.TOPIC);
        assertNotNull(event);
        FriendApplicationSentEvent actualEvent = assertInstanceOf(FriendApplicationSentEvent.class, event);
        FriendApplicationSentEvent expectedEvent = FriendApplicationSentEvent.builder()
                .fromUserId(fromUserId)
                .toUserId(request.getTargetUserId())
                .sendTime(sendTime)
                .applicationId(resp.getData().getFriendApplicationId())
                .verificationMessage(request.getVerificationMessage())
                .build();
        assertEquals(expectedEvent, actualEvent);
    }
}
