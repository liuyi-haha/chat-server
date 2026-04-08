package org.liuyi.chat.domain.exception;

import org.liuyi.common.domain.exception.DomainException;

public class FriendApplicationNotFoundException extends DomainException {
    public FriendApplicationNotFoundException() {
        super("好友申请不存在");
    }
}
