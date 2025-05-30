package kroryi.dagon.service;

import kroryi.dagon.component.CustomUserDetails;
import kroryi.dagon.entity.User;
import kroryi.dagon.repository.MyPageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final MyPageRepository myPageRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = myPageRepository.findByUid(username)
                .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다: " + username));

        return new CustomUserDetails(
                user.getUno(),                      // 유저 ID
                user.getUid(),                      // 로그인 ID (uname)
                user.getUpw(),                      // 비밀번호
                List.of(new SimpleGrantedAuthority("ROLE_" + user.getRole().name())),
                user.getRole().name() // ✅ role 전달// 권한
        );
    }
}
