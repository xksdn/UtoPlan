package UtoPlan.UtoPlan.CORS;


import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;


@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .cors(withDefaults())  // 기본 CORS 설정 사용
                .csrf(csrf -> csrf.disable())  // CSRF 비활성화
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().permitAll());  // 모든 요청을 인증 없이 허용
        return httpSecurity.build();
    }

}
