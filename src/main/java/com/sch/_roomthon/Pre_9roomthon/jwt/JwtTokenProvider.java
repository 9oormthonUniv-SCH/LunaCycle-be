package com.sch._roomthon.Pre_9roomthon.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class JwtTokenProvider {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private long expiration;

    private Key key;

    @PostConstruct
    protected void init() {
        System.out.println("Loaded secret: " + secret); // 로그로 secret 확인
        this.key = Keys.hmacShaKeyFor(secret.getBytes());
    }

    //  토큰 생성
    public String createToken(UUID userId, String role) {
        Claims claims = Jwts.claims().setSubject(userId.toString()); // UUID는 문자열로 변환
        claims.put("role", role);

        Date now = new Date();
        Date expiry = new Date(now.getTime() + expiration);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(expiry)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }


    //  토큰에서 사용자 ID 추출


    // 토큰에서 사용자 ID를 UUID로 반환하는 메서드 예시
    public UUID getUserId(String token) {
        String subject = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
        return UUID.fromString(subject);
    }




    //  토큰 유효성 검사 (예외 로그 포함)
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (SecurityException e) {
            System.out.println(" JWT 서명 오류: " + e.getMessage());
        } catch (MalformedJwtException e) {
            System.out.println(" JWT 형식 오류: " + e.getMessage());
        } catch (ExpiredJwtException e) {
            System.out.println(" JWT 만료됨: " + e.getMessage());
        } catch (UnsupportedJwtException e) {
            System.out.println(" 지원하지 않는 JWT: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.out.println(" 잘못된 JWT: " + e.getMessage());
        }
        return false;
    }
}
