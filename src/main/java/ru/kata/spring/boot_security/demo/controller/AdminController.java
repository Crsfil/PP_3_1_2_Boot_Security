package ru.kata.spring.boot_security.demo.controller;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.UserService;

import java.util.HashSet;
import java.util.Set;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final UserService userService;

    public AdminController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String listUsers(Model model) {
        model.addAttribute("users", userService.findAllUsers());
        model.addAttribute("newUser", new User());
        return "admin";
    }

    @PostMapping("/new")
    public String createUser(@ModelAttribute("newUser") User user,
                             @RequestParam(value = "roleNames", required = false) String[] roleNames,
                             Model model) {
        try {

            Set<Role> roles = new HashSet<>();
            if (roleNames != null) {
                for (String roleName : roleNames) {
                    Role role = new Role();
                    role.setName(roleName);
                    roles.add(role);
                }
            }

            // Если роли не выбраны, добавляем ROLE_USER по умолчанию
            if (roles.isEmpty()) {
                Role userRole = new Role();
                userRole.setName("ROLE_USER");
                roles.add(userRole);
            }

            user.setRoles(roles);

            // Сохраняем пользователя и роли через сервис (каскадирование обработает всё)
            userService.saveUser(user);

            return "redirect:/admin";

        } catch (DataIntegrityViolationException e) {
            model.addAttribute("error", "Ошибка: пользователь с таким именем уже существует!");
            model.addAttribute("users", userService.findAllUsers());
            model.addAttribute("newUser", user);
            return "admin";
        } catch (Exception e) {
            model.addAttribute("error", "Произошла ошибка при добавлении пользователя: " + e.getMessage());
            model.addAttribute("users", userService.findAllUsers());
            model.addAttribute("newUser", user);
            return "admin";
        }
    }

    @PostMapping("/update")
    public String updateUser(@ModelAttribute("existingUser") User user) {
        userService.updateUser(user);
        return "redirect:/admin";
    }

    @PostMapping("/delete")
    public String deleteUser(@RequestParam("id") Long id) {
        userService.deleteUser(id);
        return "redirect:/admin";
    }
}