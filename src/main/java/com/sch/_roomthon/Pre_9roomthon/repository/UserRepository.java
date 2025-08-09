package com.sch._roomthon.Pre_9roomthon.repository;

import com.sch._roomthon.Pre_9roomthon.entity.UserEntity;
import com.sch._roomthon.Pre_9roomthon.entity.UserOauth;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<UserEntity, UUID> {
    Optional<UserEntity> findByEmail(String email);
    // 필요한 추가 메서드
}