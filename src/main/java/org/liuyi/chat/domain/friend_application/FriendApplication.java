package org.liuyi.chat.domain.friend_application;


import lombok.Getter;
import org.liuyi.chat.domain.common.Remark;
import org.liuyi.chat.domain.exception.MultiHandleFriendApplicationException;
import org.liuyi.chat.domain.exception.NoPermissionToHandleException;
import org.liuyi.common.domain.exception.DomainException;
import org.liuyi.common.domain.object.RandomIdGenerator;

import java.time.Instant;

/**
 * 好友申请实体。
 * rule: 已被处理的好友申请不能被重复处理。
 */
@Getter
public class FriendApplication {
    private String id;
    private String applicantUserId;
    private String targetUserId;
    private Instant applyTime;
    private Remark remark; // 聚合的值对象
    private VerificationMessage verificationMessage; // 聚合的值对象
    private Status status; // 状态需可变，初始为 PENDING

    // 私有构造：确保通过工厂创建
    private FriendApplication(String id, String applicantUserId, String targetUserId, Instant applyTime, Remark remark, VerificationMessage verificationMessage, Status status) {
        this.id = id;
        this.applicantUserId = applicantUserId;
        this.targetUserId = targetUserId;
        this.applyTime = applyTime;
        this.remark = remark;
        this.verificationMessage = verificationMessage;
        this.status = status;
    }

    /**
     * 静态工厂方法，用于创建 FriendApplication 实例。
     * 通常用于从数据模型（如 DTO 或数据库记录）转换为领域模型。
     *
     * @param id                    申请ID
     * @param applicantUserId       申请人ID
     * @param targetUserId          目标用户ID
     * @param applyTime             申请时间
     * @param remark                申请备注
     * @param verificationMessage   验证消息
     * @return                      新的 FriendApplication 实例
     */
    public static FriendApplication of(
            String id,
            String applicantUserId,
            String targetUserId,
            Instant applyTime,
            String remark,
            String verificationMessage,
            Status status
    ) {

        // 参数校验（可选增强）
        if (id == null || id.isBlank()) {
            throw new DomainException("id cannot be null or blank");
        }
        if (applicantUserId == null || applicantUserId.isBlank()) {
            throw new DomainException("applicantUserId cannot be null or blank");
        }
        if (targetUserId == null || targetUserId.isBlank()) {
            throw new DomainException("targetUserId cannot be null or blank");
        }
        if (applyTime == null) {
            throw new DomainException("applyTime cannot be null");
        }
        // remark 和 verificationMessage 可以为 null，由它们自身的 validate 方法处理

        return new FriendApplication(id, applicantUserId, targetUserId, applyTime, new Remark(remark), new VerificationMessage(verificationMessage), status);
    }

    public static FriendApplication create(
            String applicantUserId,
            String targetUserId,
            Instant applyTime,
            String remark,
            String verificationMessage
    ) {

        // 生成id
        String id  = RandomIdGenerator.generate();
        if (applicantUserId == null || applicantUserId.isBlank()) {
            throw new DomainException("applicantUserId cannot be null or blank");
        }
        if (targetUserId == null || targetUserId.isBlank()) {
            throw new DomainException("targetUserId cannot be null or blank");
        }
        if (applyTime == null) {
            throw new DomainException("applyTime cannot be null");
        }
        // remark 和 verificationMessage 可以为 null，由它们自身的 validate 方法处理


        return new FriendApplication(id, applicantUserId, targetUserId, applyTime, new Remark(remark), new VerificationMessage(verificationMessage), Status.PENDING);
    }

    // 业务方法：接受申请
    public void accept(String operatorUserId) {
        checkHashPermissionToHandle(operatorUserId);
        if (status != Status.PENDING) {
            throw new MultiHandleFriendApplicationException();
        }
        this.status = Status.ACCEPTED;
    }

    // 业务方法：拒绝申请
    public void reject(String operatorId) {
        checkHashPermissionToHandle(operatorId);
        if (status != Status.PENDING) {
            throw new MultiHandleFriendApplicationException();
            
        }
        this.status = Status.REJECTED;
    }

    private void checkHashPermissionToHandle(String operatorUserId) {
        if (!operatorUserId.equals(targetUserId))
        {
            throw new NoPermissionToHandleException();
        }
    }
}