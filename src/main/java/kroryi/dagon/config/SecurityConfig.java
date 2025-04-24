package kroryi.dagon.config;

import kroryi.dagon.handler.CustomSocialLoginSuccessHandler;
import kroryi.dagon.service.ApiKeyService;
import kroryi.dagon.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.http.HttpStatus;

@EnableGlobalMethodSecurity(prePostEnabled = true)
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final UserDetailsService userDetailsService;
    private final JwtUtil jwtUtil; // JWT 유틸리티 주입

    @Bean
    public ApiKeyFilter apiKeyFilter(ApiKeyService apiKeyService, JwtUtil jwtTokenUtil) {
        return new ApiKeyFilter(apiKeyService, jwtTokenUtil);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, ApiKeyFilter apiKeyFilter) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        // Swagger & 정적 자원 허용
                        .requestMatchers(
                                "/swagger-ui/**",
                                "/v3/api-docs/**",
                                "/api-docs",
                                "/api-docs/swagger-config",
                                "/swagger-resources/**",
                                "/webjars/**",
                                "/favicon.ico",
                                "/css/**", "/js/**", "/img/**"
                        ).permitAll()

                        // 로그인/회원가입 허용
                        .requestMatchers(
                                "/login", "/admin/login", "/register", "/web/users/register",
                                "/api/auth/login", "/api/users/me",
                                "/login/oauth2/code/kakao"
                        ).permitAll()

                        // ✅ 관리자 API는 인증 필요
                        .requestMatchers("/api/admin/**").authenticated()

                        // ✅ 사용자 웹 경로 허용
                        .requestMatchers(
                                "/web/**",
                                "/logout",
                                "/notices/**",
                                "/faq/**",
                                "/event/**",
                                "/mypage/**",
                                "/partner/**",
                                "/community/**"
                        ).permitAll()

                        // 나머지는 인증 필요
                        .anyRequest().authenticated()
                )

                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED)) // Swagger 401 응답 설정
                )

                // 기존 폼 로그인 설정 유지
                .formLogin(formLogin -> formLogin
                        .loginPage("/login")
                        .defaultSuccessUrl("/index", true)
                        .failureUrl("/login?error")
                        .permitAll()
                )

                // 소셜 로그인 유지
                .oauth2Login(login -> login
                        .loginPage("/login")
                        .successHandler(socialLoginSuccessHandler())
                )

                .logout(logout -> logout
                        .logoutSuccessUrl("/login?logout")
                        .permitAll()
                )

                .addFilterBefore(apiKeyFilter, UsernamePasswordAuthenticationFilter.class); // ✅ 이게 핵심!!

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationSuccessHandler socialLoginSuccessHandler() {
        return new CustomSocialLoginSuccessHandler(passwordEncoder());
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager(); // ✅ 최신 방식
    }

}