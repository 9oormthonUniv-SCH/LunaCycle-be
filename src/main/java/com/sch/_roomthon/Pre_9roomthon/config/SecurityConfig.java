package com.sch._roomthon.Pre_9roomthon.config;

import com.sch._roomthon.Pre_9roomthon.jwt.JwtAuthenticationFilter;
import com.sch._roomthon.Pre_9roomthon.jwt.JwtTokenProvider;
import com.sch._roomthon.Pre_9roomthon.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(csrf -> csrf.disable())
                .httpBasic(httpBasic -> httpBasic.disable()) // 기본 로그인 팝업 비활성화
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/oauth/**",
                                "/api/oauth/**",
                                "/swagger-ui/**",
                                "/v3/api-docs/**",
                                "/h2-console/**" ,           //h2 콘솔 접근 허용
                                ("/api/auth/**")

                        ).permitAll() // 로그인/문서 관련 API는 모두 허용
                        .requestMatchers("/api/sleep/**").authenticated()
                        .anyRequest().authenticated() // 그 외에는 JWT 인증 필요
                )
                .headers(headers -> headers.
                        frameOptions(frameOptionsConfig -> frameOptionsConfig.sameOrigin())) //iframe h2 콘솔 접근 허용
                .addFilterBefore(
                        new JwtAuthenticationFilter(jwtTokenProvider, userRepository),
                        UsernamePasswordAuthenticationFilter.class
                )
                .build();
    }
}
