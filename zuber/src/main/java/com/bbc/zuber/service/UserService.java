package com.bbc.zuber.service;

import com.bbc.zuber.exception.UserNotFoundException;
import com.bbc.zuber.model.user.User;
import com.bbc.zuber.model.user.command.UpdateUserCommand;
import com.bbc.zuber.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public User getUser(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
    }

    public User save(User user) {
        return userRepository.save(user);
    }

    public void deleteById(Long id) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
        } else {
            throw new UserNotFoundException(id);
        }
    }

    @Transactional
    public User edit(User user) {
        return userRepository.findById(user.getId())
                .map(userToEdit -> {
                    Optional.ofNullable(user.getName()).ifPresent(userToEdit::setName);
                    Optional.ofNullable(user.getSurname()).ifPresent(userToEdit::setSurname);
                    Optional.ofNullable(user.getDob()).ifPresent(userToEdit::setDob);
                    Optional.ofNullable(user.getSex()).ifPresent(userToEdit::setSex);
                    Optional.ofNullable(user.getEmail()).ifPresent(userToEdit::setEmail);
                    return userToEdit;
                }).orElseThrow(() -> new UserNotFoundException(user.getId()));
    }

}