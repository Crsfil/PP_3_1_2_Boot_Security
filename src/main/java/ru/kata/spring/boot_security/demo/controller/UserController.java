package ru.kata.spring.boot_security.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.UserService;
import java.security.Principal;

@Controller
@RequestMapping("/user") // Обработка общего пути
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping // URL будет /user (без дублирования)
    public String userProfile(Model model, Principal principal) {
        User user = userService.findByUsername(principal.getName());
        if (user == null) {
            return "redirect:/error";
        }
        model.addAttribute("user", user);
        return "user"; // Соответствует имени файла user-page.html
    }
}