package org.liuyi.chat.domain.message;

import org.liuyi.common.domain.exception.DomainException;

public record SpeechContent(String fileId, Integer durationSeconds) implements Content {
    public SpeechContent {
        // 校验不为null
        if (fileId == null || fileId.isBlank()) {
            throw new DomainException("File ID cannot be null or blank");
        }
        if (durationSeconds == null || durationSeconds <= 0) {
            throw new DomainException("Duration seconds must be a positive integer");
        }
    }

    @Override
    public ContentType getType() {
        return ContentType.Speech;
    }
}
