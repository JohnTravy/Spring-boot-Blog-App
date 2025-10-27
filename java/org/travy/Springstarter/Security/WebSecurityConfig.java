package org.travy.Springstarter.Security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    private static final String[] WHITELIST = {

      "/",
      "/api/v1/",
      "/forgot-password/**",
      "/change-password/**",
      "/reset-password/**",
      "/post/**",
      "/posts/**",
      "/login",
      "/register",
      "/update_photo/**",
      "/db-console/**",
      "/css/**",
      "/fonts/**",
      "/images/**",
      "/js/**",

    };

    @Bean
    static PasswordEncoder passwordEncoder(){

      return new BCryptPasswordEncoder();

    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{

        http
        .authorizeHttpRequests(authorizeHttpRequests -> authorizeHttpRequests
        .requestMatchers(WHITELIST)
        .permitAll()
        .anyRequest()
        .authenticated())

        .formLogin(Login -> Login
        .loginPage("/login")
        .loginProcessingUrl("/login")
        .usernameParameter("email")
        .passwordParameter("password")
        .defaultSuccessUrl("/", true)
        .failureUrl("/login?error")
        .permitAll())

        .logout(logout -> logout
        .logoutUrl("/logout")
        .logoutSuccessUrl("/"))
        

        .httpBasic(httpSecurityHttpBasicConfigurer -> httpSecurityHttpBasicConfigurer
        .disable());

        http.csrf(csrf -> csrf.disable());

        http
        .headers(headers -> headers
        .frameOptions(frameOptions -> frameOptions.disable()));

        


       
        
       return http.build();


    }

    
}
