package com.example.demo.implementation;

import com.example.demo.exception.EntityNotFoundException;
import com.example.demo.exception.InvalidPasswordException;
import com.example.demo.models.dtos.userDto.GetUserDto;
import com.example.demo.models.dtos.userDto.UpdateUserRequestDto;
import com.example.demo.models.entity.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.UserService;
import com.example.demo.util.PasswordService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordService passwordService;

    @Override
    public User createUser(User user) {
        //encrypt password
        user.setPassword(passwordService.encode(user.getPassword()));
        return userRepository.save(user);
    }

    @Override
    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User",id));
    }

    @Override
    public User updateUser(Long id, User userUpdates) {
        return userRepository.findById(id).map(existingUser -> {
            if (userUpdates.getName() != null) {
                existingUser.setName(userUpdates.getName());
            }
            if (userUpdates.getEmail() != null) {
                existingUser.setEmail(userUpdates.getEmail());
            }
            if (userUpdates.getPassword() != null) {
//                existingUser.setPassword(userUpdates.getPassword());
                existingUser.setPassword(passwordService.encode(userUpdates.getPassword()));
            }
            existingUser.setUpdatedAt(LocalDateTime.now());
            return userRepository.save(existingUser);
        }).orElseThrow(() -> new EntityNotFoundException("User",id));
    }

    public GetUserDto updateUserByToken(User authenticatedUser, UpdateUserRequestDto userUpdates) {
        if (userUpdates.getName() != null) {
            authenticatedUser.setName(userUpdates.getName());
        }
        if (userUpdates.getEmail() != null) {
            authenticatedUser.setEmail(userUpdates.getEmail());
        }
        if (userUpdates.getOldPassword() != null && !("".equals(userUpdates.getOldPassword()))) {
            if(passwordService.matches(userUpdates.getOldPassword(), authenticatedUser.getPassword())){//password validation against original
                authenticatedUser.setPassword(passwordService.encode(userUpdates.getNewPassword()));
            }
            else{
                throw new InvalidPasswordException();
            }
        }
        authenticatedUser.setUpdatedAt(LocalDateTime.now());
        userRepository.save(authenticatedUser);
        return new GetUserDto(authenticatedUser.getEmail(), authenticatedUser.getName());
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User with email " + email + " not found"));
    }
}
