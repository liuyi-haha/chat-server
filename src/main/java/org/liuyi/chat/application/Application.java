package org.liuyi.chat.application;

import com.liuyi.auth.openapi.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.liuyi.chat.application.event.FriendApplicationAcceptedEvent;
import org.liuyi.chat.domain.exception.*;
import org.liuyi.chat.domain.friend_application.FriendApplication;
import org.liuyi.chat.domain.service.FriendApplicationService;
import org.liuyi.chat.domain.service.FriendShipService;
import org.liuyi.chat.domain.service.ParticipantService;
import org.liuyi.chat.domain.service.PrivateChatSessionService;
import org.liuyi.chat.port.publisher.FriendApplicationAcceptedEventPublisher;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@RequiredArgsConstructor
@Service
@Slf4j
public class Application {
    private final FriendApplicationService friendApplicationService;
    private final FriendShipService friendShipService;
    private final PrivateChatSessionService privateChatSessionService;
    private final ParticipantService participantService;
    private final FriendApplicationAcceptedEventPublisher friendApplicationAcceptedEventPublisher;

    public SendFriendApplication200Response sendFriendApplication(SendFriendApplicationRequest request, String fromUserId, Instant sendTime) {
        SendFriendApplication200Response resp = new SendFriendApplication200Response();
        try {
            String friendApplicationId = friendApplicationService.sendFriendApplication(fromUserId, request.getTargetUserId(), request.getRecipientRemark(), request.getVerificationMessage(), sendTime);

            SendFriendApplication200ResponseData data = new SendFriendApplication200ResponseData();
            data.friendApplicationId(friendApplicationId);
            resp.data(data).success(true);
        } catch (PendingFriendApplicationExistException ex) {
            log.warn("重复的好友申请", ex);
            resp.success(false).errCode(SendFriendApplication200Response.ErrCodeEnum.DUPLICATE_APPLICATION).errMsg(ex.getMessage());

        } catch (AlreadyFriendException ex) {
            log.warn("已经是好友了", ex);
            resp.success(false).errCode(SendFriendApplication200Response.ErrCodeEnum.ALREADY_FRIEND).errMsg(ex.getMessage());
        } catch (SelfFriendException ex) {
            log.warn("申请添加自己为好友", ex);
            resp.success(false).errCode(SendFriendApplication200Response.ErrCodeEnum.SELF_FRIEND).errMsg(ex.getMessage());
        } catch (Exception ex) {
            log.error("基础设施异常", ex);
            resp.success(false).errMsg(ex.getMessage());
        }
        return resp;
    }


    public RejectFriendApplication200Response rejectFriendApplication(String fromUserId, String friendApplicationId) {
        RejectFriendApplication200Response resp = new RejectFriendApplication200Response();
        try {
            friendApplicationService.rejectFriendApplication(fromUserId, friendApplicationId);
            resp.success(true);
        } catch (FriendApplicationNotFoundException ex) {
            log.warn("好友申请未找到", ex);
            resp.success(false).errCode(RejectFriendApplication200Response.ErrCodeEnum.APPLICATION_NOT_FOUND);
        } catch (MultiHandleFriendApplicationException ex) {
            log.warn("好友申请不得重复处理", ex);
            resp.success(false).errCode(RejectFriendApplication200Response.ErrCodeEnum.APPLICATION_ALREADY_HANDLED);
        } catch (NoPermissionToHandleException ex) {
            log.warn("无权处理该好友申请", ex);
            resp.success(false).errMsg(ex.getMessage());
        } catch (Exception ex) {
            log.error("基础设施异常", ex);
            resp.success(false).errMsg(ex.getMessage());
        }
        return resp;
    }

    public AcceptFriendApplication200Response acceptFriendApplication(String xUserId, String friendApplicationId, Instant operateTime, AcceptFriendApplicationRequest acceptFriendApplicationRequest) {
        AcceptFriendApplication200Response resp = new AcceptFriendApplication200Response();
        try {
            // 同意好友申请
            FriendApplication friendApplication = friendApplicationService.acceptFriendApplication(xUserId, friendApplicationId, acceptFriendApplicationRequest);
            // 创建好友关系
            String friendShipId = friendShipService.createFriendShip(friendApplication.getApplicantUserId(), friendApplication.getTargetUserId(), operateTime);
            // 创建单聊会话
            String chatSessionId = privateChatSessionService.createPrivateChatSession(friendShipId, operateTime);
            // 创建单聊会话成员
            List<String> participantIds = participantService.createPrivateSessionParticipants(
                    chatSessionId,
                    friendApplication.getApplicantUserId(),
                    friendApplication.getTargetUserId(),
                    friendApplication.getRemark().getRemark(),
                    acceptFriendApplicationRequest.getRecipientRemark());
            // 发送应用事件
            // 构造FriendApplicationAcceptedEvent
            FriendApplicationAcceptedEvent event = FriendApplicationAcceptedEvent.builder()
                    .operateTime(operateTime)
                    .applicationId(friendApplicationId)
                    .fromUserId(friendApplication.getApplicantUserId())
                    .toUserId(friendApplication.getTargetUserId())
                    .isNewFriendShip(true)
                    .friendshipId(friendShipId)
                    .privateChatSessionId(chatSessionId)
                    .applicantParticipantId(participantIds.get(0))
                    .targetUserParticipantId(participantIds.get(1))
                    .build();
            friendApplicationAcceptedEventPublisher.publish(event);


            resp.success(true);
        } catch (FriendApplicationNotFoundException ex) {
            log.warn("好友申请未找到", ex);
            resp.success(false).errCode(AcceptFriendApplication200Response.ErrCodeEnum.APPLICATION_NOT_FOUND);
        } catch (MultiHandleFriendApplicationException ex) {
            log.warn("好友申请不得重复处理", ex);
            resp.success(false).errCode(AcceptFriendApplication200Response.ErrCodeEnum.APPLICATION_ALREADY_HANDLED);
        } catch (NoPermissionToHandleException ex) {
            log.warn("无权处理该好友申请", ex);
            resp.success(false).errMsg(ex.getMessage());
        } catch (Exception ex) {
            log.error("基础设施异常", ex);
            resp.success(false).errMsg(ex.getMessage());
        }
        return resp;
    }
}
