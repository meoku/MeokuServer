package com.upgrade.meoku.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.upgrade.meoku.security.JwtUtil;
import com.upgrade.meoku.user.data.MeokuLoginRequestDTO;
import com.upgrade.meoku.user.data.MeokuSignUpRequestDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.RestTemplate;

import static org.assertj.core.api.Fail.fail;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


//@SpringBootTest
@WebMvcTest(MeokuAuthController.class)
public class meokuUserControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private JwtUtil jwtUtil;
    @MockBean
    private RestTemplate restTemplate;
    @MockBean
    private MeokuAuthService authService;


    @Test
    @DisplayName("로그인 통합 테스트!")
    public void testLogin() throws Exception {
        // 준비
        MeokuLoginRequestDTO loginRequestDTO = new MeokuLoginRequestDTO();
        loginRequestDTO.setId("mkamdin");
        loginRequestDTO.setPassword("mkamdin");

        String token = "generated.jwt.token";

        // Service가 반환할 값을 mock
        //when(authService.login(loginRequestDTO)).thenReturn(token);

        // 로그인 요청 테스트
        // CSRF 토큰을 발급
        mockMvc.perform(post("/api/v1/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"username\": \"" + loginRequestDTO.getId() + "\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value(token));  // 응답 본문에 토큰이 포함되어야 함
    }



    @Test
    @DisplayName("회원가입 테스트 - 성공")
    void signupSuccess() throws Exception {
        MeokuSignUpRequestDTO request = new MeokuSignUpRequestDTO();
        request.setId("meokuuser");
        request.setPassword("meokuuser");
        request.setEmail("user@example.com");

        mockMvc.perform(post("/api/v1/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());

    }

    @Test
    @DisplayName("회원가입 테스트 - 실패")
    void signupSuccessByEmptyField() throws Exception {
        MeokuSignUpRequestDTO request = new MeokuSignUpRequestDTO();
        request.setId("   "); // 공백만 입력
        request.setPassword("ps");
        request.setEmail("user@example.com");

        mockMvc.perform(post("/api/v1/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }
}
