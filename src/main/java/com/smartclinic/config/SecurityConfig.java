package com.smartclinic.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final UserDetailsService userDetailsService;

    public SecurityConfig(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf().disable() // Disabling for simplicity in this clinic requirement
            .authenticationProvider(authenticationProvider())
            .authorizeHttpRequests(authorize -> authorize
                .requestMatchers("/resources/**", "/login").permitAll()
                .requestMatchers("/admin/**").hasAuthority("ADMIN")
                .requestMatchers("/doctor/**").hasAuthority("DOCTOR")
                .requestMatchers("/pharmacy/**").hasAuthority("PHARMACIST")
                .requestMatchers("/patients/register", "/patients/search").hasAnyAuthority("RECEPTIONIST", "ADMIN")
                .requestMatchers("/appointments/book", "/appointments/queue").hasAnyAuthority("RECEPTIONIST", "ADMIN")
                .requestMatchers("/api/**").hasAnyAuthority("RECEPTIONIST", "ADMIN", "DOCTOR", "PHARMACIST")
                .requestMatchers("/billing/**").hasAuthority("ADMIN")
                .requestMatchers("/dashboard").authenticated()
                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage("/login")
                .loginProcessingUrl("/authenticateTheUser")
                .defaultSuccessUrl("/dashboard", true)
                .permitAll()
            )
            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login?logout")
                .permitAll()
            )
            .exceptionHandling(configurer -> configurer
                .accessDeniedPage("/access-denied")
            );

        return http.build();
    }

    @Bean(name = "mvcHandlerMappingIntrospector")
    public org.springframework.web.servlet.handler.HandlerMappingIntrospector mvcHandlerMappingIntrospector() {
        return new org.springframework.web.servlet.handler.HandlerMappingIntrospector();
    }
}
