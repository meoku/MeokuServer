package com.upgrade.meoku.security;

import com.upgrade.meoku.user.MeokuUserRepository;
import com.upgrade.meoku.user.data.MeokuUser;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
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

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final MeokuUserRepository meokuUserRepository;
    private final JwtUtil jwtUtil;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest request) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = new DefaultOAuth2UserService().loadUser(request);
        Map<String, Object> attributes = oAuth2User.getAttribute("response");
        String email = (String) attributes.get("email");

//        MeokuUser user = meokuUserRepository.findMeokuUserByEmail(email)
//                .orElseGet(() -> userRepository.save(new User(email)));

        // JWT 발급
        String token = "";

//                jwtUtil.createAccessToken(user);

        // JWT를 HttpOnly 쿠키로 클라이언트에 전달
        HttpServletResponse response = ((ServletRequestAttributes)
                RequestContextHolder.getRequestAttributes()).getResponse();

        ResponseCookie cookie = ResponseCookie.from("access_token", token)
                .httpOnly(true)
                .secure(false) // HTTPS면 true
                .path("/")
                .maxAge(3600)
                .build();

        response.addHeader("Set-Cookie", cookie.toString());

        return oAuth2User;
    }
}