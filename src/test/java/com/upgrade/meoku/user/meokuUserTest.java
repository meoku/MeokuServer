package com.upgrade.meoku.user;

import com.upgrade.meoku.user.data.MeokuUser;
import com.upgrade.meoku.user.data.MeokuUserDTO;
import com.upgrade.meoku.user.data.MeokuUserDetails;
import com.upgrade.meoku.user.data.MeokuUserRole;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static com.upgrade.meoku.user.data.UserMapper.USER_MAPPER_INSTANCE;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class meokuUserTest {

    @Autowired
    private MeokuUserRepository userRepository;
    @Autowired
    private MeokuUesrRoleRepository userRoleRepository;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private MeokuUserDetailsService userDetailsService;

    @Test
    @DisplayName("회원가입")
    @Transactional
    @Commit
    public void joinMemberTest(){
        MeokuUser meokuUser = new MeokuUser();
        //1. 비밀번호 암호화
        String encodedPassword = passwordEncoder.encode("mkadmin");
        meokuUser.setId("mkadmin");
        meokuUser.setPassword(encodedPassword);
        meokuUser.setAge(59);
        meokuUser.setName("관리자");
        meokuUser.setSex("M");
        meokuUser.setNickname("먹구관리자");
        //2. 유저 저장
        userRepository.save(meokuUser);

        MeokuUserRole meokuUserRole = new MeokuUserRole();
        meokuUserRole.setUser(meokuUser);
        meokuUserRole.setRoleName("ROLE_ADMIN");
        //3. 유저 권한 저장
        userRoleRepository.save(meokuUserRole);

        // 4. 테스트 확인
        assertNotNull(meokuUser.getUserId());
        assertEquals("mkadmin", meokuUser.getId());
        assertTrue(passwordEncoder.matches("mkadmin", meokuUser.getPassword()));

        assertNotNull(meokuUserRole.getRoleId());
        assertEquals("ROLE_ADMIN", meokuUserRole.getRoleName());
        assertEquals(meokuUser.getUserId(), meokuUserRole.getUser().getUserId());

    }

    @Test
    @DisplayName("User 가져오기, Entity to Dto Test")
    @Transactional
    public void getUserTest(){
        Optional<MeokuUser> savedUser = userRepository.findMeokuUserById("mkadmin");

        // 조회된 데이터 확인
        assertTrue(savedUser.isPresent(), "유저 없음!");
        assertEquals("mkadmin", savedUser.get().getId());
        //assertEquals("", savedUser.get().getEmail());
        assertEquals("관리자", savedUser.get().getName());
        assertEquals("ROLE_ADMIN", savedUser.get().getUserRoleList().get(0).getRoleName());

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
}
