package org.liuyi.chat.domain.private_chat_session;

import org.liuyi.common.domain.exception.DomainException;
import org.liuyi.common.domain.object.Identity;
import org.liuyi.common.domain.object.RandomIdGenerator;

public class PrivateChatSessionId implements Identity {

    private static final String PREFIX = "PS-";
    private String sessionId;


    public PrivateChatSessionId() {
        sessionId = PREFIX + RandomIdGenerator.generate();

    }

    public PrivateChatSessionId(String sessionId) {
        validate(sessionId);
        this.sessionId = sessionId;
    }

    private static String validate(String sessionId) {
        if (sessionId == null || sessionId.isBlank()) {
            throw new DomainException("PrivateChatSessionId cannot be null or blank");
        }
        if (!sessionId.startsWith(PREFIX)) {
            throw new DomainException("单聊会话ID格式非法");
        }
        return sessionId;
    }

    @Override
    public String value() {
        return sessionId;
    }
}