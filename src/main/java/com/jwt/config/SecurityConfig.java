package com.jwt.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.jwt.security.JwtAuthenticationEntryPoint;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

	  private final JwtAuthenticationEntryPoint unauthorizedHandler;
	    private final JwtAuthenticationFilter jwtAuthenticationFilter;

	    public SecurityConfig(JwtAuthenticationEntryPoint unauthorizedHandler, JwtAuthenticationFilter jwtAuthenticationFilter) {
	        this.unauthorizedHandler = unauthorizedHandler;
	        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
	    }
	    
	    @Bean
	    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
	    	http.csrf(AbstractHttpConfigurer::disable).exceptionHandling(exception->exception.authenticationEntryPoint(unauthorizedHandler))
	    	.sessionManagement(session->session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)).
	    	authorizeHttpRequests(auth->auth.requestMatchers("/api/auth/**").permitAll()
	    			.requestMatchers("/api/test/**").permitAll().
	    			anyRequest().authenticated()
	    			);
	    	http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
	    	return http.build();
	    }
	
	    @Bean
	    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception{
	    	return authConfig.getAuthenticationManager();
	    }
	    
	    
	    
	    @Bean
	    public PasswordEncoder passwordEncoder() {
	        return new BCryptPasswordEncoder();
	    }
	
}
