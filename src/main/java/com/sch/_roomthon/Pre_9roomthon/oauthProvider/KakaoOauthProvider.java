package com.sch._roomthon.Pre_9roomthon.oauthProvider;

import com.sch._roomthon.Pre_9roomthon.dto.response.OauthAccessTokenResponse;
import com.sch._roomthon.Pre_9roomthon.dto.OauthUserInfo;
import com.sch._roomthon.Pre_9roomthon.dto.response.OauthUserInfoResponse.KakaoUserInfoResponse;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;


@Service("kakao") //kakao Bean 등록
public class KakaoOauthProvider implements OauthProvider {

  private final String CLIENT_ID = "b7f077c4822ca4f033feecb69cffebf4";
  private final String REDIRECT_URI = "http://localhost:8080/api/oauth/callback/kakao";
  private final String RESPONSE_TYPE = "code";

  @Override
  public String getAuthorizationUrl() {
    //카카오 로그인
    String url = "https://kauth.kakao.com/oauth/authorize";
    return url + "?client_id=" + CLIENT_ID + "&redirect_uri=" + REDIRECT_URI + "&response_type=" + RESPONSE_TYPE;

  }

  @Override
  public OauthAccessTokenResponse getAccessToken(String authorizationCode) {
    WebClient webClient = WebClient.builder()
        .baseUrl("https://kauth.kakao.com")
        .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
        .build();

    String bodyData = "grant_type=authorization_code" +
        "&client_id=" + CLIENT_ID +
        "&redirect_uri=" + REDIRECT_URI +
        "&code=" + authorizationCode;

    try {
      // WebClient에서 바로 DTO로 받기
      return webClient.post()
          .uri("/oauth/token")
          .bodyValue(bodyData)
          .retrieve()
          .bodyToMono(OauthAccessTokenResponse.class)
          .block();

    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }


  @Override
  public OauthUserInfo getUserInfo(String accessToken) {


    try {
      WebClient webClient = WebClient.builder()
          .baseUrl("https://kapi.kakao.com")
          .defaultHeader("Authorization", "Bearer " + accessToken)
          .build();

      return webClient.get()
          .uri("/v2/user/me")
          .retrieve()
          .bodyToMono(KakaoUserInfoResponse.class)  // 직접 DTO로 받음
          .block();

    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }

}
