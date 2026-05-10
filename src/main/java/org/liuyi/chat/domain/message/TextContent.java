package org.liuyi.chat.domain.message;

import org.liuyi.common.domain.exception.DomainException;

/**
 * 文本消息内容。
 * 规则：text 长度 ∈ [1, 1000]
 */
public record TextContent(String text) implements Content {

    public TextContent {
        if (text == null || text.isBlank()) {
            throw new DomainException("Text content cannot be null or blank");
        }
        if (text.length() > 1000) {
            throw new DomainException("Text content exceeds max length 1000");
        }
    }

    @Override
    public ContentType getType() {
        return ContentType.Text;
    }
}