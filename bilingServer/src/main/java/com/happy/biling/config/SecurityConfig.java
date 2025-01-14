package com.happy.biling.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.happy.biling.security.JwtAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }
	
	@SuppressWarnings("removal")
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http.cors(cors -> cors.disable()) // CORS ��Ȱ��ȭ
				.csrf(csrf -> csrf.disable()) // CSRF ��Ȱ��ȭ
				.formLogin(formLogin -> formLogin.disable()) // �⺻ �α��� ������ ��Ȱ��ȭ
				.headers(headers -> headers.frameOptions(frameOptions -> frameOptions.disable()))// X-Frame-Options ��Ȱ��ȭ
	            .authorizeHttpRequests()
	            .requestMatchers("/auth/**").permitAll() // ���� ���� ���� ������ ��� ����
	            .anyRequest().authenticated() // ������ ��û�� ���� �ʿ�
	            .and()
	            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
		return http.build();
	}

	@Bean
	public PasswordEncoder getPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}
}