package com.moongoeun.project.user.presentation.controller;

import static com.epages.restdocs.apispec.ResourceDocumentation.parameterWithName;
import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;

import com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper;
import com.epages.restdocs.apispec.ResourceSnippetParameters;
import com.epages.restdocs.apispec.SimpleType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.moongoeun.project.user.domain.vo.RoleType;
import com.moongoeun.project.user.infrastructure.jwt.JwtUtil;
import com.moongoeun.project.user.infrastructure.userdetails.UserDetailsServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@AutoConfigureRestDocs
@AutoConfigureMockMvc
@ActiveProfiles("dev")
public class UserControllerApiV1Test {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private UserDetailsServiceImpl userDetailsServiceImpl;
    @Value("${jwt.secret.key}")
    String secretKey;

    @Test
    public void testUserPatchRoleSuccess() throws Exception {
        JwtUtil jwtUtil = new JwtUtil(userDetailsServiceImpl, secretKey);

        String accessToken = jwtUtil.createAccessToken(
            1L,
            new RoleType[]{RoleType.USER, RoleType.ADMIN},
            "nickname(PatchRoleSuccessTest)"
        );

        mockMvc.perform(
                RestDocumentationRequestBuilders.patch("/v1/users/{id}/admin", 1)
                    .header("Authorization", accessToken)
            )
            .andExpectAll(
                MockMvcResultMatchers.status().isOk()
            )
            .andDo(
                MockMvcRestDocumentationWrapper.document("회원 권한 수정 성공",
                    preprocessRequest(prettyPrint()),
                    preprocessResponse(prettyPrint()),
                    resource(ResourceSnippetParameters.builder()
                        .tag("USER v1")
                        .pathParameters(
                            parameterWithName("id").type(SimpleType.NUMBER).description("유저 ID")
                        )
                        .build()
                    )
                )
            );
    }

    @Test
    public void testUserPatchRoleFail() throws Exception {
        JwtUtil jwtUtil = new JwtUtil(userDetailsServiceImpl, secretKey);

        String accessToken = jwtUtil.createAccessToken(
            1L,
            new RoleType[]{RoleType.USER},
            "nickname(PatchRoleFailTest)"
        );

        mockMvc.perform(
                RestDocumentationRequestBuilders.patch("/v1/users/{id}/admin", 1)
                    .header("Authorization", accessToken)
            )
            .andExpectAll(
                MockMvcResultMatchers.status().is(403)
            )
            .andDo(
                MockMvcRestDocumentationWrapper.document("회원 권한 수정 실패",
                    preprocessRequest(prettyPrint()),
                    preprocessResponse(prettyPrint()),
                    resource(ResourceSnippetParameters.builder()
                        .tag("USER v1")
                        .pathParameters(
                            parameterWithName("id").type(SimpleType.NUMBER).description("유저 ID")
                        )
                        .build()
                    )
                )
            );
    }
}
