package org.liuyi.chat.domain.exception;

import org.liuyi.common.domain.exception.DomainException;

public class InvalidRemarkException extends DomainException {
    public InvalidRemarkException() {
        super("备注长度不符合要求");
    }
}
