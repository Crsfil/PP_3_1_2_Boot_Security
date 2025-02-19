package ru.kata.spring.boot_security.demo.service;

import java.util.List;
import ru.kata.spring.boot_security.demo.model.User;

public interface UserService {
    void saveUser(User user);
    void updateUser(User user);
    void deleteUser(Long id);
    User findUserById(Long id);
    List<User> findAllUsers();
}