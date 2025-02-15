package com.upgrade.meoku.user;

import com.upgrade.meoku.security.JwtUtil;
import com.upgrade.meoku.user.data.MeokuLoginRequestDTO;
import com.upgrade.meoku.user.data.MeokuUser;
import com.upgrade.meoku.user.data.MeokuUserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

import static com.upgrade.meoku.user.data.UserMapper.USER_MAPPER_INSTANCE;

@Service
public class MeokuAuthServiceimpl implements MeokuAuthService{

    private final JwtUtil jwtUtil;
    private final PasswordEncoder encoder;
    private final MeokuUserRepository meokuUserRepository;

    @Autowired
    public MeokuAuthServiceimpl(JwtUtil jwtUtil,
                                PasswordEncoder encoder,
                                MeokuUserRepository meokuUserRepository){
        this.jwtUtil = jwtUtil;
        this.encoder = encoder;
        this.meokuUserRepository = meokuUserRepository;
    }

    @Override
    public Map<String, Object> login(MeokuLoginRequestDTO loginRequestDto) {
        String id = loginRequestDto.getId();
        String password = loginRequestDto.getPassword();
        MeokuUser user = meokuUserRepository.findMeokuUserById(id)
                .orElseThrow(() -> new UsernameNotFoundException("없는 ID 입니다."));

        //비밀번호 체크
        if(!encoder.matches(password, user.getPassword())){
            throw new BadCredentialsException("비밀번호가 틀립니다.");
        }

        MeokuUserDTO userDTO = USER_MAPPER_INSTANCE.userEntityToDto(user);

        String accessToken = jwtUtil.generateAccessToken(userDTO);
        String refreshToken = jwtUtil.generateRefreshToken(userDTO);

        Map<String, Object> result = new HashMap<>();
        result.put("accessToken", accessToken);
        result.put("refreshToken", refreshToken);

        return result;
    }

}
