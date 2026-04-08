package org.liuyi.chat.domain.exception;

import org.liuyi.common.domain.exception.DomainException;

public class NoPermissionToHandleException extends DomainException {
    public NoPermissionToHandleException() {
        super("没有权限处理该好友申请");
    }
}
