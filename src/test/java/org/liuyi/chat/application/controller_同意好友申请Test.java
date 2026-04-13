package org.liuyi.chat.application;

import com.liuyi.auth.openapi.AcceptFriendApplication200Response;
import com.liuyi.auth.openapi.AcceptFriendApplication200ResponseData;
import com.liuyi.auth.openapi.AcceptFriendApplicationRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.liuyi.chat.adapter.FakeEventBus;
import org.liuyi.chat.domain.friend_application.FriendApplication;
import org.liuyi.chat.domain.friend_application.Status;
import org.liuyi.chat.domain.friend_ship.FriendShip;
import org.liuyi.chat.domain.participant.Participant;
import org.liuyi.chat.domain.participant.ParticipantRole;
import org.liuyi.chat.port.repository.FriendApplicationRepository;
import org.liuyi.chat.port.repository.FriendShipRepository;
import org.liuyi.chat.port.repository.ParticipantRepository;
import org.liuyi.chat.port.repository.PrivateChatSessionRepository;
import org.liuyi.chat_api.event.FriendApplicationHandledEvent;
import org.liuyi.chat_api.event.HandleFriendApplicationResultType;
import org.liuyi.chat_api.event.PrivateSessionCreatedEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;


@ActiveProfiles("test")
@SpringBootTest
@Transactional
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
public class controller_同意好友申请Test {
    @Autowired
    FriendApplicationRepository friendApplicationRepository;
    @Autowired
    private FriendShipRepository friendShipRepository;
    @Autowired
    private PrivateChatSessionRepository chatSessionRepository;
    @Autowired
    private ParticipantRepository participantRepository;
    @Autowired
    private Application application;
    @Autowired
    private FakeEventBus fakeEventBus;
    private FriendApplication commonFriendApplication;

    @BeforeEach
    void setUp() {
        commonFriendApplication = FriendApplication.create("123456789", "234567890", Instant.now(), "小刘", "申请添加你为好友");
        friendApplicationRepository.save(commonFriendApplication);
        fakeEventBus.reset();
    }

    @Test
    void 同意好友申请时_如果参数正常并且之前没有好友关系_好友申请状态应该被改为已同意_好友关系应该建立_单聊会话应该建立_单聊会话成员应该建立_响应应该符合预期_事件应该被正确发送() {
        String operatorUserId = commonFriendApplication.getTargetUserId();
        String applicationId = commonFriendApplication.getId();
        String remark = "给好友的备注";
        Instant operateTime = Instant.now();
        var req = new AcceptFriendApplicationRequest();
        req.recipientRemark(remark);
        AcceptFriendApplication200Response resp = application.acceptFriendApplication(operatorUserId, applicationId, operateTime, req);

        // 1.好友申请状态应该被改变
        FriendApplication updatedFriendApplication = assertThat(friendApplicationRepository.findById(applicationId)).isPresent().get().actual();
        assertEquals(Status.ACCEPTED, updatedFriendApplication.getStatus());

        // 2.好友关系应该建立
        var friendShip = assertThat(friendShipRepository.findByUserIds(updatedFriendApplication.getApplicantUserId(), updatedFriendApplication.getTargetUserId())).isPresent().get().actual();
        assertNotNull(friendShip.getId());
        var expectedFriendShip = FriendShip.of(friendShip.getId(), updatedFriendApplication.getApplicantUserId(), updatedFriendApplication.getTargetUserId(), operateTime);
        assertThat(friendShip).usingRecursiveComparison().isEqualTo(expectedFriendShip);

        // 3. 单聊会话应该建立
        var session = assertThat(chatSessionRepository.findByFriendShipId(friendShip.getId())).isPresent().get().actual();
        assertNotNull(session.getId());

        // 4.成员应该建立
        List<Participant> participants = participantRepository.findByChatSessionId(session.getId());
        assertEquals(2, participants.size());
        var participant1 = participants.get(0);
        var participant2 = participants.get(1);
        assertNotNull(participant1.getId());
        assertNotNull(participant2.getId());

        List<Participant> expectedParticipants = List.of(
                Participant.of("not important", session.getId(), updatedFriendApplication.getApplicantUserId(), ParticipantRole.PrivateMember, remark, operateTime),
                Participant.of("not important", session.getId(), updatedFriendApplication.getTargetUserId(), ParticipantRole.PrivateMember, updatedFriendApplication.getRemark().remark(), operateTime)
        );

        assertThat(participants).usingRecursiveComparison()
                .ignoringFields("id", "createTime")
                .ignoringCollectionOrder()
                .isEqualTo(expectedParticipants);


        // 5.响应应该符合预期
        String applicantUserId = updatedFriendApplication.getApplicantUserId();
        String targetUserId = updatedFriendApplication.getTargetUserId();

        var expectedData = new AcceptFriendApplication200ResponseData();
        var expectedTargetParticipantId = participant1.getUserId().equals(targetUserId) ? participant1.getId() : participant2.getId();
        var expectedApplicantParticipantId = participant1.getUserId().equals(applicantUserId) ? participant1.getId() : participant2.getId();

        expectedData.createFriendShip(true)
                .friendshipId(friendShip.getId())
                .privateChatSessionId(session.getId())
                .applicantParticipantId(expectedApplicantParticipantId)
                .targetUserParticipantId(expectedTargetParticipantId);
        var expectedResponse = new AcceptFriendApplication200Response();
        expectedResponse.success(true)
                .data(expectedData);
        assertEquals(expectedResponse, resp);


        // 验证两个事件被发布
        var event = fakeEventBus.getEvent(FriendApplicationHandledEvent.TOPIC);
        assertNotNull(event);
        var actualEvent = assertInstanceOf(FriendApplicationHandledEvent.class, event);
        var expectedEvent = FriendApplicationHandledEvent.builder()
                .operateTime(operateTime)
                .applicationId(applicationId)
                .fromUserId(commonFriendApplication.getApplicantUserId())
                .toUserId(commonFriendApplication.getTargetUserId())
                .resultType(HandleFriendApplicationResultType.ACCEPTED)
                .isNewFriendShip(true)
                .friendshipId(friendShip.getId())
                .privateChatSessionId(session.getId())
                .applicantParticipantId(expectedApplicantParticipantId)
                .targetUserParticipantId(expectedTargetParticipantId)
                .build();
        assertEquals(expectedEvent, actualEvent);

        var privateSessionCreatedEvent = fakeEventBus.getEvent(PrivateSessionCreatedEvent.TOPIC);
        var actualPrivateSessionCreatedEvent = assertInstanceOf(PrivateSessionCreatedEvent.class, privateSessionCreatedEvent);
        var expectedPrivateSessionCreatedEvent = PrivateSessionCreatedEvent.builder()
                .createTime(operateTime)
                .sessionId(session.getId())
                .userId1(updatedFriendApplication.getApplicantUserId())
                .userId2(updatedFriendApplication.getTargetUserId())
                .build();
        assertEquals(expectedPrivateSessionCreatedEvent, actualPrivateSessionCreatedEvent);
    }

    @Test
    void 同意好友申请时_如果参数正常并且之前存在好友关系_响应应该符合预期_事件应该被正确发送() {
        // 准备数据：已经存在好友关系
        FriendShip existingFriendShip = FriendShip.of("existing-friendship-id", commonFriendApplication.getApplicantUserId(), commonFriendApplication.getTargetUserId(), Instant.now());
        friendShipRepository.save(existingFriendShip);

        // 准备request
        String operatorUserId = commonFriendApplication.getTargetUserId();
        String applicationId = commonFriendApplication.getId();
        String remark = "给好友的备注";
        Instant operateTime = Instant.now();
        var req = new AcceptFriendApplicationRequest();
        req.recipientRemark(remark);

        AcceptFriendApplication200Response resp = application.acceptFriendApplication(operatorUserId, applicationId, operateTime, req);

        // 响应应该符合预期
        var expectedData = new AcceptFriendApplication200ResponseData();
        expectedData.createFriendShip(false);
        var expectedResponse = new AcceptFriendApplication200Response();
        expectedResponse.success(true)
                .data(expectedData);
        assertEquals(expectedResponse, resp);

        // 验证事件发布
        var event = fakeEventBus.getEvent(FriendApplicationHandledEvent.TOPIC);
        assertNotNull(event);
        var actualEvent = assertInstanceOf(FriendApplicationHandledEvent.class, event);
        var expectedEvent = FriendApplicationHandledEvent.builder()
                .operateTime(operateTime)
                .applicationId(applicationId)
                .fromUserId(commonFriendApplication.getApplicantUserId())
                .toUserId(commonFriendApplication.getTargetUserId())
                .resultType(HandleFriendApplicationResultType.ACCEPTED)
                .isNewFriendShip(false)
                .build();
        assertEquals(expectedEvent, actualEvent);
    }

    @Test
    void 同意好友申请时_如果好友申请不存在_错误码应该正确设置() {
        String applicationId = "invalid-application-id";
        String operatorUserId = commonFriendApplication.getTargetUserId();
        String remark = "给好友的备注";
        Instant operateTime = Instant.now();
        var req = new AcceptFriendApplicationRequest();
        req.recipientRemark(remark);

        AcceptFriendApplication200Response resp = application.acceptFriendApplication(operatorUserId, applicationId, operateTime, req);

        assertFalse(resp.getSuccess());
        assertEquals(AcceptFriendApplication200Response.ErrCodeEnum.APPLICATION_NOT_FOUND, resp.getErrCode());
    }

    @Test
    void 同意好友申请时_如果好友申请已被处理_错误码应该正确设置() {
        // 准备数据
        commonFriendApplication.accept(commonFriendApplication.getTargetUserId());
        friendApplicationRepository.save(commonFriendApplication);
        // 准备request
        String operatorUserId = commonFriendApplication.getTargetUserId();
        String applicationId = commonFriendApplication.getId();
        String remark = "给好友的备注";
        Instant operateTime = Instant.now();
        var req = new AcceptFriendApplicationRequest();
        req.recipientRemark(remark);

        AcceptFriendApplication200Response resp = application.acceptFriendApplication(operatorUserId, applicationId, operateTime, req);

        assertFalse(resp.getSuccess());
        assertEquals(AcceptFriendApplication200Response.ErrCodeEnum.APPLICATION_ALREADY_HANDLED, resp.getErrCode());
    }

    @Test
    void 同意好友申请时_如果操作人不是好友申请的目标用户_错误码应该正确设置() {
        // 准备request
        String operatorId = commonFriendApplication.getApplicantUserId(); // 申请人不能处理好友申请
        String applicationId = commonFriendApplication.getId();
        String remark = "给好友的备注";
        var req = new AcceptFriendApplicationRequest();
        req.recipientRemark(remark);


        AcceptFriendApplication200Response resp = application.acceptFriendApplication(operatorId, applicationId, Instant.now(), req);
        assertFalse(resp.getSuccess());
    }
}

