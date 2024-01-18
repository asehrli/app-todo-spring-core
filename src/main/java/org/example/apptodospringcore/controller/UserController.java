package org.example.apptodospringcore.controller;

import lombok.RequiredArgsConstructor;
import org.example.apptodospringcore.dao.UserDAO;
import org.example.apptodospringcore.model.User;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {
    private final UserDAO userDAO;

    @PreAuthorize("hasAuthority('GET_USER')")
    @GetMapping
    public String userPage(Model model) {
        List<User> users = userDAO.getAll();
        model.addAttribute("users", users);
        return "user";
    }

    @PreAuthorize("hasAuthority('BLOCK_USER')")
    @PostMapping("/enable")
    public String changeEnable(@RequestParam(name = "id") UUID id,
                               @RequestParam(name = "enabled") boolean enabled) {
        userDAO.changeEnable(id, enabled);
        return "redirect:/user";
    }

//    @PostMapping("/give-role")
}
