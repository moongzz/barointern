package com.moongoeun.project.user.presentation.controller;

import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;

import com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper;
import com.epages.restdocs.apispec.ResourceSnippetParameters;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.moongoeun.project.user.application.dto.request.ReqAuthPostJoinDTOApiV1;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@AutoConfigureRestDocs
@AutoConfigureMockMvc
@ActiveProfiles("dev")
public class AuthControllerApiV1Test {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

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
                MockMvcResultMatchers.status().isCreated()
            );

        mockMvc.perform(
                RestDocumentationRequestBuilders.post("/v1/auth/join")
                    .content(dtoToJson(dto))
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andExpectAll(
                MockMvcResultMatchers.status().isCreated()
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

    private String dtoToJson(Object dto) throws Exception {
        return objectMapper.writeValueAsString(dto);
    }
}
