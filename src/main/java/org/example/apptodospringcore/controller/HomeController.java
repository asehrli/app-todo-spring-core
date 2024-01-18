package org.example.apptodospringcore.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class HomeController {

    @GetMapping(value = {"/home", "/"})
    public String homePage() {
        return "home";
    }

    @GetMapping("/close")
    @ResponseBody
    public String authenticatedWay() {
        return "You are authenticated";
    }

    @GetMapping("/cabinet")
    public String cabinetPage() {
        return "cabinet";
    }

}
