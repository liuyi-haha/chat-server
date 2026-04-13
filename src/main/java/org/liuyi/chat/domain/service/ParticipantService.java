package org.liuyi.chat.domain.service;

import lombok.RequiredArgsConstructor;
import org.liuyi.chat.domain.participant.Participant;
import org.liuyi.chat.domain.participant.ParticipantRole;
import org.liuyi.chat.port.repository.ParticipantRepository;
import org.liuyi.common.domain.exception.DomainException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ParticipantService {
    // todo @liuyi 写到这里了
    private final ParticipantRepository participantRepository;

    public List<String> createPrivateSessionParticipants(String sessionId, String applicantUserId, String targetUserId, String targetRemark, String applicationRemark) {
        // 返回值中，第一个是申请人的成员ID，第二个是目标用户的成员ID
        Participant applicantParticipant = Participant.create(sessionId, applicantUserId, ParticipantRole.PrivateMember, applicationRemark);
        Participant targetParticipant = Participant.create(sessionId, targetUserId, ParticipantRole.PrivateMember, targetRemark);
        participantRepository.addAll(List.of(applicantParticipant, targetParticipant));
        return List.of(applicantParticipant.getId(), targetParticipant.getId());
    }


    public Set<String> getSessionUserIds(String sessionId) {
        var userIds = participantRepository.findByChatSessionId(sessionId).stream()
                .map(Participant::getUserId)
                .collect(Collectors.toSet());
        if (userIds.size() < 2) {
            throw new DomainException("聊天会话成员数量不足2");
        }
        return userIds;
    }
}
