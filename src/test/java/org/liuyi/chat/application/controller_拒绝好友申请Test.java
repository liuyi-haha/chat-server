    package org.liuyi.chat.application;

    import com.liuyi.auth.openapi.RejectFriendApplication200Response;
    import com.liuyi.auth.openapi.SendFriendApplication200Response;
    import com.liuyi.auth.openapi.SendFriendApplicationRequest;
    import org.junit.jupiter.api.BeforeEach;
    import org.junit.jupiter.api.Test;
    import org.liuyi.chat.domain.friend_application.FriendApplication;
    import org.liuyi.chat.port.repository.FriendApplicationRepository;
    import org.liuyi.chat.port.repository.FriendShipRepository;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
    import org.springframework.boot.test.context.SpringBootTest;
    import org.springframework.test.context.ActiveProfiles;
    import org.springframework.transaction.annotation.Transactional;

    import java.time.Instant;

    import static org.junit.jupiter.api.Assertions.*;


    @ActiveProfiles("test")
    @SpringBootTest
    @Transactional
    @AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
    public class controller_拒绝好友申请Test {
        @Autowired
        private FriendShipRepository friendShipRepository;
        @Autowired
        FriendApplicationRepository friendApplicationRepository;
        @Autowired
        private Application application;
        private FriendApplication commonFriendApplication;

        @BeforeEach
        void setUp() {
            commonFriendApplication = FriendApplication.create("123456789", "123456789", Instant.now(), "小刘", "申请添加你为好友");
            friendApplicationRepository.save(commonFriendApplication);
        }

        @Test
        void 拒绝好友申请时_如果参数正常_响应应该符合预期()
        {
            String fromUserId = commonFriendApplication.getTargetUserId();
            String applicationId = commonFriendApplication.getId();
            RejectFriendApplication200Response resp = application.rejectFriendApplication(fromUserId, applicationId);
            assertTrue(resp.getSuccess());
        }
        @Test
        void 拒绝好友申请时_如果好友申请不存在_错误码应该正确设置()
        {
            String fromUserId = commonFriendApplication.getTargetUserId();
            String applicationId = "invalid-application-id";
            RejectFriendApplication200Response resp = application.rejectFriendApplication(fromUserId, applicationId);
            assertFalse(resp.getSuccess());
            assertEquals(RejectFriendApplication200Response.ErrCodeEnum.APPLICATION_NOT_FOUND, resp.getErrCode());
        }

        @Test
        void 拒绝好友申请时_如果好友申请已被处理_错误码应该正确设置()
        {
            commonFriendApplication.accept(commonFriendApplication.getApplicantUserId());
            friendApplicationRepository.save(commonFriendApplication);

            String fromUserId = commonFriendApplication.getTargetUserId();
            String applicationId = commonFriendApplication.getId();
            RejectFriendApplication200Response resp = application.rejectFriendApplication(fromUserId, applicationId);
            assertFalse(resp.getSuccess());
            assertEquals(RejectFriendApplication200Response.ErrCodeEnum.APPLICATION_ALREADY_HANDLED, resp.getErrCode());
        }

        @Test
        void 拒绝好友申请时_如果操作人不是好友申请的目标用户_错误码应该正确设置()
        {
            commonFriendApplication.accept(commonFriendApplication.getApplicantUserId());
            friendApplicationRepository.save(commonFriendApplication);

            String fromUserId = commonFriendApplication.getApplicantUserId(); // 申请用户自己不能处理好友申请
            String applicationId = commonFriendApplication.getId();
            RejectFriendApplication200Response resp = application.rejectFriendApplication(fromUserId, applicationId);
            assertFalse(resp.getSuccess());
        }
    }
