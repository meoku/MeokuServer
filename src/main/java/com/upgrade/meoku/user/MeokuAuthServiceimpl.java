package com.upgrade.meoku.user;

import com.upgrade.meoku.security.JwtUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class MeokuAuthServiceimpl implements MeokuAuthService{

    private JwtUtil jwtUtil;
    private  PasswordEncoder encoder;


    @Override
    public String login(MeokuLoginRequestDto loginRequestDto) {
        String email = loginRequestDto.getEmail();
        String password = loginRequestDto.getPassword();

        return null;
    }

}
