package org.example.apptodospringcore.controller;

import lombok.RequiredArgsConstructor;
import org.example.apptodospringcore.dao.TodoDAO;
import org.example.apptodospringcore.model.Todo;
import org.example.apptodospringcore.model.User;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Controller
@RequestMapping("/todo")
@RequiredArgsConstructor
public class TodoController {
    private final TodoDAO todoDAO;
    private static final String REDIRECT_TODO = "redirect:/todo";

    @GetMapping
    public String todoPage(Model model, @AuthenticationPrincipal User user) {
        model.addAttribute("todos", todoDAO.myTodos(user.getId()));
        return "todo";
    }

    @PostMapping
    public String addTodo(@ModelAttribute Todo todo,
                          @AuthenticationPrincipal User user) {
        todo.setCreatedBy(user);
        todoDAO.add(todo);
        return REDIRECT_TODO;
    }

    @PostMapping("/delete")
    public String delete(@RequestParam(name = "id") UUID id) {
        todoDAO.deleteTodo(id);
        return REDIRECT_TODO;
    }
    @PostMapping("/complete")
    public String complete(@RequestParam(name = "id") UUID id) {
        todoDAO.complete(id);
        return REDIRECT_TODO;
    }

}
