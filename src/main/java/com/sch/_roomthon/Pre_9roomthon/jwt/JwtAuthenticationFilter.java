package com.sch._roomthon.Pre_9roomthon.jwt;

import com.sch._roomthon.Pre_9roomthon.entity.UserEntity;
import com.sch._roomthon.Pre_9roomthon.repository.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.UUID;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final Logger log = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;

    /**
     * 매 요청마다 실행되는 JWT 인증 필터
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {


        // 1. 요청 헤더에서 토큰 추출
        System.out.println("[FILTER] JwtAuthenticationFilter 실행됨");

        String token = getTokenFromRequest(request);
        System.out.println("[FILTER] Authorization 헤더에서 추출한 토큰: " + token);
        log.info("[JWT Filter] Token: {}", token);

        // 2. 토큰이 존재하고 유효한 경우
        if (token != null && jwtTokenProvider.validateToken(token)) {
            log.info("[JWT Filter] Token is valid.");

            // 3. 토큰에서 사용자 ID 추출
            UUID userId = jwtTokenProvider.getUserId(token);
            log.info("[JWT Filter] Extracted User ID: {}", userId);

            // 4. 사용자 조회
            UserEntity user = userRepository.findById(userId).orElse(null);
            if (user != null) {
                log.info("[JWT Filter] User found: {}", user.getEmail());

                // 5. 인증 객체 생성 및 등록
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(
                                user,
                                null,
                                user.getAuthorities()
                        );

                authentication.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                );

                SecurityContextHolder.getContext().setAuthentication(authentication);

                // 6. 컨트롤러에서 사용할 수 있도록 request에 userId 저장
                request.setAttribute("userId", userId);
            } else {
                log.warn("[JWT Filter] User not found in DB.");
            }
        } else {
            log.warn("[JWT Filter] Token is null or invalid.");
        }

        // 다음 필터로 전달
        filterChain.doFilter(request, response);

    }

    /**
     * Authorization 헤더에서 Bearer 토큰 추출
     */
    private String getTokenFromRequest(HttpServletRequest request) {
        String bearer = request.getHeader("Authorization");
        if (bearer != null && bearer.startsWith("Bearer ")) {
            return bearer.substring(7); // "Bearer " 이후의 문자열만 추출
        }
        return null;
    }
}