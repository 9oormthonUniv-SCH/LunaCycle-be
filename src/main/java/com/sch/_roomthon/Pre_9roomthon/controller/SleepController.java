package com.sch._roomthon.Pre_9roomthon.controller;

import com.sch._roomthon.Pre_9roomthon.dto.request.SleepInfoRequest;
import com.sch._roomthon.Pre_9roomthon.dto.request.WakeupInfoRequest;
import com.sch._roomthon.Pre_9roomthon.dto.response.SleepInfoResponse;
import com.sch._roomthon.Pre_9roomthon.service.SleepService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/sleep")
@RequiredArgsConstructor
public class SleepController {

    private final SleepService sleepService;

    @PostMapping("/input")
    public ResponseEntity<SleepInfoResponse> inputSleepInfo(
            @RequestBody SleepInfoRequest request,
            @RequestAttribute("userId") UUID userId // JwtAuthenticationFilter에 설정돼 있어야 함
    ) {
        SleepInfoResponse response = sleepService.saveSleepInfo(request, userId);
        return ResponseEntity.ok(response);
    }
    @PostMapping("/wakeup")
    public ResponseEntity<String> inputWakeupInfo(
            @RequestBody WakeupInfoRequest request,
            @RequestAttribute("userId") UUID userId
    ) {
        sleepService.updateWakeupInfo(request, userId);
        return ResponseEntity.ok("기상 후 정보가 저장되었습니다.");
    }

}