package com.sch._roomthon.Pre_9roomthon.dto.request;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@NoArgsConstructor
@Getter
@Setter
@Schema(description = "요청 데이터")
  public class HelloRequest {
    @Schema(description = "이름", example = "Seongbeom")
    private String name;
  }

  //생성자로 할당 AllArgsConstructor를 사용하여 name을 초기화할 수 있습니다.
  //NoArgsConstructor는 빈 생성자를 생성합니다.
//  public HelloRequest(String name) {
//    this.name = name;
//  }

  // Getter와 Setter 어노테이션
//  public String getName() {
//    return name;
//  }
//  public void setName(String name) {
//    this.name = name;
//  }

