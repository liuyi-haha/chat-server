package org.liuyi.chat.domain.friend_application;

// package com.yourcompany.friend.domain.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.liuyi.chat.domain.exception.InvalidVerificationMessageException;

/**
 * 验证消息值对象。
 * rule: 验证消息长度限制为 [0, 20]
 */
@Getter
@EqualsAndHashCode // 生成 equals 和 hashCode 方法
public class VerificationMessage {
    private final String message;

    public VerificationMessage(String message) {
        validate(message);
        this.message = message;
    }

    public void validate(String message) {
        if (message == null || message.length() > 20) {
            throw new InvalidVerificationMessageException();
        }
    }
}