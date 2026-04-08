package org.liuyi.chat.remote;

import com.liuyi.auth.openapi.*;
import lombok.RequiredArgsConstructor;
import org.liuyi.chat.application.Application;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/chat-server")
public class Controller implements FriendApi {
    private final Application application;

    @Override
    public ResponseEntity<SendFriendApplication200Response> sendFriendApplication(String xUserId, SendFriendApplicationRequest sendFriendApplicationRequest) {
        // 在这里生成时间
        Instant sendTime = Instant.now();
        return ResponseEntity.ok(application.sendFriendApplication(sendFriendApplicationRequest, xUserId, sendTime));

    }

    @Override
    public ResponseEntity<RejectFriendApplication200Response> rejectFriendApplication(String xUserId, String friendApplicationId) {
        return ResponseEntity.ok(application.rejectFriendApplication(xUserId, friendApplicationId));
    }

    @Override
    public ResponseEntity<AcceptFriendApplication200Response> acceptFriendApplication(String xUserId, String friendApplicationId, AcceptFriendApplicationRequest acceptFriendApplicationRequest) {
        Instant operateTime = Instant.now();
        return ResponseEntity.ok(application.acceptFriendApplication(xUserId, friendApplicationId, operateTime, acceptFriendApplicationRequest));
    }
}
