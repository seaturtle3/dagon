package kroryi.dagon.config;

import kroryi.dagon.handler.CustomSocialLoginSuccessHandler;

import kroryi.dagon.service.ApiKeyService;


import kroryi.dagon.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
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

import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final kroryi.dagon.service.pages.admin.AdminDetailsService adminDetailsService;
    private final kroryi.dagon.service.order.CustomUserDetailsService userDetailsService;
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
                        .requestMatchers(
                                "/swagger-ui/**",
                                "/v3/api-docs/**",
                                "/swagger-resources/**",
                                "/webjars/**",
                                "/**",
                                "/web/users/register",
                                "/css/**",
                                "/js/**",
                                "/img/**",
                                "/api/auth/login",
                                "/api/users/me",
                                "/admin/login",
                                "/register",
                                "/admin/registration"



                        ).permitAll()
                        .requestMatchers("/api/mypage").authenticated()
                        .requestMatchers("/partner/api").authenticated()
                        .requestMatchers("/partner/review").authenticated()
                        .requestMatchers("/login/oauth2/code/kakao").permitAll()

                        .anyRequest().authenticated()
                )
                .formLogin(formLogin -> { // 기존 폼 로그인 설정 유지
                    formLogin
                            .loginPage("/login")
                            .defaultSuccessUrl("/index", true)
                            .failureUrl("/login?error")
                            .permitAll();
                })
                .oauth2Login(
                        (login) -> login.loginPage("/login")
                                .successHandler(socialLoginSuccessHandler()))
                .logout(logout -> logout
                        .logoutSuccessUrl("/login?logout")
                        .permitAll()
                )
                .addFilterBefore(apiKeyFilter, UsernamePasswordAuthenticationFilter.class) // ✅ 이게 핵심!!
        ;

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
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        DaoAuthenticationProvider userProvider = new DaoAuthenticationProvider();
        userProvider.setUserDetailsService(userDetailsService);
        userProvider.setPasswordEncoder(passwordEncoder());

        DaoAuthenticationProvider adminProvider = new DaoAuthenticationProvider();
        adminProvider.setUserDetailsService(adminDetailsService);
        adminProvider.setPasswordEncoder(passwordEncoder());

        return new ProviderManager(List.of(userProvider, adminProvider));
    }

}