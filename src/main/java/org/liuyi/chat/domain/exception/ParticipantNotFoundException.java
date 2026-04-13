package org.liuyi.chat.domain.exception;

import org.liuyi.common.domain.exception.DomainException;

public class ParticipantNotFoundException extends DomainException {
    public ParticipantNotFoundException() {
        super("你不是聊天会话的成员");
    }
}
