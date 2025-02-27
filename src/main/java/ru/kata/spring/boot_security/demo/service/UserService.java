package ru.kata.spring.boot_security.demo.service;

import java.util.List;
import java.util.Optional;

import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.model.Role;

public interface UserService {
    Optional<User> findByUsername(String username);
    void saveRole(Role role);
    Role findRoleByName(String name);
    void saveUser(User user);
    void updateUser(User user);
    void deleteUser(Long id);
    User findUserById(Long id);
    List<Role> findAllRoles();
    List<User> findAllUsers();
}