package com.example.demo.service;

import com.example.demo.models.dtos.userDto.GetUserDto;
import com.example.demo.models.dtos.userDto.UpdateUserRequestDto;
import com.example.demo.models.entity.User;
import java.util.List;

public interface UserService {
    User createUser(User user);
    User getUserById(Long id);
    User updateUser(Long id, User userUpdates);
    GetUserDto updateUserByToken(User authenticatedUser, UpdateUserRequestDto userUpdates);
    List<User> getAllUsers();
    void deleteUser(Long id);
    User getUserByEmail(String email);
}
