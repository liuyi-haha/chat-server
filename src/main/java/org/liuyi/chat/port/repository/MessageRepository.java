package org.liuyi.chat.port.repository;

public interface MessageRepository {
    public Integer findMaxSeqInSession(String sessionId);
}
