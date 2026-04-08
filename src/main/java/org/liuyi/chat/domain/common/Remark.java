package org.liuyi.chat.domain.common;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.liuyi.common.domain.exception.InvalidRemarkException;

/**
 * 备注值对象。
 * rule: 备注长度限制为 [1, 20]
 */
@Getter
@EqualsAndHashCode // 生成 equals 和 hashCode 方法
public class Remark {
    private final String remark;

    public Remark(String remark) {
        validate(remark);
        this.remark = remark;
    }

    public void validate(String remark) {
        if (remark == null || remark.isEmpty() || remark.length() > 20) {
            throw new InvalidRemarkException();
        }
    }
}