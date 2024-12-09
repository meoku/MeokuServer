package com.upgrade.meoku.user;

import com.upgrade.meoku.user.data.MeokuLoginRequestDTO;

public interface MeokuAuthService {
    public String login(MeokuLoginRequestDTO mlrd);

}
