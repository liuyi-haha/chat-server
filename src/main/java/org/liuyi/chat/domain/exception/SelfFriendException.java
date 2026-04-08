package org.liuyi.chat.domain.exception;

import org.liuyi.common.domain.exception.DomainException;

public class SelfFriendException extends DomainException {
    public SelfFriendException() {
        super("不允许添加自己为好友");
    }
}
