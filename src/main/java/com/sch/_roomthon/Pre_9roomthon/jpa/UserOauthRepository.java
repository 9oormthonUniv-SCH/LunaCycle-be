package com.sch._roomthon.Pre_9roomthon.jpa;

import com.sch._roomthon.Pre_9roomthon.entity.UserOauth;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserOauthRepository extends JpaRepository<UserOauth, UUID> {
    Optional<UserOauth> findByProviderAndProviderUserId(String provider, String providerUserId);
}
