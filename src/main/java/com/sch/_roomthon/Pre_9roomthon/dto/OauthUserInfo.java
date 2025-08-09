package com.sch._roomthon.Pre_9roomthon.dto;

public interface OauthUserInfo {
  String getEmail();
  String getNickname(); // 없으면 null
  String getProvider();      // kakao 등
  String getProviderId();    // 카카오에서 받은 유저 ID (ex: "348273489")
  String getProfileImage();
}
