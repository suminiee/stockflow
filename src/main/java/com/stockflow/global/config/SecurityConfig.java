package com.stockflow.global.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {
    private final String[] allowUrl = {
            "/",
            "/swagger-ui/**",
            "/swagger-resources/**",
            "/v3/api-docs/**",
            "/login",
            "/signup",
            "/api/form"
    };

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        http
                .authorizeHttpRequests(request -> request
                        .requestMatchers(allowUrl).permitAll()
//                        .anyRequest().authenticated()) //다른 요청들은 인증이 필요하도록 할 때 설정
                        .anyRequest().permitAll())
//                .addFilterBefore(UsernamePasswordAuthenticationFilter.class)
//                .formLogin(AbstractHttpConfigurer::disable)
//                .httpBasic(HttpBasicConfigurer::disable)
        //OAuth2 Login 설정을 default로 설정
//                .oauth2Login(Customizer.withDefaults())
//                .csrf(AbstractHttpConfigurer::disable)
//                .exceptionHandling(exceptionHandling -> exceptionHandling
//                        .accessDeniedHandler(jwtAccessDeniedHandler)
//                        .authenticationEntryPoint(jwtAuthenticationEntryPoint))
        ;

        return http.build();
    }

//    @Bean
//    public Filter jwtFilter() {
//        return new JwtFilter(jwtProvider, principalDetailsService);
//    }

//    @Bean
//    public PasswordEncoder bCryptPasswordEncoder() {
//        return new BCryptPasswordEncoder();
//    }
}
