package org.liuyi.chat.remote;

import com.liuyi.auth.openapi.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.liuyi.chat.application.Application;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.NativeWebRequest;

import java.time.Instant;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/chat-server")
public class Controller implements FriendApi, ChatApi {
    private final Application application;

    @Override
    public ResponseEntity<SendFriendApplication200Response> sendFriendApplication(String xUserId, SendFriendApplicationRequest sendFriendApplicationRequest) {
        // 在这里生成时间
        Instant sendTime = Instant.now();
        var resp = ResponseEntity.ok(application.sendFriendApplication(sendFriendApplicationRequest, xUserId, sendTime));
        log.info(resp.toString());
        return resp;

    }

    @Override
    public ResponseEntity<RejectFriendApplication200Response> rejectFriendApplication(String xUserId, String friendApplicationId) {
        Instant operateTime = Instant.now();
        var resp = ResponseEntity.ok(application.rejectFriendApplication(xUserId, friendApplicationId, operateTime));
        log.info(resp.toString());
        return resp;
    }

    @Override
    public Optional<NativeWebRequest> getRequest() {
        return FriendApi.super.getRequest();
    }

    @Override
    public ResponseEntity<AcceptFriendApplication200Response> acceptFriendApplication(String xUserId, String friendApplicationId, AcceptFriendApplicationRequest acceptFriendApplicationRequest) {
        Instant operateTime = Instant.now();
        var resp = ResponseEntity.ok(application.acceptFriendApplication(xUserId, friendApplicationId, operateTime, acceptFriendApplicationRequest));
        log.info(resp.toString());
        return resp;
    }

    @Override
    public ResponseEntity<SendTextMessage200Response> sendTextMessage(String xUserId, String sessionId, SendTextMessageRequest sendTextMessageRequest) {
        Instant sendTime = Instant.now();
        var resp = application.sendTextMessage(xUserId, sessionId, sendTextMessageRequest, sendTime);
        log.info(resp.toString());
        return ResponseEntity.ok(resp);
    }
}
