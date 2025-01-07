package com.happy.biling.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http.cors(cors -> cors.disable()) // CORS ��Ȱ��ȭ
				.csrf(csrf -> csrf.disable()) // CSRF ��Ȱ��ȭ
				.formLogin(formLogin -> formLogin.disable()) // �⺻ �α��� ������ ��Ȱ��ȭ
				.headers(headers -> headers.frameOptions(frameOptions -> frameOptions.disable())); // X-Frame-Options
																									// ��Ȱ��ȭ
		return http.build();
	}

	@Bean
	public PasswordEncoder getPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}
}