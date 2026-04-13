package org.liuyi.chat.domain.exception;

import org.liuyi.common.domain.exception.DomainException;

public class ContentTooLongException extends DomainException {
    public ContentTooLongException() {
        super("消息内容过长");
    }
}
