package org.liuyi.chat.domain.exception;

import org.liuyi.common.domain.exception.DomainException;

public class FriendShipNotFoundException extends DomainException {
    public FriendShipNotFoundException() {
        super("你们不是好友");
    }
}
