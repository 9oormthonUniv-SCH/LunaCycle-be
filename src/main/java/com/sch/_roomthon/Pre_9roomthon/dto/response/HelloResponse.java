package com.sch._roomthon.Pre_9roomthon.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@NoArgsConstructor
@Getter
@Setter
public class HelloResponse {

  @Schema(description = "인사말", example = "Hello, Seongbeom!")
  private String message;
}
