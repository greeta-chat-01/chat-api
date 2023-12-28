package com.joejoe2.chat.config;

import com.joejoe2.chat.filter.JwtAuthenticationFilter;
import com.joejoe2.chat.service.user.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@EnableWebSecurity
public class SecurityConfig {
  private final UserService userService;
  private final JwtAuthenticationFilter jwtAuthenticationFilter;

  public SecurityConfig(UserService userService, JwtAuthenticationFilter jwtAuthenticationFilter) {
    this.userService = userService;
    this.jwtAuthenticationFilter = jwtAuthenticationFilter;
  }

  @Bean
  PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    // blank will allow any request
    return http.cors()
        .and()
        .csrf()
        .disable()
        .sessionManagement()
        .sessionCreationPolicy(SessionCreationPolicy.NEVER) // use jwt instead of session
        .and()
        .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
        .formLogin()
        .disable()
        .build();
  }

  @Bean
  public AuthenticationManager authenticationManager(HttpSecurity http)
          throws Exception {
    return http.getSharedObject(AuthenticationManagerBuilder.class)
            .userDetailsService(userService)
            .passwordEncoder(passwordEncoder())
            .and()
            .build();
  }

  @Bean
  CorsConfigurationSource corsConfigurationSource() {
    CorsConfiguration apiConfiguration = new CorsConfiguration();
    apiConfiguration.addAllowedOrigin("*");
    apiConfiguration.addAllowedHeader("*");
    apiConfiguration.addAllowedMethod("*");
    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/api/**", apiConfiguration);
    return source;
  }
}
