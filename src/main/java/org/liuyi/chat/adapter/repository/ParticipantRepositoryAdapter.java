package org.liuyi.chat.adapter.repository;

import lombok.RequiredArgsConstructor;
import org.liuyi.chat.adapter.mapper.ParticipantMapper;
import org.liuyi.chat.adapter.persistence.ParticipantDO;
import org.liuyi.chat.domain.participant.Participant;
import org.liuyi.chat.port.repository.ParticipantRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@RequiredArgsConstructor
@Repository
public class ParticipantRepositoryAdapter implements ParticipantRepository {
    private final ParticipantJpaRepository jpaRepository;


    @Override
    public void addAll(List<Participant> participants) {
        List<ParticipantDO> dataList = participants.stream()
                .map(ParticipantMapper::toDO)
                .toList();
        jpaRepository.saveAll(dataList);
    }
}
