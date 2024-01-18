package org.example.apptodospringcore.config.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final UserDetailsService userDetailsService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity security) throws Exception {

        // authorize requests
        security.authorizeHttpRequests(
                registry -> registry
                        .requestMatchers("/", "/home").permitAll()
                        .requestMatchers("/auth/**").permitAll()
                        .requestMatchers("/css/**", "/js/**", "/img/**").permitAll()
                        .anyRequest().authenticated()
        );

        // disable _csrf
        security.csrf(AbstractHttpConfigurer::disable);

        // login type
//        security.formLogin(Customizer.withDefaults());

        security.formLogin(formLogin -> formLogin
                .loginPage("/auth/login")
                .usernameParameter("email")
                .passwordParameter("password")
                .defaultSuccessUrl("/cabinet")
        );

        security.userDetailsService(userDetailsService);

        return security.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
