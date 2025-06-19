package kroryi.dagon.service.order;


import kroryi.dagon.DTO.MemberSecurityDTO;
import kroryi.dagon.entity.User;
import kroryi.dagon.enums.LoginType;
import kroryi.dagon.enums.UserLevel;
import kroryi.dagon.enums.UserRole;
import kroryi.dagon.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Log4j2
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        log.info("[OAuth2] userRequest: {}", userRequest);

        ClientRegistration clientRegistration = userRequest.getClientRegistration();
        String clientName = clientRegistration.getClientName();
        log.info("[OAuth2] clientName: {}", clientName);

        OAuth2User oAuth2User = super.loadUser(userRequest);
        Map<String, Object> attributes = oAuth2User.getAttributes();

        String email = null;
        String nickname = null;
        String profileImg = null;
        LoginType loginType = null;

        if ("kakao".equalsIgnoreCase(clientName)) {
            KakaoProfile kakaoProfile = extractKakaoProfile(attributes);
            email = kakaoProfile.email;
            nickname = kakaoProfile.nickname;
            profileImg = kakaoProfile.profileImg;
            loginType = LoginType.KAKAO;
        } else if ("google".equalsIgnoreCase(clientName)) {
            // TODO: 구글 등 다른 공급자 확장시 여기에 추가
            throw new OAuth2AuthenticationException("Google OAuth2 is not implemented yet");
        } else {
            throw new OAuth2AuthenticationException("Unsupported OAuth2 provider: " + clientName);
        }

        return generateDTO(email, nickname, profileImg, loginType, attributes);
    }

    private static class KakaoProfile {
        String email;
        String nickname;
        String profileImg;
        KakaoProfile(String email, String nickname, String profileImg) {
            this.email = email;
            this.nickname = nickname;
            this.profileImg = profileImg;
        }
    }

    private KakaoProfile extractKakaoProfile(Map<String, Object> attributes) {
        Object accountObj = attributes.get("kakao_account");
        if (!(accountObj instanceof Map)) {
            throw new OAuth2AuthenticationException("Invalid kakao_account structure");
        }
        Map<?, ?> accountMap = (Map<?, ?>) accountObj;
        String email = (String) accountMap.get("email");
        Object profileObj = accountMap.get("profile");
        String nickname = null;
        String profileImg = null;
        if (profileObj instanceof Map) {
            Map<?, ?> profileMap = (Map<?, ?>) profileObj;
            nickname = (String) profileMap.get("nickname");
            profileImg = (String) profileMap.get("profile_image_url");
        }
        return new KakaoProfile(email, nickname, profileImg);
    }

    public MemberSecurityDTO generateDTO(String email, String nickname, String profileImg, LoginType loginType, Map<String, Object> attributes) {
        if (email == null) {
            throw new OAuth2AuthenticationException("Email not found from OAuth2 provider");
        }
        Optional<User> result = userRepository.findByEmail(email);
        if (result.isEmpty()) {
            User member = User.builder()
                    .uid(email)
                    .upw(passwordEncoder.encode("1111"))
                    .email(email)
                    .loginType(loginType)
                    .phone("")
                    .points(0)
                    .nickname(nickname != null ? nickname : "")
                    .profileImg(profileImg)
                    .role(UserRole.USER)
                    .level(UserLevel.SILVER)
                    .isActive(true)
                    .build();
            userRepository.save(member);
            MemberSecurityDTO memberSecurityDTO = new MemberSecurityDTO(
                    email,
                    "1111",
                    email,
                    loginType == LoginType.KAKAO,
                    Arrays.asList(new SimpleGrantedAuthority("ROLE_USER")));
            memberSecurityDTO.setProps(attributes);
            return memberSecurityDTO;
        } else {
            User member = result.get();
            MemberSecurityDTO memberSecurityDTO = new MemberSecurityDTO(
                    member.getUid(),
                    member.getUpw(),
                    member.getEmail(),
                    member.getLoginType() == LoginType.KAKAO,
                    Collections.singletonList(
                            new SimpleGrantedAuthority("ROLE_" + member.getRole().name())
                    )
            );
            return memberSecurityDTO;
        }
    }
}