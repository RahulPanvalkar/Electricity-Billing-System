package com.ebs.config;

import com.ebs.services.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    private final CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler;
    private final CustomUserDetailsService customUserDetailService;

    @Autowired
    public SecurityConfig(CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler, CustomUserDetailsService customUserDetailService) {
        this.customAuthenticationSuccessHandler = customAuthenticationSuccessHandler;
        this.customUserDetailService = customUserDetailService;
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring()
                .antMatchers("/favicon.ico","/images/**","/stylesheet/**", "/script/**"); // Ignore JSP and static resources
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                //.csrf().disable()
                .authorizeRequests(authorize -> authorize
                        .antMatchers("/", "/sign-in", "/logout","/register").permitAll()
                        .antMatchers("/admin/**").hasRole("ADMIN")         // Only ADMIN role can access /admin
                        .antMatchers("/consumer/**").hasRole("CONSUMER")       // Only CONSUMER role can access /consumer
                        .antMatchers("/common/**").hasAnyRole("ADMIN", "CONSUMER") // Both ADMIN and CONSUMER can access
                        .antMatchers("/**","/**/**").permitAll()         // Allow access to public URLs
                        .anyRequest().authenticated()                      // Other requests require authentication
                )
                .formLogin(form -> form
                        .loginPage("/sign-in")          // Custom login page
                        .loginProcessingUrl("/login")   // URL for processing login (providing default login processing)
                        .failureUrl("/sign-in?error=true") // Redirect to login page with an error parameter
                        .successHandler(customAuthenticationSuccessHandler)  // Use custom success handler
                        .permitAll()
                );

        http.logout(logout -> logout
                .logoutUrl("/logout")          // URL to trigger logout
                .logoutSuccessUrl("/")         // URL to redirect after logout
                .invalidateHttpSession(true)   // Invalidate session
                .clearAuthentication(true)     // Clear authentication
                .deleteCookies("JSESSIONID")   // Optionally delete cookies
        );

        return http.build();
    }

    @Bean
    public DaoAuthenticationProvider authProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(customUserDetailService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

}
