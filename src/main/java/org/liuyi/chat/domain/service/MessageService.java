package org.liuyi.chat.domain.service;

import lombok.RequiredArgsConstructor;
import org.liuyi.chat.domain.exception.SessionNotFoundException;
import org.liuyi.chat.domain.message.Message;
import org.liuyi.chat.domain.message.MessageFactory;
import org.liuyi.chat.port.repository.MessageRepository;
import org.liuyi.chat.port.repository.PrivateChatSessionRepository;
import org.liuyi.common.domain.exception.DomainException;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class MessageService {
    private final MessageRepository messageRepository;
    private final MessageFactory messageFactory;
    private final PrivateChatSessionRepository privateChatSessionRepository;

    public Message sendTextMessage(String senderUserId, String sessionId, String content, Instant sendTime) {
        // todo @luiyi 校验工作待完善
        // 根据id的前缀判断是单聊还是群聊，这里暂时不区分
        privateChatSessionRepository.findById(sessionId).orElseThrow(SessionNotFoundException::new);
        
        var message = messageFactory.createTextMessage(sessionId, senderUserId, content, sendTime);
        messageRepository.save(message);
        return messageRepository.findById(message.getId()).orElseThrow(() -> new DomainException("消息保存后读取失败"));
    }
}
