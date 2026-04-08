package org.liuyi.chat.domain.exception;

import org.liuyi.common.domain.exception.DomainException;

public class InvalidVerificationMessageException extends DomainException {
    public InvalidVerificationMessageException() {
        super("验证消息长度不符合要求");
    }
}
