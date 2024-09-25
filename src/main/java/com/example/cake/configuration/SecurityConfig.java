package com.example.cake.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .cors(cors -> cors.disable()) // Disable CORS or customize it based on your needs
                .csrf(csrf -> csrf.disable()) // Disable CSRF protection
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/", "/shop/**", "/register", "/h2-console/**").permitAll() // Permit specific URLs
                        .requestMatchers("/admin/**").hasRole("ADMIN") // Protect admin routes
                        .requestMatchers("/resources/**", "/static/**", "/images/**", "/productImages/**", "/css/**", "/js/**").permitAll() // Static resources
                        .anyRequest().authenticated() // All other requests require authentication
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .permitAll()
                        .failureUrl("/login?error=true")
                        .defaultSuccessUrl("/")
                        .usernameParameter("email")
                        .passwordParameter("password")
                )
                .oauth2Login(oauth2 -> oauth2
                        .loginPage("/login")
                        .successHandler(googleOAuth2SuccessHandler) // Make sure googleOAuth2SuccessHandler is defined
                )
                .logout(logout -> logout
                        .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                        .logoutSuccessUrl("/login")
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                );
        httpSecurity.headers(headers -> headers
                .frameOptions(frameOptions -> frameOptions.disable())
        );


        return httpSecurity.build();
    }

    @@Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

//    @Bean
//    public AuthenticationProvider authenticationProvider() {
//        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
//        provider.setUserDetailsService(userDetailsService);
//        provider.setPasswordEncoder(passwordEncoder());
//        return provider;
//    }

}



}
