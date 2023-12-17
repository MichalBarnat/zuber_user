package com.bbc.zuber.controller;

import com.bbc.zuber.model.user.User;
import com.bbc.zuber.model.user.command.CreateUserCommand;
import com.bbc.zuber.model.user.command.UpdateUserPartiallyCommand;
import com.bbc.zuber.model.user.dto.UserDto;
import com.bbc.zuber.model.user.response.UserResponse;
import com.bbc.zuber.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;
    private final ModelMapper modelMapper;

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUser(@PathVariable Long id) {
        UserDto dto = modelMapper.map(userService.findById(id), UserDto.class);
        return new ResponseEntity<>(dto, OK);
    }

    @GetMapping
    public ResponseEntity<Page<UserDto>> findAll(@PageableDefault Pageable pageable) {
        Page<UserDto> dtos = userService.findAll(pageable)
                .map(user -> modelMapper.map(user, UserDto.class));
        return new ResponseEntity<>(dtos, OK);
    }

    @PostMapping
    public ResponseEntity<UserDto> save(@RequestBody @Valid CreateUserCommand command) {
        User user = modelMapper.map(command, User.class);
        UserDto dto = modelMapper.map(userService.save(user), UserDto.class);
        return new ResponseEntity<>(dto, CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<UserResponse> delete(@PathVariable Long id) {
        UserResponse response = userService.deleteById(id);
        return new ResponseEntity<>(response, NO_CONTENT);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<UserDto> editPartially(@PathVariable Long id, @RequestBody @Valid UpdateUserPartiallyCommand command) {
        User user = userService.edit(id, command);
        UserDto dto = modelMapper.map(user, UserDto.class);
        return new ResponseEntity<>(dto, OK);
    }
}
