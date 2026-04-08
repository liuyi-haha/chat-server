package org.liuyi.chat.adapter.repository;

import lombok.RequiredArgsConstructor;
import org.liuyi.chat.adapter.mapper.FriendApplicationMapper;
import org.liuyi.chat.adapter.persistence.FriendApplicationDO;
import org.liuyi.chat.domain.friend_application.FriendApplication;
import org.liuyi.chat.domain.friend_application.Status;
import org.liuyi.chat.port.repository.FriendApplicationRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component // 使用 Component 标记适配器
@RequiredArgsConstructor
public class FriendApplicationRepositoryAdapter implements FriendApplicationRepository {

    private final FriendApplicationJpaRepository friendApplicationJpaRepository;

    @Override
    public void save(FriendApplication application) {
        FriendApplicationDO newDO = FriendApplicationMapper.toDO(application);
        friendApplicationJpaRepository.findByApplicationId(application.getId())
                .ifPresent(existingDO -> newDO.setId(existingDO.getId())); // 如果存在则设置ID以进行更新

        friendApplicationJpaRepository.save(newDO);

    }

    @Override
    public Optional<FriendApplication> findById(String applicationId) {
        Optional<FriendApplicationDO> doOpt = friendApplicationJpaRepository.findByApplicationId(applicationId);
        return doOpt.map(FriendApplicationMapper::toDomain);
    }

    @Override
    public Optional<FriendApplication> findPendingByApplicantAndTarget(String applicantUserId, String targetUserId) {
        Optional<FriendApplicationDO> doOpt = friendApplicationJpaRepository.findByApplicantUserIdAndTargetUserIdAndStatus(
                applicantUserId, targetUserId, Status.PENDING
        );
        return doOpt.map(FriendApplicationMapper::toDomain);
    }
}
