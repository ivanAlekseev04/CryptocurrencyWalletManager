package com.fmi.webjava.courseproject.cryptocurrencywalletmanager.service;

import com.fmi.webjava.courseproject.cryptocurrencywalletmanager.model.User;
import com.fmi.webjava.courseproject.cryptocurrencywalletmanager.repository.UserRepository;
import com.fmi.webjava.courseproject.cryptocurrencywalletmanager.security.userdetails.CustomUserDetails;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
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
@Transactional
@Slf4j
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private AuthenticationManager authenticationManager;

    private static final int MAX_PASSWORD_LENGTH = 256;

    public User register(User user) {
        if (userRepository.findByUserName(user.getUserName()).isPresent()) {
            throw new DataIntegrityViolationException(String.format("User with name '%s' already exists"
                    , user.getUserName()));
        }

        User registered = userRepository.save(User.builder()
                .userName(user.getUserName())
                .password(passwordEncoder.encode((user.getPassword())))
                .build());

        log.info("User with name {} registered", registered.getUserName());

        return registered;
    }

    public void login(User user, HttpSession session) {
        var authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(user.getUserName(), user.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Store SecurityContext in the session
        session.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY
                , SecurityContextHolder.getContext());

        log.info("User {} logged in", user.getUserName());
    }

    public User updateCredentials(User user) {
        String oldName = SecurityContextHolder.getContext().getAuthentication().getName();
        var toUpdate = userRepository.findByUserName(oldName);

        String newName = user.getUserName();
        String newPassword = user.getPassword();

        if(toUpdate.isEmpty()) {
            throw new EntityNotFoundException(String.format("User with name '%s' is not existed", oldName));
        }

        if (newName != null) {
            if(newName.isBlank()) {
                throw new IllegalArgumentException("userName need to have minimum 1 non-white space character");
            }
            if (userRepository.findByUserName(newName).isPresent()) {
                throw new DataIntegrityViolationException(String.format("User with name '%s' already exists", newName));
            }

            toUpdate.get().setUserName(newName);
        }
        if (newPassword != null) {
            if(newPassword.isBlank() || newPassword.length() > MAX_PASSWORD_LENGTH) {
                throw new IllegalArgumentException("password need to have minimum 1" +
                        " non-white space character and maximum 256 symbols");
            }

            toUpdate.get().setPassword(passwordEncoder.encode(newPassword));
        }

        // Updating authentication object in SecurityContextHolder
        var authentication = new UsernamePasswordAuthenticationToken(CustomUserDetails.builder()
                        .id(toUpdate.get().getId())
                        .userName(toUpdate.get().getUserName())
                        .password(toUpdate.get().getPassword())
                        .build(), toUpdate.get().getPassword(),
                Collections.singleton(new SimpleGrantedAuthority("ROLE_USER")));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        log.info("User '{}' was updated successfully", oldName);
        return userRepository.save(toUpdate.get());
    }

    public void deleteUser() {
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        userRepository.deleteByUserName(userName);

        log.info("User '{}' was deleted successfully", userName);
        logout();
    }

    public void logout() {
        SecurityContextHolder.getContext().setAuthentication(null);
    }
}
