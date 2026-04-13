package org.liuyi.chat.domain.service;

import lombok.RequiredArgsConstructor;
import org.liuyi.chat.domain.exception.AlreadyFriendException;
import org.liuyi.chat.domain.exception.FriendApplicationNotFoundException;
import org.liuyi.chat.domain.exception.PendingFriendApplicationExistException;
import org.liuyi.chat.domain.friend_application.FriendApplication;
import org.liuyi.chat.domain.friend_ship.FriendShip;
import org.liuyi.chat.port.repository.FriendApplicationRepository;
import org.liuyi.chat.port.repository.FriendShipRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class FriendApplicationService {
    private final FriendShipRepository friendShipRepository;
    private final FriendApplicationRepository friendApplicationRepository;

    public String sendFriendApplication(String fromUserId, String toUserId, String remark, String verificationMessage, Instant sendTime) {
        // 1. 检查是否已经存在从fromUserId到toUserId且状态为Pending的好友申请
        // 使用 Repository 提供的新方法
        Optional<FriendApplication> existingPendingAppOpt = friendApplicationRepository.findPendingByApplicantAndTarget(fromUserId, toUserId);
        if (existingPendingAppOpt.isPresent()) {
            throw new PendingFriendApplicationExistException();
        }

        // 2. 检查fromUserId和toUserId是否已经是好友关系
        Optional<FriendShip> existingFriendShipOpt = friendShipRepository.findByUserIds(fromUserId, toUserId);
        if (existingFriendShipOpt.isPresent()) {
            // 如果已经是好友，可以选择抛出异常或返回特殊标识
            throw new AlreadyFriendException();
        }

        // 3. 创建好友申请 (使用工厂)
        FriendApplication newApplication = FriendApplication.create(fromUserId, toUserId, sendTime, remark, verificationMessage);

        // 4. 保存好友申请 (使用 Repository)
        friendApplicationRepository.save(newApplication);

        // 5. 返回好友申请ID
        return newApplication.getId(); // getId() 从实体中获取业务ID

    }

    public FriendApplication rejectFriendApplication(String operatorId, String friendApplicationId) {
        // 检验好友申请是否存在
        Optional<FriendApplication> friendApplicationOpt = friendApplicationRepository.findById(friendApplicationId);
        FriendApplication friendApplication = friendApplicationOpt.orElseThrow(FriendApplicationNotFoundException::new);

        // 修改好友申请状态(含校验权限和状态的逻辑)
        friendApplication.reject(operatorId);

        // 保存好友申请
        friendApplicationRepository.save(friendApplication);
        return friendApplication;
    }

    public FriendApplication acceptFriendApplication(String operatorId, String friendApplicationId) {
        // 检验好友申请是否存在
        Optional<FriendApplication> friendApplicationOpt = friendApplicationRepository.findById(friendApplicationId);
        FriendApplication friendApplication = friendApplicationOpt.orElseThrow(FriendApplicationNotFoundException::new);

        // 修改好友申请状态(含校验权限和状态的逻辑)
        friendApplication.accept(operatorId);

        // 保存好友申请
        friendApplicationRepository.save(friendApplication);
        return friendApplication;
    }
}
