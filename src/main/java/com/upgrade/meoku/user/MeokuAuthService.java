package com.upgrade.meoku.user;

import com.upgrade.meoku.user.data.MeokuLoginRequestDTO;
import com.upgrade.meoku.user.data.MeokuUserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public interface MeokuAuthService {
    public String login(MeokuLoginRequestDTO mlrd);
}
