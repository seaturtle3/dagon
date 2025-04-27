package kroryi.dagon.service;

import kroryi.dagon.entity.Admin;
import kroryi.dagon.repository.AdminRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminDetailsService implements UserDetailsService {

    private final AdminRepository adminRepository; // 어드민 전용 Repository

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Admin admin = adminRepository.findByAid(username)
                .orElseThrow(() -> new UsernameNotFoundException("관리자를 찾을 수 없습니다: " + username));

        return new org.springframework.security.core.userdetails.User(
                admin.getAid(),
                admin.getApw(),
                List.of(new SimpleGrantedAuthority("ROLE_ADMIN"))
        );
    }
}
