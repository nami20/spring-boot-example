package net.gradle.com.springbootexpample.service;

import net.gradle.com.springbootexpample.model.User;

public interface UserService {
    void save(User user);

    User findByUsername(String username);
}