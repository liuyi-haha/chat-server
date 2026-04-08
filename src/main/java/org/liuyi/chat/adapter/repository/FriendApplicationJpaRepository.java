package org.liuyi.chat.adapter.repository;

import org.liuyi.chat.adapter.persistence.FriendApplicationDO;
import org.liuyi.chat.domain.friend_application.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FriendApplicationJpaRepository extends JpaRepository<FriendApplicationDO, Long> {

    /**
     * Spring Data JPA 会根据方法名自动推断查询逻辑。
     * 查找申请人和目标用户相同且状态为 PENDING 的记录。
     */
    Optional<FriendApplicationDO> findByApplicantUserIdAndTargetUserIdAndStatus(
            String applicantUserId, String targetUserId, Status status);

    Optional<FriendApplicationDO> findByApplicationId(String applicationId);

    boolean existsByApplicationId(String id);
}
