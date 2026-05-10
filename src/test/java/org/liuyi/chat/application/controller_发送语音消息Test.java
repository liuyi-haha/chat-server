package org.liuyi.chat.application;

import com.liuyi.auth.openapi.SendFileMessageResponse;
import com.liuyi.auth.openapi.SendMessageResponseData;
import com.liuyi.auth.openapi.SendSpeechMessageRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.liuyi.chat.adapter.FakeEventBus;
import org.liuyi.chat.application.test_fixture.RelationTestFixture;
import org.liuyi.chat.domain.message.MessageFactory;
import org.liuyi.chat.port.repository.MessageRepository;
import org.liuyi.chat_api.event.MessageSentEvent;
import org.liuyi.chat_api.event.MessageType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.ZoneOffset;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;


@ActiveProfiles("test")
@SpringBootTest
@Transactional
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
public class controller_发送语音消息Test {
    @Autowired
    MessageRepository messageRepository;
    @Autowired
    RelationTestFixture relationTestFixture;
    @Autowired
    private Application application;
    @Autowired
    private FakeEventBus eventBus;

    private RelationTestFixture.FriendDTO friendDTO;

    private void prepareTestFixture() {

        friendDTO = relationTestFixture.makeFriends();

    }

    @BeforeEach
    void setUp() {
        prepareTestFixture();
        eventBus.reset();
    }

    @Test
    void 发送语音消息_如果参数正常_响应应该符合预期_事件应该正确发布_仓储中的消息应该符合预期() { // 领域服务中采用先存后查的方式，这样就能把存储和查找都验证了
        SendSpeechMessageRequest sendSpeechMessageRequest = new SendSpeechMessageRequest();
        String fileId = "mock-file-id";
        Integer durationSeconds = 10;
        sendSpeechMessageRequest.fileId(fileId).durationSeconds(durationSeconds);

        Instant sendTime = Instant.now();
        SendFileMessageResponse resp = application.sendSpeechMessage(friendDTO.getUserId1(), friendDTO.getPrivateChatSessionId(), sendSpeechMessageRequest, sendTime);
        // 1.验证resp字段
        SendMessageResponseData expectedRespData = new SendMessageResponseData();
        expectedRespData.sendTime(sendTime.atOffset(ZoneOffset.UTC)).sequence(0L);
        SendFileMessageResponse expectedResp = new SendFileMessageResponse();
        expectedResp.success(true).data(expectedRespData);

        assertNotNull(resp.getData());
        assertNotNull(resp.getData().getMessageId());
        assertThat(resp).usingRecursiveComparison().ignoringFields("data.messageId").isEqualTo(expectedResp);

        // 2.验证事件字段
        var event = assertInstanceOf(MessageSentEvent.class, eventBus.getEvent(MessageSentEvent.TOPIC));
        var expectedEvent = MessageSentEvent.builder()
                .messageType(MessageType.SPEECH)
                .sendTime(sendTime)
                .sessionId(friendDTO.getPrivateChatSessionId())
                .seqInSession(0)
                .messageId(resp.getData().getMessageId())
                .senderUserId(friendDTO.getUserId1())
                .fileId(fileId)
                .speechDurationSeconds(durationSeconds)
                .build();
        assertEquals(expectedEvent, event);

        // 3.仓储中的消息应该符合预期
        var expectedMessage = MessageFactory.ofSpeechMessage("not-important-id", friendDTO.getPrivateChatSessionId(), 0, sendTime, friendDTO.getUserId1(), fileId, durationSeconds);
        var actualMessage = assertThat(messageRepository.findById(event.getMessageId())).isPresent().get().actual();
        assertThat(actualMessage).usingRecursiveComparison().ignoringFields("id").isEqualTo(expectedMessage);


        // 用户2再发送一条消息给用户1，验证seq字段是否符合预期
        resp = application.sendSpeechMessage(friendDTO.getUserId2(), friendDTO.getPrivateChatSessionId(), sendSpeechMessageRequest, sendTime);
        assertNotNull(resp.getData());
        assertEquals(1L, resp.getData().getSequence());
    }
}