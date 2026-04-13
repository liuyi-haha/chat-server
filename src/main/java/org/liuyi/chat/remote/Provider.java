package org.liuyi.chat.remote;

import lombok.RequiredArgsConstructor;
import org.apache.dubbo.config.annotation.DubboService;
import org.liuyi.chat.application.Application;
import org.liuyi.chat_api.dubbo.ChatService;
import org.liuyi.chat_api.dubbo.get_session_userIds.GetSessionUserIdsRequest;
import org.liuyi.chat_api.dubbo.get_session_userIds.GetSessionUserIdsResponse;

@DubboService
@RequiredArgsConstructor
public class Provider implements ChatService {
    private final Application application;

    @Override
    public GetSessionUserIdsResponse getSessionUserIds(GetSessionUserIdsRequest request) {
        return application.getSessionUserIds(request);
    }
}
