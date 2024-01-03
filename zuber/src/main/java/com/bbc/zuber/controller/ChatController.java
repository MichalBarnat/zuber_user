package com.bbc.zuber.controller;

import com.bbc.zuber.model.message.Message;
import com.bbc.zuber.model.user.response.UserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ChatController {

    private final RestTemplate restTemplate;

    private final String serverUrl = "http://localhost:8090/api";

    @PostMapping("/sendMessage")
    public ResponseEntity<UserResponse> sendMessage(@RequestBody Message message) {
        restTemplate.postForObject(serverUrl + "/saveMessage", message, String.class);
        return ResponseEntity.ok(UserResponse.builder()
                .message("Message was sent")
                .build());
    }

    @GetMapping("/getMessages")
    public ResponseEntity<List<Message>> getMessages(@RequestParam String senderId, @RequestParam String receiverId) {
        ResponseEntity<List<Message>> response = restTemplate.exchange(
                serverUrl + "/getMessages?senderId=" + senderId + "&receiverId=" + receiverId,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Message>>() {}
        );
        return response;
    }

}