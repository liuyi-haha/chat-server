package org.liuyi.chat.adapter.mapper;

import org.liuyi.chat.adapter.persistence.ParticipantDO;
import org.liuyi.chat.domain.participant.Participant;

public class ParticipantMapper {

    /**
     * DO -> Domain
     */
    public static Participant toDomain(ParticipantDO doObj) {
        if (doObj == null) return null;
        return Participant.of(
                doObj.getBusinessId(), // 映射到领域模型的 id
                doObj.getSessionId(),
                doObj.getUserId(),
                doObj.getRole(),
                doObj.getRemark(),
                doObj.getCreateTime()
        );
    }

    /**
     * Domain -> DO
     */
    public static ParticipantDO toDO(Participant domainObj) {
        if (domainObj == null) return null;
        return ParticipantDO.builder()
                .businessId(domainObj.getId()) // 领域模型的 id 映射到 DO 的 business_id
                .sessionId(domainObj.getSessionId())
                .userId(domainObj.getUserId())
                .role(domainObj.getRole())
                .remark(domainObj.getRemark() != null ? domainObj.getRemark().getRemark() : null)
                .createTime(domainObj.getCreateTime())
                .build();
    }
}