package com.devn.studentslearn.service;

import com.devn.studentslearn.model.User;
import com.devn.studentslearn.repository.UserRepository;
import com.devn.studentslearn.service.implement.IUserService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Transactional
public class UserSevice implements IUserService {
    @Autowired
    private UserRepository userRepository;
    private BCryptPasswordEncoder Encoder = new BCryptPasswordEncoder();

    @Override
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public Optional<User> findById(String id) {
        return userRepository.findById(id);
    }

    @Override
    public User CreateUser(User user) {

        user.setPasswordHash(Encoder.encode(user.getPasswordHash()));
        return userRepository.save(user);
    }

    @Override
    public User UpdateUser(User user) {

        user.setPasswordHash(Encoder.encode(user.getPasswordHash()));
        return userRepository.save(user);
    }

    @Override
    public void deleteUser(User user) {
       userRepository.delete(user);
    }

    @Override
    public Boolean checkPassword(String rawPassword, String encodedPassword) {
        if (Encoder.matches(rawPassword, encodedPassword)) {
            return true;
        }else  {
            return false;
        }
    }

    @Override
    public User authenticate(String username, String rawPassword) {
        Optional<User> userOpt = userRepository.findByUsername(username);
        if (userOpt.isEmpty()) {
            throw new IllegalArgumentException("Username not found");
        }

        User user = userOpt.get();
        if (!Encoder.matches(rawPassword, user.getPasswordHash())) {
            throw new IllegalArgumentException("Invalid password");
        }

        return user;
    }
}
