package com.upgrade.meoku.user;

import com.upgrade.meoku.user.data.MeokuLoginRequestDTO;
import com.upgrade.meoku.user.data.MeokuUserDTO;

import java.util.Map;

public interface MeokuAuthService {
     MeokuUserDTO loadMeokuUserDTO(String username);
     Map<String, Object> login(MeokuLoginRequestDTO mlrd);
     Map<String, Object> refreshAccessToken(String refreshToken);


}
