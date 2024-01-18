package com.bbc.zuber.controller;

import com.bbc.zuber.model.message.command.CreateMessageCommand;
import com.bbc.zuber.model.message.dto.MessageDto;
import com.bbc.zuber.model.message.response.MessageResponse;
import com.bbc.zuber.service.MessageService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedList;

import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/api/user-chat")
@RequiredArgsConstructor
public class ChatController {

    private final MessageService messageService;

    @PostMapping("/sendMessage")
    public ResponseEntity<MessageResponse> sendMessage(@RequestBody @Valid CreateMessageCommand command) {
        MessageResponse response = messageService.sendMessage(command);
        return new ResponseEntity<>(response, OK);
    }

    @GetMapping("/messages/{rideInfoId}")
    public ResponseEntity<LinkedList<MessageDto>> getMessages(@PathVariable long rideInfoId) {
        LinkedList<MessageDto> messages = messageService.getMessages(rideInfoId);
        return new ResponseEntity<>(messages, OK);
    }
}