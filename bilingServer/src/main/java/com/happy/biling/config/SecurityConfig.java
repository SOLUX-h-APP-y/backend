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
		http.cors(cors -> cors.disable()) // CORS 비활성화
				.csrf(csrf -> csrf.disable()) // CSRF 비활성화
				.formLogin(formLogin -> formLogin.disable()) // 기본 로그인 페이지 비활성화
				.headers(headers -> headers.frameOptions(frameOptions -> frameOptions.disable()))// X-Frame-Options 비활성화
	            .authorizeHttpRequests()
	            .requestMatchers("/auth/**").permitAll() // 인증 없이 접근 가능한 경로 설정
	            .anyRequest().authenticated() // 나머지 요청은 인증 필요
	            .and()
	            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
		return http.build();
	}

	@Bean
	public PasswordEncoder getPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}
}