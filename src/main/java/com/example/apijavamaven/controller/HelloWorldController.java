package com.example.apijavamaven.controller;

import com.example.apijavamaven.dto.HelloWorldDTO;
import org.springframework.data.rest.webmvc.BasePathAwareController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Davi Peterlini
 */
@RestController
@BasePathAwareController
@RequestMapping("/api/hello-world")
public class HelloWorldController {

  @GetMapping
  public ResponseEntity<HelloWorldDTO> getHelloWorldInRest() {
      return ResponseEntity.ok(new HelloWorldDTO("Hello World in REST API"));
  }
}