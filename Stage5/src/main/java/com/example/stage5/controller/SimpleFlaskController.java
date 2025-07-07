package com.example.stage5.controller;

import com.example.stage5.service.SimpleFlaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/simple")
public class SimpleFlaskController {

    private final SimpleFlaskService flaskService;

    @GetMapping("/hello")
    public ResponseEntity<?> getHello() {
        try {
            Object response = flaskService.getHello();
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error: " + e.getMessage());
        }
    }

    @PostMapping("/data")
    public ResponseEntity<?> postData(@RequestBody Map<String, Object> data) {
        try {
            Object response = flaskService.postData(data);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error: " + e.getMessage());
        }
    }
}