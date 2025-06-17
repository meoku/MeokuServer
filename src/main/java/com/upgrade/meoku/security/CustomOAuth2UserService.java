package com.upgrade.meoku.security;

import com.upgrade.meoku.user.MeokuUserRepository;
import com.upgrade.meoku.user.data.MeokuUser;
import com.upgrade.meoku.user.data.MeokuUserDTO;
import com.upgrade.meoku.user.data.UserMapper;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Map;

import static com.upgrade.meoku.user.data.UserMapper.USER_MAPPER_INSTANCE;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    @Value("${ACCESS_TOKEN_EXPIRATION_TIME}")
    private long ACCESS_TOKEN_EXPIRATION_TIME;
    @Value("${REFRESH_TOKEN_EXPIRATION_TIME}")
    private long REFRESH_TOKEN_EXPIRATION_TIME;

    private final MeokuUserRepository meokuUserRepository;
    private final JwtUtil jwtUtil;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest request) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = new DefaultOAuth2UserService().loadUser(request);

        String registrationId = request.getClientRegistration().getRegistrationId();
        Map<String, Object> attributes;
        String email;

        // 네이버 소셜 로그인 일 때
        if (registrationId.equals("naver")) {
            attributes = (Map<String, Object>) oAuth2User.getAttribute("response");
            email = (String) attributes.get("email");
        } else {
            throw new OAuth2AuthenticationException("Unsupported provider");
        }

        // 이부분 약식이며 추후 보완 해야함 (없으면 회원가입 처리하도록)
        MeokuUser user = meokuUserRepository.findMeokuUserByEmail(email).get();

        MeokuUserDTO userDTO = USER_MAPPER_INSTANCE.userEntityToDto(user);

        // JWT 발급
        Map<String, Object> tokenMap = jwtUtil.generateTokenMap(userDTO);

        // JWT를 HttpOnly 쿠키로 클라이언트에 전달
        HttpServletResponse response = ((ServletRequestAttributes)
                RequestContextHolder.getRequestAttributes()).getResponse();

        ResponseCookie accessCookie = ResponseCookie.from("access_token", (String) tokenMap.get("accessToken"))
                .httpOnly(true)
                .secure(true) // HTTPS 환경이면 true
                .path("/")
                .maxAge(ACCESS_TOKEN_EXPIRATION_TIME)
                .build();

        ResponseCookie refreshCookie = ResponseCookie.from("refresh_token", (String) tokenMap.get("refreshToken"))
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(REFRESH_TOKEN_EXPIRATION_TIME)
                .build();

        response.addHeader("Set-Cookie", accessCookie.toString());
        response.addHeader("Set-Cookie", refreshCookie.toString());

        return oAuth2User;
    }
}