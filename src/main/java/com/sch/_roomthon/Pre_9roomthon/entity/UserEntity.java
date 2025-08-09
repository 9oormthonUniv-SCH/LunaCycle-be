package com.sch._roomthon.Pre_9roomthon.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserEntity implements UserDetails  {

    @Id
    @GeneratedValue(generator = "UUID")
    @org.hibernate.annotations.GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Column(columnDefinition = "uuid", updatable = false, nullable = false)
    private UUID id;


    @Column(nullable = false, unique = true, length = 255)
    private String email; // 로그인 ID 용도

    @Column(nullable = false, unique = true, length = 100)
    private String username; // 별명, 닉네임

    @Column(nullable = false)
    private String password;

    private String role; // 예: ROLE_USER, ROLE_ADMIN

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    public void prePersist() {
        LocalDateTime now = LocalDateTime.now();
        createdAt = now;
        updatedAt = now;
    }

    @PreUpdate
    public void preUpdate() {
        updatedAt = LocalDateTime.now();
    }

    // ✅ UserDetails 구현

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(() -> {
            if (role == null || !role.startsWith("ROLE_")) {
                return "ROLE_USER";
            }
            return role;
        });
    }

    @Override
    public String getUsername() {
        return this.email; // Spring Security에서 email을 ID로 사용
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override public boolean isAccountNonExpired() { return true; }

    @Override public boolean isAccountNonLocked() { return true; }

    @Override public boolean isCredentialsNonExpired() { return true; }

    @Override public boolean isEnabled() { return true; }
}
