package com.knalum.service;

import org.springframework.stereotype.Service;

import com.knalum.domain.User;

@Service
public class UserService {
    // Mock service

    public User createUser(User user) {
        return user;
    }

    public User updateUser(Long id, User user) {
        return user;
    }

    public User getUser(Long id) {
        return new User();
    }

    public User deleteUser(Long id) {
        return new User();
    }
}
