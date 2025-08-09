package com.sch._roomthon.Pre_9roomthon.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SleepInfoRequest {
    private String sleepTime;     // "23:30"
    private Integer sleepDuration; // 450 (분)
    private String wakeUpTime;    // "07:00"
    private Integer conditionScore; // 별점 1~5점
}
