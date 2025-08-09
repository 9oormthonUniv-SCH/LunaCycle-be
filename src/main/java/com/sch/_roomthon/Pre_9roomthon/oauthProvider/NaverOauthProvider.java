package com.sch._roomthon.Pre_9roomthon.oauth;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sch._roomthon.Pre_9roomthon.dto.OauthUserInfo;
import com.sch._roomthon.Pre_9roomthon.dto.response.OauthUserInfoResponse.NaverUserInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Component
@RequiredArgsConstructor
public class NaverOauthProvider {

    @Value("${naver.client-id}")
    private String clientId;

    @Value("${naver.client-secret}")
    private String clientSecret;

    @Value("${naver.redirect-uri}")
    private String redirectUri;

    private final RestTemplate restTemplate = new RestTemplate();

    private final ObjectMapper objectMapper = new ObjectMapper();

    // 1. 로그인 URL 생성
    public String getLoginUrl() {
        return UriComponentsBuilder.fromHttpUrl("https://nid.naver.com/oauth2.0/authorize")
                .queryParam("response_type", "code")
                .queryParam("client_id", clientId)
                .queryParam("redirect_uri", redirectUri)
                .queryParam("state", "naver1234") // CSRF 대응용. 랜덤 문자열도 가능
                .toUriString();
    }

    // 2. Access Token 요청
    public String getAccessToken(String code) {
        String tokenUrl = UriComponentsBuilder.fromHttpUrl("https://nid.naver.com/oauth2.0/token")
                .queryParam("grant_type", "authorization_code")
                .queryParam("client_id", clientId)
                .queryParam("client_secret", clientSecret)
                .queryParam("code", code)
                .queryParam("state", "naver1234")
                .toUriString();

        ResponseEntity<String> response = restTemplate.getForEntity(tokenUrl, String.class);

        try {
            JsonNode jsonNode = objectMapper.readTree(response.getBody());
            return jsonNode.get("access_token").asText(); // 해당부분 NullPointerException 발생합니다.

        } catch (Exception e) {
            throw new RuntimeException("네이버 토큰 파싱 실패", e);
        }
    }

    // 3. 사용자 정보 요청
    public OauthUserInfo getUserInfo(String code) {
        String accessToken = getAccessToken(code);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);

        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(
                "https://openapi.naver.com/v1/nid/me",
                HttpMethod.GET,
                entity,
                String.class
        );

        try {
            JsonNode jsonNode = objectMapper.readTree(response.getBody());
            JsonNode responseNode = jsonNode.get("response");

            String id = responseNode.get("id").asText();
            String email = responseNode.has("email") ? responseNode.get("email").asText() : null;
            String nickname = responseNode.has("name") ? responseNode.get("name").asText() : null;

            return new NaverUserInfo(
                    "naver",
                    id,
                    email,
                    nickname
            );

        } catch (Exception e) {
            throw new RuntimeException("네이버 사용자 정보 파싱 실패", e);
        }
    }
}
