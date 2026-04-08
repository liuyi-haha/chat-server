package org.liuyi.chat.domain.exception;

import org.liuyi.common.domain.exception.DomainException;

public class AlreadyFriendException extends DomainException {
    public AlreadyFriendException() {
        super("已经是好友了");
    }
}
