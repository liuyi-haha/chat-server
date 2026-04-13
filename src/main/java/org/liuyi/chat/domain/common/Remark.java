package org.liuyi.chat.domain.common;

import org.liuyi.common.domain.exception.InvalidRemarkException;

/**
 * 备注值对象。
 * rule: 备注长度限制为 [1, 20]
 */
public record Remark(String remark) {
    public Remark(String remark) {
        this.remark = remark;
        validate(remark);
    }

    public void validate(String remark) {
        if (remark == null || remark.isEmpty() || remark.length() > 20) {
            throw new InvalidRemarkException();
        }
    }
    
}