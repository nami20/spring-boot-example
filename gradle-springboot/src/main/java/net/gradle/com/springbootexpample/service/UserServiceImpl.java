package net.gradle.com.springbootexpample.service;

import net.gradle.com.springbootexpample.model.User;
import net.gradle.com.springbootexpample.repository.RoleRepository;
import net.gradle.com.springbootexpample.repository.UserRepository;
import net.gradle.com.springbootexpample.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.HashSet;
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Override
    public void save(User user) {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        user.setRoles(new HashSet<>(roleRepository.findAll()));
        System.out.println(user.getPassword());
        userRepository.save(user);
        System.out.println(userRepository.findByUsername(user.getUsername()));
    }
    @Override
    public User findByUsername(String username) {
        System.out.println(username);
        return userRepository.findByUsername(username);
    }
}