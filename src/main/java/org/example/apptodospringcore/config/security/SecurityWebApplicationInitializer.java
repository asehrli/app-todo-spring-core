package org.example.apptodospringcore.config.security;

import org.springframework.security.web.context.AbstractSecurityWebApplicationInitializer;

// --- > There has nothing
public class SecurityWebApplicationInitializer extends AbstractSecurityWebApplicationInitializer {
    @Override
    protected boolean enableHttpSessionEventPublisher() {
        return true;
    }
}
