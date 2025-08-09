package com.sch._roomthon.Pre_9roomthon.service;

import com.sch._roomthon.Pre_9roomthon.dto.request.SleepInfoRequest;
import com.sch._roomthon.Pre_9roomthon.dto.request.WakeupInfoRequest;
import com.sch._roomthon.Pre_9roomthon.dto.response.SleepInfoResponse;
import com.sch._roomthon.Pre_9roomthon.entity.SleepInfoEntity;
import com.sch._roomthon.Pre_9roomthon.entity.UserEntity;
import com.sch._roomthon.Pre_9roomthon.repository.SleepInfoRepository;
import com.sch._roomthon.Pre_9roomthon.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

/**
 * 수면 정보 관련 비즈니스 로직을 처리하는 서비스 클래스
 */
@Service
@RequiredArgsConstructor
public class SleepService {

    private final SleepInfoRepository sleepRepo;
    private final UserRepository userRepo;

    /**
     * 수면 전 정보 저장 로직
     * 사용자가 수면 시간, 기상 예정 시간, 수면 시간(분)을 입력 가능
     * 이 중 2가지만 입력해도 나머지 하나를 자동으로 계산하여 저장
     */
    public SleepInfoResponse saveSleepInfo(SleepInfoRequest request, UUID userId) {
        LocalTime sleepTime = null;
        LocalTime wakeTime = null;
        LocalDate today = LocalDate.now();
        Integer duration = request.getSleepDuration();

        // 이미 오늘 수면 정보가 존재하면 예외 발생
        if (sleepRepo.existsByUserIdAndDate(userId, today)) {
            throw new IllegalStateException("오늘의 수면 기록은 이미 저장되어 있습니다.");
        }

        // 사용자가 보낸 수면/기상 시간 파싱
        if (request.getSleepTime() != null)
            sleepTime = LocalTime.parse(request.getSleepTime());
        if (request.getWakeUpTime() != null)
            wakeTime = LocalTime.parse(request.getWakeUpTime());

        // 2개만 입력한 경우 → 나머지 하나 계산
        if (sleepTime != null && duration != null && wakeTime == null) {
            wakeTime = sleepTime.plusMinutes(duration);
        } else if (wakeTime != null && duration != null && sleepTime == null) {
            sleepTime = wakeTime.minusMinutes(duration);
        } else if (sleepTime != null && wakeTime != null && duration == null) {
            // ✅ 자정 넘김 처리 (LocalDateTime으로 안전하게 계산)
            if (sleepTime.isAfter(wakeTime)) {
                duration = (int) Duration.between(
                        sleepTime.atDate(LocalDate.now()),
                        wakeTime.atDate(LocalDate.now().plusDays(1))
                ).toMinutes();
            } else {
                duration = (int) Duration.between(
                        sleepTime.atDate(LocalDate.now()),
                        wakeTime.atDate(LocalDate.now())
                ).toMinutes();
            }
        }



        // 유저 엔티티 조회
        UserEntity user = userRepo.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("해당 유저 없음"));

        // 엔티티 생성 및 저장
        SleepInfoEntity entity = SleepInfoEntity.builder()
                .user(user)
                .sleepTime(sleepTime)
                .wakeUpTime(wakeTime)
                .sleepDuration(duration)
                .conditionScore(request.getConditionScore())  // 수면 전 컨디션 평점
                .date(today)
                .build();

        // 전날 수면 데이터 조회
        SleepInfoEntity yesterday = sleepRepo.findByUserIdAndDate(userId, today.minusDays(1)).orElse(null);

        // 점수 및 평가 계산
        float score = calculateSleepScore(entity, yesterday);
        String feedback = getSleepFeedback(score);

        // 점수와 피드백 엔티티에 설정
        entity.setSleepScore(score);
        entity.setFeedback(feedback);

        // 저장
        sleepRepo.save(entity);


        // 응답 DTO에 포함
        return SleepInfoResponse.builder()
                .sleepTime(sleepTime.toString())
                .wakeUpTime(wakeTime.toString())
                .sleepDuration(duration)
                .conditionScore(request.getConditionScore())
                .sleepScore(score)
                .feedback(feedback)
                .build();

    }

    /**
     * 수면 후 정보(기상 시간, 아침 컨디션, 메모) 업데이트 로직
     */
    @Transactional
    public void updateWakeupInfo(WakeupInfoRequest request, UUID userId) {
        LocalDate today = LocalDate.now();

        // 오늘의 수면 기록이 있는지 확인 후 조회
        SleepInfoEntity todayRecord = sleepRepo
                .findByUserIdAndDate(userId, today)
                .orElseThrow(() -> new IllegalStateException("오늘 수면 기록이 없습니다."));

        // 실제 기상 시간 업데이트
        if (request.getActualWakeTime() != null) {
            todayRecord.setActualWakeTime(LocalTime.parse(request.getActualWakeTime()));
        }

        // 아침 컨디션 평점 및 메모 업데이트
        todayRecord.setMorningConditionScore(request.getMorningConditionScore());
        todayRecord.setMemo(request.getMemo());
    }

    private float calculateSleepScore(SleepInfoEntity today, SleepInfoEntity yesterday) {
        float score = 0f;

        // 수면 시작 시간
        LocalTime sleepTime = today.getSleepTime();
        if (sleepTime != null) {
            if (!sleepTime.isBefore(LocalTime.of(22, 0)) && sleepTime.isBefore(LocalTime.MIDNIGHT)) {
                score += 1.5f;
            } else if (
                    (sleepTime.equals(LocalTime.of(20, 0)) || sleepTime.isAfter(LocalTime.of(20, 0))) &&
                            sleepTime.isBefore(LocalTime.of(22, 0))
            ) {
                score += 1f;
            } else if (
                    sleepTime.isAfter(LocalTime.MIDNIGHT) &&
                            sleepTime.isBefore(LocalTime.of(2, 0))
            ) {
                score += 1f;
            } else {
                score += 0.5f;
            }
        }

        // 기상 예정 시간
        LocalTime wakeUpTime = today.getWakeUpTime();
        if (wakeUpTime != null &&
                (wakeUpTime.equals(LocalTime.of(5, 30)) || wakeUpTime.isAfter(LocalTime.of(5, 30))) &&
                wakeUpTime.isBefore(LocalTime.of(9, 0))) {
            score += 0.5f;
        }

        // 총 수면 시간
        int duration = today.getSleepDuration(); // 단위: 분
        if (duration <= 360) score += 0.5f;
        else if (duration <= 420) score += 1f;
        else if (duration <= 510) score += 2f;
        else if (duration <= 600) score += 1.5f;
        else score += 1f;

        // 규칙성 (전날과 ±1시간 이내면 0.5점)
        boolean isRegular = false;
        if (yesterday != null &&
                yesterday.getSleepTime() != null && yesterday.getWakeUpTime() != null &&
                sleepTime != null && wakeUpTime != null
        ) {
            long startDiff = Math.abs(Duration.between(yesterday.getSleepTime(), sleepTime).toMinutes());
            long endDiff = Math.abs(Duration.between(yesterday.getWakeUpTime(), wakeUpTime).toMinutes());
            if (startDiff <= 60 && endDiff <= 60) {
                isRegular = true;
                score += 0.5f;
            }
        }

        // 사용자 만족도 (1~5점 → 0~0.5점)
        Integer mood = today.getMorningConditionScore();
        if (mood != null) {
            float converted = Math.max(0f, Math.min(5, mood)) * 0.1f;
            score += converted;
        }

        return Math.min(score, 5.0f);
    }

    private String getSleepFeedback(float score) {
        if (score >= 4.5f) return "수면습관이 아주 좋습니다! 그대로 유지해보세요";
        if (score >= 3.5f) return "괜찮은 수면입니다. 약간만 더 개선해볼까요?";
        if (score >= 2.5f) return "평균적인 수면입니다. 수면 시간을 조정해보는 것도 좋을 것 같아요 😊";
        if (score >= 1.5f) return "수면시간이나 리듬이 불규칙해보여요. 습관 개선이 필요할 것 같아요.";
        return "수면의 질이 매우 떨어지고 있어요. 빠른 개선이 필요할 것 같아요.";
    }

}
