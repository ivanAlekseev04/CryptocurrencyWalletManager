package com.fmi.webjava.courseproject.cryptocurrencywalletmanager.controller;

import com.fmi.webjava.courseproject.cryptocurrencywalletmanager.dto.UserDTOInput;
import com.fmi.webjava.courseproject.cryptocurrencywalletmanager.dto.UserDTOOutput;
import com.fmi.webjava.courseproject.cryptocurrencywalletmanager.exception.AlreadyLoggedInException;
import com.fmi.webjava.courseproject.cryptocurrencywalletmanager.mapper.UserMapper;
import com.fmi.webjava.courseproject.cryptocurrencywalletmanager.model.User;
import com.fmi.webjava.courseproject.cryptocurrencywalletmanager.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

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
    public ResponseEntity<Void> login(@RequestBody @Valid UserDTOInput input, HttpServletRequest request) {
        String authenticatedUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        if (!authenticatedUsername.equals("anonymousUser")) {
            throw new AlreadyLoggedInException("You have already been logged with user " + authenticatedUsername);
        }

        User user = userMapper.userDTOInputToUser(input);
        userService.login(user, request.getSession());

        return ResponseEntity.ok().build();
    }

    @PatchMapping("/user")
    public ResponseEntity<UserDTOOutput> updateUser(@RequestBody UserDTOInput input) {
        User user = userMapper.userDTOInputToUser(input);
        User updatedUser = userService.updateCredentials(user);

        return new ResponseEntity<>(userMapper.userToUserDTOOutput(updatedUser), HttpStatus.OK);
    }
}