package org.liuyi.chat.port.repository;

import org.liuyi.chat.domain.participant.Participant;

import java.util.List;

public interface ParticipantRepository {
    void addAll(List<Participant> participants);

    List<Participant> findByChatSessionId(String chatSessionId);
}
