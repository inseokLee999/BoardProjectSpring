package org.choongang.member;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class SecurityTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("토큰 post")
    void test1() throws Exception {
        mockMvc.perform(post("/member/join")
                        .with(csrf().asHeader())//토큰도 들어 가야 함
                        .param("email", "user02@test.org"))
                .andDo(print());

    }

    @Test
    @WithMockUser//인증받은 형태로 접근
    void test2() throws Exception{
        mockMvc.perform(get("/mypage"))
                .andDo(print());
    }
    @Test
    @WithMockUser(username = "user01@test.org",authorities = "ADMIN")//권한 부여
    void test3() throws Exception{
        mockMvc.perform(get("/admin"))
                .andDo(print());
    }
}
