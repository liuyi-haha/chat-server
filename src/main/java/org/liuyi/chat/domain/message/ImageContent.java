package org.liuyi.chat.domain.message;

import org.liuyi.common.domain.exception.DomainException;

public record ImageContent(String fileId, ImageSize size) implements Content {
    public ImageContent {
        // 校验不为null
        if (fileId == null || fileId.isBlank()) {
            throw new DomainException("File ID cannot be null or blank");
        }
        if (size == null) {
            throw new DomainException("Image size cannot be null");
        }
    }

    @Override
    public ContentType getType() {
        return ContentType.Image;
    }
}
