package com.example.demo.controller;

import com.example.demo.models.dtos.userDto.GetUserDto;
import com.example.demo.models.dtos.userDto.UpdateUserRequestDto;
import com.example.demo.models.entity.User;
import com.example.demo.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@Tag(name = "Users", description = "Manage users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Operation(summary="create User", description="include the required fields : name, email password")
    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user) {
        User createdUser = userService.createUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }

    @Operation(summary="get User By Id", description="")
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        User user = userService.getUserById(id);
        return ResponseEntity.ok(user);
    }

    @Operation(summary="update User", description="includes any or all : name, email, password")
    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User userUpdates) {
        User updatedUser = userService.updateUser(id, userUpdates);
        return ResponseEntity.ok(updatedUser);
    }

    @Operation(summary="get All Users", description="")
    @GetMapping
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @Operation(summary="update User with token", description="includes any or all : name, email, password, must include old Original password if you want to update it to a new Password")
    @PutMapping("/me")
    public ResponseEntity<GetUserDto> updateUserByToken(@AuthenticationPrincipal User user, @RequestBody UpdateUserRequestDto userUpdates) {
        GetUserDto updatedUser = userService.updateUserByToken(user, userUpdates);
        return ResponseEntity.ok(updatedUser);
    }

    @Operation(summary="get the currently logged in User", description="Pass the bearer token")
    @GetMapping("/me")
    public ResponseEntity<GetUserDto> authenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) authentication.getPrincipal();
        GetUserDto authenticatedCurrentUser = new GetUserDto(currentUser.getEmail(),currentUser.getName());
        return ResponseEntity.ok(authenticatedCurrentUser);
    }
//      testing principal works
    @GetMapping("/test/me")
    public ResponseEntity<User> authUser(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(user);
    }

    @Operation(summary="delete User", description="")
    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
    }
}
