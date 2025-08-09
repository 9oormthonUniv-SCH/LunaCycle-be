package com.sch._roomthon.Pre_9roomthon.controller;

import com.sch._roomthon.Pre_9roomthon.entity.UserEntity;
import com.sch._roomthon.Pre_9roomthon.jwt.JwtTokenProvider;
import com.sch._roomthon.Pre_9roomthon.oauthProvider.KakaoOauthProvider;
import com.sch._roomthon.Pre_9roomthon.service.UserService;
import com.sch._roomthon.Pre_9roomthon.dto.OauthUserInfo;
import com.sch._roomthon.Pre_9roomthon.oauth.NaverOauthProvider;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/oauth")
@RequiredArgsConstructor
public class OauthController {

  private final KakaoOauthProvider kakaoOauthProvider;
  private final UserService userService;
  private final JwtTokenProvider jwtTokenProvider;
  private final NaverOauthProvider naverOauthProvider;

  // login-url
  @GetMapping("/login-url/kakao")
  public ResponseEntity<Map<String, String>> getKakaoLoginUrl() {
    String loginUrl = kakaoOauthProvider.getAuthorizationUrl();
    return ResponseEntity.ok(Map.of("login_url", loginUrl));
  }

  // 카카오 Redirect URI에서 호출되는 엔드포인트
  @GetMapping("/callback/kakao")
  public ResponseEntity<Map<String, String>> kakaoCallback(@RequestParam String code) {
    var tokenResponse = kakaoOauthProvider.getAccessToken(code);
    OauthUserInfo userInfo = kakaoOauthProvider.getUserInfo(tokenResponse.getAccessToken());

    if (userInfo == null) {
      return ResponseEntity.internalServerError()
              .body(Map.of("error", "사용자 정보를 불러올 수 없습니다."));
    }

    // UserEntity로 받기
    UserEntity user = userService.loginOrRegister(userInfo);

    // 토큰 발급 따로
    String jwtToken = jwtTokenProvider.createToken(user.getId(), user.getRole());

    return ResponseEntity.ok()
            .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken)
            .body(Map.of("token", jwtToken, "email", user.getUsername()));
  }


  @GetMapping("/login-url/naver")
  public Map<String, String> naverLoginUrl() {
    return Map.of("login_url", naverOauthProvider.getLoginUrl());
  }

  @GetMapping("/callback/naver")
  public ResponseEntity<?> naverCallback(@RequestParam String code) {
    OauthUserInfo userInfo = naverOauthProvider.getUserInfo(code);
    UserEntity user = userService.loginOrRegister(userInfo);
    String token = jwtTokenProvider.createToken(user.getId(), user.getRole());
    return ResponseEntity.ok(Map.of("token", token, "email", user.getUsername()));
  }

}
