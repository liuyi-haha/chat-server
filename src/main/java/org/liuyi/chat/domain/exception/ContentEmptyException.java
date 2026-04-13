package org.liuyi.chat.domain.exception;

import org.liuyi.common.domain.exception.DomainException;

public class ContentEmptyException extends DomainException {
    public ContentEmptyException() {
        super("文本内容为空");
    }
}
