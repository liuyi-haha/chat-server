package org.liuyi.chat.domain.service;

import lombok.RequiredArgsConstructor;
import org.liuyi.chat.domain.participant.Participant;
import org.liuyi.chat.domain.participant.ParticipantRole;
import org.liuyi.chat.port.repository.ParticipantRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ParticipantService {
    // todo @liuyi 写到这里了
    private final ParticipantRepository participantRepository;

    public List<String> createPrivateSessionParticipants(String sessionId, String applicantUserId, String targetUserId, String applicationRemark, String remark) {
        // 返回值中，第一个是申请人的成员ID，第二个是目标用户的成员ID
        Participant participant1 = Participant.create(sessionId, applicantUserId, ParticipantRole.PrivateMember, remark);
        Participant participant2 = Participant.create(sessionId, targetUserId, ParticipantRole.PrivateMember, applicationRemark);
        participantRepository.addAll(List.of(participant1, participant2));
        return List.of(participant1.getId(), participant2.getId());
    }


}
