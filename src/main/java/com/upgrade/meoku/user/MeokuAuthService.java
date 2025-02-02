package com.upgrade.meoku.user;

import com.upgrade.meoku.user.data.MeokuLoginRequestDTO;
import com.upgrade.meoku.user.data.MeokuUserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public interface MeokuAuthService {
    public String login(MeokuLoginRequestDTO mlrd);
    // jwtFilter에서 사용될 유저 확인 메소드
    public MeokuUserDetails loadUserById(String id) throws UsernameNotFoundException;
}
