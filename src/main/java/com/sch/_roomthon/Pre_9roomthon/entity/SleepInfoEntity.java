package com.sch._roomthon.Pre_9roomthon.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SleepInfoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 취침 전 수면 예정 시간
    @Column(nullable = true)
    private LocalTime sleepTime;

    // 취침 전 수면 시간 (분 단위)
    @Column(nullable = true)
    private Integer sleepDuration;

    // 취침 전 기상 예정 시간
    @Column(nullable = true)
    private LocalTime wakeUpTime;

    // 기록 날짜 (ex: 2025-07-31)
    @Column(nullable = false)
    private LocalDate date;

    // 취침 전 컨디션
    @Column(nullable = true)
    private Integer conditionScore;

    // 실제 기상 시간
    @Column(nullable = true)
    private LocalTime actualWakeTime;

    // 기상 후 컨디션 평점 (별점 1~5)
    @Column(nullable = true)
    private Integer morningConditionScore;

    // 기상 후 메모
    @Column(length = 1000)
    private String memo;

    // 수면 점수 (0.0 ~ 5.0)
    @Column(nullable = true)
    private Float sleepScore;

    // 수면 점수에 따른 평가 멘트
    @Column(length = 255)
    private String feedback;


    // 사용자 연동
    @ManyToOne(fetch = FetchType.LAZY) // 한 명의 유저는 여러 개의 수면 정보 입력 가능
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    private UserEntity user;
}
