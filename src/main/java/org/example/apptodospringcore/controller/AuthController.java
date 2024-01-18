package org.example.apptodospringcore.controller;

import lombok.RequiredArgsConstructor;
import org.example.apptodospringcore.dao.UserDAO;
import org.example.apptodospringcore.model.User;
import org.example.apptodospringcore.payload.ConfirmDTO;
import org.example.apptodospringcore.payload.RegisterDTO;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final PasswordEncoder passwordEncoder;
    private final UserDAO userDAO;
    private final EmailService emailService;
    private static final  String REDIRECT_LOGIN = "redirect:/auth/login";


    // Register
    @GetMapping("/register")
    public String registerPage() {
        return "register";
    }

    @PostMapping("/register")
    public String register(@ModelAttribute RegisterDTO registerDTO) {

        User byEmail = userDAO.getByEmail(registerDTO.email());
        if (byEmail != null) {
            if (byEmail.isEnabled()) {
                return REDIRECT_LOGIN;
            } else {
                String code = emailService.generateCode();
                userDAO.saveCode(byEmail.getId(), code);
//                emailService.send(registerDTO.email(), "This is code", code);

                return "redirect:/auth/confirm?email=%s".formatted(byEmail.getEmail());
            }
        }

        User user = User.builder()
                .name(registerDTO.name())
                .email(registerDTO.email())
                .password(passwordEncoder.encode(registerDTO.password()))
                .enabled(false)
                .build();

        user = userDAO.save(user);

        String code = emailService.generateCode();
        userDAO.saveCode(user.getId(), code);

        // emailService.send(registerDTO.email(), "This is code", code);
        System.out.println(code);

        return "redirect:/auth/confirm?email=%s".formatted(user.getEmail());
    }


    // Confirm
    @GetMapping("/confirm")
    public String confirmPage(@RequestParam(name = "email") String email, Model model) {
        model.addAttribute("email", email);
        return "confirm";
    }

    @PostMapping("/confirm")
    public String confirm(@ModelAttribute ConfirmDTO confirmDTO, Model model) {
        User byEmail = userDAO.getByEmail(confirmDTO.email());

        if (byEmail == null)
            return "redirect:/auth/register";

        if (byEmail.isEnabled())
            return REDIRECT_LOGIN;

        if (!userDAO.isCodeRight(byEmail.getId(), confirmDTO.code())) {
            model.addAttribute("email", byEmail.getEmail());
            return "confirm";
        }

        userDAO.enable(byEmail.getId());
        return REDIRECT_LOGIN;
    }


    // Login

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }
}
