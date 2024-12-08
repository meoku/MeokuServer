package com.upgrade.meoku.user;

import com.upgrade.meoku.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MeokuAuthServiceimpl implements MeokuAuthService{

    private JwtUtil jwtUtil;
    private  PasswordEncoder encoder;
    private final MeokuUserRepository meokuUserRepository;

    @Override
    public String login(MeokuLoginRequestDTO loginRequestDto) {
        String id = loginRequestDto.getId();
        String password = loginRequestDto.getPassword();
        MeokuUser user = meokuUserRepository.findMeokuUserById(id)
                .orElseThrow(() -> new UsernameNotFoundException("없는 ID 입니다."));

        //비밀번호 체크
        if(!encoder.matches(password, user.getPassword())){
            throw new BadCredentialsException("비밀번호가 틀립니다.");
        }


        return null;
    }

}
