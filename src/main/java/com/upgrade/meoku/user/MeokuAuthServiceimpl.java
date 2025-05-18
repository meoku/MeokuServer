package com.upgrade.meoku.user;

import com.upgrade.meoku.security.JwtUtil;
import com.upgrade.meoku.user.data.*;
import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;

import static com.upgrade.meoku.user.data.UserMapper.USER_MAPPER_INSTANCE;

@Service
@RequiredArgsConstructor
public class MeokuAuthServiceimpl implements MeokuAuthService{

    private final JwtUtil jwtUtil;
    private final PasswordEncoder encoder;
    private final MeokuUserRepository meokuUserRepository;
    private final MeokuUesrRoleRepository meokuUesrRoleRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Override
    public MeokuUserDTO loadMeokuUserDTO(String username) {
        MeokuUser user = meokuUserRepository.findMeokuUserById(username)
                .orElseThrow(() -> new UsernameNotFoundException("없는 ID 입니다."));

        return USER_MAPPER_INSTANCE.userEntityToDto(user);
    }

    @Override
    public Map<String, Object> login(MeokuLoginRequestDTO loginRequestDto) {
        String id = loginRequestDto.getId();
        String password = loginRequestDto.getPassword();

        MeokuUserDTO userDTO = this.loadMeokuUserDTO(id);

        //비밀번호 체크
        if(!encoder.matches(password, userDTO.getPassword())){
            throw new BadCredentialsException("비밀번호가 틀립니다.");
        }

        return jwtUtil.generateTokenMap(userDTO);
    }

    @Override
    public boolean checkDuplicateId(String checkedId) {
        return meokuUserRepository.findMeokuUserById(checkedId).isPresent();
    }

    @Override
    public MeokuUserDTO signup(MeokuSignUpRequestDTO signUpRequestDTO) throws Exception {
        // ID 중복체크는 타이밍상 중복 될 수있으므로 한번 더 체크
        if (meokuUserRepository.existsById(signUpRequestDTO.getId())){
            throw new Exception("이미 사용 중인 ID입니다.");
        }

        MeokuUser meokuUser = new MeokuUser();
        //1. 비밀번호 암호화
        String encodedPassword = passwordEncoder.encode(signUpRequestDTO.getPassword());
        meokuUser.setId(signUpRequestDTO.getId());
        meokuUser.setPassword(encodedPassword);
        meokuUser.setAge(signUpRequestDTO.getAge());
        meokuUser.setName(signUpRequestDTO.getName());
        meokuUser.setSex(signUpRequestDTO.getSex());
        meokuUser.setNickname(this.generateUniqueNickname()); // nickname 생성해서 주입
        //2. 유저 저장
        MeokuUser savedUser = meokuUserRepository.save(meokuUser);

        MeokuUserRole meokuUserRole = new MeokuUserRole();
        meokuUserRole.setUser(savedUser);
        meokuUserRole.setRoleName("USER");
        //3. 유저 권한 저장
        meokuUesrRoleRepository.save(meokuUserRole);

        return USER_MAPPER_INSTANCE.userEntityToDto(savedUser);
    }

    @Override
    public Map<String, Object> refreshAccessToken(String refreshToken) {
        try{
            // refresh token 검증
            String id = jwtUtil.validateRefreshToken(refreshToken);

            // 유저 정보 가져오기
            MeokuUserDTO userDTO = this.loadMeokuUserDTO(id);

            return jwtUtil.generateTokenMap(userDTO);
        }catch(JwtException e){
            throw e;
        }catch(IllegalArgumentException e){
            throw e;
        }catch(Exception e){
            throw e;
        }
    }

    @Override
    public String generateUniqueNickname() {
        // 최대 3번까지 중복 체크 시도
        int maxRetry = 3;
        for (int i = 0; i < maxRetry; i++) {
            String nickname = NicknameGenerator.generate();
            if (!meokuUserRepository.existsByNickname(nickname)) {
                return nickname;
            }
        }

        // fallback: 3회시도 이후에도 안되면 뒤에 랜덤 숫자 붙이기
        for (int i = 0; i < 100; i++) {
            String fallback = NicknameGenerator.generate() + i;
            if (!meokuUserRepository.existsByNickname(fallback)) {
                return fallback;
            }
        }

        throw new RuntimeException("닉네임을 생성할 수 없습니다. 너무 많이 중복됨");
    }

}
