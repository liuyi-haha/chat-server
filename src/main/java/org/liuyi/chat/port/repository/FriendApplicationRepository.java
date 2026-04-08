package org.liuyi.chat.port.repository;

import org.liuyi.chat.domain.friend_application.FriendApplication;

import java.util.Optional;

public interface FriendApplicationRepository {

    /**
     * 保存好友申请。
     */
    void save(FriendApplication application);

    /**
     * 根据申请ID查找好友申请。
     */
    Optional<FriendApplication> findById(String applicationId);

    /**
     * 查找特定申请人向特定目标用户发出的待处理（Pending）申请。
     * 用于防止重复申请。
     */
    Optional<FriendApplication> findPendingByApplicantAndTarget(String applicantUserId, String targetUserId);
}
