package kroryi.dagon.service;


import kroryi.dagon.DTO.MemberSecurityDTO;
import kroryi.dagon.entity.User;
import kroryi.dagon.enums.LoginType;
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
        log.info("----------------userRequest: {}", userRequest);

        ClientRegistration clientRegistration = userRequest.getClientRegistration();
        String clientName = clientRegistration.getClientName();
        log.info("-------------clientName: {}", clientName);

        OAuth2User oAuth2User = super.loadUser(userRequest);
        Map<String, Object> paramMap = oAuth2User.getAttributes();

        String email = null;
        switch (clientName) {
            case "kakao":
                email = getKakaoEmail(paramMap);
                break;
            case "google":

                break;
            default:
        }

        return generateDTO(email, paramMap);
    }

    public MemberSecurityDTO generateDTO(String email, Map<String, Object> params) {

        Optional<User> result = userRepository.findByEmail(email);

        if (result.isEmpty()) {
            User member = User.builder()
                    .uid(email)
                    .upw(passwordEncoder.encode("1111"))
                    .email(email)
                    .loginType(LoginType.KAKAO)
                    .build();

            userRepository.save(member);

            MemberSecurityDTO memberSecurityDTO = new MemberSecurityDTO(
                    email,
                    "1111",
                    email,
                    false,
                    Arrays.asList(new SimpleGrantedAuthority("ROLE_USER")));
            memberSecurityDTO.setProps(params);
            return memberSecurityDTO;
        } else {
// 기존에 email로 가입되어 있는 경우
// SimpleGrantedAuthority 문자열 기반 권한(ROLE)를 저장하는 클래스
            User member = result.get();
            MemberSecurityDTO memberSecurityDTO = new MemberSecurityDTO(
                    member.getUid(),
                    member.getUpw(),
                    member.getEmail(),
                    member.getLoginType().equals(LoginType.KAKAO),
                    Collections.singletonList(
                            new SimpleGrantedAuthority("ROLE_" + member.getRole().name())
                    )
            );
            return memberSecurityDTO;
        }

    }


    private String getKakaoEmail(Map<String, Object> paramMap) {
        log.info("-------------KAKAO paramMap: {}", paramMap);
        Object value = paramMap.get("kakao_account");
        log.info("-------------value: {}", value);

        LinkedHashMap accountmap = (LinkedHashMap) value;
        String email = (String) accountmap.get("email");
        log.info("-------------email: {}", email);
        return email;

    }
}