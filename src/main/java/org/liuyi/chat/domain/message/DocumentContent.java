package org.liuyi.chat.domain.message;

import org.liuyi.common.domain.exception.DomainException;

public record DocumentContent(String fileId, String documentName, FileSize documentSize,
                              DocumentType type) implements Content {
    public DocumentContent {
        if (documentName == null || documentName.isBlank()) {
            throw new DomainException("File name cannot be null or blank");
        }
        // 校验不为null
        if (fileId == null || fileId.isBlank()) {
            throw new DomainException("File ID cannot be null or blank");
        }
        if (documentSize == null) {
            throw new DomainException("File size cannot be null");
        }
    }

    @Override
    public ContentType getType() {
        return ContentType.Document;
    }
}
