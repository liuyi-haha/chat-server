package org.liuyi.chat.domain.exception;

import lombok.Builder;
import lombok.Data;
import org.liuyi.common.domain.exception.DomainException;

@Data
@Builder
public class AlreadyFriendException extends DomainException {
    private String applicantUserId;
    private String targetUserId;

    public AlreadyFriendException() {
        super("已经是好友了");
    }

    public AlreadyFriendException(String applicantUserId, String targetUserId) {
        super("已经是好友了");
        this.applicantUserId = applicantUserId;
        this.targetUserId = targetUserId;
    }
}
