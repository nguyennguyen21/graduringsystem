package com.devn.studentslearn.service.implement;

import com.devn.studentslearn.model.User;
import com.devn.studentslearn.repository.UserRepository;

import java.util.Optional;

public interface IUserService  {
    Optional<User> findByUsername(String username);
    Optional<User> findById(String id);
    User CreateUser(User user);
    User UpdateUser(User user);
    void deleteUser(User user);
    Boolean checkPassword(String rawPassword, String encodedPassword);
    User authenticate(String username, String rawPassword);
}
