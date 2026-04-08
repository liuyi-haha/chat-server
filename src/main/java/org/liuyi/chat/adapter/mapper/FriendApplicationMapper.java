package org.liuyi.chat.adapter.mapper;

import org.liuyi.chat.adapter.persistence.FriendApplicationDO;
import org.liuyi.chat.domain.friend_application.FriendApplication;
import org.liuyi.chat.domain.friend_application.Status;

/**
 * FriendApplication 领域模型与数据模型之间的转换器。
 */
public class FriendApplicationMapper {

    /**
     * 将数据模型 (DO) 转换为领域模型 (Entity)。
     * 利用 FriendApplication.of 工厂方法，传入 DO 中的简单类型数据。
     *
     * @param doObj 数据模型实例
     * @return 领域模型实例
     */
    public static FriendApplication toDomain(FriendApplicationDO doObj) {
        if (doObj == null) {
            return null;
        }

        // 直接将 DO 中的简单类型字段传递给工厂方法
        return FriendApplication.of(
                doObj.getApplicationId(),
                doObj.getApplicantUserId(),
                doObj.getTargetUserId(),
                doObj.getApplyTime(),
                doObj.getRemark(), // 传入 String
                doObj.getVerificationMessage(), // 传入 String
                doObj.getStatus() // 传入 Enum
        );
        // 工厂方法内部会负责创建 Remark 和 VerificationMessage 值对象，
        // 并将状态设置好。这完美契合了你的设计！
    }


    /**
     * 将领域模型 (Entity) 转换为数据模型 (DO)。
     * 利用领域对象的 getter 方法获取简单类型数据。
     *
     * @param domainObj 领域模型实例
     * @return 数据模型实例
     */
    public static FriendApplicationDO toDO(FriendApplication domainObj) {
        if (domainObj == null) {
            return null;
        }

        return FriendApplicationDO.builder()
                .applicationId(domainObj.getId())
                .applicantUserId(domainObj.getApplicantUserId())
                .targetUserId(domainObj.getTargetUserId())
                .applyTime(domainObj.getApplyTime())
                // 提取值对象的内部值 (通过 Getter)
                .remark(domainObj.getRemark() != null ? domainObj.getRemark().getRemark() : null)
                .verificationMessage(domainObj.getVerificationMessage() != null ? domainObj.getVerificationMessage().getMessage() : null)
                .status(domainObj.getStatus()) // 直接获取 Enum
                .build();
    }
}