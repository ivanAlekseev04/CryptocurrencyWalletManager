package com.fmi.webjava.courseproject.cryptocurrencywalletmanager.controller;

import com.fmi.webjava.courseproject.cryptocurrencywalletmanager.dto.UserDTOInput;
import com.fmi.webjava.courseproject.cryptocurrencywalletmanager.dto.UserDTOOutput;
import com.fmi.webjava.courseproject.cryptocurrencywalletmanager.mapper.UserMapper;
import com.fmi.webjava.courseproject.cryptocurrencywalletmanager.model.User;
import com.fmi.webjava.courseproject.cryptocurrencywalletmanager.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/")
@Slf4j
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private UserMapper userMapper;

    @PostMapping("/register")
    public ResponseEntity<UserDTOOutput> register(@RequestBody @Valid UserDTOInput userDTOInput) {
        User newUser = userMapper.userDTOInputToUser(userDTOInput);
        User createdUser = userService.register(newUser);
        UserDTOOutput userDTOOutput = userMapper.userToUserDTOOutput(createdUser);

        return new ResponseEntity<>(userDTOOutput, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody @Valid UserDTOInput input, HttpServletRequest request) {
        String authenticatedUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        if (!authenticatedUsername.equals("anonymousUser")) {
            log.error("Error: User is already logged in with user {}", authenticatedUsername);
            return new ResponseEntity<>("You have already been logged with user " + authenticatedUsername, HttpStatus.BAD_REQUEST);
        }

        User user = userMapper.userDTOInputToUser(input);
        userService.login(user, request.getSession());
        String successfulLogin = "User " + user.getUserName() + " logged successfully!";

        return new ResponseEntity<>(successfulLogin, HttpStatus.OK);
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout() {
        String authenticatedUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        if (authenticatedUsername.equals("anonymousUser")) {
            log.error("Error: User is not logged in");
            return new ResponseEntity<>("You are not logged in any profile", HttpStatus.BAD_REQUEST);
        }
        userService.logout();
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        String successfulLogout = "User " + username + " logged out successfully!";

        return new ResponseEntity<>(successfulLogout, HttpStatus.OK);
    }

    @PatchMapping("/user")
    public ResponseEntity<UserDTOOutput> updateUser(@RequestBody UserDTOInput input) {
        User user = userMapper.userDTOInputToUser(input);
        User updatedUser = userService.updateCredentials(user);

        return new ResponseEntity<>(userMapper.userToUserDTOOutput(updatedUser), HttpStatus.OK);
    }
}