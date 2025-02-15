package com.upgrade.meoku.user;

import com.upgrade.meoku.user.data.MeokuLoginRequestDTO;

import java.util.Map;

public interface MeokuAuthService {
     Map<String, Object> login(MeokuLoginRequestDTO mlrd);
}
