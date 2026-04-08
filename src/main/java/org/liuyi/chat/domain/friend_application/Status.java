package org.liuyi.chat.domain.friend_application;

/**
 * 好友申请的状态枚举。
 */
public enum Status {
    /**
     * 已接受
     */
    ACCEPTED,
    /**
     * 已拒绝
     */
    REJECTED,
    /**
     * 待处理（默认）
     */
    PENDING
}
