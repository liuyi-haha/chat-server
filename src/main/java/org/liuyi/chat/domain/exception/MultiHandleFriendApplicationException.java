package org.liuyi.chat.domain.exception;

import org.liuyi.common.domain.exception.DomainException;

public class MultiHandleFriendApplicationException extends DomainException {
    public MultiHandleFriendApplicationException() {
        super("重复处理好友申请");
    }
}
