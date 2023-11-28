package com.bookstore.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.bookstore.security.JwtAuthenticationEntryPoint;
import com.bookstore.security.JwtAuthenticationFilter;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

	private UserDetailsService userDetailsService;

	private JwtAuthenticationEntryPoint authenticationEntryPoint;

	private JwtAuthenticationFilter authenticationFilter;

	public SecurityConfig(UserDetailsService userDetailsService, JwtAuthenticationEntryPoint authenticationEntryPoint,
			JwtAuthenticationFilter authenticationFilter) {
		this.userDetailsService = userDetailsService;
		this.authenticationEntryPoint = authenticationEntryPoint;
		this.authenticationFilter = authenticationFilter;
	}

	@Bean
	public static PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
		return configuration.getAuthenticationManager();
	}

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http.csrf().disable()
				.authorizeHttpRequests((authorize) -> 
				authorize
				.requestMatchers("/Account/User").permitAll()
						//.requestMatchers("/Account/GenerateToken").permitAll()
						.requestMatchers("/Account/Authorized").permitAll()
						.requestMatchers("/Account/User/{userId}").authenticated()
						.requestMatchers("/Account/DeleteUser/{userId}").authenticated()
						.requestMatchers("/Account/logout/{userId}").authenticated()
						.requestMatchers(HttpMethod.POST, "/BookStore/Books").authenticated()
						.requestMatchers(HttpMethod.GET, "/BookStore/Books").permitAll()
						.requestMatchers(HttpMethod.DELETE,"/BookStore/Books/{isbn}").authenticated()
						.requestMatchers(HttpMethod.GET, "/BookStore/Books/{isbn}").permitAll()
						.requestMatchers(HttpMethod.POST, "/BookStore/Book").authenticated()
						.requestMatchers(HttpMethod.DELETE,"/BookStore/Book").authenticated()
						.requestMatchers(HttpMethod.PUT, "/BookStore/Book/{isbn}").authenticated()
						.anyRequest().permitAll())
				.httpBasic(Customizer.withDefaults())
				.exceptionHandling(exception -> exception.authenticationEntryPoint(authenticationEntryPoint))
				.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

		http.addFilterBefore(authenticationFilter, UsernamePasswordAuthenticationFilter.class);

		return http.build();
	}
}
