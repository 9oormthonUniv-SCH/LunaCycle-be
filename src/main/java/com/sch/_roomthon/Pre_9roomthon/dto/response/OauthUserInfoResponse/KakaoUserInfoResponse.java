package com.sch._roomthon.Pre_9roomthon.dto.response.OauthUserInfoResponse;

import com.sch._roomthon.Pre_9roomthon.dto.OauthUserInfo;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class KakaoUserInfoResponse implements OauthUserInfo {

  private Long id;
  private KakaoAccount kakao_account;

  @Override
  public String getEmail() {
    return kakao_account != null ? kakao_account.getEmail() : null;
  }

  @Override
  public String getNickname() {
    return kakao_account != null && kakao_account.getProfile() != null
            ? kakao_account.getProfile().getNickname() : null;
  }

  @Override
  public String getProfileImage() {
    return kakao_account != null && kakao_account.getProfile() != null
            ? kakao_account.getProfile().getProfile_image_url() : null;
  }

  @Override
  public String getProvider() {
    return "kakao";  // 고정된 문자열 반환
  }

  @Override
  public String getProviderId() {
    return id != null ? id.toString() : null;
  }

  @Getter
  @Setter
  @NoArgsConstructor
  @AllArgsConstructor
  public static class KakaoAccount {
    private String email;
    private Profile profile;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Profile {
      private String nickname;
      private String profile_image_url;
      private String thumbnail_image_url;
    }
  }
}
