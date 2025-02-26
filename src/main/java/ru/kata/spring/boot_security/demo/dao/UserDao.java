package ru.kata.spring.boot_security.demo.dao;

import ru.kata.spring.boot_security.demo.model.User;

import java.util.List;
import java.util.Optional;

public interface UserDao {
    public void save(User user);
    public void update(User user);
    public void delete(User user);
    public User findById(Long id);
    public List<User> findAll();
    Optional<User> findByUsername(String username);
}
