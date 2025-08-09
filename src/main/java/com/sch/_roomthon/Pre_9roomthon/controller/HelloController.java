package com.sch._roomthon.Pre_9roomthon.controller;

import com.sch._roomthon.Pre_9roomthon.dto.request.HelloRequest;
import com.sch._roomthon.Pre_9roomthon.dto.response.HelloResponse;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class HelloController {

  @PostMapping("/hello")
  @Operation(
      summary = "Hello 추가 API",
      description = "name 파라미터를 받아 Hello name!을 출력합니다."
  )
  public HelloResponse hello(@RequestBody HelloRequest request) {
    HelloResponse response = new HelloResponse();
    response.setMessage("Hello, " + request.getName() + "!");
    return response;
    }
}