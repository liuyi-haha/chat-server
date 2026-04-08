package org.liuyi.chat.domain.message;

import org.liuyi.common.domain.exception.DomainException;
import lombok.Getter;

/**
 * 文本消息内容。
 * 规则：text 长度 ∈ [1, 1000]
 */
@Getter
public class TextContent implements Content {

    private final String text;

    public TextContent(String text) {
        if (text == null || text.isBlank()) {
            throw new DomainException("Text content cannot be null or blank");
        }
        if (text.length() > 1000) {
            throw new DomainException("Text content exceeds max length 1000");
        }
        this.text = text;
    }

    @Override
    public ContentType getType() {
        return ContentType.Text;
    }

    // 工厂方法（可选）
    public static TextContent of(String text) {
        return new TextContent(text);
    }
}