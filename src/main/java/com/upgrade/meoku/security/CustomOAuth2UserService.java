package com.upgrade.meoku.security;

import com.upgrade.meoku.user.MeokuAuthService;
import com.upgrade.meoku.user.MeokuUserRepository;
import com.upgrade.meoku.user.data.MeokuUser;
import com.upgrade.meoku.user.data.MeokuUserDTO;
import com.upgrade.meoku.user.data.UserMapper;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.upgrade.meoku.user.data.UserMapper.USER_MAPPER_INSTANCE;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final MeokuUserRepository meokuUserRepository;
    private final JwtUtil jwtUtil;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest request) throws OAuth2AuthenticationException {
        //이 loadUser 함수를 호출하는 OAuth2LoginAuthenticationProvider가 직전에 발급받은 Acceess Token이 들어있는
        //OAuth2UserRequest로 사용자 정보를 외부에서 가져옴
        OAuth2User oAuth2User = new DefaultOAuth2UserService().loadUser(request);

        String registrationId = request.getClientRegistration().getRegistrationId();
        Map<String, Object> attributes;
        String email;

        // 네이버 소셜 로그인 일 때
        if (registrationId.equals("naver")) {
            attributes = oAuth2User.getAttribute("response");
            System.out.println(attributes.toString());
        } else if (registrationId.equals("kakao")) {
            attributes = oAuth2User.getAttributes();// 카카오는 response 안감싸져있음
            System.out.println("카카오 로그인");
        } else {
            throw new OAuth2AuthenticationException("Unsupported provider");
        }

        // 소셜로그인 후 id 받아왔는지 가볍게 확인만
        String id = String.valueOf(attributes.get("id")); // 안전하게 Long → String 변환(null-safe)
        System.out.println(registrationId + " 소셜 로그인 id : " + id);
        // 소셜로그인으로 가져온 정보중 id는 불변으로 거의무조건 받아 오기 때문에 이거 기준으로 저장하고 검색한다
        Optional<MeokuUser> userOptional = meokuUserRepository.findMeokuUserById(id);
        MeokuUser meokuUser = userOptional.orElse(null);
        // email 기준으로 없는 아이디라면 회원가입
        if (!userOptional.isPresent()) {
            MeokuUser newUser = new MeokuUser();
            newUser.setId(id);
            newUser.setProvider(registrationId);
//            newUser.setEmail((String) attributes.get("email"));
//            newUser.setName((String) attributes.get("name"));
//            newUser.setAgeRange((String) attributes.get("age"));
//            newUser.setBirthYear((String) attributes.get("birthYear"));
//            newUser.setNickname(meokuAuthService.generateUniqueNickname());

            meokuUserRepository.save(newUser);

            meokuUser = newUser;
        }

        MeokuUserDTO userDTO = USER_MAPPER_INSTANCE.userEntityToDto(meokuUser);

        // loadUser()
        Map<String,Object> principalAttrs = new HashMap<>();
        principalAttrs.put("id", userDTO.getId());
        principalAttrs.put("userDTO", userDTO); // 그냥 객체로 넣어도 이 요청 동안은 OK

        return new DefaultOAuth2User(
                List.of(new SimpleGrantedAuthority("ROLE_USER")),
                principalAttrs,
                "id"
        );
//        // JWT 발급
//        Map<String, Object> tokenMap = jwtUtil.generateTokenMap(userDTO);
//
//        // JWT를 HttpOnly 쿠키로 클라이언트에 전달
//        HttpServletResponse response = ((ServletRequestAttributes)
//                RequestContextHolder.getRequestAttributes()).getResponse();
//
//        ResponseCookie accessCookie = ResponseCookie.from("access_token", (String) tokenMap.get("access_token"))
//                .httpOnly(true)
//                .secure(true) // HTTPS 환경이면 true
//                .path("/")
//                .maxAge(ACCESS_TOKEN_EXPIRATION_TIME)
//                .build();
//
//        ResponseCookie refreshCookie = ResponseCookie.from("refresh_token", (String) tokenMap.get("refresh_token"))
//                .httpOnly(true)
//                .secure(true)
//                .path("/")
//                .maxAge(REFRESH_TOKEN_EXPIRATION_TIME)
//                .build();
//
//        response.addHeader("Set-Cookie", accessCookie.toString());
//        response.addHeader("Set-Cookie", refreshCookie.toString());
//
//        return oAuth2User;
    }
}