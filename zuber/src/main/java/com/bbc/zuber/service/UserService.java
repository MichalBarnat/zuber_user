package com.bbc.zuber.service;

import com.bbc.zuber.exception.UserNotFoundException;
import com.bbc.zuber.exception.UserUuidNotFoundException;
import com.bbc.zuber.kafka.KafkaProducerService;
import com.bbc.zuber.model.user.User;
import com.bbc.zuber.model.user.command.UpdateUserPartiallyCommand;
import com.bbc.zuber.model.user.response.UserResponse;
import com.bbc.zuber.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final KafkaProducerService producerService;

    @Transactional(readOnly = true)
    public User findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
    }

    @Transactional(readOnly = true)
    public Page<User> findAll(Pageable pageable) {
        return userRepository.findAll(pageable);
    }


    @Transactional(readOnly = true)
    public User findByUuid(UUID uuid) {
        return userRepository.findByUuid(uuid)
                .orElseThrow(() -> new UserUuidNotFoundException(uuid));
    }

    @Transactional
    public User save(User user) {
        User savedUser = userRepository.save(user);

        producerService.sendSavedUser(savedUser);
        return savedUser;
    }

    @Transactional
    public UserResponse deleteById(long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));

        userRepository.delete(user);

        producerService.sendDeletedUser(user.getUuid());
        return UserResponse.builder()
                .message("Deleted successfully.")
                .build();
    }

    @Transactional
    public User edit(long id, UpdateUserPartiallyCommand command) {
        User editedUser = userRepository.findById(id)
                .map(userToEdit -> {
                    Optional.ofNullable(command.getName()).ifPresent(userToEdit::setName);
                    Optional.ofNullable(command.getSurname()).ifPresent(userToEdit::setSurname);
                    Optional.ofNullable(command.getDob()).ifPresent(userToEdit::setDob);
                    Optional.ofNullable(command.getSex()).ifPresent(userToEdit::setSex);
                    Optional.ofNullable(command.getEmail()).ifPresent(userToEdit::setEmail);
                    return userToEdit;
                })
                .orElseThrow(() -> new UserNotFoundException(id));

        producerService.sendEditedUser(editedUser);
        return editedUser;
    }

//    @Transactional
//    public void payForRide()
}