package com.fpt.chungvv3.simple_crud_spring.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/hello")
public class HelloController {
    @GetMapping
    public ResponseEntity<Map<String, Object>> hello() {
        Map<String, Object> result = new HashMap<>();
        result.put("message", "Hello World");
        result.put("status", true);
        result.put("data", new HashMap<>());
        return ResponseEntity.ok(result);
    }
}
