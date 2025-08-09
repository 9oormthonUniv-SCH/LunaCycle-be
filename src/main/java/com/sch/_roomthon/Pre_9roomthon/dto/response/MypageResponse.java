package com.sch._roomthon.Pre_9roomthon.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
public class MypageResponse {
    private String nickname;                // 사용자 닉네임
    private String email;                   // 사용자 이메일
    private String provider;                // "kakao" / "naver"
    private LocalDate joinedAt;             // 가입일

    private String averageSleepDuration;    // 최근 7일 평균 수면 시간 (예: "6시간 42분")
    private String averageBedtime;          // 평균 취침 시간 (예: "23:30")
    private String averageWakeupTime;       // 평균 기상 시간 (예: "07:15")
    private String recentCondition;         // 최근 기록된 컨디션 (예: "좋음", "보통")
}
