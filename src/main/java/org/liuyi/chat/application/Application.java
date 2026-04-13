package org.liuyi.chat.application;

import com.liuyi.auth.openapi.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.liuyi.chat.application.event.FriendApplicationAcceptedEvent;
import org.liuyi.chat.application.event.FriendApplicationRejectedEvent;
import org.liuyi.chat.application.event.FriendApplicationSentEvent;
import org.liuyi.chat.application.event.MessageSentEvent;
import org.liuyi.chat.domain.exception.*;
import org.liuyi.chat.domain.friend_application.FriendApplication;
import org.liuyi.chat.domain.message.Message;
import org.liuyi.chat.domain.message.TextContent;
import org.liuyi.chat.domain.service.*;
import org.liuyi.chat.port.publisher.Publisher;
import org.liuyi.chat_api.dubbo.get_session_userIds.GetSessionUserIdsRequest;
import org.liuyi.chat_api.dubbo.get_session_userIds.GetSessionUserIdsResponse;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Set;

@RequiredArgsConstructor
@Service
@Slf4j
public class Application {
    private final FriendApplicationService friendApplicationService;
    private final FriendShipService friendShipService;
    private final PrivateChatSessionService privateChatSessionService;
    private final ParticipantService participantService;
    private final MessageService messageService;
    private final Publisher publisher;

    public SendFriendApplication200Response sendFriendApplication(SendFriendApplicationRequest request, String fromUserId, Instant sendTime) {
        SendFriendApplication200Response resp = new SendFriendApplication200Response();
        try {
            String friendApplicationId = friendApplicationService.sendFriendApplication(fromUserId, request.getTargetUserId(), request.getRecipientRemark(), request.getVerificationMessage(), sendTime);

            // 发布应用事件
            FriendApplicationSentEvent event = FriendApplicationSentEvent.builder()
                    .fromUserId(fromUserId)
                    .toUserId(request.getTargetUserId())
                    .createTime(sendTime)
                    .applicationId(friendApplicationId)
                    .verificationMessage(request.getVerificationMessage())
                    .build();

            publisher.publish(event);

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


    public RejectFriendApplication200Response rejectFriendApplication(String operatorId, String friendApplicationId, Instant operateTime) {
        RejectFriendApplication200Response resp = new RejectFriendApplication200Response();
        try {
            FriendApplication application = friendApplicationService.rejectFriendApplication(operatorId, friendApplicationId);
            // 发布应用事件
            FriendApplicationRejectedEvent event = FriendApplicationRejectedEvent.builder()
                    .friendApplicationId(friendApplicationId)
                    .fromUserId(application.getApplicantUserId())
                    .toUserId(application.getTargetUserId())
                    .operateTime(operateTime)
                    .build();
            publisher.publish(event);

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

            // 校验对方备注长度是否符合要求
            FriendApplication.checkRemark(acceptFriendApplicationRequest.getRecipientRemark());
            // 同意好友申请
            FriendApplication friendApplication = friendApplicationService.acceptFriendApplication(xUserId, friendApplicationId);
            // 创建好友关系
            String friendShipId = friendShipService.createFriendShip(friendApplication.getApplicantUserId(), friendApplication.getTargetUserId(), operateTime);
            // 创建单聊会话
            String chatSessionId = privateChatSessionService.createPrivateChatSession(friendShipId, operateTime);
            // 创建单聊会话成员
            List<String> participantIds = participantService.createPrivateSessionParticipants(
                    chatSessionId,
                    friendApplication.getApplicantUserId(),
                    friendApplication.getTargetUserId(),
                    friendApplication.getRemark().remark(), //申请中的备注是给目标用户的
                    acceptFriendApplicationRequest.getRecipientRemark()); // 同意申请时提交的备注是给申请用户的
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
            publisher.publish(event);


            var data = new AcceptFriendApplication200ResponseData();
            data.createFriendShip(true)
                    .friendshipId(friendShipId)
                    .privateChatSessionId(chatSessionId)
                    .applicantParticipantId(participantIds.get(0))
                    .targetUserParticipantId(participantIds.get(1));
            resp.success(true).data(data);

        } catch (AlreadyFriendException ex) {
            var data = new AcceptFriendApplication200ResponseData();
            data.createFriendShip(false);
            resp.success(true).data(data);

            // 发送应用事件
            FriendApplicationAcceptedEvent event = FriendApplicationAcceptedEvent.builder()
                    .operateTime(operateTime)
                    .applicationId(friendApplicationId)
                    .fromUserId(ex.getApplicantUserId())
                    .toUserId(ex.getTargetUserId())
                    .isNewFriendShip(false)
                    .build();
            publisher.publish(event);

            log.warn("已经是好友了", ex); // 注意，这里是正确的
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

    public SendTextMessage200Response sendTextMessage(String userId, String chatSessionId, SendTextMessageRequest sendTextMessageRequest, Instant sendTime) {
        SendTextMessage200Response resp = new SendTextMessage200Response();
        try {
            Message message = messageService.sendTextMessage(userId, chatSessionId, sendTextMessageRequest.getContent(), sendTime);
            // 发布应用事件
            var event = MessageSentEvent.builder()
                    .sendTime(sendTime)
                    .messageId(message.getId())
                    .sessionId(message.getSessionId())
                    .senderId(message.getSenderUserId())
                    .seqInSession(message.getSeqInChatSession())
                    .senderId(message.getSenderUserId())
                    .contentType(message.getContent().getType())
                    .build();

            // 根据消息类型设置事件内容
            switch (message.getContent().getType()) {
                case Text:
                    var textContent = (TextContent) message.getContent();
                    event.setTextContent(textContent.getText());
                    break;
                // 其他消息类型的处理
                default:
                    // 抛出异常
                    throw new UnsupportedOperationException("不支持的消息类型: " + message.getContent().getType());
            }

            publisher.publish(event);
            var data = new SendTextMessage200ResponseData();
            data.messageId(message.getId()).sequence(message.getSeqInChatSession().longValue()).sendTime(sendTime.atOffset(ZoneOffset.UTC));
            resp.success(true).data(data);

        } catch (FriendApplicationNotFoundException ex) {
            log.warn("聊天会话不存在", ex);
            resp.success(false).errCode(SendTextMessage200Response.ErrCodeEnum.SESSION_NOT_FOUND);
        } catch (FriendShipNotFoundException ex) {
            log.warn("好友关系存在", ex);
            resp.success(false).errCode(SendTextMessage200Response.ErrCodeEnum.FRIENDSHIP_REQUIRED);
        } catch (ParticipantNotFoundException ex) {
            log.warn("聊天会话成员不存在", ex);
            resp.success(false).errMsg(ex.getMessage());
        } catch (ContentEmptyException ex) {
            log.warn("消息内容不能为空", ex);
            resp.success(false).errCode(SendTextMessage200Response.ErrCodeEnum.CONTENT_EMPTY);
        } catch (ContentTooLongException ex) {
            log.warn("消息内容过长", ex);
            resp.success(false).errCode(SendTextMessage200Response.ErrCodeEnum.CONTENT_TOO_LONG);
        } catch (Exception ex) {
            log.error("基础设施异常", ex);
            resp.success(false).errMsg(ex.getMessage());
        }
        return resp;
    }

    public GetSessionUserIdsResponse getSessionUserIds(GetSessionUserIdsRequest request) {
        GetSessionUserIdsResponse resp = new GetSessionUserIdsResponse();
        try {
            Set<String> userIds = participantService.getSessionUserIds(request.getSessionId());
            resp.setSuccess(true);
            resp.setUserIds(userIds);
        } catch (Exception ex) {
            log.error("基础设施异常", ex);
            resp.setSuccess(false);
            resp.setErrMsg(ex.getMessage());
        }
        return resp;
    }
}
