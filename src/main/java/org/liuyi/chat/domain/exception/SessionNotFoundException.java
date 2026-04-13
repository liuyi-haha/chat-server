package org.liuyi.chat.domain.exception;

import org.liuyi.common.domain.exception.DomainException;

public class SessionNotFoundException extends DomainException {
    public SessionNotFoundException() {
        super("聊天会话不存在");
    }
}
