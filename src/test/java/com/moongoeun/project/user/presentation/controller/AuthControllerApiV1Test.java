package com.moongoeun.project.user.presentation.controller;

import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;

import com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper;
import com.epages.restdocs.apispec.ResourceSnippetParameters;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.moongoeun.project.user.application.dto.request.ReqAuthPostJoinDTOApiV1;
import com.moongoeun.project.user.application.dto.request.ReqAuthPostLoginDTOApiV1;
import com.moongoeun.project.user.application.dto.request.ReqAuthPostLoginDTOApiV1.User;
import com.moongoeun.project.user.application.exception.AuthExceptionCode;
import com.moongoeun.project.user.domain.entity.UserEntity;
import com.moongoeun.project.user.domain.repository.UserRepository;
import com.moongoeun.project.user.domain.vo.RoleType;
import com.moongoeun.project.user.infrastructure.jwt.JwtUtil;
import com.moongoeun.project.user.infrastructure.userdetails.UserDetailsServiceImpl;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@AutoConfigureRestDocs
@AutoConfigureMockMvc
@ActiveProfiles("dev")
public class AuthControllerApiV1Test {

    private static final Logger log = LoggerFactory.getLogger(AuthControllerApiV1Test.class);
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserDetailsServiceImpl userDetailsServiceImpl;
    @Value("${jwt.secret.key}")
    String secretKey;

    @Test
    public void testAuthPostJoinSuccess() throws Exception {
        ReqAuthPostJoinDTOApiV1 dto = ReqAuthPostJoinDTOApiV1.builder()
            .user(
                ReqAuthPostJoinDTOApiV1.User.builder()
                    .username("username(JoinSuccessTest)")
                    .password("password(JoinSuccessTest)")
                    .nickname("nickname(JoinSuccessTest)")
                    .build()
            )
            .build();

        mockMvc.perform(
                RestDocumentationRequestBuilders.post("/v1/auth/join")
                    .content(dtoToJson(dto))
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andExpectAll(
                MockMvcResultMatchers.status().isCreated()
            )
            .andDo(
                MockMvcRestDocumentationWrapper.document("회원 가입 성공",
                    preprocessRequest(prettyPrint()),
                    preprocessResponse(prettyPrint()),
                    resource(ResourceSnippetParameters.builder()
                        .tag("AUTH v1")
                        .build()
                    )
                )
            );
    }

    @Test
    public void testAuthPostJoinFail() throws Exception {
        UserEntity userEntity = UserEntity.of(
            "username(JoinFailTest)",
            "password(JoinFailTest)",
            "nickname(JoinFailTest)",
            new RoleType[]{RoleType.USER}
        );

        userRepository.save(userEntity);

        ReqAuthPostJoinDTOApiV1 dto = ReqAuthPostJoinDTOApiV1.builder()
            .user(
                ReqAuthPostJoinDTOApiV1.User.builder()
                    .username("username(JoinFailTest)")
                    .password("password(JoinFailTest)")
                    .nickname("nickname(JoinFailTest)")
                    .build()
            )
            .build();

        mockMvc.perform(
                RestDocumentationRequestBuilders.post("/v1/auth/join")
                    .content(dtoToJson(dto))
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andExpectAll(
                MockMvcResultMatchers.status()
                    .is(AuthExceptionCode.DUPLICATE_USERNAME.getStatus().value())
            )
            .andDo(
                MockMvcRestDocumentationWrapper.document("회원 가입 실패",
                    preprocessRequest(prettyPrint()),
                    preprocessResponse(prettyPrint()),
                    resource(ResourceSnippetParameters.builder()
                        .tag("AUTH v1")
                        .build()
                    )
                )
            );
    }

    @Test
    public void testAuthPostLoginSuccess() throws Exception {
        UserEntity userEntity = UserEntity.of(
            "username(LoginSuccessTest)",
            passwordEncoder.encode("password(LoginSuccessTest)"),
            "nickname(LoginSuccessTest)",
            new RoleType[]{RoleType.USER}
        );
        userRepository.save(userEntity);

        ReqAuthPostLoginDTOApiV1 dto = new ReqAuthPostLoginDTOApiV1(
            new User("username(LoginSuccessTest)", "password(LoginSuccessTest)")
        );

        mockMvc.perform(
                RestDocumentationRequestBuilders.post("/v1/auth/login")
                    .content(dtoToJson(dto))
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andExpectAll(
                MockMvcResultMatchers.status().isOk()
            )
            .andDo(
                MockMvcRestDocumentationWrapper.document("로그인 성공",
                    preprocessRequest(prettyPrint()),
                    preprocessResponse(prettyPrint()),
                    resource(ResourceSnippetParameters.builder()
                        .tag("AUTH v1")
                        .build()
                    )
                )
            );
    }

    @Test
    public void testAuthPostLoginFail() throws Exception {
        UserEntity userEntity = UserEntity.of(
            "username(LoginFailTest)",
            passwordEncoder.encode("password(LoginFailTest)"),
            "nickname(LoginFailTest)",
            new RoleType[]{RoleType.USER}
        );
        userRepository.save(userEntity);

        ReqAuthPostLoginDTOApiV1 dto = new ReqAuthPostLoginDTOApiV1(
            new User("username(LoginFailTest)", "password(LoginSuccessTest)")
        );

        mockMvc.perform(
                RestDocumentationRequestBuilders.post("/v1/auth/login")
                    .content(dtoToJson(dto))
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andExpectAll(
                MockMvcResultMatchers.status()
                    .is(AuthExceptionCode.AUTHENTICATION_FAILED.getStatus().value())
            )
            .andDo(
                MockMvcRestDocumentationWrapper.document("로그인 실패",
                    preprocessRequest(prettyPrint()),
                    preprocessResponse(prettyPrint()),
                    resource(ResourceSnippetParameters.builder()
                        .tag("AUTH v1")
                        .build()
                    )
                )
            );
    }

    @Test
    public void testAuthGetRefreshSuccess() throws Exception {
        UserEntity userEntity = UserEntity.of(
            "username(RefreshSuccessTest)",
            passwordEncoder.encode("password(RefreshSuccessTest)"),
            "nickname(RefreshSuccessTest)",
            new RoleType[]{RoleType.USER}
        );
        userRepository.save(userEntity);

        JwtUtil jwtUtil = new JwtUtil(userDetailsServiceImpl, secretKey);

        String accessToken = jwtUtil.createAccessToken(
            1L,
            new RoleType[]{RoleType.USER},
            "nickname(RefreshSuccessTest)"
        );

        accessToken = accessToken.startsWith("Bearer ") ? accessToken.substring(7) : accessToken;
        String refreshToken = jwtUtil.createRefreshToken(accessToken);

        mockMvc.perform(
                RestDocumentationRequestBuilders.get("/v1/auth/refresh")
                    .param("accessToken", accessToken)
                    .param("refreshToken", refreshToken)
            )
            .andExpectAll(
                MockMvcResultMatchers.status().isOk()
            )
            .andDo(
                MockMvcRestDocumentationWrapper.document("토큰 재발급 성공",
                    preprocessRequest(prettyPrint()),
                    preprocessResponse(prettyPrint()),
                    resource(ResourceSnippetParameters.builder()
                        .tag("AUTH v1")
                        .build()
                    )
                )
            );
    }

    @Test
    public void testAuthGetRefreshFail() throws Exception {
        UserEntity userEntity = UserEntity.of(
            "username(RefreshFailTest)",
            passwordEncoder.encode("password(RefreshFailTest)"),
            "nickname(RefreshFailTest)",
            new RoleType[]{RoleType.USER}
        );
        userRepository.save(userEntity);

        JwtUtil jwtUtil = new JwtUtil(userDetailsServiceImpl, secretKey);

        String accessToken = jwtUtil.createToken(
            "ACCESS",
            1L,
            new RoleType[]{RoleType.USER},
            "nickname(RefreshFailTest)",
            300L
        );
        String refreshToken = jwtUtil.createToken(
            "REFRESH",
            1L,
            new RoleType[]{RoleType.USER},
            "nickname(RefreshSuccessTest)",
            0L
        );

        mockMvc.perform(
                RestDocumentationRequestBuilders.get("/v1/auth/refresh")
                    .param("accessToken", accessToken)
                    .param("refreshToken", refreshToken)
            )
            .andExpectAll(
                MockMvcResultMatchers.status()
                    .is(AuthExceptionCode.EXPIRED_TOKEN.getStatus().value())
            )
            .andDo(
                MockMvcRestDocumentationWrapper.document("토큰 재발급 실패",
                    preprocessRequest(prettyPrint()),
                    preprocessResponse(prettyPrint()),
                    resource(ResourceSnippetParameters.builder()
                        .tag("AUTH v1")
                        .build()
                    )
                )
            );
    }

    private String dtoToJson(Object dto) throws Exception {
        return objectMapper.writeValueAsString(dto);
    }
}
