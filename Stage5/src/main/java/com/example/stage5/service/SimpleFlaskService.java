package com.example.stage5.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class SimpleFlaskService {

    private final RestTemplate restTemplate;
    private final String FLASK_URL = "http://localhost:5000";

    public Object getHello() {
        String url = FLASK_URL + "/hello";
        ResponseEntity<Object> response = restTemplate.getForEntity(url, Object.class);
        return response.getBody();
    }

    public Object postData(Map<String, Object> data) {
        String url = FLASK_URL + "/data";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(data, headers);
        ResponseEntity<Object> response = restTemplate.postForEntity(url, entity, Object.class);
        return response.getBody();
    }
}