package com.upgrade.meoku.user;

import com.upgrade.meoku.user.data.MeokuUser;
import com.upgrade.meoku.user.data.MeokuUserDTO;
import com.upgrade.meoku.user.data.MeokuUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.upgrade.meoku.user.data.UserMapper.USER_MAPPER_INSTANCE;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MeokuUserDetailsService implements UserDetailsService {

    private final MeokuUserRepository meokuUserRepository;

    // jwtFilter에서 사용될 유저 확인 메소드
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        MeokuUser user = meokuUserRepository.findMeokuUserById(username)
                .orElseThrow(() -> new UsernameNotFoundException("없는 ID 입니다."));

        MeokuUserDTO userDTO = USER_MAPPER_INSTANCE.userEntityToDto(user);

        return new MeokuUserDetails(userDTO);
    }
}
