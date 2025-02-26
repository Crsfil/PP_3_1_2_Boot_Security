package ru.kata.spring.boot_security.demo.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.dao.UserDao;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class UserServiceImpl implements UserService {
    private final UserDao userDao;

    @PersistenceContext
    private EntityManager entityManager;

    public UserServiceImpl(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    @Transactional(readOnly = true)
    public Role findRoleByName(String name) {
        return entityManager.createQuery("FROM Role WHERE name = :name", Role.class)
                .setParameter("name", name)
                .getResultStream()
                .findFirst()
                .orElse(null);
    }


    @Override
    @Transactional
    public void saveUser(User user) {
        // Проверяем и сохраняем роли перед сохранением пользователя
        Set<Role> managedRoles = new HashSet<>();
        for (Role role : user.getRoles()) {
            Role existingRole = findRoleByName(role.getName());
            if (existingRole == null) {
                saveRole(role);
                existingRole = role;
            }
            managedRoles.add(existingRole);
        }
        user.setRoles(managedRoles);
        userDao.save(user);
    }

    @Override
    @Transactional
    public void saveRole(Role role) {
        entityManager.persist(role);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<User> findByUsername(String username) {
        return userDao.findByUsername(username);
    }

    @Transactional
    @Override
    public void updateUser(User user) {
        userDao.update(user);
    }

    @Transactional(readOnly = true)
    @Override
    public User findUserById(Long id) {
        return userDao.findById(id);
    }

    @Transactional
    @Override
    public void deleteUser(Long id) {
        User user = userDao.findById(id);
        if (user != null) {
            userDao.delete(user);
        }
    }

    @Transactional(readOnly = true)
    @Override
    public List<User> findAllUsers() {
        return userDao.findAll();
    }
}