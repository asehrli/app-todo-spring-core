package org.example.apptodospringcore.config.security;

import org.example.apptodospringcore.model.User;
import org.springframework.security.core.context.SecurityContextHolder;

public class CurrentPrinsiple {
    public static User get() {
        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}
