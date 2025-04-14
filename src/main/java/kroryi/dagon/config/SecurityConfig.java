package kroryi.dagon.config;

import kroryi.dagon.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final UserDetailsService userDetailsService;
    private final JwtUtil jwtUtil; // JWT 유틸리티 주입

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable) // API 서버이므로 CSRF 비활성화 (다른 방식으로 보안 처리 필요)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/**", "/login", "/web/users/register", "/css/**", "/js/**", "/img/**").permitAll() // 기존 경로는 허용
                        .requestMatchers("/api/auth/login").permitAll() // API 로그인 경로는 모두 허용
                        .anyRequest().authenticated() // 나머지 API 경로는 인증 필요
                )
                .formLogin(formLogin -> { // 기존 폼 로그인 설정 유지
                    formLogin
                            .loginPage("/login")
                            .defaultSuccessUrl("/index", true)
                            .failureUrl("/login?error")
                            .permitAll();
                })
                .logout(logout -> logout
                        .logoutSuccessUrl("/login?logout")
                        .permitAll()
                )
        // JWT 인증 필터 추가 (구현 후)
        // .addFilterBefore(new JwtAuthFilter(jwtUtil, userDetailsService), UsernamePasswordAuthenticationFilter.class)
        ;

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return userDetailsService;
    }
}