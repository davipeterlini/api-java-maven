package com.example.apijavamaven.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/hello-world")
public class HelloWorldController {

  @GetMapping
  public ResponseEntity<String> getHelloWorldInRest() {
      return ResponseEntity.ok("Hello World in REST API");
  }
}