package org.liuyi.chat.adapter.mapper;


import org.liuyi.chat.adapter.persistence.PrivateChatSessionDO;
import org.liuyi.chat.domain.private_chat_session.PrivateChatSession;

public class PrivateChatSessionMapper {

    public static PrivateChatSession toDomain(PrivateChatSessionDO doObj) {
        if (doObj == null) return null;
        return PrivateChatSession.of(
                doObj.getBusinessId(),
                doObj.getFriendShipId(),
                doObj.getCreateTime()
        );
    }

    public static PrivateChatSessionDO toDO(PrivateChatSession domainObj) {
        if (domainObj == null) return null;
        return PrivateChatSessionDO.builder()
                .businessId(domainObj.getId())
                .friendShipId(domainObj.getFriendShipId())
                .createTime(domainObj.getCreateTime())
                .build();
    }
}