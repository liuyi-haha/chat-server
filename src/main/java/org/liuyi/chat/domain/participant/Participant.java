package org.liuyi.chat.domain.participant;

import lombok.Getter;
import org.liuyi.chat.domain.common.Remark;
import org.liuyi.common.domain.exception.DomainException;
import org.liuyi.common.domain.object.RandomIdGenerator;

import java.time.Instant;

@Getter
public class Participant {

    private String id;              // 随机生成的业务ID，无业务含义
    private String sessionId;
    private String userId;
    private ParticipantRole role;
    private Remark remark;          // 可变值对象
    private Instant createTime;

    // 私有构造，强制通过工厂创建
    private Participant(String id, String sessionId, String userId, ParticipantRole role,
                        Remark remark, Instant createTime) {
        if (id == null || id.isBlank()) {
            throw new DomainException("id cannot be null or blank");
        }
        if (sessionId == null || sessionId.isBlank()) {
            throw new DomainException("sessionId cannot be null or blank");
        }
        if (userId == null || userId.isBlank()) {
            throw new DomainException("userId cannot be null or blank");
        }
        if (role == null) {
            throw new DomainException("role cannot be null");
        }
        if (createTime == null) {
            throw new DomainException("createTime cannot be null");
        }
        this.id = id;
        this.sessionId = sessionId;
        this.userId = userId;
        this.role = role;
        this.remark = remark;
        this.createTime = createTime;
    }

    /**
     * 静态工厂：创建 Participant 实例。
     * 通常用于从数据模型（如 DTO 或数据库记录）转换为领域模型。
     *
     * @param id         随机生成的ID
     * @param sessionId  会话ID
     * @param userId     用户ID
     * @param role       角色
     * @param remark     备注
     * @param createTime 创建时间
     * @return           新的 Participant 实例
     */
    public static Participant of(
            String id,
            String sessionId,
            String userId,
            ParticipantRole role,
            String remark,
            Instant createTime
    ) {
        if (id == null || id.isBlank()) {
            throw new DomainException("id cannot be null or blank");
        }
        if (sessionId == null || sessionId.isBlank()) {
            throw new DomainException("sessionId cannot be null or blank");
        }
        if (userId == null || userId.isBlank()) {
            throw new DomainException("userId cannot be null or blank");
        }
        if (role == null) {
            throw new DomainException("role cannot be null");
        }

        return new Participant(id, sessionId, userId, role, new Remark(remark), createTime);
    }

    public static Participant create(
            String sessionId,
            String userId,
            ParticipantRole role,
            String remark
    ) {
        String id = RandomIdGenerator.generate();
        if (sessionId == null || sessionId.isBlank()) {
            throw new DomainException("sessionId cannot be null or blank");
        }
        if (userId == null || userId.isBlank()) {
            throw new DomainException("userId cannot be null or blank");
        }
        if (role == null) {
            throw new DomainException("role cannot be null");
        }

        return new Participant(id, sessionId, userId, role, new Remark(remark), Instant.now());
    }

    // 业务方法：设置备注（权限由外部控制）
    public void setRemark(String remark) {
        this.remark = new Remark(remark);
    }
}