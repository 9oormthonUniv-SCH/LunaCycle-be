package com.sch._roomthon.Pre_9roomthon.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class OauthAccessTokenResponse {

  @JsonProperty("access_token")
  private String accessToken;

  @JsonProperty("token_type")
  private String tokenType;

  @JsonProperty("refresh_token")
  private String refreshToken;

  @JsonProperty("expires_in")
  private int expiresIn;

  private String scope;

  @JsonProperty("refresh_token_expires_in")
  private int refreshTokenExpiresIn;
}

