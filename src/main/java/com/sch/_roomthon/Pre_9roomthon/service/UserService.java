package com.sch._roomthon.Pre_9roomthon.service;

import com.sch._roomthon.Pre_9roomthon.dto.OauthUserInfo;
import com.sch._roomthon.Pre_9roomthon.entity.UserEntity;
import com.sch._roomthon.Pre_9roomthon.entity.UserOauth;
import com.sch._roomthon.Pre_9roomthon.jwt.JwtTokenProvider;
import com.sch._roomthon.Pre_9roomthon.repository.UserRepository;
import jakarta.transaction.Transactional;
import com.sch._roomthon.Pre_9roomthon.jpa.UserOauthRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserOauthRepository userOauthRepository;
    private final JwtTokenProvider jwtTokenProvider;

    @Transactional
    public UserEntity loginOrRegister(OauthUserInfo userInfo) {
        Optional<UserOauth> userOauthOpt = userOauthRepository.findByProviderAndProviderUserId(
                userInfo.getProvider(), userInfo.getProviderId()
        );

        if (userOauthOpt.isPresent()) {
            return userOauthOpt.get().getUser();
        }

        String email = Optional.ofNullable(userInfo.getEmail())
                .orElse(userInfo.getProvider() + "_" + userInfo.getProviderId() + "@example.com");
        String username = Optional.ofNullable(userInfo.getNickname())
                .orElse("익명유저");
        String password = UUID.randomUUID().toString();

        UserEntity newUser = UserEntity.builder()
                .email(email)
                .username(username)
                .password(password)
                .role("ROLE_USER")
                .build();

        UserEntity savedUser = userRepository.save(newUser);

        UserOauth userOauth = UserOauth.builder()
                .user(savedUser)
                .provider(userInfo.getProvider())
                .providerUserId(userInfo.getProviderId())
                .build();

        userOauthRepository.save(userOauth);

        return savedUser;
    }

}
