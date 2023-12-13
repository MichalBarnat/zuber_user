package com.bbc.zuber.controller;

import com.bbc.zuber.model.user.User;
import com.bbc.zuber.model.user.command.CreateUserCommand;
import com.bbc.zuber.model.user.command.UpdateUserCommand;
import com.bbc.zuber.model.user.dto.UserDto;
import com.bbc.zuber.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;
    private final ModelMapper modelMapper;
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final ObjectMapper objectMapper;

    @GetMapping("/{id}")
    public User getUser(@PathVariable Long id) {
        return userService.getUser(id);
    }

    @PostMapping
    public ResponseEntity<UserDto> save(@RequestBody @Valid CreateUserCommand command) throws JsonProcessingException {
        User userToSave = modelMapper.map(command, User.class);
        User savedUser = userService.save(userToSave);
        String userJson = objectMapper.writeValueAsString(savedUser);
        kafkaTemplate.send("user-registration", userJson);
        return ResponseEntity.ok(modelMapper.map(savedUser, UserDto.class));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        userService.deleteById(id);
        kafkaTemplate.send("user-deleted", getUser(id).getUuid());
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDto> edit(@PathVariable Long id, @RequestBody @Valid UpdateUserCommand command) throws JsonProcessingException {
        User userToEdit = modelMapper.map(command, User.class);
        userToEdit.setId(id);
        User editedUser = userService.edit(userToEdit);
        String editedUserJson = objectMapper.writeValueAsString(editedUser);
        kafkaTemplate.send("user-edited", editedUserJson);
        return ResponseEntity.ok(modelMapper.map(editedUser, UserDto.class));
    }

}
