package com.sch._roomthon.Pre_9roomthon.controller;

import com.sch._roomthon.Pre_9roomthon.entity.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController {

    @GetMapping("/me")
    public ResponseEntity<?> getMyInfo(@AuthenticationPrincipal UserEntity user) {
        if (user == null) {
            return ResponseEntity.status(401).body("인증되지 않은 사용자입니다.");
        }

        return ResponseEntity.ok().body(user);
    }
}
