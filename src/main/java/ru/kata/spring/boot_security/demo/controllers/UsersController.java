package ru.kata.spring.boot_security.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.models.Role;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.services.UserService;

import java.security.Principal;

@Controller
public class UsersController {
    private final UserService userService;

    @Autowired
    public UsersController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping(value = "/admin")
    public String getUsersPage(Model model, Principal principal) {
        User user = userService.findByUsername(principal.getName());
        model.addAttribute("user", user);
        model.addAttribute("users", userService.allUsers());
        return "admin";
    }

    @GetMapping(value = "/edit")
    public String setAsAdmin(@RequestParam("id") Long id) {
        User user = userService.findByUserid(id);
        user.addRole(new Role(2L, "ROLE_ADMIN"));
        userService.save(user);

        return "redirect:/admin";
    }

    @GetMapping(value = "/delete/{id}")
    public String deleteUser(@PathVariable("id") Long id,
                             @ModelAttribute("user") User user) {
        userService.delete(id);

        return "redirect:/admin";
    }

    @GetMapping(value = "/user")
    public String showUserInfo(Principal principal, Model model) {
        User user = userService.findByUsername(principal.getName());
        model.addAttribute("user", user);
        return "user";
    }

}
