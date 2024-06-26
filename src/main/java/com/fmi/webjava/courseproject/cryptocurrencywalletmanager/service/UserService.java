package com.fmi.webjava.courseproject.cryptocurrencywalletmanager.service;

import com.fmi.webjava.courseproject.cryptocurrencywalletmanager.model.User;
import jakarta.servlet.http.HttpSession;

public interface UserService {
    User register(User user);
    void login(User user, HttpSession session);
    User updateCredentials(User user);
    void logout();
}
