package com.fmi.webjava.courseproject.cryptocurrencywalletmanager.service;

import com.fmi.webjava.courseproject.cryptocurrencywalletmanager.model.User;
import com.fmi.webjava.courseproject.cryptocurrencywalletmanager.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@Slf4j
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private AuthenticationManager authenticationManager;
    private static final int MAX_PASSWORD_LENGTH = 256;

    public User register(String name, String password) {
        User registered = userRepository.save(new User(name, passwordEncoder.encode(password)));

        log.info("User with name {} registered", registered.getUserName());

        return registered;
    }

    public void login(String name, String password, HttpSession session) {
        var authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(name, password));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Store SecurityContext in the session
        session.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY
                , SecurityContextHolder.getContext());

        log.info("User {} logged in", name);
    }

    public void logout() {
        SecurityContextHolder.getContext().setAuthentication(null);
    }

    public User updateCredentials(String newName, String newPassword, ServletRequest request) {
        String oldName = SecurityContextHolder.getContext().getAuthentication().getName();
        var toUpdate = userRepository.findByUserName(oldName);

        if(toUpdate.isEmpty()) {
            throw new EntityNotFoundException(String.format("User with name '%s' is existed", oldName));
        }

        if (newName != null) {
            if(newName.isEmpty() || newPassword.isBlank()) {
                throw new IllegalArgumentException("userName need to have minimum 1 non-white space character");
            }
            if (userRepository.findByUserName(newName).isPresent()) {
                throw new DataIntegrityViolationException(String.format("User with name '%s' already exists", newName));
            }

            toUpdate.get().setUserName(newName);
        }
        if (newPassword != null) {
            if(newPassword.isEmpty() || newPassword.isBlank() || newPassword.length() > MAX_PASSWORD_LENGTH) {
                throw new IllegalArgumentException("password need to have minimum 1" +
                        " non-white space character and maximum 256 symbols");
            }

            toUpdate.get().setPassword(passwordEncoder.encode(newPassword));
        }

        // Updating authentication object in SecurityContextHolder
        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(toUpdate.get().getUserName(),
                        null,  Collections.singleton(
                                new SimpleGrantedAuthority("ROLE_USER")));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        log.info("User '{}' was updated successfully", oldName);
        return userRepository.save(toUpdate.get());
    }

    public void deleteUser() { // TODO: or maybe search by password
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        userRepository.deleteByUserName(userName);

        log.info("User '{}' was deleted successfully", userName);
        logout();
    }
}