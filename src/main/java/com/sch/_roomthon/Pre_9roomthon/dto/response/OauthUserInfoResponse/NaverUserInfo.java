package com.sch._roomthon.Pre_9roomthon.dto.response.OauthUserInfoResponse;

import com.sch._roomthon.Pre_9roomthon.dto.OauthUserInfo;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class NaverUserInfo implements OauthUserInfo {

    private final String provider;
    private final String providerId;
    private final String email;
    private final String nickname;

    @Override
    public String getEmail() {
        return email;
    }

    @Override
    public String getNickname() {
        return nickname;
    }

    @Override
    public String getProvider() {
        return provider;
    }

    @Override
    public String getProviderId() {
        return providerId;
    }

    @Override
    public String getProfileImage() {
        return null; // 네이버는 기본 제공 안 함 (설정 시 응답에 있을 수 있음)
    }
}
