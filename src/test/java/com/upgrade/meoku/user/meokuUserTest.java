package com.upgrade.meoku.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.upgrade.meoku.user.data.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.annotation.Commit;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.hamcrest.Matchers.containsString;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.Optional;

import static com.upgrade.meoku.user.data.UserMapper.USER_MAPPER_INSTANCE;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureMockMvc
public class meokuUserTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MeokuUserRepository userRepository;
    @Autowired
    private MeokuUesrRoleRepository userRoleRepository;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private MeokuUserDetailsService userDetailsService;
    @Autowired
    private MeokuAuthService authService;

    @Test
    @DisplayName("회원가입")
    public void generateNickname(){
        String newNickname = authService.generateUniqueNickname();
        System.out.println(newNickname);
    }

    @Test
    @DisplayName("회원가입 테스트 - 오류 테스트")
    @Transactional// 이걸 해야 실제로 데이터가 안남음
    public void testSignupSuccess() throws Exception {
        MeokuSignUpRequestDTO signUpRequest = new MeokuSignUpRequestDTO();
        signUpRequest.setId("테스터");
//        signUpRequest.setEmail("test@example.com");
        signUpRequest.setPassword("Test@1234");
        signUpRequest.setName("테스터");
//        signUpRequest.setNickname("tester");
        signUpRequest.setSex("M");
        signUpRequest.setAge(25);
        mockMvc.perform(post("/api/v1/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(signUpRequest)))
                        .andExpect(status().isBadRequest())
//                        .andExpect(content().string(containsString("잘못된 성별 표기입니다.")));
                        .andExpect(content().string(containsString("아이디는 5~20자의 영문 또는 숫자여야 합니다.")));

//                .andExpect(status().isOk())
//                .andExpect(content().string(containsString("회원가입 성공! ID:")));
    }
    @Test
    @DisplayName("회원가입해보기")
    @Transactional
    @Commit
    public void joinMember(){
        MeokuUser meokuUser = new MeokuUser();
        //1. 비밀번호 암호화
        String encodedPassword = passwordEncoder.encode("testuser");
        meokuUser.setId("testuser");
        meokuUser.setPassword(encodedPassword);
        meokuUser.setAge(59);
        meokuUser.setName("테스트회원");
        meokuUser.setSex("M");
        meokuUser.setNickname("테스트회원");
        //2. 유저 저장
        userRepository.save(meokuUser);

        MeokuUserRole meokuUserRole = new MeokuUserRole();
        meokuUserRole.setUser(meokuUser);
        meokuUserRole.setRoleName("USER");
        //3. 유저 권한 저장
        userRoleRepository.save(meokuUserRole);

        // 4. 테스트 확인
        assertNotNull(meokuUser.getUserId());
        assertEquals("testuser", meokuUser.getId());
        assertTrue(passwordEncoder.matches("testuser", meokuUser.getPassword()));

        assertNotNull(meokuUserRole.getRoleId());
        assertEquals("USER", meokuUserRole.getRoleName());
        assertEquals(meokuUser.getUserId(), meokuUserRole.getUser().getUserId());

    }

    @Test
    @DisplayName("User 가져오기, Entity to Dto Test")
    @Transactional
    public void getUserTest(){
        Optional<MeokuUser> savedUser = userRepository.findMeokuUserById("Mkadmin");

        // 조회된 데이터 확인
//        assertTrue(savedUser.isPresent(), "유저 없음!");
//        assertEquals("testuser", savedUser.get().getId());
//        assertEquals("", savedUser.get().getEmail());
//        assertEquals("관리자", savedUser.get().getName());
//        assertEquals("ROLE_USER", savedUser.get().getUserRoleList().get(0).getRoleName());

        MeokuUserDTO meokuUserDTO = USER_MAPPER_INSTANCE.userEntityToDto(savedUser.get());
        System.out.println(meokuUserDTO.toString());
    }

    @Test
    @DisplayName("jwt filter에서 사용될 유저 확인 메소드 테스트")
    public void getUserByIdTest(){
        MeokuUserDetails meokuUserDetails = (MeokuUserDetails) userDetailsService.loadUserByUsername("mkadmin");

        assertNotNull(meokuUserDetails.getUsername());
        System.out.println(meokuUserDetails.getAuthorities().toString());
    }

    @Test
    @DisplayName("Secret Key 생성")
    public void getSecretKeyTest(){
        SecureRandom random = new SecureRandom();
        byte[] key = new byte[32]; // 32바이트 키
        random.nextBytes(key);
        String secretKey = Base64.getEncoder().encodeToString(key);
        System.out.println(secretKey);
    }
}
