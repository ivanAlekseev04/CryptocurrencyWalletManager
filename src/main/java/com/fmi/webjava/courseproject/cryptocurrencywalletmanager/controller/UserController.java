package com.fmi.webjava.courseproject.cryptocurrencywalletmanager.controller;

import com.fmi.webjava.courseproject.cryptocurrencywalletmanager.dto.UserDTO;
import com.fmi.webjava.courseproject.cryptocurrencywalletmanager.mapper.UserMapper;
import com.fmi.webjava.courseproject.cryptocurrencywalletmanager.model.User;
import com.fmi.webjava.courseproject.cryptocurrencywalletmanager.service.UserService;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/")
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private UserMapper userMapper;

    @PostMapping("/register")
    public ResponseEntity<UserDTO> register(@RequestBody @Valid User user) {
        UserDTO created = userMapper.userToUserDTO(userService.register(user));

        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<Void> login(@RequestBody @Valid User user, HttpServletRequest request) {
        userService.login(user, request.getSession());

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/logout")
    public final ResponseEntity<Void> logout(/*ServletRequest request, ServletResponse response*/) {
        userService.logout();

        return ResponseEntity.noContent().build();
    }

    @PatchMapping("user/update")
    public ResponseEntity<UserDTO> updateUser(@RequestBody User user, ServletRequest request) {
        UserDTO updated = userMapper.userToUserDTO(userService.updateCredentials(user, request));

        return new ResponseEntity<>(updated, HttpStatus.OK);
    }

    @DeleteMapping("user/delete")
    public final ResponseEntity<Void> deleteUser() {
        userService.deleteUser();

        return ResponseEntity.noContent().build();
    }
}