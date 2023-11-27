package com.bbc.zuber.controller;

import com.bbc.zuber.model.user.User;
import com.bbc.zuber.model.user.command.CreateUserCommand;
import com.bbc.zuber.model.user.dto.UserDto;
import com.bbc.zuber.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;
    private final ModelMapper modelMapper;
    private final KafkaTemplate<String, User> kafkaTemplate;

    @GetMapping("/{id}")
    public User getUser(@PathVariable Long id) {
        return userService.getUser(id);
    }

    @PostMapping
    public ResponseEntity<UserDto> save(@RequestBody CreateUserCommand command) {
        User userToSave = modelMapper.map(command, User.class);
        User savedDoctor = userService.save(userToSave);
        kafkaTemplate.send("user-registration", savedDoctor);
        return ResponseEntity.ok(modelMapper.map(savedDoctor, UserDto.class));
    }

}
