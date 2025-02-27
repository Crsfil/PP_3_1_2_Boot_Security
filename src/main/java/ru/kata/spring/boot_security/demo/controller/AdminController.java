package ru.kata.spring.boot_security.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
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
    public String adminPanel(Model model) {
        model.addAttribute("users", userService.findAllUsers());
        if (!model.containsAttribute("newUser")) {
            model.addAttribute("newUser", new User());
        }
        return "admin";
    }

    @GetMapping("/user/{id}")
    @ResponseBody
    public User getUserForEdit(@PathVariable Long id) {
        return userService.findUserById(id);
    }

    @PostMapping("/new")
    public String createUser(
            @ModelAttribute("newUser") User user,
            @RequestParam(value = "roleNames", required = false) String[] roleNames,
            RedirectAttributes redirectAttributes) {

        Set<Role> roles = processRoles(roleNames);
        user.setRoles(roles);
        userService.saveUser(user);
        redirectAttributes.addFlashAttribute("success", "User created successfully!");

        return "redirect:/admin";
    }

    @PostMapping("/edit")
    public String updateUser(
            @ModelAttribute User user,
            @RequestParam(value = "roleNames", required = false) String[] roleNames,
            RedirectAttributes redirectAttributes) {

        try {
            Set<Role> roles = processRoles(roleNames);
            user.setRoles(roles);
            userService.updateUser(user);
            redirectAttributes.addFlashAttribute("success", "User updated successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error updating user: " + e.getMessage());
        }

        return "redirect:/admin";
    }

    @PostMapping("/delete")
    public String deleteUser(@RequestParam("id") Long id, RedirectAttributes redirectAttributes) {
        try {
            userService.deleteUser(id);
            redirectAttributes.addFlashAttribute("success", "User deleted successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error deleting user: " + e.getMessage());
        }
        return "redirect:/admin";
    }

    private Set<Role> processRoles(String[] roleNames) {
        Set<Role> roles = new HashSet<>();
        if (roleNames != null) {
            for (String roleName : roleNames) {
                Role existingRole = userService.findRoleByName(roleName);
                if (existingRole != null) {
                    roles.add(existingRole);
                }
            }
        }

        if (roles.isEmpty()) {
            Role defaultRole = userService.findRoleByName("ROLE_USER");
            if (defaultRole != null) {
                roles.add(defaultRole);
            }
        }
        return roles;
    }
}