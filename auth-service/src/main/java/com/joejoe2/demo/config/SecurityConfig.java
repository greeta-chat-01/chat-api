package com.joejoe2.demo.config;

import com.joejoe2.demo.filter.JwtAuthenticationFilter;
import com.joejoe2.demo.service.user.UserDetailService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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
@Configuration
public class SecurityConfig {
  @Autowired UserDetailService userDetailService;
  @Autowired JwtAuthenticationFilter jwtAuthenticationFilter;
  @Autowired CorsConfig corsConfig;

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
            .userDetailsService(userDetailService)
            .passwordEncoder(passwordEncoder())
            .and()
            .build();
  }

  @Bean
  CorsConfigurationSource corsConfigurationSource() {
    CorsConfiguration apiConfiguration = new CorsConfiguration();
    apiConfiguration.setAllowedOriginPatterns(List.of(corsConfig.getAllowOrigin()));
    apiConfiguration.setAllowCredentials(true);
    apiConfiguration.addAllowedHeader(CorsConfiguration.ALL);
    apiConfiguration.addAllowedMethod(CorsConfiguration.ALL);
    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/api/**", apiConfiguration);
    source.registerCorsConfiguration("/web/api/**", apiConfiguration);
    return source;
  }
}
