package com.sch._roomthon.Pre_9roomthon.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WakeupInfoRequest {
    private String actualWakeTime;        // 실제 기상 시간 (e.g. "07:30")
    private Integer morningConditionScore; // 별점 1~5
    private String memo;                   // 메모 내용
}