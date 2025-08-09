package com.sch._roomthon.Pre_9roomthon.dto.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SleepInfoResponse {
    private String sleepTime;
    private Integer sleepDuration;
    private String wakeUpTime;
    private Integer conditionScore;
    private Float sleepScore;
    private String feedback;

}
