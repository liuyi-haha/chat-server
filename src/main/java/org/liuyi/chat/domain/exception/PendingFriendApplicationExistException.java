package org.liuyi.chat.domain.exception;

public class PendingFriendApplicationExistException extends RuntimeException {
    public PendingFriendApplicationExistException() {
        super("当前存在未处理的好友申请");
    }
}
