package com.sch._roomthon.Pre_9roomthon.oauthProvider;

import com.sch._roomthon.Pre_9roomthon.dto.response.OauthAccessTokenResponse;
import com.sch._roomthon.Pre_9roomthon.dto.OauthUserInfo;

public interface OauthProvider{
  String getAuthorizationUrl();  // 로그인 URL 생성 (인가코드 받아오기 )
  OauthAccessTokenResponse getAccessToken(String code);  // Access Token 요청 (Access Token 받아오기 인가코드 이용)
  OauthUserInfo getUserInfo(String accessToken);  // 사용자 정보 요청 (Access Token 이용 사용자 정보 요청)

}
