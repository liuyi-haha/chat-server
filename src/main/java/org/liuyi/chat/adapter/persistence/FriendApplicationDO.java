package org.liuyi.chat.adapter.persistence;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.liuyi.chat.domain.friend_application.Status;
import org.springframework.data.domain.Persistable;

import java.time.Instant;

/**
 * 好友申请的数据模型 (Data Object)。
 * 对应数据库表 friend_application。
 * 值对象 Remark 和 VerificationMessage 被拆解为其内部的基础类型进行存储。
 */
@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "friend_application")
public class FriendApplicationDO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 数据库主键

    @Column(name = "application_id", unique = true, nullable = false, length = 64) // 假设业务ID是字符串
    private String applicationId; // 业务主键，对应 FriendApplication.id

    @Column(name = "applicant_user_id", nullable = false, length = 64)
    private String applicantUserId;

    @Column(name = "target_user_id", nullable = false, length = 64)
    private String targetUserId;

    @Column(name = "apply_time", nullable = false)
    private Instant applyTime;

    // 直接存储值对象内部的 String
    @Column(name = "remark", length = 20) // 长度对应 Remark 的校验规则
    private String remark;

    @Column(name = "verification_message", length = 20) // 长度对应 VerificationMessage 的校验规则
    private String verificationMessage;

    @Enumerated(EnumType.STRING) // 存储枚举名称
    @Column(name = "status", nullable = false)
    private Status status; // 直接映射 Status 枚举
}