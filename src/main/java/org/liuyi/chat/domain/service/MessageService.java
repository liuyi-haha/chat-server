package org.liuyi.chat.domain.service;

import lombok.RequiredArgsConstructor;
import org.liuyi.chat.domain.exception.SessionNotFoundException;
import org.liuyi.chat.domain.message.*;
import org.liuyi.chat.port.repository.MessageRepository;
import org.liuyi.chat.port.repository.PrivateChatSessionRepository;
import org.liuyi.chat.utils.JwtIssuer;
import org.liuyi.common.domain.exception.DomainException;
import org.liuyi.common.domain.object.RandomIdGenerator;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class MessageService {
    private final MessageRepository messageRepository;
    private final MessageFactory messageFactory;
    private final PrivateChatSessionRepository privateChatSessionRepository;
    private final JwtIssuer jwtIssuer;

    public Message sendTextMessage(String senderUserId, String sessionId, String content, Instant sendTime) {
        // todo @luiyi 校验工作待完善
        // 根据id的前缀判断是单聊还是群聊，这里暂时不区分
        privateChatSessionRepository.findById(sessionId).orElseThrow(SessionNotFoundException::new);

        var message = messageFactory.createTextMessage(sessionId, senderUserId, content, sendTime);
        messageRepository.save(message);
        return messageRepository.findById(message.getId()).orElseThrow(() -> new DomainException("消息保存后读取失败"));
    }

    public Message sendImageMessage(String senderUserId, String sessionId, Instant sendTime, String fileId, ImageSize imageSize) {
        privateChatSessionRepository.findById(sessionId).orElseThrow(SessionNotFoundException::new);
        var message = messageFactory.createImageMessage(sessionId, senderUserId, sendTime, fileId, imageSize);
        messageRepository.save(message);
        return messageRepository.findById(message.getId()).orElseThrow(() -> new DomainException("消息保存后读取失败"));
    }

    public Message sendSpeechMessage(String userId, String sessionId, Instant sendTime, String fileId, Integer durationSeconds) {
        privateChatSessionRepository.findById(sessionId).orElseThrow(SessionNotFoundException::new);
        var message = messageFactory.createSpeechMessage(sessionId, userId, sendTime, fileId, durationSeconds);
        messageRepository.save(message);
        return messageRepository.findById(message.getId()).orElseThrow(() -> new DomainException("消息保存后读取失败"));
    }

    public Message sendDocumentMessage(String userId, String sessionId, Instant sendTime, String fileId, String documentName, Long documentSize, DocumentType documentType) {
        privateChatSessionRepository.findById(sessionId).orElseThrow(SessionNotFoundException::new);
        var message = messageFactory.createDocumentMessage(sessionId, userId, sendTime, fileId, documentName, documentSize, documentType);
        messageRepository.save(message);
        return messageRepository.findById(message.getId()).orElseThrow(() -> new DomainException("消息保存后读取失败"));
    }


    public String createUploadMessageFileCredential(String userId, String sessionId, ContentType contentType) {
        // todo @liuyi 检查用户是否能在这个聊天会话发送消息（暂时不做）
        // todo @liuyi 可以根据contentType限制上传的文件的类型和大小，暂时也不做了
        // 生成fileId
        String fileId = RandomIdGenerator.generate();
        return jwtIssuer.generateToken(userId, fileId);
    }


}
