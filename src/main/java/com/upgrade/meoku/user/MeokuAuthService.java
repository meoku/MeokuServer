package com.upgrade.meoku.user;

import com.upgrade.meoku.user.data.MeokuLoginRequestDTO;
import com.upgrade.meoku.user.data.MeokuSignUpRequestDTO;
import com.upgrade.meoku.user.data.MeokuUserDTO;

import java.util.Map;

public interface MeokuAuthService {
     MeokuUserDTO loadMeokuUserDTO(String username);
     Map<String, Object> login(MeokuLoginRequestDTO mlrd);
     boolean checkDuplicateId(String checkedId);
     MeokuUserDTO signup(MeokuSignUpRequestDTO signUpRequestDTO);
     Map<String, Object> refreshAccessToken(String refreshToken);
}
