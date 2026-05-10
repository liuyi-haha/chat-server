package org.liuyi.chat.application;

import com.liuyi.auth.openapi.ApplyCredentialToUploadMessageFile200Response;
import com.liuyi.auth.openapi.ApplyCredentialToUploadMessageFileRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.liuyi.chat.application.test_fixture.RelationTestFixture;
import org.liuyi.chat.port.repository.MessageRepository;
import org.liuyi.chat.utils.JwtIssuer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;


@ActiveProfiles("test")
@SpringBootTest
@Transactional
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
public class controller_申请上传消息中文件的凭证Test {
    @Autowired
    MessageRepository messageRepository;
    @Autowired
    RelationTestFixture relationTestFixture;
    @Autowired
    private Application application;
    @Autowired
    private JwtIssuer jwtIssuer;

    private RelationTestFixture.FriendDTO friendDTO;

    private void prepareTestFixture() {

        friendDTO = relationTestFixture.makeFriends();

    }

    @BeforeEach
    void setUp() {
        prepareTestFixture();
    }

    @Test
    void 申请上传消息中文件的凭证_如果参数正常_响应应该符合预期() { // 领域服务中采用先存后查的方式，这样就能把存储和查找都验证了
        ApplyCredentialToUploadMessageFileRequest req = new ApplyCredentialToUploadMessageFileRequest();
        req.setMessageType(ApplyCredentialToUploadMessageFileRequest.MessageTypeEnum.IMAGE);

        ApplyCredentialToUploadMessageFile200Response resp = application.applyCredentialToUploadMessageFile(friendDTO.getUserId1(), friendDTO.getPrivateChatSessionId(), req);

        var expectedResp = new ApplyCredentialToUploadMessageFile200Response()
                .success(true)
                .data(null);
        assertThat(resp).usingRecursiveComparison().ignoringFields("data").isEqualTo(expectedResp);
        assertNotNull(resp.getData());
        assertNotNull(resp.getData().getUploadToken());
    }
}